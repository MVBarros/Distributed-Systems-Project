package com.forkexec.cc.ws.cli;


/** 
 * 
 * Exception to be thrown when something is wrong with the client. 
 * 
 */
public class CCClientException extends Exception {

	private static final long serialVersionUID = 1112233L;

	public CCClientException() {
		super();
	}

	public CCClientException(String message) {
		super(message);
	}

	public CCClientException(Throwable cause) {
		super(cause);
	}

	public CCClientException(String message, Throwable cause) {
		super(message, cause);
	}

}
