package com.forkexec.hub.domain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import com.forkexec.hub.ws.FoodInit;
import com.forkexec.rst.domain.BadMenuInitiationException;
import com.forkexec.rst.domain.RestaurantMenu;




/**
 * Hub
 *
 * A restaurants hub server.
 *
 */
public class Hub {
	

	private Map<String, HubFood> foodsMap = new ConcurrentHashMap<>();

	
	
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
	
	public void initFood(List<HubFood> foods) {
		//reset();

		for (HubFood menu : foods) {
			if (!acceptFood(menu)) {
				/*remove all menus from restaurant*/
				//reset(); 
				//throw new BadMenuInitiationException("Invalid menu initiation");
			}
			foodsMap.put(menu.getId(), menu);
		}
		
		
	}
	
	public boolean acceptFood(HubFood food) {
		return (Stream.of(food.getId(), food.getEntree(), food.getPlate(), food.getDessert())
				.noneMatch(e-> (e == null) || ((e.trim().isEmpty()))) 
				&& 
				Stream.of(food.getPrice(), food.getPreparationTime(), food.getQuantity())
				.allMatch(e -> e > 0))
				&&
				!foodsMap.containsKey(food.getFoodId());
	}
	
}
