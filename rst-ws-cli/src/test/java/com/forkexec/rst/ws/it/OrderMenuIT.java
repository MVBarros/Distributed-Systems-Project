package com.forkexec.rst.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.forkexec.rst.ws.*;

public class OrderMenuIT extends BaseIT {

	// initialization and clean-up for each test
	@Before
	public void setUp() throws BadInitFault_Exception {
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

	@After
	public void tearDown() {
		client.ctrlClear();
	}

	@Test
	public void succesOrderMenu() throws Exception {
		MenuId menuId = new MenuId();
		menuId.setId("id");
		MenuOrder order = client.orderMenu(menuId, 1);

		assertEquals(order.getMenuId().getId(), "id");
		assertEquals(order.getMenuQuantity(), 1);
		assertEquals(order.getId().getId(), Integer.toString(1));
	}

	@Test(expected = InsufficientQuantityFault_Exception.class)
	public void toMuchQuantityOrderMenu() throws Exception {
		MenuId menuId = new MenuId();
		menuId.setId("id");
		client.orderMenu(menuId, 6);

	}

	@Test(expected = BadQuantityFault_Exception.class)
	public void invalidQuantityOrderMenu() throws Exception {
		MenuId menuId = new MenuId();
		menuId.setId("id");
		client.orderMenu(menuId, -1);
	}

	@Test(expected = BadMenuIdFault_Exception.class)
	public void nullMenuIdValueOrdertest() throws Exception {
		MenuId menuId = new MenuId();
		menuId.setId(null);
		client.orderMenu(menuId, 1);
	}

	@Test(expected = BadMenuIdFault_Exception.class)
	public void noSuchMenuIdOrdertest() throws Exception {
		MenuId menuId = new MenuId();
		menuId.setId("noId");
		client.orderMenu(menuId, 1);
	}

	@Test(expected = BadMenuIdFault_Exception.class)
	public void nullMenuIdOrdertest() throws Exception {
		MenuId menuId = null;
		client.orderMenu(menuId, 1);
	}

	@Test(expected = InsufficientQuantityFault_Exception.class)
	public void quantityIsDecreasedOrdertest() throws Exception {
		MenuId menuId = new MenuId();
		menuId.setId("id");
		MenuOrder order = client.orderMenu(menuId, 5);
		assertEquals(order.getMenuId().getId(), "id");
		assertEquals(order.getMenuQuantity(), 5);

		/* no more quantity of menu, exception will be thrown */
		client.orderMenu(menuId, 1);
	}

	@Test
	public void quantityIsSuccessfullyFilledOrdertest() throws Exception {
		MenuId menuId = new MenuId();
		menuId.setId("id");
		MenuOrder order = client.orderMenu(menuId, 5);
		assertEquals(order.getMenuId().getId(), "id");
		assertEquals(order.getMenuQuantity(), 5);
	}

	@Test
	public void idIsCorrectTest() throws Exception {
		MenuId menuId = new MenuId();
		menuId.setId("id");

		for (int i = 0; i < 5; i++) {
			MenuOrder order = client.orderMenu(menuId, 1);
			assertEquals(order.getMenuId().getId(), "id");
			assertEquals(order.getMenuQuantity(), 1);
			assertEquals(order.getId().getId(), Integer.toString(i + 1));
		}

	}

}
