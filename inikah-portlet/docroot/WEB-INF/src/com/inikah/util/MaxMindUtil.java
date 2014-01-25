package com.inikah.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.inikah.slayer.model.Location;
import com.inikah.slayer.service.BridgeServiceUtil;
import com.inikah.slayer.service.LocationLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Country;
import com.liferay.portal.model.User;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.OmniResponse;

public class MaxMindUtil {
	
	static WebServiceClient client;
	
	static {
		int maxMindUserId = GetterUtil.getInteger(AppConfig.get(IConstants.MAX_MIND_USER_ID));
		String maxMindLicenseKey = AppConfig.get(IConstants.MAX_MIND_LICENSE_KEY);
		client = new WebServiceClient.Builder(maxMindUserId, maxMindLicenseKey).build();
	}

	public static void setCoordinates(User user) {

		if (Validator.isNull(user)) return;
		
		String ipAddress = user.getLastLoginIP();
		if (Validator.isNull(ipAddress) || ipAddress.equals("127.0.0.1")) {
			return;
		}	
				
		// check if the MaxMind coordinates are already set for this user
		if (LocationLocalServiceUtil.isLocationSet(user)) return;

		long userId = user.getUserId();
		
		InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getByName(ipAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(inetAddress)) return;
		
		OmniResponse omniResponse = null;
		try {
			omniResponse = client.omni(inetAddress);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (GeoIp2Exception e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(omniResponse)) return;
		
		String isoCode = omniResponse.getCountry().getIsoCode();

		Country country = BridgeServiceUtil.getCountry(isoCode);
				
		if (Validator.isNull(country)) return;
		
		isoCode = omniResponse.getMostSpecificSubdivision().getIsoCode();
		String name = omniResponse.getMostSpecificSubdivision().getName();
		
		if (Validator.isNull(name)) return; 
		
		Location region = LocationLocalServiceUtil.getLocation(
				country.getCountryId(), isoCode, name, userId);
		
		long countryId = country.getCountryId();
		long regionId = region.getLocationId();
		
		String cityName = omniResponse.getCity().getName();
		
		if (Validator.isNull(cityName)) return;
		
		Location city = LocationLocalServiceUtil.getLocation(regionId,
				cityName, IConstants.LOC_TYPE_CITY,
				userId);
		
		// latitude, longitude and continent
		String street1 = String.valueOf(omniResponse.getLocation().getLatitude());
		String street2 = String.valueOf(omniResponse.getLocation().getLongitude());
		String street3 = omniResponse.getContinent().getName();
				
		LocationLocalServiceUtil.insertAddress(userId, street1, street2, street3,
				city.getLocationId(), regionId, countryId, ipAddress);
		
		// check queries remaining...
		int queriesRemaining = omniResponse.getMaxMind().getQueriesRemaining();
		if (queriesRemaining < 50) {
			// email Ahmed Hasan
		}
	}
}