package com.forkexec.hub.ws.cli;

/** 
 * 
 * Exception to be thrown when something is wrong with the client. 
 * 
 */
public class HubClientException extends Exception {

	private static final long serialVersionUID = 1L;

	public HubClientException() {
		super();
	}

	public HubClientException(String message) {
		super(message);
	}

	public HubClientException(Throwable cause) {
		super(cause);
	}

	public HubClientException(String message, Throwable cause) {
		super(message, cause);
	}

}
