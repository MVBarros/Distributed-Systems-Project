package com.forkexec.pts.ws.cli;

/** 
 * 
 * Exception to be thrown when something is wrong with the client. 
 * 
 */
public class PointsClientException extends Exception {

	private static final long serialVersionUID = 1L;

	public PointsClientException() {
		super();
	}

	public PointsClientException(String message) {
		super(message);
	}

	public PointsClientException(Throwable cause) {
		super(cause);
	}

	public PointsClientException(String message, Throwable cause) {
		super(message, cause);
	}

}
