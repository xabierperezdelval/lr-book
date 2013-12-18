package com.inikah.util;

public interface IConstants {
	int PROFILE_STATUS_CREATED = 0;
	int PROFILE_STATUS_STEP1_DONE = 1;
	int PROFILE_STATUS_STEP2_DONE = 2;
	int PROFILE_STATUS_STEP3_DONE = 3;
	int PROFILE_STATUS_STEP4_DONE = 4;
	int PROFILE_STATUS_PAYMENT_PENDING = 5;
	int PROFILE_STATUS_ACTIVE = 6;
	int PROFILE_STATUS_INACTIVE = 7;
	
	String PROFILE_STATUS_PENDING = "pending";
	String PROFILE_STATUS_DELETED = "deleted";
	
	String LIST_MARITAL_STATUS = "maritalStatus"; 
	String LIST_CREATED_FOR = "createdFor";
	String LIST_COMPLEXION = "complexion";
	String LIST_REMARRIAGE_REASON = "remarriageReason";
	String LIST_EDUCATION = "education";
	String LIST_RELIGIOUS_EDUCATION = "religiousEducation";
	String LIST_PROFESSION ="profession";
	String LIST_INCOME_FREQUENCY = "incomeFrequency";

	int INT_ACTION_VIEWED = 1;
	int INT_ACTION_BOOKMARKED = 2;
	int INT_ACTION_PROSPOSED = 3;
	int INT_ACTION_VIEWED_PROPOSAL = 4;
	int INT_ACTION_ACCEPTED = 4;
	int INT_ACTION_DECLINED = 5;
	int INT_ACTION_BLOCKED = 6;
	int INT_ACTION_CAPTURED_IN_REPORT = 7;
	int INT_ACTION_VEIWED_CONTACT_INFO = 8;
	
	int LOC_TYPE_REGION = 1;
	int LOC_TYPE_CITY = 2;
	int LOC_TYPE_AREA = 3;
	int LOC_TYPE_MASJID = 4;
	
	int IMAGE_TYPE_PHOTO = 1;
	int IMAGE_TYPE_FACE = 2;
	int IMAGE_TYPE_DOCUMENT = 3;
	
	String PAY_MODE_CHEQUE_DD = "chequeDD";
	
	int PHONE_VERIFIED = 1;
	
	String OWNER_VIEW = "/html/list/owner-view.jsp";
}