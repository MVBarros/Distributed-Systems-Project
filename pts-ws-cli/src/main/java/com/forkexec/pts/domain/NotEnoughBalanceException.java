package com.forkexec.pts.domain;

public class NotEnoughBalanceException extends Exception{
	
	
	private static final long serialVersionUID = 2412331L;

	public NotEnoughBalanceException()  {
	}

	public NotEnoughBalanceException(String message) {
		super(message);
	}

}
