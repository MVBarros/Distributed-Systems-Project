package com.forkexec.pts.ws.it;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.forkexec.pts.domain.EmailAlreadyExistsException;
import com.forkexec.pts.domain.InvalidEmailException;
import com.forkexec.pts.domain.InvalidPointsException;
import com.forkexec.pts.ws.BadInitFault_Exception;


/**
 * Test suite
 */
public class AddPointsIT extends BaseIT {
	
	// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp() {
		client.ctrlClear();

		
	}
	
	@AfterClass
	public static void oneTimeTearDown() {
		// clear remote service state after all tests
		client.ctrlClear();


	}
	
	@Before
	public void setUp() throws EmailAlreadyExistsException, InvalidEmailException, BadInitFault_Exception {
		client.activateUser("registered@mail.com");

	}

	@After
	public void tearDown() {
		client.ctrlClear();


	}
	
	//Bad Input Tests
	
	@Test(expected =InvalidEmailException.class)
	public void addPointsNullEmail() throws InvalidEmailException, InvalidPointsException {
		client.addPoints(null, 10);
	}
	
	@Test(expected =InvalidEmailException.class)
	public void addPointsEmptyEmail() throws InvalidEmailException, InvalidPointsException {
		client.addPoints("", 10);
	}
	
	@Test(expected =InvalidEmailException.class)
	public void addPointsInvalidEmail() throws InvalidEmailException, InvalidPointsException {
		client.addPoints("a@", 10);
	}
	
	public void addPointsUnregisteredEmail() throws InvalidEmailException, InvalidPointsException {
		int a = client.addPoints("notRegistered@mail.com", 10);
		assertEquals(a, 110);
	}
	
	@Test(expected =InvalidPointsException.class)
	public void addPointsZeroPoints() throws InvalidEmailException, InvalidPointsException {
		client.addPoints("registered@mail.com", 0);
	}
	
	@Test(expected =InvalidPointsException.class)
	public void addPointsNegativePoints() throws InvalidEmailException, InvalidPointsException {
		client.addPoints("registered@mail.com", -1);
	}
	
	//Main Tests
	@Test
	public void addPointsSucess() throws InvalidEmailException, InvalidPointsException {
		client.addPoints("registered@mail.com", 1);
		assertEquals(client.pointsBalance("registered@mail.com"), 101);
	}
	
	
	@Test(expected =InvalidPointsException.class)
	public void addPointsOneSucessOneFail() throws InvalidEmailException, InvalidPointsException {
		client.addPoints("registered@mail.com", 1);
		client.addPoints("registered@mail.com", -1);
	}
	
	@Test
	public void addPoints5TimesSucess() throws InvalidEmailException, InvalidPointsException {
		client.addPoints("registered@mail.com", 1);
		client.addPoints("registered@mail.com", 1);
		client.addPoints("registered@mail.com", 1);
		client.addPoints("registered@mail.com", 1);
		client.addPoints("registered@mail.com", 1);
		assertEquals(client.pointsBalance("registered@mail.com"), 105);

	}
	
	
}
















































	
	