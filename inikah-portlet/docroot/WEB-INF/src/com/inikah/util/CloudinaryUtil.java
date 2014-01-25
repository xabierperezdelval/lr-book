package com.inikah.util;

import java.util.HashMap;
import java.util.Map;

import com.cloudinary.Cloudinary;

public class CloudinaryUtil {

	static Cloudinary cloudinary;
	
	static {
		String cloudName = AppConfig.get(IConstants.CLDY_CLOUD_NAME);
		Map<String, String> config = new HashMap<String, String>();
		config.put("cloud_name", cloudName);
		config.put("api_key", AppConfig.get(IConstants.CLDY_API_KEY));
		config.put("api_secret", AppConfig.get(IConstants.CLDY_API_SECRET));
		cloudinary = new Cloudinary(config);		
	}
	
	public static Cloudinary getService() {
		return cloudinary;
	}
}