package com.opencdk.dynamicaction;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.net.URLDecoder;
import java.util.HashMap;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.opencdk.dynamicaction.interceptor.AbstractInterceptor;
import com.opencdk.utils.Log;
import com.opencdk.utils.RegexUtil;

/**
 * 动态Action
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2016-3-13
 * @Modify 2016-3-27
 */
public class DA {

	private static final String TAG = "DA";

	/**
	 * Intent中携带的交互数据, 使用JSON数据结构
	 * 
	 * <pre>
	 * 示例:
	 * opencdk://1$UserDetail?data={"userId":"1024"}
	 * </pre>
	 */
	public static final String URI_DATA = "data";

	/**
	 * H5链接地址
	 * 
	 * <pre>
	 * 示例: 使用浏览器访问opencdk官网
	 * opencdk://0$Browser?url=http://www.opencdk.com
	 * </pre>
	 */
	public static final String URI_URL = "url";

	/**
	 * 事件源, 表示触发此操作的动作或事件等.
	 * 
	 * <pre>
	 * 示例: 点击用户头像跳转到用户详情界面
	 * opencdk://1$UserDetail?data={"userId":"1024"}&from=User_Avatar_Click
	 * </pre>
	 */
	public static final String URI_FROM = "from";

	/**
	 * 动态某个Action跳转目标的数据结构, 如: packageId$ActionName
	 * 
	 * <pre>
	 * 示例: 点击用户头像跳转到用户详情的H5界面
	 * opencdk://1$UserDetail?data={"userId":"1024"}&from=User_Avatar_Click&to=0$Browser?url=http://www.opencdk.com/userdetail.html
	 * </pre>
	 */
	public static final String URI_TO = "to";

	/**
	 * Activity的标题
	 * 
	 * <pre>
	 * 示例: 
	 * opencdk://1$UserDetail?data={"userId":"1024"}&from=User_Avatar_Click&title=用户详情
	 * </pre>
	 */
	public static final String URI_TITLE = "title";

	/**
	 * 与{@link Intent#setFlags(int)}的功能保持一致
	 * 
	 * <pre>
	 * 示例: 为Intent添加{@link android.content.Intent#FLAG_ACTIVITY_NEW_TASK}
	 * opencdk://1$UserDetail?title=用户详情&flags=268435456
	 * </pre>
	 */
	public static final String URI_FLAG = "flags";

	/**
	 * 与{@link Activity#startActivityForResult(Intent, int)}的功能保持一致
	 * 
	 * <pre>
	 *  示例: 
	 *  opencdk://1$UserDetail?data={"userId":"1024"}&requestCode=1000
	 * </pre>
	 */
	public static final String URI_REQUEST_CODE = "requestCode";

	private static final String EXTRA_URL = TAG + "." + URI_URL;
	private static final String EXTRA_DATA = TAG + "." + URI_DATA;
	private static final String EXTRA_FROM = TAG + "." + URI_FROM;
	private static final String EXTRA_TO = TAG + "." + URI_TO;
	private static final String EXTRA_TITLE = TAG + "." + URI_TITLE;

	private Builder mBuilder;
	private Context mContext;

	protected DA(Builder builder) {
		this.mBuilder = builder;
		this.mContext = builder.mContext;
	}

