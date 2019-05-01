package com.forkexec.pts.domain;

public class InvalidPointCountException extends Exception {
	private static final long serialVersionUID = 21035098L;

	public InvalidPointCountException() {
	}

	public InvalidPointCountException(String message) {
		super(message);
	}
}
