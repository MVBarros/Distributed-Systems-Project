package com.forkexec.hub.domain;

public class HubFood {


	private String restaurantId;
	private String id;
	private String foodId;
	private String entree;
	private String plate;
	private String dessert;
	private int price;
	private int preparationTime;
	private volatile int quantity;

	public HubFood(String id, String entree, String plate, String dessert, int price, int prepTime,
			int quantity, String restaurantId) {
		this.id = id;
		this.entree = entree;
		this.plate = plate;
		this.dessert = dessert;
		this.price = price;
		this.preparationTime = prepTime;
		this.quantity = quantity;
		this.restaurantId = restaurantId;
		this.foodId = id + restaurantId;
	}
	
	public synchronized int getQuantity() {
		return quantity;
	}

	public synchronized void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getId() {
		return id;
	}

	public String getEntree() {
		return entree;
	}

	public String getPlate() {
		return plate;
	}

	public String getDessert() {
		return dessert;
	}

	public int getPrice() {
		return price;
	}

	public int getPreparationTime() {
		return preparationTime;
	}
	
	public String getRestaurantId() {
		return restaurantId;
	}
	
	public String getFoodId() {
		return foodId;
	}

	
}
