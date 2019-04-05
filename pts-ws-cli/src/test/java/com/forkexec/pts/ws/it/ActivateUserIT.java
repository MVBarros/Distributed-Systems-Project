package com.forkexec.pts.ws.it;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;

/**
 * Test suite
 */
public class ActivateUserIT extends BaseIT {
	
	
	// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp(){
		// clear remote service state before all tests
	}
	
	@AfterClass
	public static void oneTimeTearDown() {
		// clear remote service state after all tests

	}
	
	@Before
	public void setUp() {
		client.ctrlClear();
	}

	@After
	public void tearDown() {
		client.ctrlClear();
	}
	
	// bad input tests
	@Test(expected =InvalidEmailFault_Exception.class)
	public void activateUserNullEmail() throws EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception {
		client.activateUser(null);
	}
	
	@Test(expected =InvalidEmailFault_Exception.class)
	public void activateUserEmptyEmail() throws EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception {
		client.activateUser("");
	}
	
	@Test(expected =InvalidEmailFault_Exception.class)
	public void activateUserInvalidEmail() throws EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception {
		client.activateUser("a@");
	}
	
	@Test
	public void activateUserSucess() throws EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception {
		client.activateUser("teste@gmail.com");
	}
	
	@Test
	public void activateTwoUsersSucess() throws EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception {
		client.activateUser("teste@gmail.com");
		client.activateUser("teste1@gmail.com");
	}
	
	@Test(expected =EmailAlreadyExistsFault_Exception.class)
	public void activateWithMailAlreadyUsed() throws EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception {
		client.activateUser("teste@gmai.com");
		client.activateUser("teste@gmai.com");
	}
	
	
	

}






























