package com.sil.donation.exception;

/**
 * @author Zubayer Ahamed
 *
 */
public class SilException extends Exception {

	private static final long serialVersionUID = 2262710514900732829L;

	public SilException() {
		super();
	}

	public SilException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SilException(String message, Throwable cause) {
		super(message, cause);
	}

	public SilException(String message) {
		super(message);
	}

	public SilException(Throwable cause) {
		super(cause);
	}

}
