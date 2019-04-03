package com.forkexec.rst.domain;

/** Exception used to signal insufficient quantities */
public class InsufficientQuantityException extends Exception{

	private static final long serialVersionUID = 241241L;

	public InsufficientQuantityException()  {
	}

	public InsufficientQuantityException(String message) {
		super(message);
	}
}