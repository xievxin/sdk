package com.igexin.assist.control.vivo;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.igexin.assist.MessageBean;
import com.igexin.assist.action.MessageManger;
import com.igexin.assist.control.AbstractPushManager;
import com.igexin.assist.sdk.AssistPushConsts;
import com.igexin.sdk.PushManager;
import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;

import java.io.File;

public class VivoPushManager implements AbstractPushManager {

    public static final String TAG = AssistPushConsts.LOG_TAG + "VV";

    public static final String PLUGIN_VERSION = "1.0.1";

    public static final String VIVO = "Vivo".toLowerCase();

    private static final String phoneBrand;

    private String mSdkSwitchPath;


    static {
        phoneBrand = Build.BRAND;
    }

    public VivoPushManager(Context context) {
        try {
            Log.d(TAG, "vivo plugin version = " + PLUGIN_VERSION + ", vivo sdk version = " + PushClient.getInstance(context).getVersion());
            mSdkSwitchPath = context.getFilesDir().getPath() + "/" + "init.pid";
            PushClient.getInstance(context).initialize();
        } catch (Throwable e) {
            // #debug
            Log.d(TAG, e.getMessage());
        }
    }
    @Override
    public void register(Context context) {
        try {

            Log.d(TAG, "Register vivo push, pkg = " + context.getPackageName());
            if (PushManager.getInstance().isPushTurnedOn(context) || !isSdkInit()) {
                // 由于初始化第三方推送插件时，可能sdk push开关还未开启，需要额外判断是否是sdk首次初始化
                turnOnPush(context);
            }

        } catch (Throwable e) {
            // #debug
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void unregister(Context context) {
        try {
            turnOffPush(context);
        } catch (Throwable e) {
            // #debug
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void turnOnPush(final Context context) {
        try {
            PushClient.getInstance(context).turnOnPush(new IPushActionListener() {
                @Override
                public void onStateChanged(int state) {
                    Log.d(TAG, "turnOnPush finish, state = " + state);
                    if (state == 0) {
                        String token = PushClient.getInstance(context).getRegId();
                        Log.d(TAG, "turnOnPush token = " + token);
                        if (context != null && !TextUtils.isEmpty(token)) {
                            MessageBean mb = new MessageBean(context, AssistPushConsts.MSG_TYPE_TOKEN, AssistPushConsts.VIVO_PREFIX + token);
                            MessageManger.getInstance().addMessage(mb);
                        }
                    } else if (state == 101) {
                        Log.d(TAG, "the vivo rom not support system push");
                    }
                }
            });
        } catch (Throwable e) {
            // #debug
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void turnOffPush(Context context) {
        try {
            PushClient.getInstance(context).turnOffPush(new IPushActionListener() {
                @Override
                public void onStateChanged(int state) {
                    Log.d(TAG, "turnOffPush finish, state = " + state);
                }
            });
        } catch (Throwable e) {
            // #debug
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public String getToken(Context context) {
        return PushClient.getInstance(context).getRegId();
    }

    @Override
    public void setSilentTime(Context context, int beginHour, int duration) {}

    public static boolean checkVivoDevice(Context con) {
        try {
            return TextUtils.equals(VIVO, phoneBrand.toLowerCase());
        } catch (Throwable e) {
            // #debug
            Log.d(TAG, e.getMessage());
        }
        return false;
    }

    private boolean isSdkInit() {
        if (mSdkSwitchPath != null) {
            File lock = new File(mSdkSwitchPath);
            return lock.exists();
        }

        return false;
    }
}
