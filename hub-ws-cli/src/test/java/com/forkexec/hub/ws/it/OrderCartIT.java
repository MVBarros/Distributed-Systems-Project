package com.forkexec.hub.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.forkexec.hub.ws.EmptyCartFault_Exception;
import com.forkexec.hub.ws.Food;
import com.forkexec.hub.ws.FoodId;
import com.forkexec.hub.ws.FoodInit;
import com.forkexec.hub.ws.FoodOrder;
import com.forkexec.hub.ws.InvalidFoodIdFault_Exception;
import com.forkexec.hub.ws.InvalidFoodQuantityFault_Exception;
import com.forkexec.hub.ws.InvalidInitFault_Exception;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;
import com.forkexec.hub.ws.NotEnoughPointsFault_Exception;

public class OrderCartIT extends BaseIT {
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
	public void setUp() throws InvalidInitFault_Exception, InvalidUserIdFault_Exception  {
		// First foodinit
		FoodId foodid1 = new FoodId();
		foodid1.setMenuId("1");
		foodid1.setRestaurantId("T08_Restaurant1");
		
		Food f1 = new Food();
		f1.setId(foodid1);
		f1.setEntree("Salada de Polvo");
		f1.setPlate("Arroz");
		f1.setDessert("Bolo");
		f1.setPrice(51);
		f1.setPreparationTime(1);
		
		FoodInit foodinit1 = new FoodInit();
		foodinit1.setFood(f1);
		foodinit1.setQuantity(2);
		

		List<FoodInit> foodInits = new ArrayList<>(Arrays.asList(foodinit1));
		client.ctrlInitFood(foodInits);

		client.activateAccount("teste@mail.com");

	}

	@After
	public void tearDown() {
		client.ctrlClear();

	}
	
	//Bad Input Tests
	@Test(expected =InvalidUserIdFault_Exception.class)
	public void orderCartUserIdNull() throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception  {
		client.orderCart(null);
	}
	
	
	
	@Test(expected =InvalidUserIdFault_Exception.class)
	public void orderCartUserIdWhiteSpace() throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception  {
		client.orderCart(" ");
	}
	
	
	//Main Tests
	@Test
	public void sucess() throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception, InvalidFoodIdFault_Exception  {
		FoodId foodid = new FoodId();
		foodid.setMenuId("1");
		foodid.setRestaurantId("T08_Restaurant1");
		client.clearCart("teste@mail.com");
		
		client.addFoodToCart("teste@mail.com", foodid, 1);
		
		FoodOrder fo = client.orderCart("teste@mail.com");
		
		assertEquals(fo.getItems().size(), 1);
	
		assertEquals(fo.getItems().get(0).getFoodId().getMenuId(), foodid.getMenuId());
		assertEquals(fo.getItems().get(0).getFoodId().getRestaurantId(), foodid.getRestaurantId());

	}
	
	
	@Test(expected =InvalidUserIdFault_Exception.class)
	public void orderCartUserIdNotRegistered() throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception  {
		client.orderCart("NotRegistered");
	}
	
	@Test(expected =EmptyCartFault_Exception.class)
	public void orderCartEmpty() throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception, InvalidFoodIdFault_Exception  {
	
		client.clearCart("teste@mail.com");

		client.orderCart("teste@mail.com");
	}
	
	@Test(expected=NotEnoughPointsFault_Exception.class)
	public void orderCartNotEnoughPoints() throws InvalidUserIdFault_Exception, InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, EmptyCartFault_Exception, NotEnoughPointsFault_Exception {
	
		client.clearCart("teste@mail.com");
		FoodId foodid = new FoodId();
		foodid.setMenuId("1");
		foodid.setRestaurantId("T08_Restaurant1");
		client.clearCart("teste@mail.com");
		
		client.addFoodToCart("teste@mail.com", foodid, 2);
		client.orderCart("teste@mail.com");
	}
	
	
	
	
}