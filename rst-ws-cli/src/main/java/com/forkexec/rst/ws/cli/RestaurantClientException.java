package com.forkexec.rst.ws.cli;

/** 
 * 
 * Exception to be thrown when something is wrong with the client. 
 * 
 */
public class RestaurantClientException extends Exception {

	private static final long serialVersionUID = 1L;

	public RestaurantClientException() {
		super();
	}

	public RestaurantClientException(String message) {
		super(message);
	}

	public RestaurantClientException(Throwable cause) {
		super(cause);
	}

	public RestaurantClientException(String message, Throwable cause) {
		super(message, cause);
	}

}
