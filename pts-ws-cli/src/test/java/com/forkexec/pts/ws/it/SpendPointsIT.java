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
import com.forkexec.pts.ws.NotEnoughBalanceFault_Exception;

/**
 * Test suite
 */
public class SpendPointsIT extends BaseIT {
	
	

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
	public void spendPointsNullEmail() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
		client.spendPoints(null, 1);
	}
	
	@Test(expected =InvalidEmailFault_Exception.class)
	public void spendPointsEmptyEmail() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
		client.spendPoints("", 1);
	}
	
	@Test(expected =InvalidEmailFault_Exception.class)
	public void spendPointsInvalidEmail() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
		client.spendPoints("a@", 1);
	}
	
	@Test(expected =InvalidEmailFault_Exception.class)
	public void spendPointsUnregisteredEmail1() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
		client.spendPoints("notRegistered@mail.com", 1);
	}
	
	@Test(expected =InvalidPointsFault_Exception.class)
	public void spendPointsZeroPoints() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
		client.spendPoints("registered@mail.com", 0);
	}
	
	@Test(expected =InvalidPointsFault_Exception.class)
	public void spendPointsNegativePoints() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
		client.spendPoints("registered@mail.com", -1);
	}
	
	@Test
	public void spendPointsSuccess() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
		client.spendPoints("registered@mail.com", 1);
		assertEquals(client.pointsBalance("registered@mail.com"), 99);

	}
	
	//Main Tests
	@Test(expected =InvalidPointsFault_Exception.class)
	public void spendPointsOneSucessOneFail() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
		client.spendPoints("registered@mail.com", 1);
		client.spendPoints("registered@mail.com", -1);
	}
	
	@Test
	public void spendPoints5TimesSucess() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
		client.spendPoints("registered@mail.com", 1);
		client.spendPoints("registered@mail.com", 1);
		client.spendPoints("registered@mail.com", 1);
		client.spendPoints("registered@mail.com", 1);
		client.spendPoints("registered@mail.com", 1);
		assertEquals(client.pointsBalance("registered@mail.com"), 95);
	}
	
	@Test
	public void spendAllPoints() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
		client.spendPoints("registered@mail.com", 100);
		assertEquals(client.pointsBalance("registered@mail.com"), 0);

	}
	
	@Test(expected =NotEnoughBalanceFault_Exception.class)
	public void spendPointsMoreThanBalance() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
		client.spendPoints("registered@mail.com", 101);
	}
	
	@Test(expected =NotEnoughBalanceFault_Exception.class)
	public void spendPointsCombinedMoreThanBalance() throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
		client.spendPoints("registered@mail.com", 90);
		client.spendPoints("registered@mail.com", 11);
	}
}
