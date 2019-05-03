package com.forkexec.pts.ws.cli;

public class PointsFrontEndCache {
	
	private PointsFrontEnd frontEnd;
	public static final int CACHE_SIZE = 10;
	
	public PointsCacheElement cache[];
	public int index;
	
	public PointsFrontEndCache( PointsFrontEnd fe) {
		this.cache = new PointsCacheElement[CACHE_SIZE];
		/*init cache*/
		for (int i = 0; i < CACHE_SIZE; i++) {
			cache[i] = new PointsCacheElement();
		}
		this.index = 0;
		this.frontEnd = fe;
	}
	
	public int WriteCache(String email, int points, long tag) {
		
		for (PointsCacheElement elem: cache) {
			if(elem.isValid() && elem.getMail().equals(email)) {
				// valid, dirty, points, tag, mail
				elem.setCacheElement(true, true, points, tag, email);
				return points;
		}}
		
		// Write Back
		if (cache[index].isDirty())
			frontEnd.pointsWrite(cache[index].getMail(), cache[index].getPoints(), cache[index].getTag());
	
		// valid, dirty, points, tag, mail
		cache[index].setCacheElement(true, true, points, tag, email);
		index = (index + 1) % CACHE_SIZE;
		return points;
		
	}
}
