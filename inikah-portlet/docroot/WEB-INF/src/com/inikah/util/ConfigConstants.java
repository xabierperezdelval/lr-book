package com.inikah.util;

import com.liferay.portal.kernel.util.GetterUtil;

public interface ConfigConstants {
	String MAX_MIND_USER_ID = "max.mind.user.id";
	
	String MAX_MIND_LICENSE_KEY = "max.mind.license.key";
	
	String PAYPAL_ENVIRONMENT = "paypal.environment";
	String PAYPAL_ENVIRONMENT_LIVE = "live";
	String PAYPAL_ENVIRONMENT_SANDBOX = "sandbox";
	
	String PAYPAL_MERCHANT_USERNAME = "acct1.UserName";
	String PAYPAL_MERCHANT_PASSWORD = "acct1.Password";
	String PAYPAL_MERCHANT_SIGNATURE = "acct1.Signature";
	
	String PAYPAL_OAUTH_CLIENTID = "oauth.clientId";
	String PAYPAL_OAUTH_SECRET = "oauth.secret";	
	
	// clickatell (for SMS)
	String CLICKATELL_USERNAME = "clickatell.username";
	String CLICKATELL_PASSWORD = "clickatell.password";
	String CLICKATELL_API_ID = "clickatell.api.id";
	
	// openxchangerates.org
	boolean UPDATE_XCHAGE_RATES = GetterUtil.getBoolean("update.xchange.rates");
	
	// Cloudinary (for thumbnail conversion)
	String CLDY_CLOUD_NAME = "cloudinary.cloud.name";
	String CLDY_API_KEY = "cloudinary.api.key";
	String CLDY_API_SECRET = "cloudinary.api.secret";
}