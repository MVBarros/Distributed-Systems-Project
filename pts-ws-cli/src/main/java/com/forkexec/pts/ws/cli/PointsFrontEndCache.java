package com.forkexec.pts.ws.cli;

import com.forkexec.pts.ws.Balance;

public class PointsFrontEndCache {

	public static final int CACHE_SIZE = 10;

	public PointsCacheElement cache[];
	public int index;

	public PointsFrontEndCache() {
		this.cache = new PointsCacheElement[CACHE_SIZE];
		/* init cache */
		for (int i = 0; i < CACHE_SIZE; i++) {
			cache[i] = new PointsCacheElement();
		}
		this.index = 0;
	}

	public Balance cacheRead(String email) {
		synchronized (cache) {
			for (int i = 0; i < CACHE_SIZE; i++) 
				if (cache[i].isValid()) 
					if (cache[i].getMail().equals(email)) 
						return cache[i].getBalance();			
		}
		return null;

	}

	public void WriteCache(String email, int points, long tag) {
		return;
	}
}
