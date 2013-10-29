package com.inikah.util;

public interface IConstants {
	int PROFILE_STATUS_CREATED = 0;
	String PROFILE_STATUS_DRAFT = "draft";
	String PROFILE_STATUS_PENDING = "pending";
	String PROFILE_STATUS_ACTIVE = "active";
	String PROFILE_STATUS_DELETED = "deleted";
	
	String LIST_MARITAL_STATUS = "maritalStatus"; 
	String LIST_CREATED_FOR = "createdFor";
	
	int INT_ACTION_VIEWED = 1;
	int INT_ACTION_BOOKMARKED = 2;
	int INT_ACTION_PROSPOSED = 3;
	int INT_ACTION_VIEWED_PROPOSAL = 4;
	int INT_ACTION_ACCEPTED = 4;
	int INT_ACTION_DECLINED = 5;
	int INT_ACTION_BLOCKED = 6;
	int INT_ACTION_CAPTURED_IN_REPORT = 7;
	int INT_ACTION_VEIWED_CONTACT_INFO = 8;
	
	String OWNER_VIEW = "/html/list/owner-view.jsp";
}
