package com.forkexec.pts.ws.it;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.forkexec.pts.ws.BadInitFault_Exception;
import com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;
import com.forkexec.pts.ws.InvalidPointsFault_Exception;

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
	public void setUp() throws EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception, BadInitFault_Exception {
		client.activateUser("registered@mail.com");

	}

	@After
	public void tearDown() {
		client.ctrlClear();


	}
	
	//Bad Input Tests
	
	@Test(expected =InvalidEmailFault_Exception.class)
	public void addPointsNullEmail() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception {
		client.addPoints(null, 10);
	}
	
	@Test(expected =InvalidEmailFault_Exception.class)
	public void addPointsEmptyEmail() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception {
		client.addPoints("", 10);
	}
	
	@Test(expected =InvalidEmailFault_Exception.class)
	public void addPointsInvalidEmail() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception {
		client.addPoints("a@", 10);
	}
	
	@Test(expected =InvalidEmailFault_Exception.class)
	public void addPointsUnregisteredEmail() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception {
		client.addPoints("notRegistered@mail.com", 10);
	}
	
	@Test(expected =InvalidPointsFault_Exception.class)
	public void addPointsZeroPoints() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception {
		client.addPoints("registered@mail.com", 0);
	}
	
	@Test(expected =InvalidPointsFault_Exception.class)
	public void addPointsNegativePoints() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception {
		client.addPoints("registered@mail.com", -1);
	}
	
	//Main Tests
	@Test
	public void addPointsSucess() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception {
		client.addPoints("registered@mail.com", 1);
		assertEquals(client.pointsBalance("registered@mail.com"), 101);
	}
	
	
	@Test(expected =InvalidPointsFault_Exception.class)
	public void addPointsOneSucessOneFail() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception {
		client.addPoints("registered@mail.com", 1);
		client.addPoints("registered@mail.com", -1);
	}
	
	@Test
	public void addPoints5TimesSucess() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception {
		client.addPoints("registered@mail.com", 1);
		client.addPoints("registered@mail.com", 1);
		client.addPoints("registered@mail.com", 1);
		client.addPoints("registered@mail.com", 1);
		client.addPoints("registered@mail.com", 1);
		assertEquals(client.pointsBalance("registered@mail.com"), 105);

	}
	
	
}
















































	
	