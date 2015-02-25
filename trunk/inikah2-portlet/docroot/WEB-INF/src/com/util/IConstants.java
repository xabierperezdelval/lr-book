package com.util;

import com.liferay.portal.kernel.util.GetterUtil;

public interface IConstants {
	int CREATED_FOR_SELF = 1;
	
	int STATUS_SUBMITTED = 5;
	int STATUS_DELETED = 99;
	
	int LOC_TYPE_REGION = 1;
	int LOC_TYPE_CITY = 2;
	int LOC_TYPE_AREA = 3;
	int LOC_TYPE_MASJID = 4;
	
	int PHONE_UNVERIFIED = 777;
	int PHONE_VERIFIED = 999;	
	
	// openxchangerates.org
	boolean CFG_OPENXCHAGE_UPDATE = GetterUtil.getBoolean(AppConfig.get("openxchange-update"));
	String CFG_OPENXCHAGE_API_ID = "openxchange-api-id";	
	
	// clickatell (for SMS)
	String CFG_CLICKATELL_USERNAME = "clickatell-username";
	String CFG_CLICKATELL_PASSWORD = "clickatell-password";
	String CFG_CLICKATELL_API_ID = "clickatell-api-id";	
	
	boolean CFG_NOTIFY_OLD_USERS = GetterUtil.getBoolean(AppConfig.get("notify-old-users"));
}