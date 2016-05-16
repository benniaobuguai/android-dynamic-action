package com.opencdk.dynamicaction;

import java.util.HashMap;

/**
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0 
 * @since 2016-3-13
 * @Modify 2016-3-20
 */
public class DAConfig {

	/**
	 * Const map
	 */
	private HashMap<String, Object> mConstMap = new HashMap<String, Object>();

	/**
	 * Key: packageId
	 */
	private HashMap<String, DAPackage> mPackageMap;

	/**
	 * 动态修改Action, [host]+[from]作为[Key], [to]配置成scheme方式
	 */
	private HashMap<String, String> mDynamicMap = new HashMap<String, String>();

	// TEST Start
	/**
	 * Interceptors
	 */
	private HashMap<String, String> mInterceptorMap = new HashMap<String, String>();
	private HashMap<String, String> mAcceptInterceptorMap = new HashMap<String, String>();

	/**
	 * Key: 拦截器名称, Value: 拦截器包名+类名
	 * 
	 * @return
	 */
	public HashMap<String, String> getInterceptorMap() {
		return mInterceptorMap;
	}

	public void putInterceptorMap(String name, String className) {
		this.mInterceptorMap.put(name, className);
	}

	/**
	 * Key: packageId$activityName, Value: 拦截器名称, 多个用";"隔开
	 * 
	 * @return
	 */
	public HashMap<String, String> getActionInterceptorMap() {
		return mAcceptInterceptorMap;
	}

	public void putActionInterceptorMap(String actionKey, String interceptorName) {
		this.mAcceptInterceptorMap.put(actionKey, interceptorName);
	}

	// TEST End
	
	public HashMap<String, DAPackage> getPackageMap() {
		return mPackageMap;
	}

	public void setPackageMap(HashMap<String, DAPackage> packageMap) {
		this.mPackageMap = packageMap;
	}

	public HashMap<String, Object> getConstMap() {
		return mConstMap;
	}

	public void putConstMap(String name, String value) {
		this.mConstMap.put(name, value);
	}

	public HashMap<String, String> getDynamicMap() {
		return mDynamicMap;
	}

	public String getDynamicAction(String actionKey) {
		return mDynamicMap.get(actionKey);
	}

	public void putDynamicAction(String actionKey, String scheme) {
		this.mDynamicMap.put(actionKey, scheme);
	}

	/**
	 * 配置文件是否存在此包对应的配置信息
	 * 
	 * @param packageId
	 * @return
	 */
	public boolean exists(String packageId) {
		if (this.mPackageMap != null) {
			return this.mPackageMap.containsKey(packageId);
		}

		return false;
	}

	/**
	 * 返回当前所在的包
	 * 
	 * @param packageId
	 * @return
	 */
	public DAPackage getPackage(String packageId) {
		if (this.mPackageMap != null) {
			return this.mPackageMap.get(packageId);
		}

		return null;
	}

	/**
	 * DA框架内部使用的Scheme
	 * 
	 * @return
	 */
	public String getScheme() {
		if (mConstMap.containsKey("DA.scheme")) {
			return mConstMap.get("DA.scheme").toString();
		}

		return null;
	}

	/**
	 * APP提供给外部使用的Scheme, 经过校验确认安全后, 可转换成DA框架内部使用的Scheme, 与内部进行交互.
	 * 
	 * @return
	 */
	public String getAppScheme() {
		if (mConstMap.containsKey("DA.appscheme")) {
			return mConstMap.get("DA.appscheme").toString();
		}

		return null;
	}

	public boolean isDevMode() {
		if (mConstMap.containsKey("DA.devMode")) {
			return Boolean.valueOf(mConstMap.get("DA.devMode").toString());
		}

		return false;
	}

}
