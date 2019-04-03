package com.forkexec.rst.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.forkexec.rst.ws.*;

public class GetMenuIT extends BaseIT {

	// static members

	// one-time initialization and clean-up
	@BeforeClass
	public static void oneTimeSetUp() throws BadInitFault_Exception {
		Menu menu = new Menu();
		menu.setPrice(1);
		menu.setPreparationTime(1);
		menu.setEntree("entree");
		menu.setPlate("plate");
		menu.setDessert("desert");

		MenuId idMenu = new MenuId();
		idMenu.setId("id");
		menu.setId(idMenu);

		MenuInit toInsert = new MenuInit();
		toInsert.setMenu(menu);
		toInsert.setQuantity(5);

		List<MenuInit> menuList = new ArrayList<MenuInit>();
		menuList.add(toInsert);
		client.ctrlInit(menuList);
	}

	@AfterClass
	public static void oneTimeTearDown() {
		client.ctrlClear();
	}

	// initialization and clean-up for each test
	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void successfullyGetMenuTest() throws BadMenuIdFault_Exception {
		MenuId idMenu = new MenuId();
		idMenu.setId("id");

		Menu menu = client.getMenu(idMenu);

		assertEquals(menu.getPrice(), 1);
		assertEquals(menu.getPreparationTime(), 1);
		assertEquals(menu.getEntree(), "entree");
		assertEquals(menu.getPlate(), "plate");
		assertEquals(menu.getDessert(), "desert");
		assertEquals(menu.getId().getId(), "id");
	}
	
	@Test(expected = BadMenuIdFault_Exception.class)
	public void getMenuNullTest() throws BadMenuIdFault_Exception {
		client.getMenu(null);
		
	}
	
	@Test(expected = BadMenuIdFault_Exception.class)
	public void getMenuMenuIdNullTest() throws BadMenuIdFault_Exception {
		MenuId idMenu = new MenuId();
		idMenu.setId(null);
		client.getMenu(idMenu);
		
	}
	@Test(expected = BadMenuIdFault_Exception.class)
	public void getMenuMenuNoSuchMenuTest() throws BadMenuIdFault_Exception {
		MenuId idMenu = new MenuId();
		idMenu.setId("id2");
		client.getMenu(idMenu);
		
	}


}
