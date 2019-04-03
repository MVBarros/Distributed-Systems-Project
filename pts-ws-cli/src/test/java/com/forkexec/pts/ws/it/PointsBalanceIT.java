package com.forkexec.pts.ws.it;
import static org.junit.Assert.assertEquals;

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
public class PointsBalanceIT extends BaseIT {
	
	
// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp() throws EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception{
		client.ctrlClear();
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
	@Test(expected=InvalidEmailFault_Exception.class)
	public void getPointsWithNullEmail() throws InvalidEmailFault_Exception {
		client.pointsBalance(null);
	}
	
	@Test(expected=InvalidEmailFault_Exception.class)
	public void getPointsWithEmptyEmail() throws InvalidEmailFault_Exception {
		client.pointsBalance("");
	}
	
	@Test(expected=InvalidEmailFault_Exception.class)
	public void getPointsWithInvalidEmail() throws InvalidEmailFault_Exception {
		client.pointsBalance("test@");
	}
	
	
	//Main Tests
	
	@Test
	public void getPointsSucess() throws InvalidEmailFault_Exception, EmailAlreadyExistsFault_Exception {
		client.activateUser("valid@mail.com");
		int b = client.pointsBalance("valid@mail.com");
		assertEquals(b, 100);
	}
	
	@Test
	public void getTwicePointsSucess() throws InvalidEmailFault_Exception, EmailAlreadyExistsFault_Exception {
		client.activateUser("valid@mail.com");
		int a = client.pointsBalance("valid@mail.com");
		int b = client.pointsBalance("valid@mail.com");
		assertEquals(a, b);
	}
	
	@Test(expected=InvalidEmailFault_Exception.class)
	public void getPointsOneSucessOneFail() throws InvalidEmailFault_Exception, EmailAlreadyExistsFault_Exception {
		client.activateUser("valid@mail.com");
		client.pointsBalance("valid@mail.com");
		client.pointsBalance("unregistered@mail.com");
	}

	
	
	
	

}






























