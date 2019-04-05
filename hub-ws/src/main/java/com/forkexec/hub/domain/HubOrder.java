package com.forkexec.hub.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HubOrder {

	private Map<String, Integer> cart = new ConcurrentHashMap<>();

	private String orderId = null;

	public void setorderId() {
		setOrderId(Hub.getInstance().getcurrentOrderId());
	}

	public String getOrderId() {
		return orderId;
	}

	public Map<String, Integer> getCart() {
		return cart;
	}

	private void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public synchronized void addToCart(String foodId, int quantity) throws CartQuantityException {
		if (cart.get(foodId) != null) {
			/* Just update Quantity */
			if (cart.get(foodId) + quantity <= 0) {
				throw new CartQuantityException("Cannot remove more quantity than what you have currently");
			} else {
				cart.put(foodId, cart.get(foodId) + quantity);
			}
		} else {
			/* Add food to cart */
			if (quantity > 0) {
				cart.put(foodId, quantity);
			} else {
				throw new CartQuantityException("Cannot had negative amount of menu to cart");
			}
		}
	}

	public synchronized void clear() {
		cart.clear();
		orderId = null;
	}

}
