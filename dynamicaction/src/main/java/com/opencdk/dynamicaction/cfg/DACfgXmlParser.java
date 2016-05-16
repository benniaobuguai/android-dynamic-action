package com.opencdk.dynamicaction.cfg;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import android.text.TextUtils;

import com.opencdk.dynamicaction.DAAction;
import com.opencdk.dynamicaction.DAConfig;
import com.opencdk.dynamicaction.DAException;
import com.opencdk.dynamicaction.DALoader;
import com.opencdk.dynamicaction.DAPackage;

/**
 * Xml数据结构的配置解析器
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2016-4-17
 * @Modify 2016-4-17
 */
public class DACfgXmlParser implements CfgParseable<DAConfig> {

	@Override
	public DAConfig parseCfg(InputStream is) throws DAException {
		DocumentBuilderFactory dBuilderFactory = DocumentBuilderFactory.newInstance();
		Document doc = null;
		try {
			DocumentBuilder dBuilder = dBuilderFactory.newDocumentBuilder();
			doc = dBuilder.parse(is);
		} catch (Exception e) {
			throw new DAException("文件加载/解析出错!!!", e);
		}

		if (doc == null) {
			return null;
		}

		DAConfig daConfig = new DAConfig();

		Element root = doc.getDocumentElement();
		NodeList constNodeList = root.getElementsByTagName("constant");
		parseConstantData(daConfig, constNodeList);

		NodeList packageNodeList = root.getElementsByTagName("package");
		parsePackageList(daConfig, packageNodeList);

		NodeList interceptorsNodeList = root.getElementsByTagName("interceptors");
		parseInterceptorList(daConfig, interceptorsNodeList);
		
		
		Element interceptorsElement = null;
		if (interceptorsNodeList != null) {
			interceptorsElement = (Element) interceptorsNodeList.item(0);
		}
		if (interceptorsElement != null) {
			NodeList interceptorNodeList = interceptorsElement.getElementsByTagName("interceptor");

			if (interceptorNodeList != null) {
				int interceptorCount = interceptorNodeList.getLength();
				Element interceptorElement = null;

				String interceptorName = null;
				String interceptorClass = null;

				for (int i = 0; i < interceptorCount; i++) {
					interceptorElement = (Element) interceptorNodeList.item(i);
					interceptorName = interceptorElement.getAttribute("name");
					interceptorClass = interceptorElement.getAttribute("class");

					daConfig.putInterceptorMap(interceptorName, interceptorClass);
				}
			}

			NodeList actionInterceptorNodeList = root.getElementsByTagName("actionInterceptor");
			Element actionInterceptorElement = null;
			if (actionInterceptorNodeList != null) {
				actionInterceptorElement = (Element) actionInterceptorNodeList.item(0);
			}
			NodeList acceptNodeList = null;
			if (actionInterceptorElement != null) {
				acceptNodeList = actionInterceptorElement.getElementsByTagName("accept");
			}
			if (acceptNodeList != null) {
				int acceptCount = acceptNodeList.getLength();
				Element acceptElement = null;

				String actionKey = null;
				String interceptorName = null;
				for (int i = 0; i < acceptCount; i++) {
					acceptElement = (Element) acceptNodeList.item(i);
					actionKey = acceptElement.getAttribute("name");
					NodeList interceptorRef = acceptElement.getElementsByTagName("interceptor-ref");

					if (interceptorRef != null) {
						for (int j = 0; j < interceptorRef.getLength(); j++) {
							interceptorName = interceptorRef.item(j).getTextContent();
							daConfig.putActionInterceptorMap(actionKey, interceptorName);
						}
					}
				}
			}
		}

		return daConfig;
	}

	private void parseConstantData(DAConfig daConfig, NodeList constNodeList) {
		if (constNodeList != null) {
			String name = null;
			String value = null;

			int constCount = constNodeList.getLength();
			Element constElement = null;
			NamedNodeMap namedNodeMap = null;
			for (int i = 0; i < constCount; i++) {
				constElement = (Element) constNodeList.item(i);

				namedNodeMap = constElement.getAttributes();
				name = namedNodeMap.getNamedItem("name").getNodeValue();
				value = namedNodeMap.getNamedItem("value").getNodeValue();

				daConfig.putConstMap(name, value);
			}
		}
	}

