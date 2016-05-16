package com.opencdk.dynamicaction;

/**
 * DA Exception
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2016-3-13
 * @Modify 2016-3-13
 */
public class DAException extends Exception {

	private static final long serialVersionUID = 1L;

	public DAException() {
		super();
	}

	public DAException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public DAException(String detailMessage) {
		super(detailMessage);
	}

	public DAException(Throwable throwable) {
		super(throwable);
	}

}
