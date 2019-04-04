package com.forkexec.hub.ws;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.jws.WebService;

import com.forkexec.cc.ws.cli.CCClient;
import com.forkexec.hub.domain.Hub;
import com.forkexec.pts.ws.BadInitFault_Exception;
import com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;
import com.forkexec.pts.ws.InvalidPointsFault_Exception;
import com.forkexec.pts.ws.NotEnoughBalanceFault_Exception;
import com.forkexec.pts.ws.cli.PointsClient;
import com.forkexec.pts.ws.cli.PointsClientException;
import com.forkexec.rst.ws.BadMenuIdFault_Exception;
import com.forkexec.rst.ws.BadQuantityFault_Exception;
import com.forkexec.rst.ws.BadTextFault_Exception;
import com.forkexec.rst.ws.InsufficientQuantityFault_Exception;
import com.forkexec.rst.ws.Menu;
import com.forkexec.rst.ws.MenuId;
import com.forkexec.rst.ws.MenuInit;
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
			throwInvalidUserId("Account has already been activated");
		} catch (InvalidEmailFault_Exception e) {
			throwInvalidUserId("Email not valid");
		}
	}

	@Override
	public void loadAccount(String userId, int moneyToAdd, String creditCardNumber)
			throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {

		if (userId == null)
			throwInvalidUserId("User Id can't be null");

		if (!getCreditCard().validateNumber(creditCardNumber))
			throwInvalidCreditCard("Invalid credit card number");

		try {
			switch (moneyToAdd) {
			case 10:
				getPoints().addPoints(userId, 1000);
				break;
			case 20:
				getPoints().addPoints(userId, 2100);
				break;
			case 30:
				getPoints().addPoints(userId, 3300);
				break;
			case 50:
				getPoints().addPoints(userId, 5500);
				break;
			default:
				throwInvalidMoney("Invalid Money Amount");
			}
		} catch (InvalidEmailFault_Exception e) {
			throwInvalidUserId("Invalid user Id");

		} catch (InvalidPointsFault_Exception e) {
			throwInvalidMoney("Invalid Money Amount");
		}

	}

	@Override
	public List<Food> searchDeal(String description) throws InvalidTextFault_Exception {
		List<Food> foods = searchFood(description);

		Collections.sort(foods, new Comparator<Food>() {
			@Override
			public int compare(Food lhs, Food rhs) {
				// -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
				return lhs.price < rhs.price ? -1 : (lhs.price > rhs.price) ? 1 : 0;
			}
		});
		return foods;
	}

	@Override
	public List<Food> searchHungry(String description) throws InvalidTextFault_Exception {

		List<Food> foods = searchFood(description);

		Collections.sort(foods, new Comparator<Food>() {
			@Override
			public int compare(Food lhs, Food rhs) {
				// -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
				return lhs.getPreparationTime() < rhs.getPreparationTime() ? -1
						: (lhs.getPreparationTime() > rhs.getPreparationTime()) ? 1 : 0;
			}
		});

		return foods;
	}

	private List<Food> searchFood(String description) throws InvalidTextFault_Exception {
		List<Food> foods = new ArrayList<>();

		for (Map.Entry<String, RestaurantClient> pair : getRestaurants().entrySet()) {
			try {
				for (Menu menu : pair.getValue().searchMenus(description)) {
					foods.add(newFood(menu, pair.getKey()));
				}
			} catch (BadTextFault_Exception e) {
				throwInvalidText("Invalid description");
			}
		}

		return foods;
	}

	@Override
	public void addFoodToCart(String userId, FoodId foodId, int foodQuantity)
			throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {

		if (userId == null || userId.contains(" "))
			throwInvalidUserId("Invalid user id");
		if (foodQuantity <= 0)
			throwInvalidFoodQuantity("Invalid food quantity");
		if (foodId == null)
			throwInvalidFoodId("Invalid food id");
		if (foodId.getMenuId() == null)
			throwInvalidFoodId("menu can't be null");

		if (foodId.getRestaurantId() == null)
			throwInvalidFoodId("restaurant can't be null");
		
		if (!getRestaurantIds().contains(foodId.getRestaurantId()))
			throwInvalidFoodId("Non existing restaurant");
		
		try {
			getPoints().pointsBalance(userId);
		} catch (InvalidEmailFault_Exception e) {
			throwInvalidUserId("Invalid user id");
		}

		/* Order Id is menuId\nrestaurantId */
		Hub.getInstance().add2Cart(userId, foodId.getMenuId() + "\n" + foodId.getRestaurantId(), foodQuantity);

	}

	@Override
	public void clearCart(String userId) throws InvalidUserIdFault_Exception {

		if (userId == null || userId.contains(" "))
			throwInvalidUserId("Invalid user id");
		try {
			getPoints().pointsBalance(userId);
		} catch (InvalidEmailFault_Exception e) {
			throwInvalidUserId("User id isn't registered");
		}

		Hub.getInstance().clearCart(userId);

	}

	@Override
	public FoodOrder orderCart(String userId)
			throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception {

		List<FoodOrderItem> items = cartContents(userId);
		if (items.size() == 0)
			throwEmptyCart("Cart is empty");

		int totalpoints = 0;
		int price = 0;
		for (FoodOrderItem item : items) {

			try {
				price = getFood(item.getFoodId()).getPrice();
			} catch (InvalidFoodIdFault_Exception e) {
				/* will never happen */
				throw new RuntimeException();
			}

			totalpoints += item.getFoodQuantity() * price;

		}
		try {
			getPoints().spendPoints(userId, totalpoints);
		} catch (NotEnoughBalanceFault_Exception e) {
			throwNotEnoughPoints("");
		} catch (Exception e) {
			/* Will never happen */
			throw new RuntimeException();
		}

		for (FoodOrderItem item : items) {
			try {
				getRestaurantbyId(item.getFoodId().getRestaurantId()).orderMenu(newMenuId(item.getFoodId()),
						item.getFoodQuantity());
			} catch (BadMenuIdFault_Exception e) {
				/* will never happen */
				throw new RuntimeException();
			} catch (BadQuantityFault_Exception e) {
				/* will never happen */
				throw new RuntimeException();
			} catch (InsufficientQuantityFault_Exception e) {
				// TODO What
			}
		}

		FoodOrder order = new FoodOrder();
		FoodOrderId orderId = new FoodOrderId();
		orderId.setId(Hub.getInstance().getcurrentOrderId());
		order.setFoodOrderId(orderId);

		/* nao da para fazer set dos items do order */
		return order;
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

		if (foodId.getMenuId() == null)
			throwInvalidFoodId("menu can't be null");

		if (foodId.getRestaurantId() == null)
			throwInvalidFoodId("restaurant can't be null");

		RestaurantClient client = getRestaurantbyId(foodId.getRestaurantId());

		if (client == null) {
			throwInvalidFoodId("Restaurant does not exist");
		}

		Menu menu = null;
		try {
			menu = client.getMenu(newMenuId(foodId));
		} catch (BadMenuIdFault_Exception e) {
			throwInvalidFoodId("No such food with that name in that restaurant");
		}

		return newFood(menu, foodId.getRestaurantId());

	}

	@Override
	public List<FoodOrderItem> cartContents(String userId) throws InvalidUserIdFault_Exception {
		List<FoodOrderItem> items = new ArrayList<>();
		try {
			getPoints().pointsBalance(userId);
		} catch (InvalidEmailFault_Exception e) {
			throwInvalidUserId("Id given isn't valid, got exception" + e);
		}
		Map<String, Integer> carts = Hub.getInstance().getCart(userId);

		for (String cartName : carts.keySet()) {
			String[] ids = cartName.split("\\n");
			String restaurantId = ids[0];
			String menuId = ids[1];

			FoodId foodId = new FoodId();
			foodId.setMenuId(menuId);
			foodId.setRestaurantId(restaurantId);

			int quantity = carts.get(cartName);

			FoodOrderItem item = new FoodOrderItem();
			item.setFoodId(foodId);
			item.setFoodQuantity(quantity);
			items.add(item);
		}

		return items;
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
				bindingsCol = this.endpointManager.getUddiNaming().list("T08_Points%");
				for (String binding : bindingsCol) {
					responses.append(new PointsClient(binding).ctrlPing("hub").concat("\n"));
				}
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
		Hub.getInstance().clear();
		getPoints().ctrlClear();
		Map<String, RestaurantClient> clients = getRestaurants();

		for (RestaurantClient client : clients.values()) {
			client.ctrlClear();
		}
	}

	/** Set variables with specific values. */
	@Override
	public void ctrlInitFood(List<FoodInit> initialFoods) throws InvalidInitFault_Exception {
		if (initialFoods == null) {
			throwInvalidInit("initialFoods can't be null");
		}

		if (initialFoods.contains(null))
			throwInvalidInit("Cannot Init null food");

		Map<String, List<MenuInit>> initMap = new ConcurrentHashMap<>();

		Collection<String> ids = getRestaurantIds();

		for (String serviceId : ids)
			initMap.put(serviceId, new ArrayList<MenuInit>());

		for (FoodInit foodInit : initialFoods) {
			if (foodInit.getFood() == null)
				throwInvalidInit("Cannot Init null food");

			if (foodInit.getFood().getId() == null)
				throwInvalidInit("Cannot Init food with null Id");

			MenuInit init = newMenuInit(foodInit);

			List<MenuInit> inits = initMap.get(foodInit.getFood().getId().getRestaurantId());

			if (inits == null) {
				throwInvalidInit("No such restaurant");
			}
			inits.add(init);

		}

		for (String serviceId : ids) {
			try {
				getRestaurantbyId(serviceId).ctrlInit(initMap.get(serviceId));
			} catch (com.forkexec.rst.ws.BadInitFault_Exception e) {
				throwInvalidInit("Invalid Menu to init");

			}
		}

	}

	@Override
	public void ctrlInitUserPoints(int startPoints) throws InvalidInitFault_Exception {
		try {
			getPoints().ctrlInit(startPoints);
		} catch (BadInitFault_Exception e) {
			throwInvalidInit("cannot init Points with those points");
		}

	}

	public RestaurantClient getRestaurantbyId(String id) {

		return getRestaurants().get(id);

	}

	private Collection<String> getRestaurantIds() {
		Collection<String> ids = new ArrayList<String>();

		Collection<String> bindingsCol = null;
		try {
			bindingsCol = this.endpointManager.getUddiNaming().list("T08_Restaurant%");
		} catch (UDDINamingException e) {
			System.out.println("UDDI Service unreachable, got exception" + e);
			throw new RuntimeException();
		}

		int i = 1;

		for (@SuppressWarnings("unused")
		String binding : bindingsCol) {
			ids.add("T08_Restaurant" + i);
			i++;
		}

		return ids;

	}

	private Map<String, RestaurantClient> getRestaurants() {
		Collection<String> bindingsCol = null;
		try {
			bindingsCol = this.endpointManager.getUddiNaming().list("T08_Restaurant%");
		} catch (UDDINamingException e) {
			System.out.println("UDDI Service unreachable, got exception" + e);
			throw new RuntimeException();
		}

		Map<String, RestaurantClient> restaurants = new HashMap<>();

		int i = 1;

		for (String binding : bindingsCol) {
			try {
				restaurants.put("T08_Restaurant" + i, new RestaurantClient(binding));
				i++;
			} catch (RestaurantClientException e) {
				System.out.println("Cannot Reach restaurant at " + binding + " got exception" + e);
				throw new RuntimeException();
			}
		}

		return restaurants;

	}

	private PointsClient getPoints() {
		String binding = null;
		try {
			binding = this.endpointManager.getUddiNaming().lookup("T08_Points%");
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

	private CCClient getCreditCard() {

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
	/*
	 * private FoodId newFoodId(MenuId id, String restaurantId) { FoodId foodId =
	 * new FoodId(); foodId.setMenuId(id.getId());
	 * foodId.setRestaurantId(restaurantId); return foodId; }
	 */

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

	private MenuInit newMenuInit(FoodInit foodInit) {
		MenuInit menuInit = new MenuInit();
		MenuId id = newMenuId(foodInit.getFood().getId());

		Menu menu = new Menu();
		menu.setDessert(foodInit.getFood().getDessert());
		menu.setEntree(foodInit.getFood().getEntree());
		menu.setPlate(foodInit.getFood().getPlate());
		menu.setId(id);
		menu.setPreparationTime(foodInit.getFood().getPreparationTime());
		menu.setPrice(foodInit.getFood().getPrice());

		menuInit.setMenu(menu);
		menuInit.setQuantity(foodInit.getQuantity());

		return menuInit;
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

	private void throwInvalidMoney(final String message) throws InvalidMoneyFault_Exception {
		InvalidMoneyFault faultInfo = new InvalidMoneyFault();
		faultInfo.message = message;
		throw new InvalidMoneyFault_Exception(message, faultInfo);
	}

	private void throwInvalidCreditCard(final String message) throws InvalidCreditCardFault_Exception {
		InvalidCreditCardFault faultInfo = new InvalidCreditCardFault();
		faultInfo.message = message;
		throw new InvalidCreditCardFault_Exception(message, faultInfo);
	}

	private void throwInvalidText(final String message) throws InvalidTextFault_Exception {
		InvalidTextFault faultInfo = new InvalidTextFault();
		faultInfo.message = message;
		throw new InvalidTextFault_Exception(message, faultInfo);
	}

	private void throwInvalidFoodId(final String message) throws InvalidFoodIdFault_Exception {
		InvalidFoodIdFault faultInfo = new InvalidFoodIdFault();
		faultInfo.message = message;
		throw new InvalidFoodIdFault_Exception(message, faultInfo);
	}

	private void throwInvalidFoodQuantity(final String message) throws InvalidFoodQuantityFault_Exception {
		InvalidFoodQuantityFault faultInfo = new InvalidFoodQuantityFault();
		faultInfo.message = message;
		throw new InvalidFoodQuantityFault_Exception(message, faultInfo);
	}

	private void throwInvalidInit(final String message) throws InvalidInitFault_Exception {
		InvalidInitFault faultInfo = new InvalidInitFault();
		faultInfo.message = message;
		throw new InvalidInitFault_Exception(message, faultInfo);
	}

	private void throwEmptyCart(final String message) throws EmptyCartFault_Exception {
		EmptyCartFault faultInfo = new EmptyCartFault();
		faultInfo.message = message;
		throw new EmptyCartFault_Exception(message, faultInfo);
	}

	private void throwNotEnoughPoints(final String message) throws NotEnoughPointsFault_Exception {
		NotEnoughPointsFault faultInfo = new NotEnoughPointsFault();
		faultInfo.message = message;
		throw new NotEnoughPointsFault_Exception(message, faultInfo);
	}
}
