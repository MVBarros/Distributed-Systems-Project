package com.forkexec.pts.ws.it;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.forkexec.pts.domain.*;

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
	@Test(expected =InvalidEmailException.class)
	public void activateUserNullEmail() throws  InvalidEmailException {
		client.activateUser(null);
	}
	
	@Test(expected =InvalidEmailException.class)
	public void activateUserEmptyEmail() throws  InvalidEmailException {
		client.activateUser("");
	}
	@Test(expected =InvalidEmailException.class)
	public void activateUserWhiteSpaceEmail() throws  InvalidEmailException {
		client.activateUser(" ");
	}
	@Test(expected =InvalidEmailException.class)
	public void activateUserNewilineEmail() throws  InvalidEmailException {
		client.activateUser("\n");
	}
	
	@Test(expected =InvalidEmailException.class)
	public void activateUserTabEmail() throws  InvalidEmailException {
		client.activateUser("\t");
	}
	
	@Test(expected =InvalidEmailException.class)
	public void activateUserInvalidEmail() throws  InvalidEmailException {
		client.activateUser("a@");
	}
	
	@Test(expected =InvalidEmailException.class)
	public void activateUserInvalidEmail2() throws  InvalidEmailException {
		client.activateUser("a.@");
	}
	
	@Test(expected =InvalidEmailException.class)
	public void activateUserInvalidEmail3() throws  InvalidEmailException {
		client.activateUser(".33@");
	}
	
	@Test(expected =InvalidEmailException.class)
	public void activateUserInvalidEmail4() throws  InvalidEmailException {
		client.activateUser("@");
	}
	
	@Test(expected =InvalidEmailException.class)
	public void activateUserInvalidEmail5() throws  InvalidEmailException {
		client.activateUser("@a");
	}
	
	@Test(expected =InvalidEmailException.class)
	public void activateUserInvalidEmail6() throws  InvalidEmailException {
		client.activateUser("@a..");
	}
	
	@Test
	public void activateUserSucess() throws  InvalidEmailException {
		client.activateUser("teste@gmail.com");
	}
	
	@Test
	public void activateTwoUsersSucess() throws  InvalidEmailException {
		client.activateUser("teste@gmail.com");
		client.activateUser("teste1@gmail.com");
	}

}






























