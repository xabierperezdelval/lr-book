package com.inikah.util;

import java.util.List;
import java.util.Locale;

import com.inikah.slayer.service.MyKeyValueLocalServiceUtil;
import com.liferay.portal.kernel.util.KeyValuePair;

public class FilterUtil {

	public static List<KeyValuePair> getResidingCountries(boolean bride, Locale locale) {
		return MyKeyValueLocalServiceUtil.getItemsForFilter(bride, "residingCountry", 0l, null);
	}
	
	public static List<KeyValuePair> getResidingRegions(boolean bride, String countryId) {
		return MyKeyValueLocalServiceUtil.getItemsForFilter(!bride, "residingState", Long.valueOf(countryId), "residingCountry");
	}
	
	public static List<KeyValuePair> getResidingCities(boolean bride, String regionId) {
		return MyKeyValueLocalServiceUtil.getItemsForFilter(!bride, "residingCity", Long.valueOf(regionId), "residingState");
	}
	
	public static List<KeyValuePair> getCountriesOfBirth(boolean bride, Locale locale) {
		return MyKeyValueLocalServiceUtil.getItemsForFilter(bride, "countryOfBirth", 0l, null);
	}
	
	public static List<KeyValuePair> getRegionsOfBirth(boolean bride, String countryId) {
		return MyKeyValueLocalServiceUtil.getItemsForFilter(!bride, "stateOfBirth", Long.valueOf(countryId), "residingCountry");
	}
	
	public static List<KeyValuePair> getCitiesOfBirth(boolean bride, String regionId) {
		return MyKeyValueLocalServiceUtil.getItemsForFilter(!bride, "cityOfBirth", Long.valueOf(regionId), "residingState");
	}
	
	public static List<KeyValuePair> getLanguagesSpoken(boolean bride) {
		return MyKeyValueLocalServiceUtil.getItemsForFilter(!bride, "motherTongue", 0l, null);
	}
}