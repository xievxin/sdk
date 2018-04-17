## 直接启动PushService（后台服务）
Android O直接startService()启动其他应用Service会抛出`IllegalStateException: Not allowed to start service`，原因参见[官方资料](https://developer.android.com/about/versions/oreo/android-8.0-changes.html)：

![](https://github.com/xievxin/getuiGit/blob/master/images/stt01.png)

Google给的解决办法就是在新启动的`PushService.onCreate()`中开启一个前台进程，即Notification。一旦Notification被移除，PushService也由前台进程->后台进程，会马上被系统杀掉。
因此，如果后台Service想在Android O上常驻，必须挂一个`notification.flags |= Notification.FLAG_NO_CLEAR;`的Notification。

## 通过透明Activity启动Service
定义一个Activity：
```Java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, PushService.class));
        finish();
    }
```
这类方式不会被用户察觉，而且能在Android O上成功启动PushService。但是PushService存活时间的测试结果并不乐观
![](https://github.com/xievxin/getuiGit/blob/master/images/stt02.png)
本人试了2次，存活时间都是在60.8s

## 通过代理Service启动
其原理和Activity类似，定义ProxyService如下：
```Java
public class ProxyService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        
        startService(new Intent(this, PushService.class));
        startForeground(1, new Notification());
        stopSelf();
    }

}
```
这种方式也同样不会被用户察觉到，新启动的Notification会瞬间随着`stopSelf()`消失，同样可行。只是PushService的存活时间依然还是60s。

## bindService()启动

















