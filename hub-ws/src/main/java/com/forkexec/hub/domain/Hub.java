package com.forkexec.hub.domain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import com.forkexec.hub.ws.Food;
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

	// clientId --> (FoodId & RestaurantId) + quantity 
	private Map<String, HubOrder> carts = new ConcurrentHashMap<String, HubOrder>();
	
	private volatile int currentOrderId = 0;


	
	
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
	

	public synchronized void add2Cart(String userId, String foodid, Integer quantity) {
		if (!carts.containsKey(userId)) {
			carts.put(userId, new HubOrder());
		}	
		HubOrder order = carts.get(userId);
		order.addToCart(foodid, quantity);
		carts.put(userId, order);
	}
	
	public synchronized void clearCart(String userId) {
		if (!carts.containsKey(userId)) 
			return;
		carts.get(userId).clear();
	}
	
	public synchronized String getcurrentOrderId() {
		currentOrderId++;
		return Integer.toString(currentOrderId);
	}

	


	
}
