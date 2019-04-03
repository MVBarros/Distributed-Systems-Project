package com.forkexec.rst.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.forkexec.rst.ws.*;

public class CtrlInitIT extends BaseIT {

	// static members

	// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp() {
	}

	@AfterClass
	public static void oneTimeTearDown() {
	client.ctrlClear();
}

	// members

	// initialization and clean-up for each test
	@Before
	public void setUp() {
		client.ctrlClear();
	}

	@After
	public void tearDown() {
	}

	/* Auxiliary functions */
	private Menu initWithParams(int price, int prepTime, String desert, String entree, String plate, String id,
			int quantity) throws BadInitFault_Exception {
		Menu menu = new Menu();
		menu.setPrice(price);
		menu.setPreparationTime(prepTime);
		menu.setEntree(entree);
		menu.setPlate(plate);
		menu.setDessert(desert);

		MenuId idMenu = new MenuId();
		idMenu.setId(id);
		menu.setId(idMenu);

		MenuInit toInsert = new MenuInit();
		toInsert.setMenu(menu);
		toInsert.setQuantity(quantity);

		List<MenuInit> menuList = new ArrayList<MenuInit>();
		menuList.add(toInsert);
		client.ctrlInit(menuList);

		return menu;

	}

	private void initWith2Ids(int price, int prepTime, String desert, String entree, String plate, String idFirst,
			String idSecond) throws BadInitFault_Exception {
		Menu menu = new Menu();
		menu.setPrice(price);
		menu.setPreparationTime(prepTime);
		menu.setEntree(entree);
		menu.setPlate(plate);
		menu.setDessert(desert);

		Menu menu2 = new Menu();
		menu2.setPrice(price);
		menu2.setPreparationTime(prepTime);
		menu2.setEntree(entree);
		menu2.setPlate(plate);
		menu2.setDessert(desert);

		MenuId idMenu = new MenuId();
		idMenu.setId(idFirst);
		menu.setId(idMenu);

		MenuId idMenu2 = new MenuId();
		idMenu2.setId(idSecond);
		menu2.setId(idMenu2);

		MenuInit toInsertFirst = new MenuInit();
		toInsertFirst.setMenu(menu);
		toInsertFirst.setQuantity(3);

		MenuInit toInsertSecond = new MenuInit();
		toInsertSecond.setMenu(menu2);
		toInsertSecond.setQuantity(3);

		List<MenuInit> menuList = new ArrayList<MenuInit>();
		menuList.add(toInsertFirst);
		menuList.add(toInsertSecond);
		client.ctrlInit(menuList);

	}

	@Test
	public void initValid() throws BadInitFault_Exception, BadMenuIdFault_Exception {
		// Create menu
		Menu menu = initWithParams(1, 1, "1", "1", "1", "1", 3);
		String menuId = "1";
		MenuId id = new MenuId();
		id.setId(menuId);
		// Test
		assertEquals(client.getMenu(id).getEntree(), menu.getEntree());
		assertEquals(client.getMenu(id).getDessert(), menu.getDessert());
		assertEquals(client.getMenu(id).getId().getId(), menu.getId().getId());
		assertEquals(client.getMenu(id).getPlate(), menu.getPlate());
		assertEquals(client.getMenu(id).getPreparationTime(), menu.getPreparationTime());
		assertEquals(client.getMenu(id).getPrice(), menu.getPrice());
	}

	@Test(expected = BadInitFault_Exception.class)
	public void badPriceInit() throws BadInitFault_Exception {
		initWithParams(-1, 1, "1", "1", "1", "1", 3);

	}

	@Test(expected = BadInitFault_Exception.class)
	public void badPrepTimeInit() throws BadInitFault_Exception {
		initWithParams(1, -1, "1", "1", "1", "1", 3);

	}

	@Test(expected = BadInitFault_Exception.class)
	public void nullDesertInit() throws BadInitFault_Exception {
		initWithParams(1, 1, null, "1", "1", "1", 3);

	}

	@Test(expected = BadInitFault_Exception.class)
	public void nullEntreeInit() throws BadInitFault_Exception {
		initWithParams(1, 1, "1", null, "1", "1", 3);

	}

	@Test(expected = BadInitFault_Exception.class)
	public void nullPlateInit() throws BadInitFault_Exception {
		initWithParams(1, 1, "1", "1", null, "1", 3);

	}

	@Test(expected = BadInitFault_Exception.class)
	public void nullIdInit() throws BadInitFault_Exception {
		initWithParams(1, 1, "1", "1", "1", null, 3);

	}

	@Test(expected = BadInitFault_Exception.class)
	public void badQuantityInit() throws BadInitFault_Exception {
		initWithParams(1, 1, "1", "1", "1", "1", -1);

	}

	@Test(expected = BadInitFault_Exception.class)
	public void twoEqualMenusInit() throws BadInitFault_Exception {
		initWith2Ids(1, 1, "1", "1", "1", "1", "1");
		
	}
	
	/*@Test(expected = BadInitFault_Exception.class)
	public void nullInit() throws BadInitFault_Exception {
		client.ctrlInit(null);
	}*/
	
	@Test(expected = BadInitFault_Exception.class)
	public void listWithNullInit() throws BadInitFault_Exception {
		List<MenuInit> menuList = new ArrayList<MenuInit>();
		menuList.add(null);
		client.ctrlInit(menuList);
	}
	
	@Test(expected = BadInitFault_Exception.class)
	public void nullMenuIdInit() throws BadInitFault_Exception {
		Menu menu = new Menu();
		menu.setPrice(1);
		menu.setPreparationTime(1);
		menu.setEntree("1");
		menu.setPlate("1");
		menu.setDessert("1");

		MenuId idMenu = null;
		menu.setId(idMenu);

		MenuInit toInsert = new MenuInit();
		toInsert.setMenu(menu);
		toInsert.setQuantity(1);

		List<MenuInit> menuList = new ArrayList<MenuInit>();
		menuList.add(toInsert);
		client.ctrlInit(menuList);
	}

}
