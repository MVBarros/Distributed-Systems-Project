package com.forkexec.pts.ws.cli;

import com.forkexec.pts.ws.Balance;

public class PointsFrontEndCache {

	private PointsFrontEnd frontEnd;

	public static final int CACHE_SIZE = 10;

	public PointsCacheElement cache[];
	public int index;

	public PointsFrontEndCache(PointsFrontEnd fe) {
		this.cache = new PointsCacheElement[CACHE_SIZE];
		/* init cache */
		for (int i = 0; i < CACHE_SIZE; i++) {
			cache[i] = new PointsCacheElement();
		}
		this.index = 0;
		this.frontEnd = fe;
	}

	public int writeCache(String email, int points, long tag) {

		synchronized (cache) {
			for (PointsCacheElement elem : cache) {
				if (elem.isValid() && elem.getMail().equals(email)) 
					if (tag > elem.getTag()) {
						// valid, dirty, points, tag, mail
						elem.setCacheElement(true, true, points, tag, email);
						return points;
					}
					else return elem.getPoints();		
			}

			writeBack();

			// valid, dirty, points, tag, mail
			cache[index].setCacheElement(true, true, points, tag, email);
			index = (index + 1) % CACHE_SIZE;
			return points;
		}
	}

	public void writeBack() {
		if (cache[index].isDirty())
			frontEnd.pointsWrite(cache[index].getMail(), cache[index].getPoints(), cache[index].getTag());
	}

	public Balance cacheRead(String email) {
		synchronized (cache) {
			for (int i = 0; i < CACHE_SIZE; i++)
				if (cache[i].isValid() && cache[i].getMail().equals(email))
					return cache[i].getBalance();

			Balance balance = frontEnd.pointsRead(email);

			writeBack();
			cache[index].setCacheElement(true, false, balance.getPoints(), balance.getTag(), email);
			index = (index + 1) % CACHE_SIZE;
			return balance;
		}
	}
}
