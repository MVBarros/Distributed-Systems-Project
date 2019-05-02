package com.forkexec.pts.domain;

public class InvalidEmailException extends Exception {
	
	
	private static final long serialVersionUID = 241232231L;

	public InvalidEmailException()  {
	}

	public InvalidEmailException(String message) {
		super(message);
	}
}
