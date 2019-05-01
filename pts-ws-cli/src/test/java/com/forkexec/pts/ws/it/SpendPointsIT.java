package com.forkexec.pts.ws.it;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.forkexec.pts.ws.BadInitException;
import com.forkexec.pts.ws.BadInitFault_Exception;
import com.forkexec.pts.domain.*;


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
	public void setUp() throws EmailAlreadyExistsException, InvalidEmailException, BadInitFault_Exception {
		client.activateUser("registered@mail.com");

	}

	@After
	public void tearDown() {
		client.ctrlClear();
		}
	
	//Bad Input Tests
	
	@Test(expected =InvalidEmailException.class)
	public void spendPointsNullEmail() throws InvalidEmailException, InvalidPointsException, NotEnoughBalanceException {
		client.spendPoints(null, 1);
	}
	
	@Test(expected =InvalidEmailException.class)
	public void spendPointsEmptyEmail() throws InvalidEmailException, InvalidPointsException, NotEnoughBalanceException {
		client.spendPoints("", 1);
	}
	
	@Test(expected =InvalidEmailException.class)
	public void spendPointsInvalidEmail() throws InvalidEmailException, InvalidPointsException, NotEnoughBalanceException {
		client.spendPoints("a@", 1);
	}
	
	public void spendPointsUnregisteredEmail1() throws InvalidEmailException, InvalidPointsException, NotEnoughBalanceException {
		int i = client.spendPoints("notRegistered@mail.com", 1);
		assertEquals(i, 99);
	}
	
	@Test(expected =InvalidPointsException.class)
	public void spendPointsZeroPoints() throws InvalidEmailException, InvalidPointsException, NotEnoughBalanceException {
		client.spendPoints("registered@mail.com", 0);
	}
	
	@Test(expected =InvalidPointsException.class)
	public void spendPointsNegativePoints() throws InvalidEmailException, InvalidPointsException, NotEnoughBalanceException {
		client.spendPoints("registered@mail.com", -1);
	}
	
	@Test
	public void spendPointsSuccess() throws InvalidEmailException, InvalidPointsException, NotEnoughBalanceException {
		client.spendPoints("registered@mail.com", 1);
		assertEquals(client.pointsBalance("registered@mail.com"), 99);

	}
	
	//Main Tests
	@Test(expected =InvalidPointsException.class)
	public void spendPointsOneSucessOneFail() throws InvalidEmailException, InvalidPointsException, NotEnoughBalanceException {
		client.spendPoints("registered@mail.com", 1);
		client.spendPoints("registered@mail.com", -1);
	}
	
	@Test
	public void spendPoints5TimesSucess() throws InvalidEmailException, InvalidPointsException, NotEnoughBalanceException {
		client.spendPoints("registered@mail.com", 1);
		client.spendPoints("registered@mail.com", 1);
		client.spendPoints("registered@mail.com", 1);
		client.spendPoints("registered@mail.com", 1);
		client.spendPoints("registered@mail.com", 1);
		assertEquals(client.pointsBalance("registered@mail.com"), 95);
	}
	
	@Test
	public void spendAllPoints() throws InvalidEmailException, InvalidPointsException, NotEnoughBalanceException {
		client.spendPoints("registered@mail.com", 100);
		assertEquals(client.pointsBalance("registered@mail.com"), 0);

	}
	
	@Test(expected =NotEnoughBalanceException.class)
	public void spendPointsMoreThanBalance() throws InvalidEmailException, InvalidPointsException, NotEnoughBalanceException {
		client.spendPoints("registered@mail.com", 101);
	}
	
	@Test(expected =NotEnoughBalanceException.class)
	public void spendPointsCombinedMoreThanBalance() throws InvalidEmailException, InvalidPointsException, NotEnoughBalanceException {
		client.spendPoints("registered@mail.com", 90);
		client.spendPoints("registered@mail.com", 11);
	}
}
