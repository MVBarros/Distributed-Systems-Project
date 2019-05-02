package com.forkexec.pts.ws.it;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.forkexec.pts.domain.EmailAlreadyExistsException;
import com.forkexec.pts.domain.InvalidEmailException;




/**
 * Test suite
 */
public class PointsBalanceIT extends BaseIT {
	
	
// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp(){
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
	@Test(expected=InvalidEmailException.class)
	public void getPointsWithNullEmail() throws InvalidEmailException {
		client.pointsBalance(null);
	}
	
	@Test(expected=InvalidEmailException.class)
	public void getPointsWithEmptyEmail() throws InvalidEmailException {
		client.pointsBalance("");
	}
	
	@Test(expected=InvalidEmailException.class)
	public void getPointsWithInvalidEmail() throws InvalidEmailException {
		client.pointsBalance("test@");
	}
	
	
	//Main Tests
	
	@Test
	public void getPointsSucess() throws  InvalidEmailException, EmailAlreadyExistsException {
		client.activateUser("valid@mail.com");
		int b = client.pointsBalance("valid@mail.com");
		assertEquals(b, 100);
	}
	
	@Test
	public void getTwicePointsSucess() throws InvalidEmailException, EmailAlreadyExistsException{
		client.activateUser("valid@mail.com");
		int a = client.pointsBalance("valid@mail.com");
		int b = client.pointsBalance("valid@mail.com");
		assertEquals(a, b);
	}
	
	@Test(expected=InvalidEmailException.class)
	public void getPointsOneSucessOneFail() throws InvalidEmailException, EmailAlreadyExistsException{
		client.activateUser("valid@mail.com");
		client.pointsBalance("valid@mail.com");
		client.pointsBalance("ololol");
	}

	
	
	
	

}






























