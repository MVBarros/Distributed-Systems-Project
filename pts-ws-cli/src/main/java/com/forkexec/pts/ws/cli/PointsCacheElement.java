package com.forkexec.pts.ws.cli;

import com.forkexec.pts.ws.Balance;

public class PointsCacheElement {
	private boolean valid;
	private boolean dirty;
	private Balance balance;
	private String mail;
	
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
		this.mail = null;
		this.valid = false;
	}
	
	public String getMail() {
		return mail;
	}
	
	public void setMail(String mail) {
		this.mail = mail;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}

	
}
