package com.forkexec.hub.ws;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.jws.WebService;

import com.forkexec.cc.ws.cli.CCClient;
import com.forkexec.hub.domain.Hub;
import com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;
import com.forkexec.pts.ws.cli.PointsClient;
import com.forkexec.pts.ws.cli.PointsClientException;
import com.forkexec.rst.ws.BadMenuIdFault_Exception;
import com.forkexec.rst.ws.Menu;
import com.forkexec.rst.ws.MenuId;
import com.forkexec.rst.ws.cli.RestaurantClient;
import com.forkexec.rst.ws.cli.RestaurantClientException;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;

/**
 * This class implements the Web Service port type (interface). The annotations
 * below "map" the Java class to the WSDL definitions.
 */
@WebService(endpointInterface = "com.forkexec.hub.ws.HubPortType", wsdlLocation = "HubService.wsdl", name = "HubWebService", portName = "HubPort", targetNamespace = "http://ws.hub.forkexec.com/", serviceName = "HubService")
public class HubPortImpl implements HubPortType {

	/**
	 * The Endpoint manager controls the Web Service instance during its whole
	 * lifecycle.
	 */
	private HubEndpointManager endpointManager;

	/** Constructor receives a reference to the endpoint manager. */
	public HubPortImpl(HubEndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}

	// Main operations -------------------------------------------------------

