package com.igexin.sdk;

import android.content.Context;
import android.util.Log;

import com.coloros.mcssdk.PushService;
import com.coloros.mcssdk.mode.AppMessage;
import com.coloros.mcssdk.mode.SptDataMessage;
import com.igexin.assist.MessageBean;
import com.igexin.assist.action.MessageManger;
import com.igexin.assist.sdk.AssistPushConsts;
import com.igexin.assist.util.AssistUtils;

public class OppoPushService extends PushService {

    public static final String TAG = AssistPushConsts.LOG_TAG + "OP";

    @Override
    public void processMessage(Context context, AppMessage appMessage) {
        try {

            Log.d(TAG, "processMessage receive app meaasge ...");
            // #debug
            Log.d(TAG, "processMessage receive app meaasge:" + appMessage);

            if (context != null && appMessage != null) {
                MessageBean mb = new MessageBean(context, AssistPushConsts.MSG_TYPE_PAYLOAD, appMessage.getContent());
                mb.setMessageSource(AssistPushConsts.OPPO_PREFIX);
                MessageManger.getInstance().addMessage(mb);
            }

            AssistUtils.startGetuiService(context);
        } catch (Throwable e) {
            // #debug
            Log.d(TAG, e.getMessage());
        }


    }

    @Override
    public void processMessage(Context context, SptDataMessage sptDataMessage) {
        try {

            Log.d(TAG, "processMessage receive sptData meaasge ...");
            // #debug
            Log.d(TAG, "processMessage receive sptData meaasge:" + sptDataMessage);

            if (context != null && sptDataMessage != null) {
                MessageBean mb = new MessageBean(context, AssistPushConsts.MSG_TYPE_PAYLOAD, sptDataMessage.getContent());
                mb.setMessageSource(AssistPushConsts.OPPO_PREFIX);
                MessageManger.getInstance().addMessage(mb);
            }

            AssistUtils.startGetuiService(context);
        } catch (Throwable e) {
            // #debug
            Log.d(TAG, e.getMessage());
        }
    }
}
