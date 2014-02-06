package com.rootrip.platform.exception;

/**
 * 框架底层抛出的异常,一般不捕获
 * @author liubin
 *
 */
public class PlatformException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public PlatformException() {
		super();
	}

	public PlatformException(String message, Throwable cause) {
		super(message, cause);
	}

	public PlatformException(String message) {
		super(message);
	}

	public PlatformException(Throwable cause) {
		super(cause);
	}

}
