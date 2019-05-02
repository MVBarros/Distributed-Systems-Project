package com.forkexec.hub.domain;

public class CartQuantityException extends Exception {

	private static final long serialVersionUID = 2412232231L;

	public CartQuantityException()  {
	}

	public CartQuantityException(String message) {
		super(message);
	}
}
