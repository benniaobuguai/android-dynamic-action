package com.opencdk.dynamicaction;

import android.content.Context;

import com.opencdk.dynamicaction.cfg.CfgDataType;

public class DALoaderConfig {

	private Context mContext;

	private CfgDataType mCfgDataType = CfgDataType.getDefault();

	private String mCfgFileName;

	private boolean mDebug;

	public DALoaderConfig(Context context) {
		this.mContext = context;
	}

	public Context getContext() {
		return mContext;
	}

	public String getCfgFileName() {
		return mCfgFileName;
	}

	public void setCfgFileName(String filePath) {
		this.mCfgFileName = filePath;
	}

	public boolean isDebug() {
		return mDebug;
	}

	public void setDebug(boolean isDebug) {
		this.mDebug = isDebug;
	}

	public CfgDataType getCfgDataType() {
		return mCfgDataType;
	}

	public void setCfgDataType(CfgDataType cfgDataType) {
		this.mCfgDataType = cfgDataType;
	}

}
