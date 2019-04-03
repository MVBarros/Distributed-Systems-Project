package com.forkexec.rst.domain;

import java.util.stream.Stream;

/**Restaurant Menu Domain Object*/
public class RestaurantMenu {

	private String id;
	private String entree;
	private String plate;
	private String dessert;
	private int price;
	private int preparationTime;
	private volatile int quantity;

	public RestaurantMenu(String id, String entree, String plate, String dessert, int price, int prepTime,
			int quantity) {
		this.id = id;
		this.entree = entree;
		this.plate = plate;
		this.dessert = dessert;
		this.price = price;
		this.preparationTime = prepTime;
		this.quantity = quantity;
	}
	
	public String getDescription() {
		StringBuilder builder = new StringBuilder();
		Stream.of(entree, plate, dessert).forEach(e -> builder.append(e + "\n"));

		return builder.toString();
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

}
