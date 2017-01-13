package com.opencdk.dynamicaction.extra;

import android.content.Context;
import android.content.Intent;

import com.opencdk.utils.Log;

/**
 * 动态Action扩展, 可自行定义Action的跳转
 *
 * @author 笨鸟不乖
 * @version 1.0.0
 * @email benniaobuguai@gmail.com
 * @Modify 2017-1-13
 * @since 2017-1-13
 */
public class DAExtra implements IntentHandler {

    private static final String TAG = "DAExtra";

    private static volatile DAExtra mDAExtra = null;

    private IntentHandler mIntentHandler = null;

    private boolean mCustomHandleIntent = false;

    private DAExtra() {

    }

    public static DAExtra getInstance() {
        if (mDAExtra == null) {
            synchronized (DAExtra.class) {
                if (mDAExtra == null) {
                    mDAExtra = new DAExtra();
                }
            }
        }

        return mDAExtra;
    }

    public void registerIntentHandler(IntentHandler intentHandler) {
        this.mIntentHandler = intentHandler;
        this.mCustomHandleIntent = true;
    }

    public void unregisterIntentHandler() {
        this.mIntentHandler = null;
        this.mCustomHandleIntent = false;
    }

    public boolean isCustomHandleIntent() {
        return mCustomHandleIntent;
    }

    @Override
    public void handlerIntent(Context context, Intent intent, String packageName, String className, int requestCode) {
        if (mIntentHandler != null) {
            mIntentHandler.handlerIntent(context, intent, packageName, className, requestCode);
        } else {
            Log.W(TAG, "Not found a valid intent handler, please call registerIntentHandler() first!!!");
        }
    }

}
