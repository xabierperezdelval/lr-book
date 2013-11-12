package com.inikah.invite;

public interface InviteConstants {
	public int STATUS_ALL=-1;
	public int STATUS_CREATED = 0;//PENDING
	public int STATUS_REGISTERED = 1;//INVITED
	public int STATUS_LINKED = 2;//ACCEPTED
		
	static final String TAB_MY_INVITATIONS = "Pending Requests";
}
