/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.inikah.slayer.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.inikah.slayer.model.MMCity;
import com.inikah.slayer.model.MMRegion;
import com.inikah.slayer.model.MyKeyValue;
import com.inikah.slayer.service.MMCityLocalServiceUtil;
import com.inikah.slayer.service.MMRegionLocalServiceUtil;
import com.inikah.slayer.service.base.MyKeyValueLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.KeyValuePairComparator;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Country;
import com.liferay.portal.service.CountryServiceUtil;

/**
 * The implementation of the my key value local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.inikah.slayer.service.MyKeyValueLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.inikah.slayer.service.base.MyKeyValueLocalServiceBaseImpl
 * @see com.inikah.slayer.service.MyKeyValueLocalServiceUtil
 */
public class MyKeyValueLocalServiceImpl extends MyKeyValueLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.inikah.slayer.service.MyKeyValueLocalServiceUtil} to access the my key value local service.
	 */
	
	public List<KeyValuePair> getLanguagesSpokenForFilter(boolean bride) {
		List<MyKeyValue> items = myKeyValueFinder.findMotherTongue(bride);
		
		List<KeyValuePair> kvPairs = new ArrayList<KeyValuePair>();
		for (MyKeyValue myKeyValue: items) {
			kvPairs.add(new KeyValuePair(String.valueOf(myKeyValue.getMyKey()), myKeyValue.getMyValue()));
		}
		
		return kvPairs;
	}
	
	public List<KeyValuePair> getResidingCountriesForFilter(boolean bride, Locale locale) {
		List<MyKeyValue> items = myKeyValueFinder.findResidingCountries(bride);
		
		List<KeyValuePair> kvPairs = new ArrayList<KeyValuePair>();
		for (MyKeyValue myKeyValue: items) {
			
			long countryId = myKeyValue.getMyKey();
			StringBuilder sb = new StringBuilder();
			
			Country country = null;
			try {
				country = CountryServiceUtil.fetchCountry(countryId);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			
			if (Validator.isNull(country)) continue;
			
			sb.append(LanguageUtil.get(locale, country.getName()));
			sb.append(StringPool.SPACE);
			sb.append(StringPool.OPEN_PARENTHESIS);
			sb.append(myKeyValue.getMyValue());
			sb.append(StringPool.CLOSE_PARENTHESIS);
			kvPairs.add(new KeyValuePair(String.valueOf(countryId), sb.toString()));
		}
		
		return ListUtil.sort(kvPairs, new KeyValuePairComparator(false, true));
	}
	
	public List<KeyValuePair> getResidingRegionsForFilter(boolean bride, long countryId) {
		List<MyKeyValue> items = myKeyValueFinder.findResidingRegions(bride, countryId);
		
		List<KeyValuePair> kvPairs = new ArrayList<KeyValuePair>();
		for (MyKeyValue myKeyValue: items) {
			
			long regionId = myKeyValue.getMyKey();
			StringBuilder sb = new StringBuilder();
			
			MMRegion mmRegion = null;
			try {
				mmRegion = MMRegionLocalServiceUtil.fetchMMRegion(regionId);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			
			if (Validator.isNull(mmRegion)) continue;
			
			sb.append(mmRegion.getName());
			sb.append(StringPool.SPACE);
			sb.append(StringPool.OPEN_PARENTHESIS);
			sb.append(myKeyValue.getMyValue());
			sb.append(StringPool.CLOSE_PARENTHESIS);
			kvPairs.add(new KeyValuePair(String.valueOf(regionId), sb.toString()));
		}
		
		return ListUtil.sort(kvPairs, new KeyValuePairComparator(false, true));		
	}
	
	public List<KeyValuePair> getResidingCitiesForFilter(boolean bride, long regionId) {
		List<MyKeyValue> items = myKeyValueFinder.findResidingCities(bride, regionId);
		
		List<KeyValuePair> kvPairs = new ArrayList<KeyValuePair>();
		for (MyKeyValue myKeyValue: items) {
			
			long cityId = myKeyValue.getMyKey();
			StringBuilder sb = new StringBuilder();
			
			MMCity mmCity = null;
			try {
				mmCity = MMCityLocalServiceUtil.fetchMMCity(cityId);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			
			if (Validator.isNull(mmCity)) continue;
			
			sb.append(mmCity.getName());
			sb.append(StringPool.SPACE);
			sb.append(StringPool.OPEN_PARENTHESIS);
			sb.append(myKeyValue.getMyValue());
			sb.append(StringPool.CLOSE_PARENTHESIS);
			kvPairs.add(new KeyValuePair(String.valueOf(cityId), sb.toString()));
		}
		
		return ListUtil.sort(kvPairs, new KeyValuePairComparator(false, true));		
	}
}