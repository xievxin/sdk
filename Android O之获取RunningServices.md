Google在Android O上对Service和广播又加强了限制，具体可以看[官网介绍](https://developer.android.com/about/versions/oreo/android-8.0-changes.html#back-all) ，本文将尝试能否在Android O的设备上获取到其他应用正在运行的service。

## 方法一、使用ActiviyManager
```Java
ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
List<ActivityManager.RunningServiceInfo> infoList = manager.getRunningServices(Integer.MAX_VALUE);
```

并在AndroidManifest.xml文件中加上权限（为何添加这两个权限的原因详见方法三）：

```Java
<uses-permission android:name="android.permission.INTERACT_ACROSS_USERS_FULL"/>
<uses-permission android:name="android.permission.GET_TASKS"/>
```
运行后，发现infoList中只有本应用内的Service。原因：
![](https://github.com/xievxin/getuiGit/blob/master/images/WX20180416-180521.png)
        
## 方法二、adb命令
我们知道`adb shell dumpsys activity services` 可以在控制台输出app的所有运行service信息，下面我们尝试在代码中执行这条命令，看下返回结果。
        
```Java
BufferedReader reader = null;
String content = "";
try {
    Process process = Runtime.getRuntime().exec("dumpsys activity services");
    reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    StringBuffer output = new StringBuffer();
    int read;
    char[] buffer = new char[4096];
    while ((read = reader.read(buffer)) > 0) {
        output.append(buffer, 0, read);
    }
    reader.close();
    content = output.toString();
    Log.i(TAG, content);
} catch (IOException e) {
    e.printStackTrace();
}
```
接着在AndroidManifest.xml中添加权限
![](https://github.com/xievxin/getuiGit/blob/master/images/WX20180416-182018.png)

输出结果如下：
```Java
Permission Denial: can't dump ActivityManager from from pid=6038, uid=10087 due to missing android.permission.DUMP permission
```

## 方法三、反射
从`ActivityManager.getRunningServices`方法入手，一步步往下看
![](https://github.com/xievxin/getuiGit/blob/master/images/ref01.png)
![](https://github.com/xievxin/getuiGit/blob/master/images/ref02.png)

IActivityManager是一个aidl文件，getService（）拿到的实际是和系统ActivityManagerService跨进程通信的BinderProxy，通过transact()到AMS进程IActivityManager.Stub中的onTransact()来最终调用到AMS的getServices()，如下：

![](https://github.com/xievxin/getuiGit/blob/master/images/ref03.png)
![](https://github.com/xievxin/getuiGit/blob/master/images/ref04.png)

最终调用到`ActiveServices mServices`的`getRunningServiceInfoLocked()`并返回最终结果（正在运行service列表）。
```Java
List<ActivityManager.RunningServiceInfo> getRunningServiceInfoLocked(int maxNum, int flags,
        int callingUid, boolean allowed, boolean canInteractAcrossUsers) {
        ArrayList<ActivityManager.RunningServiceInfo> res
                = new ArrayList<ActivityManager.RunningServiceInfo>();

        final long ident = Binder.clearCallingIdentity();
        try {
            if (canInteractAcrossUsers) {
                int[] users = mAm.mUserController.getUsers();
                for (int ui=0; ui<users.length && res.size() < maxNum; ui++) {
                    ArrayMap<ComponentName, ServiceRecord> alls = getServicesLocked(users[ui]);
                    for (int i=0; i<alls.size() && res.size() < maxNum; i++) {
                        ServiceRecord sr = alls.valueAt(i);
                        res.add(makeRunningServiceInfoLocked(sr));
                    }
                }

                for (int i=0; i<mRestartingServices.size() && res.size() < maxNum; i++) {
                    ServiceRecord r = mRestartingServices.get(i);
                    ActivityManager.RunningServiceInfo info =
                            makeRunningServiceInfoLocked(r);
                    info.restarting = r.nextRestartTime;
                    res.add(info);
                }
            } else {
                int userId = UserHandle.getUserId(callingUid);
                ArrayMap<ComponentName, ServiceRecord> alls = getServicesLocked(userId);
                for (int i=0; i<alls.size() && res.size() < maxNum; i++) {
                    ServiceRecord sr = alls.valueAt(i);

                    if (allowed || (sr.app != null && sr.app.uid == callingUid)) {
                        res.add(makeRunningServiceInfoLocked(sr));
                    }
                }

                for (int i=0; i<mRestartingServices.size() && res.size() < maxNum; i++) {
                    ServiceRecord r = mRestartingServices.get(i);
                    if (r.userId == userId
                        && (allowed || (r.app != null && r.app.uid == callingUid))) {
                        ActivityManager.RunningServiceInfo info =
                                makeRunningServiceInfoLocked(r);
                        info.restarting = r.nextRestartTime;
                        res.add(info);
                    }
                }
            }
        } finally {
            Binder.restoreCallingIdentity(ident);
        }

        return res;
    }
```
        
        
**梳理一下**，实际上AMS最终都是调用ActivityServices相关方法。
我们可以在app中通过反射拿到BinderProxy，获取到mServices，然后继续反射执行mServices中的方法来避开权限验证，直接获取service列表。
```java
ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
try {
        Method method = manager.getClass().getDeclaredMethod("getService");
        method.setAccessible(true);
        Object amsProxy = method.invoke(manager);
        Log.i(TAG, "test: succ!!");
} catch (Exception e) {
        e.printStackTrace();
}
```

一切似乎都很美好，直到。。

![](https://github.com/xievxin/getuiGit/blob/master/images/ref05.png)

发现ActiviyServices不支持跨进程传输。。

        

## 总结：三个方案都行不通。