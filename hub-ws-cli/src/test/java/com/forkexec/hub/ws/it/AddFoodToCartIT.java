package com.forkexec.hub.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.forkexec.hub.ws.Food;
import com.forkexec.hub.ws.FoodId;
import com.forkexec.hub.ws.FoodInit;
import com.forkexec.hub.ws.FoodOrderItem;
import com.forkexec.hub.ws.InvalidFoodIdFault_Exception;
import com.forkexec.hub.ws.InvalidFoodQuantityFault_Exception;
import com.forkexec.hub.ws.InvalidInitFault_Exception;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;

public class AddFoodToCartIT extends BaseIT {
	// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp() throws InvalidInitFault_Exception, InvalidUserIdFault_Exception {
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

		// Second foodinit
		FoodId foodid2 = new FoodId();
		foodid2.setMenuId("2");
		foodid2.setRestaurantId("T08_Restaurant1");

		Food f2 = new Food();
		f2.setId(foodid2);
		f2.setEntree("Salada de Polvo");
		f2.setPlate("Arroz");
		f2.setDessert("Bolo");
		f2.setPrice(15);
		f2.setPreparationTime(4);

		FoodInit foodinit2 = new FoodInit();
		foodinit2.setFood(f2);
		foodinit2.setQuantity(2);

		List<FoodInit> foodInits = new ArrayList<>(Arrays.asList(foodinit1, foodinit2));
		client.ctrlInitFood(foodInits);

