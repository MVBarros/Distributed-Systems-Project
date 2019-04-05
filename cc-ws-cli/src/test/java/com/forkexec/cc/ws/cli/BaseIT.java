package com.forkexec.cc.ws.cli;

import java.io.IOException;
import java.util.Properties;


import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Base class for testing a Park Load properties from test.properties
 */
public class BaseIT {

	protected static CCClient client;

	@BeforeClass
	public static void oneTimeSetup() throws Exception {
		
		client = new CCClient();

		client.setVerbose(true);
	}

	@AfterClass
	public static void cleanup() {
	}

}
