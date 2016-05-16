package com.opencdk.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.os.Environment;

public class Utils {

	/**
	 * 返回assets配置文件的输入流
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static InputStream getInternalConfig(Context context, String fileName) throws IOException {
		return context.getApplicationContext().getResources().getAssets().open(fileName);
	}

	/**
	 * 返回sdcard或/data/data/files目录下配置文件的输入流
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static InputStream getExternalConfig(Context context, String fileName) throws IOException {
		final String rootDirName = context.getPackageName();
		String dir = null;
		if (sdcardRunning()) {
			dir = Environment.getExternalStorageDirectory() + File.separator + rootDirName;
		} else {
			dir = context.getFilesDir().getPath() + File.separator + rootDirName;
		}

		File cfgFile = new File(dir, fileName);
		if (!cfgFile.exists()) {
			return null;
		}

		return new FileInputStream(cfgFile);
	}

	/**
	 * sdcard是否正常使用
	 * 
	 * @return
	 */
	public static boolean sdcardRunning() {
		String state = android.os.Environment.getExternalStorageState();
		return android.os.Environment.MEDIA_MOUNTED.equals(state);
	}

}
