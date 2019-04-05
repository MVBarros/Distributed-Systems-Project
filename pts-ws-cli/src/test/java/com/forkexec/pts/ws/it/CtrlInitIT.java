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

/**
 * Test suite
 */
public class CtrlInitIT extends BaseIT {
	
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
	
	//Bad Input Tests
	@Test
	public void ctrInitSucess() throws BadInitFault_Exception, EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception   {
		client.ctrlInit(0);
		client.activateUser("teste@gmail.com");
		assertEquals(client.pointsBalance("teste@gmail.com"), 0);

	}
	
	@Test(expected =BadInitFault_Exception.class)
	public void ctrInitNegativeInput() throws BadInitFault_Exception  {
		client.ctrlInit(-1);
		
	}
}
	
	