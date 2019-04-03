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

public class SearchMenusIT extends BaseIT{
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
		
		Menu menu2 = new Menu();
		menu2.setPrice(1);
		menu2.setPreparationTime(1);
		menu2.setEntree("entree2");
		menu2.setPlate("plate2");
		menu2.setDessert("desert2");

		MenuId idMenu2 = new MenuId();
		idMenu2.setId("id2");
		menu2.setId(idMenu2);

		MenuInit toInsert2 = new MenuInit();
		toInsert2.setMenu(menu2);
		toInsert2.setQuantity(5);

		List<MenuInit> menuList = new ArrayList<MenuInit>();
		menuList.add(toInsert);
		menuList.add(toInsert2);
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
	public void successSearchMenusTest() throws BadTextFault_Exception {
		List<Menu> menuList = client.searchMenus("plate2");
		
		assertEquals(menuList.size(), 1);
		
		Menu menu = menuList.get(0);
		
		assertEquals(menu.getDessert(), "desert2");
		assertEquals(menu.getPlate(), "plate2");
		assertEquals(menu.getEntree(), "entree2");
		assertEquals(menu.getPrice(), 1);
		assertEquals(menu.getPreparationTime(), 1);
		assertEquals(menu.getId().getId(), "id2");
		
	}
	
	@Test
	public void successSearchMultipleMenusTest() throws BadTextFault_Exception {
		List<Menu> menuList = client.searchMenus("plate");
		
		assertEquals(menuList.size(), 2);
		
		Menu menu = menuList.get(0);
		
		assertEquals(menu.getDessert(), "desert2");
		assertEquals(menu.getPlate(), "plate2");
		assertEquals(menu.getEntree(), "entree2");
		assertEquals(menu.getPrice(), 1);
		assertEquals(menu.getPreparationTime(), 1);
		assertEquals(menu.getId().getId(), "id2");
		
		menu = menuList.get(1);
		
		assertEquals(menu.getDessert(), "desert");
		assertEquals(menu.getPlate(), "plate");
		assertEquals(menu.getEntree(), "entree");
		assertEquals(menu.getPrice(), 1);
		assertEquals(menu.getPreparationTime(), 1);
		assertEquals(menu.getId().getId(), "id");
		
	}
	
	@Test
	public void noSuchMenuSearchTest() throws BadTextFault_Exception {
		List<Menu> menuList = client.searchMenus("plate3");
		
		assertEquals(menuList.size(), 0);
	}
	
	@Test(expected = BadTextFault_Exception.class)
	public void nullMenuSearchTest() throws BadTextFault_Exception {
		client.searchMenus(null);
		
	}
	
	@Test(expected = BadTextFault_Exception.class)
	public void emptyMenuSearchTest() throws BadTextFault_Exception {
		client.searchMenus("");
		
	}
	
	@Test(expected = BadTextFault_Exception.class)
	public void spaceInStringMenuSearchTest() throws BadTextFault_Exception {
		client.searchMenus("plate ");
		
	}



}
