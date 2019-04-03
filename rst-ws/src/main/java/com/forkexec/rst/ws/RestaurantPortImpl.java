package com.forkexec.rst.ws;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import com.forkexec.rst.domain.BadMenuIdException;
import com.forkexec.rst.domain.BadMenuInitiationException;
import com.forkexec.rst.domain.BadQuantityException;
import com.forkexec.rst.domain.BadTextException;
import com.forkexec.rst.domain.InsufficientQuantityException;
import com.forkexec.rst.domain.Restaurant;
import com.forkexec.rst.domain.RestaurantMenu;
import com.forkexec.rst.domain.RestaurantMenuOrder;

/**
 * This class implements the Web Service port type (interface). The annotations
 * below "map" the Java class to the WSDL definitions.
 */
@WebService(endpointInterface = "com.forkexec.rst.ws.RestaurantPortType", wsdlLocation = "RestaurantService.wsdl", name = "RestaurantWebService", portName = "RestaurantPort", targetNamespace = "http://ws.rst.forkexec.com/", serviceName = "RestaurantService")
public class RestaurantPortImpl implements RestaurantPortType {

	/**
	 * The Endpoint manager controls the Web Service instance during its whole
	 * lifecycle.
	 */
	private RestaurantEndpointManager endpointManager;

	/** Constructor receives a reference to the endpoint manager. */
	public RestaurantPortImpl(RestaurantEndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}

	
	// Main operations -------------------------------------------------------

	
	/** Get menu given a menuId */
	@Override
	public Menu getMenu(MenuId menuId) throws BadMenuIdFault_Exception {
		if (menuId == null || menuId.getId() == null)
			throwBadMenuId("Menu Id can't be null");

		if (menuId.getId().trim().isEmpty())
			throwBadMenuId("Menu Id can't be empty");

		RestaurantMenu menu = Restaurant.getInstance().getMenu(menuId.getId());

		if (menu == null)
			throwBadMenuId("No such menu with that Id ");

		return newMenu(menu);
	}
	
	
	/**Search Menus by description*/
	@Override
	public List<Menu> searchMenus(String descriptionText) throws BadTextFault_Exception {
		List<Menu> result = new ArrayList<Menu>();
		try {
			for (RestaurantMenu rest : Restaurant.getInstance().searchMenus(descriptionText)) {
				result.add(newMenu(rest));
			}
		} catch (BadTextException e) {
			throwBadTextFault(e.getMessage());
			return null;
		}
		return result;
	}

	
	/**Order a menu by it's Id*/
	@Override
	public MenuOrder orderMenu(MenuId arg0, int arg1)
			throws BadMenuIdFault_Exception, BadQuantityFault_Exception, InsufficientQuantityFault_Exception {
		if (arg0 == null)
			throwBadMenuId("MenuId can't be null");
		try {
			RestaurantMenuOrder newOrder = Restaurant.getInstance().acceptMenuOrder(arg0.getId(), arg1);
			return newMenuOrder(Restaurant.getInstance().addMenuOrder(newOrder));
		} catch (BadMenuIdException e) {
			throwBadMenuId(e.getMessage());
			return null;
		} catch (BadQuantityException e) {
			throwBadQuantityFaul(e.getMessage());
			return null;
		} catch (InsufficientQuantityException e) {
			throwInsufficientQuantityFault(e.getMessage());
			return null;
		}

	}

	// Control operations ----------------------------------------------------

	/** Diagnostic operation to check if service is running. */
	@Override
	public String ctrlPing(String inputMessage) {
		// If no input is received, return a default name.
		if (inputMessage == null || inputMessage.trim().length() == 0)
			inputMessage = "friend";

		// If the park does not have a name, return a default.
		String wsName = endpointManager.getWsName();
		if (wsName == null || wsName.trim().length() == 0)
			wsName = "Restaurant";

		// Build a string with a message to return.
		StringBuilder builder = new StringBuilder();
		builder.append("Hello ").append(inputMessage);
		builder.append(" from ").append(wsName);
		return builder.toString();
	}

