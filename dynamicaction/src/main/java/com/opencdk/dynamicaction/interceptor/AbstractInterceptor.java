package com.opencdk.dynamicaction.interceptor;

import android.content.Context;

import com.opencdk.dynamicaction.DAConstants;

public abstract class AbstractInterceptor {

	public static final String RESULT_OK = DAConstants.RESULT_OK;
	public static final String RESULT_FAILED = DAConstants.RESULT_FAILED;
	public static final String RESULT_UNKNOWN = DAConstants.RESULT_UNKNOWN;

	private Context mContext;

	public AbstractInterceptor(Context context) {
		this.mContext = context;
	}

	protected Context getContext() {
		return mContext;
	}

	protected void setUp() throws Exception {

	}

	protected void tearDown() throws Exception {

	}

	public abstract String intercept(String text);

}
