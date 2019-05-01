package com.forkexec.pts.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


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
	
	
	
	private Map<String, BalanceSequence> accounts = new ConcurrentHashMap<String, BalanceSequence>();


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

	public synchronized void pointsWrite(String email, int points, long tag) {
		if(!accounts.containsKey(email)) {
			accounts.put(email, new BalanceSequence(points, tag));
		}
		else {
			if (tag > accounts.get(email).getSequence())
				accounts.put(email, new BalanceSequence(points, tag));
			return;
		}
	}

}
