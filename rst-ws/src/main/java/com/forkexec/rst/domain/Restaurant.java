package com.forkexec.rst.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;


/**
 * Restaurant
 *
 * A restaurant server.
 *
 */
public class Restaurant {

	private Map<String, RestaurantMenu> menus = new ConcurrentHashMap<>();
	private Map<String, RestaurantMenuOrder> menuOrders = new ConcurrentHashMap<>();
	private volatile int currentOrder = 0;

	// Singleton -------------------------------------------------------------

	/** Private constructor prevents instantiation from other classes. */
	private Restaurant() {
		// Initialization of default values
	}

	/**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance()
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder {
		private static final Restaurant INSTANCE = new Restaurant();
	}

	public static synchronized Restaurant getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	//Control Operations--------------------------------------------------------------
	
	public synchronized void reset() {
		menus.clear();
		menuOrders.clear();
		currentOrder = 0;
	}


	public synchronized void init(List<RestaurantMenu> initialMenus) throws BadMenuInitiationException {
		reset();

		for (RestaurantMenu menu : initialMenus) {
			if (!acceptMenu(menu)) {
				/*remove all menus from restaurant*/
				reset(); 
				throw new BadMenuInitiationException("Invalid menu initiation");
			}
			menus.put(menu.getId(), menu);
		}
	}

		
	// RestaurantMenus-------------------------------------------------------------

	/**Return true if menu is valid and it's ID is not duplicate, false otherwise*/
	public boolean acceptMenu(RestaurantMenu menu) {
		return (Stream.of(menu.getId(), menu.getEntree(), menu.getPlate(), menu.getDessert())
				.noneMatch(e-> (e == null) || ((e.trim().isEmpty()))) 
				&& 
				Stream.of(menu.getPrice(), menu.getPreparationTime(), menu.getQuantity())
				.allMatch(e -> e > 0))
				&&
				!menus.containsKey(menu.getId())
				&&
				!menu.getId().contains("\n");
	}
	

	public RestaurantMenu getMenu(String menuid) {
		return menus.get(menuid);

	}
	
	public synchronized List<RestaurantMenu> searchMenus (String descriptionText) throws BadTextException {
		
		if (descriptionText == null || descriptionText.contentEquals("") || descriptionText.contains(" "))
			throwBadTextException("Description text is empty or null");
		
		List<RestaurantMenu> result = new ArrayList<RestaurantMenu>();
		
		for(RestaurantMenu rm : menus.values()) 
			if (rm.getDescription().contains(descriptionText))
				result.add(rm);
		
		return result;
		
		
	}
	
	//Menu Orders----------------------------------------------------------------
	
	/**Return RestaurantMenuOrder if menuOrder is valid and quantity acceptable*/
	public RestaurantMenuOrder acceptMenuOrder(String arg0, int arg1) 
			throws BadMenuIdException, BadQuantityException, InsufficientQuantityException {
		
		if (arg0 == null)
			throwBadMenuId("Menu Id can't be null");
		if (arg0.trim().isEmpty())
			throwBadMenuId("Menu Id can't be empty");
		
		RestaurantMenu menu = this.getMenu(arg0);
		
		//Menu with that ID doesn't exist
		if (menu == null)
			throwBadMenuId("No such menu with that Id");
		
		if (arg1 < 1) throwBadQuantity("Quantity has to be equals or greater than one");
		if (menu.getQuantity() < arg1) throwInsufficientQuantityFault("Can't order because quantity too big");

		return new RestaurantMenuOrder(this.getCurrentOrderId() , arg0, arg1);


	}
	
	
	public synchronized String getCurrentOrderId () {
		currentOrder ++;
		return Integer.toString(currentOrder);
	}
	
	
	public synchronized RestaurantMenuOrder addMenuOrder(RestaurantMenuOrder newOrder) {
		RestaurantMenu menu = this.getMenu(newOrder.getMenuId());
		menu.setQuantity(menu.getQuantity()-newOrder.getMenuQuantity());
		menuOrders.put(newOrder.getId(), newOrder);
		return newOrder;
	}
	
	
	/*** EXCEPTION ***/
	
	private void throwBadMenuId(final String message) throws BadMenuIdException {
		throw new BadMenuIdException(message);
	}
	
	private void throwBadQuantity(final String message) throws BadQuantityException {
		throw new BadQuantityException(message);
	}
	
	private void throwInsufficientQuantityFault(final String message) throws InsufficientQuantityException {
		throw new InsufficientQuantityException(message);
	}

	private void throwBadTextException (final String message) throws BadTextException  {
		throw new BadTextException (message);
	}
}
