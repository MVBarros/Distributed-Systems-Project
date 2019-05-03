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
	
	public void setCacheElement (boolean valid, boolean dirty, int points, long tag, String mail) {
		setValid(valid);
		setDirty(dirty);
		this.balance.setPoints(points);
		this.balance.setTag(tag);
		setMail(mail);
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
	
	public int getPoints() {
		return this.balance.getPoints();
	}
	
	public long getTag() {
		return this.balance.getTag();
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
