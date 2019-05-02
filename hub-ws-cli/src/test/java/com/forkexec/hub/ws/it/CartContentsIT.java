package com.forkexec.hub.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.forkexec.hub.ws.Food;
import com.forkexec.hub.ws.FoodId;
import com.forkexec.hub.ws.FoodInit;
import com.forkexec.hub.ws.FoodOrder;
import com.forkexec.hub.ws.FoodOrderItem;
import com.forkexec.hub.ws.InvalidFoodIdFault_Exception;
import com.forkexec.hub.ws.InvalidFoodQuantityFault_Exception;
import com.forkexec.hub.ws.InvalidInitFault_Exception;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;

public class CartContentsIT extends BaseIT{
	
	@BeforeClass
	public static void oneTimeSetUp() throws InvalidInitFault_Exception, InvalidUserIdFault_Exception, InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception {
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
		
		
		//Second foodinit
		FoodId foodid2 = new FoodId();
		foodid2.setMenuId("1");
		foodid2.setRestaurantId("T08_Restaurant2");
		
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
		client.addFoodToCart("teste@mail.com", foodid1, 1);
		
		client.activateAccount("teste2@mail.com");

	}
	
	@AfterClass
	public static void oneTimeTearDown() {
		client.ctrlClear();
	}
	
	
	@Test
	public void sucesssCartContents() throws InvalidUserIdFault_Exception {
		
		List<FoodOrderItem> contents = client.cartContents("teste@mail.com");
		
		assertEquals(contents.size(), 1);
		assertEquals(contents.get(0).getFoodId().getMenuId(), "1");
		assertEquals(contents.get(0).getFoodId().getRestaurantId(), "T08_Restaurant1");
		assertEquals(contents.get(0).getFoodQuantity(), 1);
	}
	
	@Test
	public void sucesssCartContentsTwoFoods() throws InvalidUserIdFault_Exception, InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception {
		
		FoodId foodid2 = new FoodId();
		foodid2.setMenuId("1");
		foodid2.setRestaurantId("T08_Restaurant2");
		
		client.addFoodToCart("teste@mail.com", foodid2, 2);
		List<FoodOrderItem> contents = client.cartContents("teste@mail.com");
		
		assertEquals(contents.size(), 2);
		assertEquals(contents.get(0).getFoodId().getMenuId(), "1");
		assertEquals(contents.get(0).getFoodId().getRestaurantId(), "T08_Restaurant2");
		assertEquals(contents.get(0).getFoodQuantity(), 2);

		assertEquals(contents.get(1).getFoodId().getMenuId(), "1");
		assertEquals(contents.get(1).getFoodId().getRestaurantId(), "T08_Restaurant1");
		assertEquals(contents.get(1).getFoodQuantity(), 1);
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void nullFoodIdCartContents() throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {
		client.cartContents(null);
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void emptyFoodIdCartContents() throws InvalidUserIdFault_Exception {
		client.cartContents("");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void newLineIdCartContents() throws InvalidUserIdFault_Exception {
		client.cartContents("\n");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void tabIdCartContents() throws InvalidUserIdFault_Exception {
		client.cartContents("\t");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void emptyIdCartContents() throws InvalidUserIdFault_Exception {
		client.cartContents("");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void whiteSpaceIdCartContentsContents() throws InvalidUserIdFault_Exception {
		client.cartContents(" ");
	}
	
	@Test(expected = InvalidUserIdFault_Exception.class)
	public void notAnEmailCartContents() throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {
		client.cartContents("Tony Stark");
	}
}
