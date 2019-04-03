 package com.forkexec.rst.domain;

/** Exception used to signal invalid text */
public class BadTextException extends Exception{

	private static final long serialVersionUID = 241241L;

	public BadTextException()  {
	}

	public BadTextException(String message) {
		super(message);
	}
}