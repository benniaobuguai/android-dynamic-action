package com.opencdk.dynamicaction.cfg;

import java.io.InputStream;

import com.opencdk.dynamicaction.DAConfig;

/**
 * DA配置文件解析器
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2016-4-17
 * @Modify 2016-4-17
 */
public class DACfgParser {

	private CfgDataType mCfgDataType;

	public DACfgParser(CfgDataType cfgDataType) {
		this.mCfgDataType = cfgDataType;
	}

	public DAConfig parse(InputStream is) throws Exception {
		CfgParseable<DAConfig> cfgParser = null;
		switch (mCfgDataType) {
		case XML:
			cfgParser = new DACfgXmlParser();
			break;
		case JSON:
		default:
			cfgParser = new DACfgJsonParser();
			break;
		}

		return cfgParser.parseCfg(is);
	}

}
