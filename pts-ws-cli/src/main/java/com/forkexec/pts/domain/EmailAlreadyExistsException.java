package com.forkexec.pts.domain;

public class EmailAlreadyExistsException extends Exception {
	private static final long serialVersionUID = 24125098L;

	public EmailAlreadyExistsException() {
	}

	public EmailAlreadyExistsException(String message) {
		super(message);
	}
}
