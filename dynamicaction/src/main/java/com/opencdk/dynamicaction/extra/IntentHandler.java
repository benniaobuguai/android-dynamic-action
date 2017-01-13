package com.opencdk.dynamicaction.extra;

import android.content.Context;
import android.content.Intent;

/**
 * Custom Intent Handler
 */
public interface IntentHandler {

    public void handlerIntent(Context context, Intent intent, String packageName, String className, int requestCode);

}
