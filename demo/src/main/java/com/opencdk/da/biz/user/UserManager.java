package com.opencdk.da.biz.user;

public final class UserManager {

	private UserInfo mUserInfo = null;

	private static UserManager mUserManager = null;

	private boolean mLoginSuccess = false;

	private UserManager() {

	}

	public static UserManager getInstance() {
		if (mUserManager == null) {
			synchronized (UserManager.class) {
				if (mUserManager == null) {
					mUserManager = new UserManager();
				}
			}
		}

		return mUserManager;
	}

	public void setLoginUserInfo(UserInfo userInfo) {
		if (userInfo == null) {
			return;
		}

		this.mLoginSuccess = true;
		this.mUserInfo = userInfo;
	}

	public void logout() {
		this.mLoginSuccess = false;
		this.mUserInfo = null;
	}

	public UserInfo getUserInfo() {
		return this.mUserInfo;
	}

	public boolean isLoginSuccess() {
		return this.mLoginSuccess;
	}

}
