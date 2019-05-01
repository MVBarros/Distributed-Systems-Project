package com.forkexec.pts.ws.cli;

public class InvalidEmailNameException extends Exception {
	
	
	private static final long serialVersionUID = 241232231L;

	public InvalidEmailNameException()  {
	}

	public InvalidEmailNameException(String message) {
		super(message);
	}
}
