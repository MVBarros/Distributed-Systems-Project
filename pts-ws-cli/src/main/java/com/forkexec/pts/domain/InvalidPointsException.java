package com.forkexec.pts.domain;

public class InvalidPointsException extends Exception {
	private static final long serialVersionUID = 21035098L;

	public InvalidPointsException() {
	}

	public InvalidPointsException(String message) {
		super(message);
	}
}
