package com.opencdk.utils;

/**
 * 日志工具类
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 2.0.0
 * @since 2014-10-20
 * @Modify 2015-8-2
 */
public final class Log {

	/** use Log.v. */
	public static final int VERBOSE = 2;

	/** use Log.d. */
	public static final int DEBUG = 3;

	/** use Log.i. */
	public static final int INFO = 4;

	/** use Log.w. */
	public static final int WARN = 5;

	/** use Log.e. */
	public static final int ERROR = 6;

	/** Priority constant for the println method. */
	public static final int ASSERT = 7;

	/** 接受输出 日志范围[2,7] */
	private static int mLogLevel = 2;

	private Log() {

	}

	/**
	 * 输出VERBOSE级别的日志
	 * 
	 */
	public static void V(String tag, String msg) {
		if (mLogLevel == VERBOSE) {
			android.util.Log.v(tag, msg);
		}
	}

	/**
	 * 输出VERBOSE级别的日志
	 * 
	 */
	public static void V(String tag, String msg, Throwable tr) {
		if (mLogLevel == VERBOSE) {
			android.util.Log.v(tag, msg, tr);
		}
	}

	/**
	 * 输出DEBUG级别的日志
	 * 
	 */
	public static void D(String tag, String msg) {
		if (mLogLevel <= DEBUG) {
			android.util.Log.d(tag, msg);
		}
	}

	/**
	 * 输出DEBUG级别的日志
	 * 
	 */
	public static void D(String tag, String msg, Throwable tr) {
		if (mLogLevel <= DEBUG) {
			android.util.Log.d(tag, msg, tr);
		}
	}

	/**
	 * 输出INFO级别的日志
	 * 
	 */
	public static void I(String tag, String msg) {
		if (mLogLevel <= INFO) {
			android.util.Log.i(tag, msg);
		}
	}

	/**
	 * 输出INFO级别的日志
	 * 
	 */
	public static void I(String tag, String msg, Throwable tr) {
		if (mLogLevel <= INFO) {
			android.util.Log.i(tag, msg, tr);
		}
	}

	/**
	 * 输出WARN级别的日志
	 * 
	 */
	public static void W(String tag, String msg) {
		if (mLogLevel <= WARN) {
			android.util.Log.w(tag, msg);
		}
	}

	/**
	 * 输出WARN级别的日志
	 * 
	 */
	public static void W(String tag, Throwable tr) {
		if (mLogLevel <= WARN) {
			android.util.Log.w(tag, tr);
		}
	}

	/**
	 * 输出WARN级别的日志
	 * 
	 */
	public static void W(String tag, String msg, Throwable tr) {
		if (mLogLevel <= WARN) {
			android.util.Log.w(tag, msg, tr);
		}
	}

	/**
	 * 输出ERROR级别的日志
	 * 
	 */
	public static void E(String tag, String msg) {
		if (mLogLevel <= ERROR) {
			android.util.Log.e(tag, msg);
		}
	}

	/**
	 * 输出ERROR级别的日志
	 * 
	 */
	public static void E(String tag, String msg, Throwable tr) {
		if (mLogLevel <= ERROR) {
			android.util.Log.e(tag, msg, tr);
		}
	}

	/**
	 * 输出ASSERT级别的日志
	 * 
	 */
	public static void WTF(String tag, String msg) {
		if (mLogLevel <= ASSERT) {
			android.util.Log.wtf(tag, msg, null);
		}
	}

	/**
	 * 输出ASSERT级别的日志
	 * 
	 */
	public static void WTF(String tag, Throwable tr) {
		if (mLogLevel <= ASSERT) {
			android.util.Log.wtf(tag, tr.getMessage(), tr);
		}
	}

}
