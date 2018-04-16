Google在Android O上对Service和广播又加强了限制，具体可以看[官网介绍](https://developer.android.com/about/versions/oreo/android-8.0-changes.html#back-all) ，本文将尝试能否在Android O的设备上获取到其他应用正在运行的service。

## 一、使用ActiviyManager
```Java
ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
List<ActivityManager.RunningServiceInfo> infoList = manager.getRunningServices(Integer.MAX_VALUE);
```        

并在AndroidManifest.xml文件中加上权限：

```Java
<uses-permission android:name="android.permission.INTERACT_ACROSS_USERS_FULL"/>
<uses-permission android:name="android.permission.GET_TASKS"/>
```         
运行后，发现infoList中只有本应用内的Service。原因：
![](https://github.com/xievxin/getuiGit/blob/master/images/WX20180416-180521.png)         
        
        
        
        
        
        
        
        
        
        
