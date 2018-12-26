package com.igexin.assist.control.oppo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.coloros.mcssdk.PushManager;
import com.coloros.mcssdk.callback.PushCallback;
import com.coloros.mcssdk.mode.SubscribeResult;
import com.igexin.assist.MessageBean;
import com.igexin.assist.action.MessageManger;
import com.igexin.assist.control.AbstractPushManager;
import com.igexin.assist.sdk.AssistPushConsts;

import java.util.ArrayList;
import java.util.List;

public class OppoPushManager implements AbstractPushManager, PushCallback {

    public static final String TAG = AssistPushConsts.LOG_TAG + "OP";

    public static final String PLUGIN_VERSION = "1.0.2";

    public static final String OPPO_VERSION = "1.0.1";

    private Context context;

    private String appKey = "";
    private String appSecret = "";

    public OppoPushManager(Context context) {
        try {
            Log.d(TAG, "oppo plugin version = " + PLUGIN_VERSION + ", oppo sdk version = " + OPPO_VERSION);
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            appKey = (String) appInfo.metaData.get(AssistPushConsts.OPPOPUSH_APPKEY);
            appKey = appKey.replace(AssistPushConsts.OPPO_PREFIX, "");
            appSecret = (String) appInfo.metaData.get(AssistPushConsts.OPPOPUSH_APPSECRET);
            appSecret = appSecret.replace(AssistPushConsts.OPPO_PREFIX, "");

        } catch (Throwable e) {
            // #debug
            Log.d(TAG, e.getMessage());
        }
    }
    @Override
    public void register(Context context) {
        try {
            this.context = context;
            if (TextUtils.isEmpty(appKey) || TextUtils.isEmpty(appSecret)) {
                Log.d(TAG, "Register oppo push appKey or appSecret is null or empty");
                return;
            }

            Log.d(TAG, "Register oppo push, pkg = " + context.getPackageName() + ", appKey = " + appKey + ", appSecret = " + appSecret);

            PushManager.getInstance().register(context, appKey, appSecret, this);

        } catch (Throwable e) {
            // #debug
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void unregister(Context context) {
        try {
            PushManager.getInstance().unRegister();
        } catch (Throwable e) {
            // #debug
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void turnOnPush(Context context) {
        try {
            PushManager.getInstance().resumePush();
        } catch (Throwable e) {
            // #debug
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void turnOffPush(Context context) {
        try {
            PushManager.getInstance().pausePush();
        } catch (Throwable e) {
            // #debug
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public String getToken(Context context) {
        return PushManager.getInstance().getRegisterID();
    }

    @Override
    public void setSilentTime(Context context, int beginHour, int duration) {
        if (duration == 0) {
            turnOnPush(context);
            return;
        }

        int oppoStart = (beginHour + duration) % 24;

        // #debug
        Log.d(TAG, "getui setSilentTime" + beginHour + ":" + duration);

        // #debug
        Log.d(TAG, "oppo push setAcceptTime" + oppoStart + ":" + beginHour);

        List<Integer> weekDays = new ArrayList<Integer>();
        for (int day = 0; day < 7; day++) {
            weekDays.add(day);
        }
        PushManager.getInstance().setPushTime(weekDays, oppoStart, 0, beginHour, 0);
    }

    public static boolean checkOppoDevice(Context con) {
        try {
            // #debug
            Log.e(TAG, "oppo version = " + com.coloros.mcssdk.c.e.b(con, "com.coloros.mcs"));
            return PushManager.isSupportPush(con);
        } catch (Throwable e) {
            // #debug
            Log.d(TAG, e.getMessage());
        }
        return false;
    }

    @Override
    public void onRegister(int code, String token) {
        try {
            Log.d(TAG, "onToken :" + token + ", code = " + code);

            if (context != null && code == 0 && !TextUtils.isEmpty(token) && !token.equalsIgnoreCase("InvalidAppKey")) {
                MessageBean mb = new MessageBean(context, AssistPushConsts.MSG_TYPE_TOKEN, AssistPushConsts.OPPO_PREFIX + token);
                MessageManger.getInstance().addMessage(mb);
            }
        } catch (Throwable e) {
            // #debug
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onUnRegister(int code) {

    }

    @Override
    public void onGetAliases(int code, List<SubscribeResult> alias) {

    }

    @Override
    public void onSetAliases(int code, List<SubscribeResult> alias) {

    }

    @Override
    public void onUnsetAliases(int code, List<SubscribeResult> alias) {

    }

    @Override
    public void onSetUserAccounts(int code, List<SubscribeResult> accounts) {

    }

    @Override
    public void onUnsetUserAccounts(int code, List<SubscribeResult> accounts) {

    }

    @Override
    public void onGetUserAccounts(int code, List<SubscribeResult> accounts) {

    }

    @Override
    public void onSetTags(int code, List<SubscribeResult> tags) {

    }

    @Override
    public void onUnsetTags(int code, List<SubscribeResult> tags) {

    }

    @Override
    public void onGetTags(int code, List<SubscribeResult> tags) {

    }

    @Override
    public void onGetPushStatus(int code, int status) {

    }

    @Override
    public void onSetPushTime(int code, String time) {

    }

    @Override
    public void onGetNotificationStatus(int code, int status) {

    }
}