	/** Return all variables to default values. */
	@Override
	public void ctrlClear() {
		Restaurant.getInstance().reset();
	}

	
	/** Set variables with specific values. */
	@Override
	public void ctrlInit(List<MenuInit> initialMenus) throws BadInitFault_Exception {

		if (initialMenus == null)
			throwBadInit("Initial Menu list can't be null");

		if (initialMenus.contains(null))
			throwBadInit("Initial Menu list can't contain null object");

		// Convert MenuInitList to RestaurantMenuList
		List<RestaurantMenu> menus = new ArrayList<>();

		for (MenuInit menu : initialMenus)
			menus.add(newRestaurantMenu(menu));

		try {
			Restaurant.getInstance().init(menus);
		} catch (BadMenuInitiationException e) {
			throwBadInit("BadInit got exception" + e.getMessage());
		}

	}

	// View helpers ----------------------------------------------------------

	/**
	 * Helper to convert domain object RestaurantMenuOrder to view Object MenuOrder
	 */
	private MenuOrder newMenuOrder(RestaurantMenuOrder oldMenu) {
		MenuOrder menuOrderView = new MenuOrder();

		MenuOrderId id = new MenuOrderId();
		id.setId(oldMenu.getId());
		menuOrderView.setId(id);

		MenuId menuId = new MenuId();
		menuId.setId(oldMenu.getMenuId());
		menuOrderView.setMenuId(menuId);

		menuOrderView.setMenuQuantity(oldMenu.getMenuQuantity());

		return menuOrderView;
	}

	/**
	 * Helper to convert domain object RestaurantMenu to view Object Menu
	 */
	private Menu newMenu(RestaurantMenu menu) {
		Menu menuView = new Menu();

		menuView.setEntree(menu.getEntree());
		menuView.setPlate(menu.getPlate());
		menuView.setDessert(menu.getDessert());

		MenuId menuId = new MenuId();
		menuId.setId(menu.getId());
		menuView.setId(menuId);

		menuView.setPreparationTime(menu.getPreparationTime());
		menuView.setPrice(menu.getPrice());
		return menuView;
	}

	/**
	 * Helper to convert view object MenuInit to domain Object RestaurantMenu
	 */
	private RestaurantMenu newRestaurantMenu(MenuInit menuInit) throws BadInitFault_Exception {

		if (menuInit.getMenu() == null) {
			throwBadInit("Cannot add a null menu");
		}
		if (menuInit.getMenu().getId() == null) {
			throwBadInit("Cannot add a null menu");
		}

		Menu menu = menuInit.getMenu();
		int quantity = menuInit.getQuantity();
		String entree = menu.getEntree();
		String plate = menu.getPlate();
		String dessert = menu.getDessert();
		String id = menu.getId().getId();
		int preparationTime = menu.getPreparationTime();
		int price = menu.getPrice();

		return new RestaurantMenu(id, entree, plate, dessert, price, preparationTime, quantity);
	}

	// Exception helpers -----------------------------------------------------

	/** Helper to throw a new BadInit exception. */
	private void throwBadInit(final String message) throws BadInitFault_Exception {
		BadInitFault faultInfo = new BadInitFault();
		faultInfo.message = message;
		throw new BadInitFault_Exception(message, faultInfo);
	}

	private void throwBadMenuId(final String message) throws BadMenuIdFault_Exception {
		BadMenuIdFault faultInfo = new BadMenuIdFault();
		faultInfo.message = message;
		throw new BadMenuIdFault_Exception(message, faultInfo);
	}

	private void throwBadQuantityFaul(final String message) throws BadQuantityFault_Exception {
		BadQuantityFault faultInfo = new BadQuantityFault();
		faultInfo.message = message;
		throw new BadQuantityFault_Exception(message, faultInfo);
	}

	private void throwInsufficientQuantityFault(final String message) throws InsufficientQuantityFault_Exception {
		InsufficientQuantityFault faultInfo = new InsufficientQuantityFault();
		faultInfo.message = message;
		throw new InsufficientQuantityFault_Exception(message, faultInfo);
	}

	private void throwBadTextFault(final String message) throws BadTextFault_Exception {
		BadTextFault faultInfo = new BadTextFault();
		faultInfo.message = message;
		throw new BadTextFault_Exception(message, faultInfo);
	}

}
