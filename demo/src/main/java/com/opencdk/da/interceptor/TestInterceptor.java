package com.opencdk.da.interceptor;

import android.content.Context;

import com.opencdk.dynamicaction.interceptor.AbstractInterceptor;
import com.opencdk.utils.Log;

public class TestInterceptor extends AbstractInterceptor {

	private static final String TAG = "TestInterceptor";

	public TestInterceptor(Context context) {
		super(context);
	}

	@Override
	public String intercept(String text) {
		Log.D(TAG, "intercept(), text: " + text);

		return RESULT_FAILED;
	}

}
