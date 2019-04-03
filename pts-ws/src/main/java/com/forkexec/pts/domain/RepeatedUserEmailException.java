package com.forkexec.pts.domain;

public class RepeatedUserEmailException extends Exception {
	
	private static final long serialVersionUID = 2412331L;

	public RepeatedUserEmailException()  {
	}

	public RepeatedUserEmailException(String message) {
		super(message);
	}

}