	public void go() {
		String packageId = null;
		String actionName = null;
		if (mBuilder.mPackageId != null && mBuilder.mActionName != null) {
			packageId = mBuilder.mPackageId;
			actionName = mBuilder.mActionName;
		} else if (mBuilder.mHost != null) {
			String[] hostArray = DALoader.splitHost(mBuilder.mHost);
			if (hostArray == null) {
				Log.W(TAG, "Unknown host[" + mBuilder.mHost + "]");
				return;
			}
			packageId = hostArray[0];
			actionName = hostArray[1];
		} else if (!TextUtils.isEmpty(mBuilder.mUriString)) {
			Log.D(TAG, mBuilder.mUriString);

			final Uri uri = Uri.parse(mBuilder.mUriString);
			final String uriScheme = uri.getScheme();
			if (uriScheme == null) {
				Log.E(TAG, "Unknown scheme[" + uriScheme + "]");
				return;
			}

			fillInBuilder(mBuilder.mUriString, mBuilder);
			go();

			return;
		}

		if (transferAction(packageId, actionName)) {
			return;
		}

		if (breakAction(packageId, actionName)) {
			return;
		}

		DAPackage daPackage = DALoader.getDAConfig().getPackage(packageId);
		String daClassName = DALoader.makeDAClassName(daPackage.getPackageName(), actionName);

		Intent intent = new Intent();
		intent.setClassName(mContext, daClassName);
		if (mBuilder.mFlags != -1) {
			intent.setFlags(mBuilder.mFlags);
		}

		intent.putExtra(DA.EXTRA_DATA, mBuilder.mData);
		intent.putExtra(DA.EXTRA_URL, mBuilder.mUrl);
		intent.putExtra(DA.EXTRA_TITLE, mBuilder.mTitle == null ? "" : mBuilder.mTitle);

		try {
			if (mBuilder.mRequestCode == 0) {
				if (mContext instanceof Activity) {
					mContext.startActivity(intent);
				} else {
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);
				}
			} else {
				if (mContext instanceof Activity) {
					((Activity) mContext).startActivityForResult(intent, mBuilder.mRequestCode);
				} else {
					Log.W(TAG,
							"Current context is not Activity Context, use context.startActivity() to replace context.startActivityForResult().");
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);
				}
			}
		} catch (ActivityNotFoundException e) {
			Log.E(TAG, "Not found activity[" + daClassName + "]", e);
		}
	}

	/**
	 * 解析uriString, 将参数填充到{@link Builder}
	 * 
	 * @param uriString
	 * @param builder
	 */
	private static void fillInBuilder(String uriString, Builder builder) {
		if (builder == null) {
			return;
		}

		Uri uri = Uri.parse(uriString);
		builder.mHost = uri.getHost();
		builder.mData = uri.getQueryParameter(DA.URI_DATA);
		builder.mFrom = uri.getQueryParameter(DA.URI_TO);
		builder.mTitle = uri.getQueryParameter(DA.URI_TITLE);

		String sFlags = uri.getQueryParameter(DA.URI_FLAG);
		if (RegexUtil.isDigits(sFlags)) {
			builder.mFlags = Integer.parseInt(sFlags);
		}
		String sRequestCode = uri.getQueryParameter(DA.URI_REQUEST_CODE);
		if (RegexUtil.isDigits(sRequestCode)) {
			builder.mRequestCode = Integer.parseInt(sRequestCode);
		}

		String url = uri.getQueryParameter(DA.URI_URL);
		if (!TextUtils.isEmpty(url)) {
			try {
				builder.mUrl = URLDecoder.decode(url, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				builder.mUrl = "";
			}
		}
	}

	/**
	 * 跳转-->转移
	 * 
	 * @param srcPackageId
	 * @param srcActionName
	 * @return
	 */
	private boolean transferAction(String srcPackageId, String srcActionName) {
		if (!TextUtils.isEmpty(mBuilder.mFrom)) {
			// 当前Action启用动态配置, Action将被转移
			// 1. 优先匹配带From的配置
			String daKey = DALoader.makeDAKey(srcPackageId, srcActionName, mBuilder.mFrom);
			String daTo = DALoader.getDAConfig().getDynamicAction(daKey);
			if (!TextUtils.isEmpty(daTo)) {
				mBuilder.mPackageId = null;
				mBuilder.mActionName = null;
				mBuilder.mTo = daTo;

				String uriString = DALoader.getScheme() + "://" + mBuilder.mTo;
				fillInBuilder(uriString, mBuilder);
				go();

				return true;
			} else {
				// 2. 匹配无From配置
				daKey = DALoader.makeDAKey(srcPackageId, srcActionName, null);
				daTo = DALoader.getDAConfig().getDynamicAction(daKey);
				if (!TextUtils.isEmpty(daTo)) {
					mBuilder.mPackageId = null;
					mBuilder.mActionName = null;
					mBuilder.mTo = daTo;

					String uriString = DALoader.getScheme() + "://" + mBuilder.mTo;
					fillInBuilder(uriString, mBuilder);
					go();

					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 跳转拦截
	 * 
	 * @param packageId
	 * @param actionName
	 * @return
	 */
	private boolean breakAction(String packageId, String actionName) {
		// 获取配置文件中的所有可能启用的拦截器
		HashMap<String, String> actionInterceptorMap = DALoader.getDAConfig().getActionInterceptorMap();
		if (actionInterceptorMap != null && actionInterceptorMap.size() > 0) {
			// 配置中, 存在已经启用的拦截器
			String actionInterceptors = actionInterceptorMap.get(DALoader.makeDAKey(packageId, actionName));
			if (actionInterceptors != null) {
				// 当前请求的目标Activity, 已经启用了拦截器
				String interceptorClassName = DALoader.getDAConfig().getInterceptorMap().get(actionInterceptors);
				if (interceptorClassName != null) {
					try {
						// 尝试去读取拦截器
						Class<?> clazz = Class.forName(interceptorClassName);
						Constructor<?> constructor = clazz.getConstructor(Context.class);
						if (!constructor.isAccessible()) {
							constructor.setAccessible(true);
						}

						// 执行拦截行为
						AbstractInterceptor interceptor = (AbstractInterceptor) constructor.newInstance(mContext);
						String execResult = interceptor.intercept(null);

						if (DAConstants.RESULT_OK.equalsIgnoreCase(execResult)) {
							Log.D(TAG, "OK!");
						} else {
							Log.D(TAG, "FAILED!!!");

							return true;
						}

					} catch (Exception e) {
						// 拦截器配置信息有误
						e.printStackTrace();
					}
				}
			}
		}

		return false;
	}

	/**
	 * 返回JSON的数据结构
	 * 
	 * @param intent
	 * @return
	 */
	public static String getActionData(final Intent intent) {
		if (intent == null) {
			return null;
		}

		String dataString = intent.getStringExtra(DA.EXTRA_DATA);
		return dataString;
	}

//	/**
//	 * 返回JSON的数据结构, fastjson
//	 * 
//	 * @param intent
//	 * @return
//	 */
//	public static JSONObject obtainActionData(final Intent intent) {
//		if (intent == null) {
//			return null;
//		}
//
//		String dataString = intent.getStringExtra(DA.EXTRA_DATA);
//		if (dataString != null) {
//			try {
//				return JSON.parseObject(dataString, JSONObject.class);
//			} catch (Exception e) {
//				Log.E(TAG, "DA.data is not a json string!!!");
//			}
//		}
//
//		return null;
//	}

	/**
	 * 返回请求携带的地址
	 * 
	 * @param intent
	 * @return
	 */
	public static String getActionUrl(final Intent intent) {
		if (intent == null) {
			return null;
		}

		return intent.getStringExtra(DA.EXTRA_URL);
	}

	/**
	 * 返回请求携带的事件源
	 * 
	 * @param intent
	 * @return
	 */
	public static String getActionFrom(final Intent intent) {
		if (intent == null) {
			return null;
		}

		return intent.getStringExtra(DA.EXTRA_FROM);
	}

	/**
	 * 返回请求携带的目标地址
	 * 
	 * @param intent
	 * @return
	 */
	public static String getActionTo(final Intent intent) {
		if (intent == null) {
			return null;
		}

		return intent.getStringExtra(DA.EXTRA_TO);
	}

	/**
	 * 返回请求携带的标题
	 * 
	 * @param intent
	 * @return
	 */
	public static String getActionTitle(final Intent intent) {
		if (intent == null) {
			return null;
		}

		return intent.getStringExtra(DA.EXTRA_TITLE);
	}

	public static class Builder {

		private Context mContext;

		private String mScheme;
		private String mHost;

		private String mPackageId;
		private String mActionName;

		@Deprecated
		private String mPort;
		@Deprecated
		private String mPath;

		private String mFrom;
		private String mTo;

		private String mUrl;

		private String mData;

		private String mUriString;

		private String mTitle;

		private int mRequestCode = 0;

		private int mFlags = -1;

		public Builder(Context context) {
			this.mContext = context;
		}

		public Builder setScheme(String scheme) {
			this.mScheme = scheme;
			return this;
		}

		public Builder setHost(String host) {
			this.mHost = host;
			return this;
		}

		public Builder setPackageId(String packageId) {
			this.mPackageId = packageId;
			return this;
		}

		public Builder setActionName(String actionName) {
			this.mActionName = actionName;
			return this;
		}

		@Deprecated
		public Builder setPort(String port) {
			this.mPort = port;
			return this;
		}

		@Deprecated
		public Builder setPath(String path) {
			this.mPath = path;
			return this;
		}

		public Builder setFrom(String from) {
			this.mFrom = from;
			return this;
		}

		public Builder setTo(String to) {
			this.mTo = to;
			return this;
		}

		public Builder setUrl(String url) {
			this.mUrl = url;
			return this;
		}

		public Builder setData(String data) {
			this.mData = data;
			return this;
		}

		public Builder setUriString(String uriString) {
			this.mUriString = uriString;
			return this;
		}

		public Builder setTitle(String title) {
			this.mTitle = title;
			return this;
		}

		public Builder setRequestCode(int requestCode) {
			this.mRequestCode = requestCode;
			return this;
		}

		public Builder setFlags(int flags) {
			this.mFlags = flags;
			return this;
		}

		public DA create() {
			DA da = new DA(this);
			return da;
		}

		public DA go() {
			DA da = create();
			da.go();
			return da;
		}

	}

}
