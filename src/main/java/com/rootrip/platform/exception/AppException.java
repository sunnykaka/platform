package com.rootrip.platform.exception;

/**
 * view层必须要捕获的异常
 * @author liubin
 *
 */
public class AppException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public AppException() {
		super();
	}

	public AppException(String message, Throwable cause) {
		super(message, cause);
	}

	public AppException(String message) {
		super(message);
	}

	public AppException(Throwable cause) {
		super(cause);
	}

}