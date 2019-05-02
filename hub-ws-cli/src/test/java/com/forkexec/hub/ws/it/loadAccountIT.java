package com.forkexec.hub.ws.it;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.forkexec.hub.ws.InvalidCreditCardFault_Exception;
import com.forkexec.hub.ws.InvalidInitFault_Exception;
import com.forkexec.hub.ws.InvalidMoneyFault_Exception;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;

public class loadAccountIT extends BaseIT {

	@Before
	public void setUp() throws InvalidUserIdFault_Exception {
		client.activateAccount("ola@ola.com");
	}

	@After
	public void tearDown() {
		// clear remote service state after all tests
		client.ctrlClear();
	}

	// Main Test
	@Test
	public void sucessLoadAccountwith10()
			throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount("ola@ola.com", 10, "4024007102923926");

		assertEquals(client.accountBalance("ola@ola.com"), 1100);
	}

	@Test
	public void sucessLoadAccountwith20()
			throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount("ola@ola.com", 20, "4024007102923926");

		assertEquals(client.accountBalance("ola@ola.com"), 2200);
	}

	@Test
	public void sucessLoadAccountwith30()
			throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount("ola@ola.com", 30, "4024007102923926");

		assertEquals(client.accountBalance("ola@ola.com"), 3400);
	}

	@Test
	public void sucessLoadAccountwith50()
			throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount("ola@ola.com", 50, "4024007102923926");

		assertEquals(client.accountBalance("ola@ola.com"), 5600);
	}

	// badInputTests
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void nullEmailLoadAccount()
			throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount(null, 10, "4024007102923926");

	}

	@Test(expected = InvalidUserIdFault_Exception.class)
	public void emptyEmailLoadAccount()
			throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount("", 10, "4024007102923926");
	}

	@Test(expected = InvalidUserIdFault_Exception.class)
	public void newlineLoadAccount()
			throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount("\n", 10, "4024007102923926");
	}

	@Test(expected = InvalidUserIdFault_Exception.class)
	public void tablineLoadAccount()
			throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount("\t", 10, "4024007102923926");
	}

	@Test(expected = InvalidMoneyFault_Exception.class)
	public void negativePointsLoadAccount()
			throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount("ola@ola.com", -10, "4024007102923926");
	}

	@Test(expected = InvalidMoneyFault_Exception.class)
	public void nonValidPointsLoadAccount()
			throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount("ola@ola.com", 15, "4024007102923926");
	}

	@Test(expected = InvalidCreditCardFault_Exception.class)
	public void invalidCreditCardLoadAccount()
			throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount("ola@ola.com", 10, "4");
	}

	@Test(expected = InvalidCreditCardFault_Exception.class)
	public void nullCreditCardLoadAccount()
			throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount("ola@ola.com", 10, null);
	}

	@Test(expected = InvalidCreditCardFault_Exception.class)
	public void emptyCreditCardLoadAccount()
			throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		client.loadAccount("ola@ola.com", 10, "");
	}
}