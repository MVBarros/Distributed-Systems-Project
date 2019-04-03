

package com.forkexec.rst.domain;

/** Exception used to signal invalid quantities */
public class BadQuantityException extends Exception{

	private static final long serialVersionUID = 241241L;

	public BadQuantityException()  {
	}

	public BadQuantityException(String message) {
		super(message);
	}
}