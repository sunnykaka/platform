package com.rootrip.platform.exception;

/**
 * @author liubin
 *
 */
public class PermissionException extends BusinessException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public PermissionException() {
		super();
	}

	public PermissionException(String message, Throwable cause) {
		super(message, cause);
	}

	public PermissionException(String message) {
		super(message);
	}

	public PermissionException(Throwable cause) {
		super(cause);
	}

}
