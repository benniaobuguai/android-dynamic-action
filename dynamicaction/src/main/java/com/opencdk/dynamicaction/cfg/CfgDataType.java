package com.opencdk.dynamicaction.cfg;

/**
 * 配置数据类型
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2016-4-17
 * @Modify 2016-4-17
 */
public enum CfgDataType {

	XML, JSON;

	public static CfgDataType getDefault() {
		return XML;
	}

}
