package com.forkexec.hub.ws.it;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.forkexec.hub.ws.InvalidUserIdFault_Exception;

public class ActivateAccountIT extends BaseIT {

	@Before
	public void setUp() {
		client.ctrlClear();
	}
	

	// Success tests
	@Test
	public void successActivateAccount() throws InvalidUserIdFault_Exception {
		client.activateAccount("ola@ola.com");

		// no exception is thrown
	}

	@Test
	public void successActivate2Accounts() throws InvalidUserIdFault_Exception {
		client.activateAccount("ola@ola.com");
		client.activateAccount("ola2@ola.com");

		// no exception is thrown
	}
	
	//Bad Input Tests
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void nullActivateAccount() throws InvalidUserIdFault_Exception {
		client.activateAccount(null);
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void notanEmailActivateAccount() throws InvalidUserIdFault_Exception {
		client.activateAccount("ola");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void sameEmailActivateAccount() throws InvalidUserIdFault_Exception {
		client.activateAccount("ola@ola.com");
		client.activateAccount("ola@ola.com");

	}
}
