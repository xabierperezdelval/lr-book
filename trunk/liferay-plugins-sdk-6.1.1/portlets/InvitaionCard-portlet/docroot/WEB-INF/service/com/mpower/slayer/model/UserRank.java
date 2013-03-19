package com.mpower.slayer.model;

public class UserRank {
	
	private long userId;
	private int countofAcceptedInvitations;
	private int rank;

	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public int getCountofAcceptedInvitations() {
		return countofAcceptedInvitations;
	}
	public void setCountofAcceptedInvitations(int countofAcceptedInvitations) {
		this.countofAcceptedInvitations = countofAcceptedInvitations;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
}
