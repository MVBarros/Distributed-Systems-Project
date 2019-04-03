package com.forkexec.rst.domain;

import com.forkexec.rst.ws.BadMenuIdFault_Exception;
import com.forkexec.rst.ws.BadQuantityFault_Exception;
import com.forkexec.rst.ws.InsufficientQuantityFault_Exception;
import com.forkexec.rst.ws.MenuId;
import com.forkexec.rst.ws.MenuOrder;

public class RestaurantMenuOrder {
	
	private String id;
    protected String menuId;
    protected int menuQuantity;
    
    public RestaurantMenuOrder(String id, String menuId, int quantity) {
		this.id = id;
		this.menuId = menuId;
		this.menuQuantity = quantity;
	}
    
    public RestaurantMenuOrder(String menuId, int quantity) {
		this.id = Restaurant.getInstance().getCurrentOrderId();
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



	