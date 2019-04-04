package com.forkexec.hub.ws.it;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;


public class GetFoodIT extends BaseIT {

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
	public void setUp()  {
		

	}

	@After
	public void tearDown() {
		client.ctrlClear();


	}
	
	//Bad input tests
}