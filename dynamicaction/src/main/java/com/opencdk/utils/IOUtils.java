package com.opencdk.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * IO Utils
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2016-4-17
 * @Modify 2016-4-17
 */
public class IOUtils {

	public static String inputStream2String(InputStream in) throws IOException {
		String result = null;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int len = -1;
		byte[] buffer = new byte[1024 * 4];
		try {
			while ((len = in.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}

			baos.flush();
			result = baos.toString("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			throw e;
		} finally {
			if (baos != null) {
				baos.close();
			}
		}

		return result;
	}

}
