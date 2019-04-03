package com.forkexec.rst.domain;


/**Restaurant Order Domain Object*/
public class RestaurantMenuOrder {
	
	private String id;
    protected String menuId;
    protected int menuQuantity;
    
    public RestaurantMenuOrder(String id, String menuId, int quantity) {
		this.id = id;
		this.menuId = menuId;
		this.menuQuantity = quantity;
	}
    

	public String getId() {
		return id;
	}

	public String getMenuId() {
		return menuId;
	}

	public int getMenuQuantity() {
		return menuQuantity;
	}
    
    
}



	