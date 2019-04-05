package com.forkexec.hub.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.forkexec.hub.ws.Food;
import com.forkexec.hub.ws.FoodId;
import com.forkexec.hub.ws.FoodInit;
import com.forkexec.hub.ws.InvalidFoodIdFault_Exception;
import com.forkexec.hub.ws.InvalidFoodQuantityFault_Exception;
import com.forkexec.hub.ws.InvalidInitFault_Exception;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;

public class ClearCart extends BaseIT {
	
	@Before
	public void setUp() throws InvalidUserIdFault_Exception, InvalidInitFault_Exception, InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception {
		client.ctrlClear();

		
		// First foodinit
		FoodId foodid1 = new FoodId();
		foodid1.setMenuId("1");
		foodid1.setRestaurantId("T08_Restaurant1");
		
		Food f1 = new Food();
		f1.setId(foodid1);
		f1.setEntree("Salada de Polvo");
		f1.setPlate("Arroz");
		f1.setDessert("Bolo");
		f1.setPrice(15);
		f1.setPreparationTime(3);
		
		FoodInit foodinit1 = new FoodInit();
		foodinit1.setFood(f1);
		foodinit1.setQuantity(2);
		
		
		
		List<FoodInit> foodInits = new ArrayList<>(Arrays.asList(foodinit1));
		client.ctrlInitFood(foodInits);
		
		client.activateAccount("teste@mail.com");
		client.addFoodToCart("ola@ola.com", foodid1, 1);
		
		client.activateAccount("teste2@mail.com");

	}
	
	@After
	public void tearDown() {
		// clear remote service state after all tests
		client.ctrlClear();
	}
	
	@Test
	public void sucessClearCart() throws InvalidUserIdFault_Exception {
		client.clearCart("ola@ola.com");
		assertEquals(client.cartContents("ola@ola.com").size(), 0);
	}
	
	@Test
	public void sucessClearEmptyCart() throws InvalidUserIdFault_Exception {
		client.clearCart("ola2@ola.com");
		assertEquals(client.cartContents("ola@ola.com").size(), 0);
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void nullIdClearEmpty() throws InvalidUserIdFault_Exception {
		client.clearCart(null);
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void emptyIdClearEmpty() throws InvalidUserIdFault_Exception {
		client.clearCart("");
	}

	@Test(expected = InvalidUserIdFault_Exception.class)
	public void newlineClearEmpty() throws InvalidUserIdFault_Exception {
		client.clearCart("\n");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void tabIdClearEmpty() throws InvalidUserIdFault_Exception {
		client.clearCart("\t");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void noSuchEmailClearEmpty() throws InvalidUserIdFault_Exception {
		client.clearCart("olaola@ola.com");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void notanEmailClearEmpty() throws InvalidUserIdFault_Exception {
		client.clearCart("olaola");
	}


	
}
