package com.forkexec.pts.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.regex.Pattern;

/**
 * Points
 * <p>
 * A points server.
 */
public class Points {

	/**
	 * Constant representing the default initial balance for every new client
	 */
	private static final int DEFAULT_INITIAL_BALANCE = 100;

	/**
	 * Global with the current value for the initial balance of every new client
	 */
	private final AtomicInteger initialBalance = new AtomicInteger(DEFAULT_INITIAL_BALANCE);

	private Map<String, Integer> accounts = new ConcurrentHashMap<String, Integer>();

	/** Regex to check emails */
	private static final Pattern PATTERN = Pattern
			.compile("^[a-zA-Z0-9]+(.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+(.[a-zA-Z0-9]+)*");

	// Singleton -------------------------------------------------------------

	/**
	 * Private constructor prevents instantiation from other classes.
	 */
	private Points() {
	}

	/**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance()
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder {
		private static final Points INSTANCE = new Points();
	}

	public static synchronized Points getInstance() {
		return SingletonHolder.INSTANCE;
	}

	/* Points functions */
	public synchronized void resetPoints() {
		accounts.clear();
		setStartingPoints(DEFAULT_INITIAL_BALANCE);
	}

	public synchronized void initPoints(int points) {
		accounts.clear();
		setStartingPoints(points);
	}

	public synchronized void setStartingPoints(int points) {
		this.initialBalance.set(points);
	}

	public synchronized int getUserPoints(String email) throws NoSuchEmailException{
		
		if(!accounts.containsKey(email)) throw new NoSuchEmailException();

		return accounts.get(email);
	}

	public synchronized int addUserPoints(String email, int toAdd) throws InvalidPointCountException, NoSuchEmailException  {
		
		if (toAdd <= 0) throw new InvalidPointCountException();
		
		if(!accounts.containsKey(email)) throw new NoSuchEmailException();
		
		accounts.replace(email, accounts.get(email) + toAdd);

		return accounts.get(email);
	}
	

	public synchronized int removeUserPoints(String email, int toRemove) throws InvalidPointCountException, NoSuchEmailException, NotEnoughBalanceException {
		
		if (toRemove <= 0) throw new InvalidPointCountException();
		
		if(!accounts.containsKey(email)) throw new NoSuchEmailException();
		
		if(toRemove > accounts.get(email)) throw new NotEnoughBalanceException();
		
		accounts.replace(email, accounts.get(email) - toRemove);

		return accounts.get(email);
	}

	/* Email functions */

	public void checkEmail(String email) throws InvalidEmailNameException {

		if (!PATTERN.matcher(email).matches()) throw new InvalidEmailNameException();
	}

	public synchronized void checkEmailExists(String email) throws RepeatedUserEmailException {
		if (accounts.containsKey(email)) throw new RepeatedUserEmailException();
	}

	/* Account functions */
	public synchronized void addAccount(String email) throws InvalidEmailNameException,
												RepeatedUserEmailException {
		checkEmail(email);
		checkEmailExists(email);
		accounts.put(email, initialBalance.get());
	}

}
