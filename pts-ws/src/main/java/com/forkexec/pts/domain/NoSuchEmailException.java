package com.forkexec.pts.domain;

public class NoSuchEmailException extends Exception {
	
	private static final long serialVersionUID = 21515321098L;

	public NoSuchEmailException()  {
	}

	public NoSuchEmailException(String message) {
		super(message);
	}

}
