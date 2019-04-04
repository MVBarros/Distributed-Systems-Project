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
		
		//Second foodinit
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
		
		//Third foodinit
		FoodId foodid3 = new FoodId();
		foodid3.setMenuId("2");
		foodid3.setRestaurantId("T08_Restaurant2");
		
		Food f3 = new Food();
		f3.setId(foodid3);
		f3.setEntree("Salada de Polvo");
		f3.setPlate("Arroz");
		f3.setDessert("Bolo");
		f3.setPrice(15);
		f3.setPreparationTime(4);
		
		FoodInit foodinit3 = new FoodInit();
		foodinit3.setFood(f3);
		foodinit3.setQuantity(22);
		
		List<FoodInit> foodInits = new ArrayList<>(Arrays.asList(foodinit1, foodinit2, foodinit3));
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
		foodid.setRestaurantId("T08_Restaurant2");
		
		client.getFood(foodid);
	}
	
	@Test(expected =InvalidFoodIdFault_Exception.class)
	public void getFoodNonExistentMenu() throws InvalidFoodIdFault_Exception{
		
		FoodId foodid = new FoodId();
		foodid.setMenuId("NaoExiste");
		foodid.setRestaurantId("T08_Restaurant2");
		
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
		foodid.setMenuId("2");
		foodid.setRestaurantId("T08_Restaurant1");
		Food f = client.getFood(foodid);
		
		RestaurantClient rest = getRestaurantbyId(foodid.getRestaurantId());
		Menu menu = rest.getMenu(newMenuId(foodid));
		assertEquals(f.getDessert(), menu.getDessert());
		assertEquals(f.getEntree(), menu.getEntree());
		assertEquals(f.getId(), menu.getId());
		assertEquals(f.getPlate(), menu.getPlate());
		assertEquals(f.getPreparationTime(), menu.getPreparationTime());
		assertEquals(f.getPrice(), menu.getPrice() );
	}
	
	
}