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
    
    /**Regex to check emails*/
    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\."+ 
            "[a-zA-Z0-9_+&*-]+)*@" + 
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
            "A-Z]{2,7}$"); 

    // Singleton -------------------------------------------------------------

    /**
     * Private constructor prevents instantiation from other classes.
     */
    private Points() { }

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
    
    /*Points functions*/
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
    
    
    /*Email functions*/
    
    public boolean checkEmail(String email) {
    	if (email == null) return false;
    	
    	return PATTERN.matcher(email).matches();
    }
    
    public synchronized boolean checkNewEmail(String email) {
    	return accounts.containsKey(email);
    }
    
    /*Account functions*/
    public synchronized void addAccount(String email) {
    	accounts.put(email, initialBalance.get());
    }
    
}
