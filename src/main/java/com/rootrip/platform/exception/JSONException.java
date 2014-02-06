package com.rootrip.platform.exception;

/**
 * @author liubin
 *
 */
public class JSONException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public JSONException() {
		super();
	}

	public JSONException(String message, Throwable cause) {
		super(message, cause);
	}

	public JSONException(String message) {
		super(message);
	}

	public JSONException(Throwable cause) {
		super(cause);
	}

}