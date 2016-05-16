package com.opencdk.da;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;

public class GApplication extends Application {

	private static boolean mEntryFlag = false;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	public static void setEntryFlag(boolean entryFlag) {
		mEntryFlag = entryFlag;
	}

	public static boolean isEntry() {
		return mEntryFlag;
	}

	/**
	 * 重写此方法, 使得app使用统一的默认配置
	 */
	@Override
	public Resources getResources() {
		Resources res = super.getResources();
		Configuration config = new Configuration();
		config.setToDefaults();
		res.updateConfiguration(config, res.getDisplayMetrics());
		return res;
	}

}
