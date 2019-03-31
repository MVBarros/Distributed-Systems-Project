package com.forkexec.rst.domain;

import com.forkexec.rst.ws.BadInitFault_Exception;
import com.forkexec.rst.ws.Menu;
import com.forkexec.rst.ws.MenuId;
import com.forkexec.rst.ws.MenuInit;

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
	
	//Menus

	
	public void init(List<RestaurantMenu> initialMenus){
		reset();
		
		initialMenus.stream().forEach(menu -> menus.put(menu.getId(), menu));
		
	}
	
	public RestaurantMenu getMenu(String menuid) {
		return menus.get(menuid);
		
	}
	
	
}
