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

	public void addToCart(String foodId, int quantity) {
		if (cart.get(foodId) != null)
			cart.put(foodId, cart.get(foodId) + quantity);
		else
			cart.put(foodId, quantity);
	}
	
	public void clear() {
		cart.clear();
		orderId = null;
	}

}
