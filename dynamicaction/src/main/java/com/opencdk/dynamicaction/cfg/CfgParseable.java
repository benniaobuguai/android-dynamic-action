package com.opencdk.dynamicaction.cfg;

import java.io.InputStream;

/**
 * 可被解析的配置
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2016-4-17
 * @Modify 2016-4-17
 * @param <T>
 */
public interface CfgParseable<T> {

	public T parseCfg(InputStream is) throws Exception;

}
