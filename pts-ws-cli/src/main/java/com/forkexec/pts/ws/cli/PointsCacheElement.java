package com.forkexec.pts.ws.cli;

import com.forkexec.pts.ws.Balance;

public class PointsCacheElement {
	
	private boolean dirty;
	private Balance balance;
	
	public boolean isDirty() {
		return dirty;
	}
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	public Balance getBalance() {
		return balance;
	}
	public void setBalance(Balance balance) {
		this.balance = balance;
	}
	
	public PointsCacheElement() {
		this.dirty = false;
		this.balance = null;
	}

	
}
