

package com.forkexec.rst.domain;

/** Exception used to signal invalid ids */
public class BadMenuIdException extends Exception{

	private static final long serialVersionUID = 241241L;

	public BadMenuIdException()  {
	}

	public BadMenuIdException(String message) {
		super(message);
	}
}