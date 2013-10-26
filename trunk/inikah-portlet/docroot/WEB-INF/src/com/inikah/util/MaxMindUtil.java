package com.inikah.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.inikah.slayer.model.MMCity;
import com.inikah.slayer.model.MMRegion;
import com.inikah.slayer.service.BridgeServiceUtil;
import com.inikah.slayer.service.MMCityServiceUtil;
import com.inikah.slayer.service.MMRegionServiceUtil;
import com.inikah.slayer.service.ProfileLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Country;
import com.liferay.portal.model.User;
import com.liferay.portal.service.AddressLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextThreadLocal;
import com.liferay.util.portlet.PortletProps;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.Omni;

public class MaxMindUtil {

	public static void setCoordinates(User user) {

		if (Validator.isNull(user)) return;
		
		long userId = user.getUserId();
		String className = MMRegion.class.getName();
		
		// check if the MaxMind coordinates are already set for this user
		if (ProfileLocalServiceUtil.maxMindCoordinatesSet(user)) return;
		
		int maxMindUserId = GetterUtil.getInteger(PortletProps.get("max.mind.user.id"));
		String maxMindLicenseKey = PortletProps.get("max.mind.license.key");
		
		WebServiceClient client = 
			new WebServiceClient.Builder(maxMindUserId, maxMindLicenseKey).build();
		
		String ipAddress = user.getLastLoginIP();
		if (Validator.isNull(ipAddress) || ipAddress.equals("127.0.0.1")) {
			return;
		}
		
		InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getByName(ipAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(inetAddress)) return;
		
		Omni omni = null;
		try {
			omni = client.omni(inetAddress);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (GeoIp2Exception e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(omni)) return;
		
		String isoCode = omni.getCountry().getIsoCode();

		Country country = BridgeServiceUtil.getCountry(isoCode);
		
		if (Validator.isNull(country)) return;
		
		isoCode = omni.getMostSpecificSubdivision().getIsoCode();
		String name = omni.getMostSpecificSubdivision().getName();
		
		MMRegion mmRegion = MMRegionServiceUtil.getRegion(country.getCountryId(), isoCode, name);
		
		String zip = omni.getPostal().getCode();
		
		if (Validator.isNull(zip)) {
			zip = "NO-ZIP";
		}
		
		long countryId = country.getCountryId();
		long regionId = mmRegion.getRegionId();
		
		MMCity mmCity = MMCityServiceUtil.getCity(regionId, omni.getCity().getName());
		
		// latitude, longitude and continent
		String street1 = String.valueOf(omni.getLocation().getLatitude());
		String street2 = String.valueOf(omni.getLocation().getLongitude());
		String street3 = omni.getContinent().getName();
		
		String city = String.valueOf(mmCity.getCityId());
		
		int typeId = 100;
		boolean mailing = false;
		boolean primary = true;
		
		ServiceContext serviceContext = ServiceContextThreadLocal.getServiceContext();
		
		try {
			AddressLocalServiceUtil.addAddress(userId, className, userId, 
					street1, street2, street3, city, zip, regionId, countryId, 
					typeId, mailing, primary, serviceContext);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		// check queries remaining...
		int queriesRemaining = omni.getMaxMind().getQueriesRemaining();
		if (queriesRemaining < 50) {
			// email Ahmed Hasan
		}
	}
}