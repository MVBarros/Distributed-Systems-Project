package com.forkexec.hub.domain;

/** Exception used to signal insufficient quantities */
public class InvalidUserIdException extends Exception{

	private static final long serialVersionUID = 241241L;

	public InvalidUserIdException()  {
	}

	public InvalidUserIdException(String message) {
		super(message);
	}
}