	@Override
	public void activateAccount(String userId) throws InvalidUserIdFault_Exception {
		try {
			getPoints().activateUser(userId);
		} catch (EmailAlreadyExistsFault_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidEmailFault_Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadAccount(String userId, int moneyToAdd, String creditCardNumber)
			throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Food> searchDeal(String description) throws InvalidTextFault_Exception {
		// TODO return lowest price menus first
		return null;
	}

	@Override
	public List<Food> searchHungry(String description) throws InvalidTextFault_Exception {
		// TODO return lowest preparation time first
		return null;
	}

	@Override
	public void addFoodToCart(String userId, FoodId foodId, int foodQuantity)
			throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {
		// TODO

	}

	@Override
	public void clearCart(String userId) throws InvalidUserIdFault_Exception {
		// TODO

	}

	@Override
	public FoodOrder orderCart(String userId)
			throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception {
		// TODO
		return null;
	}

	@Override
	public int accountBalance(String userId) throws InvalidUserIdFault_Exception {

		int points = 0;
		
		try {
			points = getPoints().pointsBalance(userId);	
		} catch (InvalidEmailFault_Exception e) {
			throwInvalidUserId("Id given isn't valid, got exception" + e);
		}
		return points;
	}

	@Override
	public Food getFood(FoodId foodId) throws InvalidFoodIdFault_Exception {
		if (foodId == null)
			throwInvalidFoodId("foodId can't be null");
		if(foodId.getMenuId() == null)
			throwInvalidFoodId("menu can't be null");
		
		if(foodId.getRestaurantId() == null)
			throwInvalidFoodId("restaurant can't be null");
		
		RestaurantClient client = getRestaurantbyId(foodId.getRestaurantId());
		
		
		if (client == null) {
			throwInvalidFoodId("Restaurant does not exist");
		}
		
		MenuId menuId = null;
		try {
			menuId = client.getMenu(newMenuId(foodId)).getId();
		}catch (BadMenuIdFault_Exception e) {
			throwInvalidFoodId("No such food with that name in that restaurant");
		}
		
		
		return null;

	}

	@Override
	public List<FoodOrderItem> cartContents(String userId) throws InvalidUserIdFault_Exception {
		// TODO
		return null;
	}

	// Control operations ----------------------------------------------------

	/** Diagnostic operation to check if service is running. */
	@Override
	public String ctrlPing(String inputMessage) {
		// If no input is received, return a default name.
		if (inputMessage == null || inputMessage.trim().length() == 0)
			inputMessage = "friend";

		// If the service does not have a name, return a default.
		String wsName = endpointManager.getWsName();
		if (wsName == null || wsName.trim().length() == 0)
			wsName = "Hub";

		// Build a string with a message to return.
		StringBuilder builder = new StringBuilder();
		builder.append("Hello ").append(inputMessage);
		builder.append(" from ").append(wsName);
		StringBuilder responses = new StringBuilder();

		// Connect to Restaurant Server
		try {
			Collection<String> bindingsCol = this.endpointManager.getUddiNaming().list("T08_Restaurant%");

			bindingsCol.stream().forEach(binding -> {
				try {
					responses.append(pingRestaurant(binding));
				} catch (RestaurantClientException e) {
					throw new RuntimeException();
				}
			});

			try {
				String binding = this.endpointManager.getUddiNaming().lookup("T08_Points");
				responses.append(new PointsClient(binding).ctrlPing("hub").concat("\n"));
			} catch (PointsClientException e) {
				throw new RuntimeException();
			}

		} catch (UDDINamingException e) {
			System.out.println("UDDI Service unreachable, got exception" + e);
			return null;
		}
		responses.append(new CCClient().ping("hub").concat("\n"));
		return responses.append(builder.toString()).toString();
	}

	public String pingRestaurant(String binding) throws RestaurantClientException {
		System.out.println("Connecting to endpoint: " + binding);
		return new RestaurantClient(binding).ctrlPing("hub").concat("\n");
	}

	/** Return all variables to default values. */
	@Override
	public void ctrlClear() {
	}

	/** Set variables with specific values. */
	@Override
	public void ctrlInitFood(List<FoodInit> initialFoods) throws InvalidInitFault_Exception {
		

	}

	@Override
	public void ctrlInitUserPoints(int startPoints) throws InvalidInitFault_Exception {
		// TODO Auto-generated method stub

	}
	
	public RestaurantClient getRestaurantbyId(String id) {
		

		return getRestaurants().get(id);

	}

	public Map<String, RestaurantClient> getRestaurants() {
		Collection<String> bindingsCol = null;
		try {
			bindingsCol = this.endpointManager.getUddiNaming().list("T08_Restaurant%");
		} catch (UDDINamingException e) {
			System.out.println("UDDI Service unreachable, got exception" + e);
			throw new RuntimeException();
		}

		Map<String, RestaurantClient> restaurants = new HashMap<>();
		int i = 1;

		bindingsCol.stream().forEach(binding -> {
			try {
				restaurants.put("T08_Restaurant" + i, new RestaurantClient(binding));
			} catch (RestaurantClientException e) {
				System.out.println("Cannot Reach restaurant at " + binding + " got exception" + e);
				throw new RuntimeException();
			}
		});

		return restaurants;

	}

	public PointsClient getPoints() {
		String binding = null;
		try {
			binding = this.endpointManager.getUddiNaming().lookup("T08_Points");
		} catch (UDDINamingException e) {
			System.out.println("UDDI Service unreachable, got exception" + e);
			throw new RuntimeException();
		}
		try {
			return new PointsClient(binding);
		} catch (PointsClientException e) {
			System.out.println("Cannot Reach Points server at " + binding + " got exception" + e);
			throw new RuntimeException();
		}
	}

	public CCClient getCreditCard() {

		return new CCClient();
	}

	// View helpers ----------------------------------------------------------

	// /** Helper to convert a domain object to a view. */
	// private ParkInfo buildParkInfo(Park park) {
	// ParkInfo info = new ParkInfo();
	// info.setId(park.getId());
	// info.setCoords(buildCoordinatesView(park.getCoordinates()));
	// info.setCapacity(park.getMaxCapacity());
	// info.setFreeSpaces(park.getFreeDocks());
	// info.setAvailableCars(park.getAvailableCars());
	// return info;
	// }
	
	private MenuId newMenuId(FoodId id) {
		MenuId menuId = new MenuId();
		menuId.setId(id.getMenuId());
		return menuId;
	}
	
	private FoodId newFoodId(MenuId id, String restaurantId) {
		FoodId foodId = new FoodId();
		foodId.setMenuId(id.getId());
		foodId.setRestaurantId(restaurantId);
		return foodId;
	}
	
	private Food newFood(Menu menu, String restaurantId) {
		
		Food food = new Food();
		food.setDessert(menu.getDessert());
		food.setEntree(food.getEntree());
		food.setPlate(menu.getPlate());
		food.setPreparationTime(menu.getPreparationTime());
		food.setPrice(menu.getPreparationTime());
		
		FoodId foodId = new FoodId();
		foodId.setMenuId(menu.getId().getId());
		foodId.setRestaurantId(restaurantId);
		food.setId(foodId);
		
		return food;
	}

	// Exception helpers -----------------------------------------------------

	/** Helper to throw a new BadInit exception. */
//	private void throwBadInit(final String message) throws BadInitFault_Exception {
//		BadInitFault faultInfo = new BadInitFault();
//		faultInfo.message = message;
//		throw new BadInitFault_Exception(message, faultInfo);
//	}

	private void throwInvalidUserId(final String message) throws InvalidUserIdFault_Exception {
		InvalidUserIdFault faultInfo = new InvalidUserIdFault();
		faultInfo.message = message;
		throw new InvalidUserIdFault_Exception(message, faultInfo);
	}
	
	private void throwInvalidFoodId(final String message) throws InvalidFoodIdFault_Exception {
		InvalidFoodIdFault faultInfo = new InvalidFoodIdFault();
		faultInfo.message = message;
		throw new InvalidFoodIdFault_Exception(message, faultInfo);
	}
}
