package com.forkexec.pts.domain;

public class BalanceSequence {
	private int points;
	private long sequence;
	
	public int compareTo(BalanceSequence bs) {
		return (int) (sequence - bs.getSequence() );
	}
	
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public long getSequence() {
		return sequence;
	}
	public void setSequence(long sequence) {
		this.sequence = sequence;
	}
	
	
}
