package com.igexin.sdk;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.igexin.assist.MessageBean;
import com.igexin.assist.action.MessageManger;
import com.igexin.assist.sdk.AssistPushConsts;
import com.vivo.push.model.UPSNotificationMessage;
import com.vivo.push.sdk.OpenClientPushMessageReceiver;

public class VivoPushMessageReceiver extends OpenClientPushMessageReceiver {

    public static final String TAG = AssistPushConsts.LOG_TAG + "VV";


    @Override
    public void onNotificationMessageClicked(Context context, UPSNotificationMessage upsNotificationMessage) {
        try {
            Log.d(TAG, "onNotificationMessageClicked upsNotificationMessage = " + upsNotificationMessage);
            if (upsNotificationMessage != null && upsNotificationMessage.getSkipType() == UPSNotificationMessage.CUSTOM) {
                // 接收服务器推送的消息透传
                String payload = upsNotificationMessage.getSkipContent();
                if (context != null && !TextUtils.isEmpty(payload)) {
                    MessageBean mb = new MessageBean(context, AssistPushConsts.MSG_TYPE_PAYLOAD, payload);
                    mb.setMessageSource(AssistPushConsts.VIVO_PREFIX);
                    MessageManger.getInstance().addMessage(mb);
                }
            }
        } catch (Throwable e) {
            // #debug
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onReceiveRegId(Context context, String token) {
        try {
            Log.d(TAG, "onReceiveRegId :" + token);

            if (context != null && !TextUtils.isEmpty(token)) {
                MessageBean mb = new MessageBean(context, AssistPushConsts.MSG_TYPE_TOKEN, AssistPushConsts.VIVO_PREFIX + token);
                MessageManger.getInstance().addMessage(mb);
            }
        } catch (Throwable e) {
            // #debug
            Log.d(TAG, e.getMessage());
        }
    }
}
