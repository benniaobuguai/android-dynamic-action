package com.opencdk.dynamicaction.cfg;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.opencdk.dynamicaction.DAAction;
import com.opencdk.dynamicaction.DAConfig;
import com.opencdk.dynamicaction.DAException;
import com.opencdk.dynamicaction.DALoader;
import com.opencdk.dynamicaction.DAPackage;
import com.opencdk.utils.IOUtils;

public class DACfgJsonParser implements CfgParseable<DAConfig> {

	@Override
	public DAConfig parseCfg(InputStream is) throws DAException {
		String configString = null;
		try {
			configString = IOUtils.inputStream2String(is);
		} catch (IOException e) {
			throw new DAException("文件加载!!!", e);
		}

		if (TextUtils.isEmpty(configString)) {
			return null;
		}

		JSONObject cfgJSON = null;
		try {
			cfgJSON = new JSONObject(configString);
		} catch (JSONException e) {
			throw new DAException("文件加载/解析出错, 非JSON!!!", e);
		}

		final DAConfig daConfig = new DAConfig();
		JSONObject constantsJSON = cfgJSON.optJSONObject("constants");
		parseConstantData(daConfig, constantsJSON);

		JSONArray packagesJSONArray = cfgJSON.optJSONArray("packages");
		parsePackageList(daConfig, packagesJSONArray);

		JSONObject interceptorJSON = cfgJSON.optJSONObject("interceptor");
		parseInterceptorList(daConfig, interceptorJSON);

		return daConfig;
	}

	private void parseConstantData(DAConfig daConfig, JSONObject constantsJSON) {
		if (constantsJSON != null) {
			Iterator<String> constantKeys = constantsJSON.keys();
			if (constantKeys != null) {
				String name = null;
				String value = null;
				while (constantKeys.hasNext()) {
					name = constantKeys.next();
					value = constantsJSON.optString(name, "");
					daConfig.putConstMap(name, value);
				}
			}
		}
	}

	private void parsePackageList(DAConfig daConfig, JSONArray packagesJSONArray) {
		if (packagesJSONArray != null) {
			int packageCount = packagesJSONArray.length();
			JSONObject packageJSON = null;

			HashMap<String, DAPackage> packageMap = new HashMap<String, DAPackage>();
			DAPackage pkg = null;
			for (int i = 0; i < packageCount; i++) {
				packageJSON = packagesJSONArray.optJSONObject(i);
				pkg = parsePackage(daConfig, packageJSON);
				
				packageMap.put(pkg.getPackageId(), pkg);
			}

			daConfig.setPackageMap(packageMap);
		}
	}

	private DAPackage parsePackage(DAConfig daConfig, JSONObject packageJSON) {
		DAPackage pkg = new DAPackage();
		pkg.setPackageId(packageJSON.optString("id"));
		pkg.setPackageName(packageJSON.optString("name"));

		JSONArray actionsJSONArray = packageJSON.optJSONArray("actions");
		if (actionsJSONArray == null) {
			return pkg;
		}

		List<DAAction> actions = parseActionList(daConfig, pkg, actionsJSONArray);
		pkg.setActions(actions);

		return pkg;
	}

	private List<DAAction> parseActionList(DAConfig daConfig, DAPackage pkg, JSONArray actionsJSONArray) {
		DAAction action = null;
		int actionCount = actionsJSONArray.length();
		String actionName = null;
		String icon = null;
		String label = null;
		String from = null;
		String to = null;
		String title = null;
		JSONObject actionJSON = null;

		List<DAAction> actions = new ArrayList<DAAction>();
		for (int j = 0; j < actionCount; j++) {
			actionJSON = actionsJSONArray.optJSONObject(j);

			actionName = actionJSON.optString("name");
			label = actionJSON.optString("label");
			icon = actionJSON.optString("icon");
			from = actionJSON.optString("from");
			to = actionJSON.optString("to");
			title = actionJSON.optString("title");

			action = new DAAction();
			action.setName(actionName);
			action.setLabel(label);
			action.setIcon(icon);
			action.setFrom(from);
			action.setTo(to);
			action.setTitle(title == null ? "" : title);

			actions.add(action);

			if (!TextUtils.isEmpty(actionName) && !TextUtils.isEmpty(to)) {
				// 同时配置to, 才具有动态跳转的能力.
				String daKey = DALoader.makeDAKey(pkg.getPackageId(), actionName, from);
				daConfig.putDynamicAction(daKey, to);
			}
		}

		return actions;
	}

	private void parseInterceptorList(DAConfig daConfig, JSONObject interceptorsJSON) {
		JSONArray interceptorsJSONArray = interceptorsJSON.optJSONArray("interceptors");

		if (interceptorsJSONArray != null) {
			int interceptorsCount = interceptorsJSONArray.length();
			JSONObject interceptorJSON = null;
			for (int i = 0; i < interceptorsCount; i++) {
				interceptorJSON = interceptorsJSONArray.optJSONObject(i);

				String interceptorName = null;
				String interceptorClass = null;
				if (interceptorJSON != null) {
					interceptorName = interceptorJSON.optString("name");
					interceptorClass = interceptorJSON.optString("class");

					daConfig.putInterceptorMap(interceptorName, interceptorClass);
				}
			}
		}

		JSONArray acceptsJSONArray = interceptorsJSON.optJSONArray("accepts");
		if (acceptsJSONArray != null) {
			int acceptCount = acceptsJSONArray.length();
			JSONObject acceptJSON = null;

			String actionKey = null;
			String interceptorName = null;
			for (int i = 0; i < acceptCount; i++) {
				acceptJSON = acceptsJSONArray.optJSONObject(i);
				actionKey = acceptJSON.optString("name");

				JSONArray interceptorRefJSONArray = acceptJSON.optJSONArray("interceptor-ref");
				if (interceptorRefJSONArray != null) {
					int refCount = interceptorRefJSONArray.length();
					for (int j = 0; j < refCount; j++) {
						interceptorName = interceptorRefJSONArray.optString(j);
						daConfig.putActionInterceptorMap(actionKey, interceptorName);
					}
				}
			}
		}
	}

}
