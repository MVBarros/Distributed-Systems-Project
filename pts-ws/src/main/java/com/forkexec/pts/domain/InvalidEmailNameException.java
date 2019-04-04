package com.forkexec.pts.domain;

public class InvalidEmailNameException extends Exception {
	
	
	private static final long serialVersionUID = 241232231L;

	public InvalidEmailNameException()  {
	}

	public InvalidEmailNameException(String message) {
		super(message);
	}
}