package com.opencdk.da.interceptor;

import android.content.Context;

import com.opencdk.da.biz.user.UserManager;
import com.opencdk.dynamicaction.DA;
import com.opencdk.dynamicaction.interceptor.AbstractInterceptor;
import com.opencdk.utils.Log;

public class LoginInterceptor extends AbstractInterceptor {

	private static final String TAG = "LoginInterceptor";

	public LoginInterceptor(Context context) {
		super(context);
	}

	@Override
	public String intercept(String text) {
		Log.D(TAG, "intercept(), text: " + text);
		
		final boolean isLogin = UserManager.getInstance().isLoginSuccess();
		if (!isLogin) {
			new DA.Builder(getContext())
				.setHost("1$Login")
				.go();

			return RESULT_FAILED;
		}

		return RESULT_OK;
	}

}