	private void parsePackageList(DAConfig daConfig, NodeList packageNodeList) {
		if (packageNodeList != null) {
			int packageCount = packageNodeList.getLength();

			Element packageElement = null;
			HashMap<String, DAPackage> packageMap = new HashMap<String, DAPackage>();
			DAPackage pkg = null;
			for (int i = 0; i < packageCount; i++) {
				packageElement = (Element) packageNodeList.item(i);
				pkg = parsePackage(daConfig, packageElement);

				packageMap.put(pkg.getPackageId(), pkg);
			}

			daConfig.setPackageMap(packageMap);
		}
	}

	private DAPackage parsePackage(DAConfig daConfig, Element packageElement) {
		DAPackage pkg = new DAPackage();
		pkg.setPackageId(packageElement.getAttribute("id"));
		pkg.setPackageName(packageElement.getAttribute("name"));

		NodeList actionNodeList = packageElement.getElementsByTagName("action");
		if (actionNodeList == null) {
			return pkg;
		}
		List<DAAction> actions = parseActionList(daConfig, actionNodeList, pkg.getPackageId());
		pkg.setActions(actions);

		return pkg;
	}

	private List<DAAction> parseActionList(DAConfig daConfig, NodeList actionNodeList, String packageId) {
		List<DAAction> actions = new ArrayList<DAAction>();
		int actionCount = actionNodeList.getLength();
		Element actionElement = null;

		String actionName = null;
		String icon = null;
		String label = null;
		String from = null;
		String to = null;
		String title = null;

		DAAction action = null;
		for (int j = 0; j < actionCount; j++) {
			actionElement = (Element) actionNodeList.item(j);

			actionName = actionElement.getAttribute("name");
			label = actionElement.getAttribute("label");
			icon = actionElement.getAttribute("icon");
			from = actionElement.getAttribute("from");
			to = actionElement.getAttribute("to");
			title = actionElement.getAttribute("title");

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
				String daKey = DALoader.makeDAKey(packageId, actionName, from);
				daConfig.putDynamicAction(daKey, to);
			}
		}

		return actions;
	}

	private void parseInterceptorList(DAConfig daConfig, NodeList interceptorsNodeList) {
		Element interceptorsElement = null;
		if (interceptorsNodeList != null) {
			interceptorsElement = (Element) interceptorsNodeList.item(0);
		}
		if (interceptorsElement != null) {
			NodeList interceptorNodeList = interceptorsElement.getElementsByTagName("interceptor");

			if (interceptorNodeList != null) {
				int interceptorCount = interceptorNodeList.getLength();
				Element interceptorElement = null;

				String interceptorName = null;
				String interceptorClass = null;
				for (int i = 0; i < interceptorCount; i++) {
					interceptorElement = (Element) interceptorNodeList.item(i);
					interceptorName = interceptorElement.getAttribute("name");
					interceptorClass = interceptorElement.getAttribute("class");

					daConfig.putInterceptorMap(interceptorName, interceptorClass);
				}
			}

			NodeList actionInterceptorNodeList = interceptorsElement.getElementsByTagName("actionInterceptor");
			Element actionInterceptorElement = null;
			if (actionInterceptorNodeList != null) {
				actionInterceptorElement = (Element) actionInterceptorNodeList.item(0);
			}
			NodeList acceptNodeList = null;
			if (actionInterceptorElement != null) {
				acceptNodeList = actionInterceptorElement.getElementsByTagName("accept");
			}
			if (acceptNodeList != null) {
				int acceptCount = acceptNodeList.getLength();
				Element acceptElement = null;

				String actionKey = null;
				String interceptorName = null;
				for (int i = 0; i < acceptCount; i++) {
					acceptElement = (Element) acceptNodeList.item(i);
					actionKey = acceptElement.getAttribute("name");
					NodeList interceptorRef = acceptElement.getElementsByTagName("interceptor-ref");

					if (interceptorRef != null) {
						for (int j = 0; j < interceptorRef.getLength(); j++) {
							interceptorName = interceptorRef.item(j).getTextContent();
							daConfig.putActionInterceptorMap(actionKey, interceptorName);
						}
					}
				}
			}
		}
	}

}
