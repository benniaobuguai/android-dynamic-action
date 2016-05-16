package com.opencdk.dynamicaction;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.text.TextUtils;

import com.opencdk.dynamicaction.cfg.CfgDataType;
import com.opencdk.dynamicaction.cfg.DACfgParser;
import com.opencdk.utils.Log;
import com.opencdk.utils.Utils;

/**
 * Dynamic Action Controller
 * 
 * <pre>
 * 约定关键字:
 * scheme: 唯一标识应用程序的字符串
 * host: 资源地址, 即Activity的名称, 由 packageId$activityName
 * 地址参数说明: 
 * data: 携带的数据体, json数据结构, 业务逻辑数据
 * event: 事件索引
 * url: 网址
 * fragment: 可动态组合Activity+Fragment构建新UI
 * 
 * 
 * 
 * 1. 原生交互协议:
 * 数据格式: scheme://host?data={"key":"value"}&from=user_login
 * host数据格式: packageId$activityName
 * data数据格式: json数据结构即可
 * 
 * 2. H5间交互协议:
 * 数据格式: scheme://host?url=url&data={"key":"value"}
 * host数据格式: packageId$activityName
 * data数据格式: json数据结构即可
 * 
 * 3. Activity+Fragment
 * 数据格式: scheme://host?fragment=name&data={"key":"value"}
 * 
 * </pre>
 * 
 */

/**
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2016-3-13
 * @Modify 2016-3-27
 */
public final class DALoader {

	private static final String TAG = "DA.Loader";
	private String mCfgFileName = "dynamic_action.cfg";

	private static volatile DALoader mDALoader = null;

	private Context mContext = null;
	private CfgDataType mCfgDataType = null;

	private boolean mDevMode = false;
	private String mScheme = null;
	private String mAppScheme = null;
	private DAConfig mDAConfig = null;

	private DALoader() {
		mDAConfig = new DAConfig();
	}

	public static DALoader getInstance() {
		if (mDALoader == null) {
			synchronized (DALoader.class) {
				if (mDALoader == null) {
					mDALoader = new DALoader();
				}
			}
		}

		return mDALoader;
	}

	public void setUp(DALoaderConfig config) {
		this.mContext = config.getContext();
		this.mCfgFileName = config.getCfgFileName();

		// 加载配置信息
		this.mCfgDataType = config.getCfgDataType();
		if (this.mCfgDataType == null) {
			this.mCfgDataType = CfgDataType.getDefault();
		}

		this.mDAConfig = getConfiguration(mContext, mCfgDataType);
		this.mScheme = this.mDAConfig.getScheme();
		this.mAppScheme = this.mDAConfig.getAppScheme();
		this.mDevMode = this.mDAConfig.isDevMode();

		Log.D(TAG, "Scheme: " + this.mScheme);
		Log.D(TAG, "AppScheme: " + this.mAppScheme);
		Log.D(TAG, "DevMode: " + this.mDevMode);
	}

	public static DAConfig getDAConfig() {
		return getInstance().mDAConfig;
	}

	public static Context getContext() {
		return getInstance().mContext;
	}

	public static String getScheme() {
		return getInstance().mScheme;
	}

	public static String getAppScheme() {
		return getInstance().mAppScheme;
	}

	public DAConfig getConfiguration(Context context, CfgDataType cfgDataType) {
		DACfgParser daParser = new DACfgParser(cfgDataType);

		InputStream is = null;
		DAConfig data = null;

		try {
			is = Utils.getExternalConfig(context, mCfgFileName);

			if (is == null) {
				Log.W(TAG, "Not found external [" + mCfgFileName + "], try to read from assets.");
				is = Utils.getInternalConfig(context, mCfgFileName);
			} else {
				Log.D(TAG, "Found external [" + mCfgFileName + "].");

				try {
					// parse external dynamic_action_config.xml
					data = daParser.parse(is);

					return data;
				} catch (DAException e) {
					Log.E(TAG, "Error parsing external [" + mCfgFileName + "], try to read from assets.");

					is = Utils.getInternalConfig(context, mCfgFileName);
				}
			}

			if (is == null) {
				// double check
				Log.W(TAG, "Not found [" + mCfgFileName + "] in assets!");
				return new DAConfig();
			} else {
				try {
					data = daParser.parse(is);
				} catch (DAException e) {
					Log.E(TAG, "Error parsing " + mCfgFileName + " in assets!");
				}
			}
		} catch (Exception e) {
			Log.E(TAG, "[" + mCfgFileName + "] load failed!!!", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return data;
	}

	/**
	 * 解析Host结构, Host结构: packageId$actionName
	 * 
	 * @param host
	 * @return
	 */
	public static String[] splitHost(String host) {
		if (host == null) {
			return null;
		}

		String[] hostArray = host.split("\\$");
		if (hostArray == null || hostArray.length != 2) {
			return null;
		}

		return hostArray;
	}

	/**
	 * packageId$actionName
	 * 
	 * @param packageId
	 * @param actionName
	 * @return
	 */
	public static String makeDAKey(String packageId, String actionName) {
		return makeDAKey(packageId, actionName, null);
	}

	/**
	 * packageId$actionName$from
	 * 
	 * @param packageId
	 * @param actionName
	 * @param from
	 * @return
	 */
	public static String makeDAKey(String packageId, String actionName, String from) {
		StringBuffer sb = new StringBuffer();
		sb.append(packageId).append('$');
		sb.append(actionName);

		if (!TextUtils.isEmpty(from)) {
			sb.append('$').append(from);
		}

		return sb.toString();
	}

	/**
	 * packagePath + . + actionName + Activity
	 * 
	 * @param packagePath
	 * @param actionName
	 * @return
	 */
	public static String makeDAClassName(String packagePath, String actionName) {
		return packagePath + "." + actionName + "Activity";
	}
	
}
