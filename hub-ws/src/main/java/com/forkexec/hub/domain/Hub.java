package com.forkexec.hub.domain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import com.forkexec.hub.ws.FoodInit;
//import com.forkexec.rst.domain.BadMenuInitiationException;
//import com.forkexec.rst.domain.RestaurantMenu;




/**
 * Hub
 *
 * A restaurants hub server.
 *
 */
public class Hub {
	




	
	
	// Singleton -------------------------------------------------------------

	/** Private constructor prevents instantiation from other classes. */
	private Hub() {
		// Initialization of default values
	}

	/**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance()
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder {
		private static final Hub INSTANCE = new Hub();
	}

	public static synchronized Hub getInstance() {
		return SingletonHolder.INSTANCE;
	}

		
	

	
}
