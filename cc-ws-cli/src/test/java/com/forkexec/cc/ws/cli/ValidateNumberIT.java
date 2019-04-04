package com.forkexec.cc.ws.cli;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ValidateNumberIT extends BaseIT{
	
	@Test
	public void sucessValidation() {
		boolean result = client.validateNumber("4024007102923926");
		assertTrue(result);
	}
	
	@Test
	public void emptyValidation() {
		boolean result = client.validateNumber("");
		assertTrue(!result);
	}
	
	@Test
	public void nullValidation() {
		boolean result = client.validateNumber(null);
		assertTrue(!result);
	}
	
	
	@Test
	public void whiteSpaceValidation() {
		boolean result = client.validateNumber(" ");
		assertTrue(!result);
	}
	
	@Test
	public void newLineValidation() {
		boolean result = client.validateNumber("\n");
		assertTrue(!result);
	}
	
	@Test
	public void noSuchCCalidation() {
		boolean result = client.validateNumber("12");
		assertTrue(!result);
	}
}
