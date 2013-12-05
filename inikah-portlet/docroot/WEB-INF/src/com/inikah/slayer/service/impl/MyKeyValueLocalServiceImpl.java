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

import com.inikah.slayer.model.Location;
import com.inikah.slayer.model.MyKeyValue;
import com.inikah.slayer.service.LocationLocalServiceUtil;
import com.inikah.slayer.service.base.MyKeyValueLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.KeyValuePairComparator;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Country;
import com.liferay.portal.model.ListType;
import com.liferay.portal.service.CountryServiceUtil;
import com.liferay.portal.service.ListTypeServiceUtil;

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
	
	public List<KeyValuePair> getItemsForFilter(boolean bride, String column, long parentId, String parentColumn) {
		List<MyKeyValue> items = myKeyValueFinder.findResults(bride, column, parentId, parentColumn);
		
		List<KeyValuePair> kvPairs = new ArrayList<KeyValuePair>();
		for (MyKeyValue myKeyValue: items) {
			
			long key = myKeyValue.getMyKey();
			String name = getName(column, key);
						
			StringBuilder sb = new StringBuilder(name);
			sb.append(StringPool.SPACE);
			sb.append(StringPool.OPEN_PARENTHESIS);
			sb.append(myKeyValue.getMyValue());
			sb.append(StringPool.CLOSE_PARENTHESIS);
			kvPairs.add(new KeyValuePair(String.valueOf(key), sb.toString()));
		}
		
		return ListUtil.sort(kvPairs, new KeyValuePairComparator(false, true));		
	}

	private String getName(String column, long key) {
		
		String name = StringPool.BLANK;
			
		if (column.equalsIgnoreCase("residingCountry") || column.equalsIgnoreCase("countryOfBirth")) {
			Country country = null;
			try {
				country = CountryServiceUtil.fetchCountry(key);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			
			if (Validator.isNotNull(country)) {
				name = country.getName();
			}
		} else if (column.equalsIgnoreCase("residingState") 
				|| column.equalsIgnoreCase("stateOfBirth") 
				|| column.equalsIgnoreCase("residingCity") 
				|| column.equalsIgnoreCase("cityOfBirth")) {
			Location location = null;
			try {
				location = LocationLocalServiceUtil.fetchLocation(key);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			
			if (Validator.isNotNull(location)) {
				name = location.getName();
			}
		} else if (column.equalsIgnoreCase("motherTongue")) {
			ListType listType = null;
			try {
				listType = ListTypeServiceUtil.getListType((int)key);
			} catch (PortalException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}
			
			if (Validator.isNotNull(listType)) {
				name = LanguageUtil.get(Locale.ENGLISH, listType.getName());
			}
		}
			
		return name;
	}
}