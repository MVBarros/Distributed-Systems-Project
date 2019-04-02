package com.forkexec.rst.domain;

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

	public Map<String, RestaurantMenu> menus = new ConcurrentHashMap<>();

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

	public void reset() {
		menus.clear();
	}

	// Menus

	public void init(List<RestaurantMenu> initialMenus) throws BadMenuInitiationException {
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
	
	/**Return true if menu is valid and it's ID is not duplicate, false otherwise*/
	public boolean acceptMenu(RestaurantMenu menu) {
		return (Stream.of(menu.getId(), menu.getEntree(), menu.getPlate(), menu.getDessert())
				.noneMatch(e-> (e == null) || ((e.equals("")))) 
				&& 
				Stream.of(menu.getPrice(), menu.getPreparationTime(), menu.getQuantity())
				.allMatch(e -> e > 0))
				&&
				!menus.containsKey(menu.getId());
	}
	

	public RestaurantMenu getMenu(String menuid) {
		return menus.get(menuid);

	}

}