		client.activateAccount("teste@mail.com");

	}

	@AfterClass
	public static void oneTimeTearDown() {
		// clear remote service state after all tests
		client.ctrlClear();

	}

	@Before
	public void setUp() throws InvalidUserIdFault_Exception {
		client.clearCart("teste@mail.com");

	}

	@After
	public void tearDown() {

	}

	// Bad input tests
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void addFoodToCartNullUserID()
			throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {

		FoodId foodid = new FoodId();
		foodid.setMenuId("2");
		foodid.setRestaurantId("T08_Restaurant1");

		client.addFoodToCart(null, foodid, 1);
	}

	@Test(expected = InvalidUserIdFault_Exception.class)
	public void addFoodToCartNonRegisteredUserID()
			throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {

		FoodId foodid = new FoodId();
		foodid.setMenuId("2");
		foodid.setRestaurantId("T08_Restaurant1");

		client.addFoodToCart("naoExiste", foodid, 1);
	}

	@Test(expected = InvalidFoodQuantityFault_Exception.class)
	public void addFoodToCartZeroQuantity()
			throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {

		FoodId foodid = new FoodId();
		foodid.setMenuId("2");
		foodid.setRestaurantId("T08_Restaurant1");

		client.addFoodToCart("teste@mail.com", foodid, 0);
	}

	@Test(expected = InvalidFoodQuantityFault_Exception.class)
	public void addFoodToCartNegativeCartQuantity()
			throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {

		FoodId foodid = new FoodId();
		foodid.setMenuId("2");
		foodid.setRestaurantId("T08_Restaurant1");

		client.addFoodToCart("teste@mail.com", foodid, -1);
	}

	@Test(expected = InvalidFoodIdFault_Exception.class)
	public void addFoodToCartNullFoodId()
			throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {

		client.addFoodToCart("teste@mail.com", null, 1);
	}

	@Test(expected = InvalidFoodIdFault_Exception.class)
	public void addFoodToCartFoodIdWithouthMenu()
			throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {
		FoodId foodid = new FoodId();
		foodid.setMenuId(null);
		foodid.setRestaurantId("T08_Restaurant1");

		client.addFoodToCart("teste@mail.com", foodid, 1);
	}

	@Test(expected = InvalidFoodIdFault_Exception.class)
	public void addFoodToCartFoodIdWithouthRestaurant()
			throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {
		FoodId foodid = new FoodId();
		foodid.setMenuId("2");
		foodid.setRestaurantId(null);

		client.addFoodToCart("teste@mail.com", foodid, 1);
	}

	@Test(expected = InvalidFoodIdFault_Exception.class)
	public void addFoodToCartNonExistingRestaurant()
			throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {
		FoodId foodid = new FoodId();
		foodid.setMenuId("2");
		foodid.setRestaurantId("NaoExiste");

		client.addFoodToCart("teste@mail.com", foodid, 1);
	}
	
	@Test(expected = InvalidFoodQuantityFault_Exception.class)
	public void adddFoodToCartandRemoveToMutchFood()
			throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {
		FoodId foodid = new FoodId();
		foodid.setMenuId("2");
		foodid.setRestaurantId("T08_Restaurant1");
		client.addFoodToCart("teste@mail.com", foodid, 2);
		
		client.addFoodToCart("teste@mail.com", foodid, -5);
		
		List<FoodOrderItem> carrinho = client.cartContents("teste@mail.com");

		/*Quantity was not added*/
		for (FoodOrderItem foi : carrinho) {
			if (foi.getFoodId().getMenuId().equals(foodid.getMenuId()) && foi.getFoodId().getRestaurantId().equals(foodid.getRestaurantId()))
				assertEquals(foi.getFoodQuantity(), 2);
		}
		

	}

	// Main Tests

	@Test
	public void success()
			throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {
		FoodId foodid = new FoodId();
		foodid.setMenuId("1");
		foodid.setRestaurantId("T08_Restaurant1");

		client.addFoodToCart("teste@mail.com", foodid, 1);

		List<FoodOrderItem> carrinho = client.cartContents("teste@mail.com");

		assertEquals(carrinho.size(), 1);

		for (FoodOrderItem foi : carrinho) {
			assertEquals(foi.getFoodId().getMenuId(), "1");
			assertEquals(foi.getFoodId().getRestaurantId(), "T08_Restaurant1");
			assertEquals(foi.getFoodQuantity(), 1);
		}
	}

	@Test
	public void adddFoodToCartSameFoodTwice()
			throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {
		FoodId foodid = new FoodId();
		foodid.setMenuId("2");
		foodid.setRestaurantId("T08_Restaurant1");
		client.addFoodToCart("teste@mail.com", foodid, 1);
		client.addFoodToCart("teste@mail.com", foodid, 1);

		List<FoodOrderItem> carrinho = client.cartContents("teste@mail.com");

		assertEquals(carrinho.size(), 1);

		for (FoodOrderItem foi : carrinho) {
			if (foi.getFoodId().getMenuId().equals(foodid.getMenuId()) && foi.getFoodId().getRestaurantId().equals(foodid.getRestaurantId()))
				assertEquals(foi.getFoodQuantity(), 2);
		}
	}

	@Test
	public void adddFoodToCartandRemoveFood()
			throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {
		FoodId foodid = new FoodId();
		foodid.setMenuId("2");
		foodid.setRestaurantId("T08_Restaurant1");
		client.addFoodToCart("teste@mail.com", foodid, 2);
		client.addFoodToCart("teste@mail.com", foodid, -1);

		List<FoodOrderItem> carrinho = client.cartContents("teste@mail.com");

		assertEquals(carrinho.size(), 1);

		for (FoodOrderItem foi : carrinho) {
			if (foi.getFoodId().getMenuId().equals(foodid.getMenuId())
					&& foi.getFoodId().getRestaurantId().equals(foodid.getRestaurantId()))
				assertEquals(foi.getFoodQuantity(), 1);
		}
	}

	@Test
	public void addTwoFoodsToCart()
			throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {

		FoodId foodid = new FoodId();
		foodid.setMenuId("2");
		foodid.setRestaurantId("T08_Restaurant1");
		client.addFoodToCart("teste@mail.com", foodid, 2);

		FoodId foodid1 = new FoodId();
		foodid1.setMenuId("1");
		foodid1.setRestaurantId("T08_Restaurant1");

		client.addFoodToCart("teste@mail.com", foodid1, 1);

		List<FoodOrderItem> carrinho = client.cartContents("teste@mail.com");

		assertEquals(carrinho.size(), 2);

		for (FoodOrderItem foi : carrinho) {
			if (foi.getFoodId().getMenuId().equals(foodid.getMenuId())
					&& foi.getFoodId().getRestaurantId().equals(foodid.getRestaurantId()))
				assertEquals(foi.getFoodQuantity(), 2);
			else if (foi.getFoodId().getMenuId().equals(foodid1.getMenuId())
					&& foi.getFoodId().getRestaurantId().equals(foodid1.getRestaurantId()))
				assertEquals(foi.getFoodQuantity(), 1);
		}
	}

}
