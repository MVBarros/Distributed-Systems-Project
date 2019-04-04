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
import com.forkexec.hub.ws.InvalidFoodIdFault_Exception;
import com.forkexec.hub.ws.InvalidInitFault_Exception;
import com.forkexec.rst.ws.Menu;
import com.forkexec.rst.ws.cli.RestaurantClient;


public class GetFoodIT extends BaseIT {

	// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp() throws InvalidInitFault_Exception {
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
	
	@Test(expected =InvalidFoodIdFault_Exception.class)
	public void getFoodNullId() throws InvalidFoodIdFault_Exception{
		client.getFood(null);
	}
	
	@Test(expected =InvalidFoodIdFault_Exception.class)
	public void getFoodNullMenu() throws InvalidFoodIdFault_Exception{
		
		FoodId foodid = new FoodId();
		foodid.setMenuId(null);
		foodid.setRestaurantId("T08_Restaurant1");
		
		client.getFood(foodid);
	}
	
	@Test(expected =InvalidFoodIdFault_Exception.class)
	public void getFoodNonExistentMenu() throws InvalidFoodIdFault_Exception{
		
		FoodId foodid = new FoodId();
		foodid.setMenuId("NaoExiste");
		foodid.setRestaurantId("T08_Restaurant1");
		
		client.getFood(foodid);
	}
	
	@Test(expected =InvalidFoodIdFault_Exception.class)
	public void getFoodNullRestaurant() throws InvalidFoodIdFault_Exception{
		
		FoodId foodid = new FoodId();
		foodid.setMenuId("1");
		foodid.setRestaurantId(null);
		
		client.getFood(foodid);
	}
	
	@Test(expected =InvalidFoodIdFault_Exception.class)
	public void getFoodNonExistentRestaurant() throws InvalidFoodIdFault_Exception{
		
		FoodId foodid = new FoodId();
		foodid.setMenuId("1");
		foodid.setRestaurantId("NaoExiste");
		
		client.getFood(foodid);
	}
	
	//Main Tests

	@Test
	public void getFoodSuccess() throws InvalidFoodIdFault_Exception{
		FoodId foodid = new FoodId();
		foodid.setMenuId("1");
		foodid.setRestaurantId("T08_Restaurant1");
		Food f = client.getFood(foodid);
		
		assertEquals(f.getDessert(), "Bolo");
		assertEquals(f.getEntree(), "Salada de Polvo");
		assertEquals(f.getId(), foodid);
		assertEquals(f.getPlate(),"Arroz");
		assertEquals(f.getPreparationTime(),3 );
		assertEquals(f.getPrice(), 15);
	}
	
	
}