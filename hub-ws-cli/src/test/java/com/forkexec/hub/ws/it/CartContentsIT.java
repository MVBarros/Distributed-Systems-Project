package com.forkexec.hub.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;

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
	public void oneTimeSetUp() throws InvalidInitFault_Exception, InvalidUserIdFault_Exception, InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception {
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
	
	@AfterClass
	public void oneTimeTearDown() {
		client.ctrlClear();
	}
	
	public void sucesssCartContents() throws InvalidUserIdFault_Exception {
		
		List<FoodOrderItem> contents = client.cartContents("ola@ola.com");
		
		assertEquals(contents.size(), 1);
	//	assertEquals(contents.get(0));
		
	}

}
