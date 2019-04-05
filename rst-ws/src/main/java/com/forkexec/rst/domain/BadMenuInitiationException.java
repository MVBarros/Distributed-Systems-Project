package com.forkexec.rst.domain;

/** Exception used to signal Duplicate Menu Identifiers */
public class BadMenuInitiationException extends Exception {

	private static final long serialVersionUID = 241241L;

	public BadMenuInitiationException() {
	}

	public BadMenuInitiationException(String message) {
		super(message);
	}
}
