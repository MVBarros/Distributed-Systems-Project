package com.forkexec.rst.domain;

import com.forkexec.rst.ws.BadInitFault_Exception;
import com.forkexec.rst.ws.MenuId;
import com.forkexec.rst.ws.MenuInit;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Restaurant
 *
 * A restaurant server.
 *
 */
public class Restaurant {
	
	public Map<MenuId, MenuInit> menus = new ConcurrentHashMap<>();


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

	
	public void init(List<MenuInit> initialMenus) throws BadInitFault_Exception{
		reset();
		
		for (MenuInit menu: initialMenus) {
			menus.put(menu.getMenu().getId(), menu);
		}
		
	}
	
	
}
