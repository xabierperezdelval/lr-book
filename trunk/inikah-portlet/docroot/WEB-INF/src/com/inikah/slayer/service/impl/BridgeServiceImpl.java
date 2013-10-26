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

import java.util.List;

import com.inikah.slayer.model.Profile;
import com.inikah.slayer.service.base.BridgeServiceBaseImpl;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Country;
import com.liferay.portal.model.ListType;

/**
 * The implementation of the bridge remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.slayer.service.BridgeService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.slayer.service.base.BridgeServiceBaseImpl
 * @see com.slayer.service.BridgeServiceUtil
 */
public class BridgeServiceImpl extends BridgeServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.slayer.service.BridgeServiceUtil} to access the bridge remote service.
	 */


	
	public Country getCountry(String isoCode) {
		
		Country country = null;
		try {
			country = countryPersistence.fetchByA2(isoCode);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	
		if (Validator.isNull(country)) return null;
		
		// check if country already 'enabled'
		if (!country.getActive()) return country;
		
		country.setActive(false);
		
		try {
			country = countryPersistence.update(country);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return country;
	}
	
	public List<Country> getCountries() {
		List<Country> countries = null;
		try {
			countries = countryPersistence.findByActive(false);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return countries;
	}
	
	public void createListItem(String type, String value) {
		
		boolean itemExists = false;
		
		String prefix = Profile.class.getName() + StringPool.PERIOD;
		
		String name = prefix + type + StringPool.PERIOD + value;
		
		try {
			List<ListType> items =  listTypePersistence.findByType(prefix + type);
			
			for (ListType item: items) {
				if (item.getName().equalsIgnoreCase(name)) {
					itemExists = true;
					break;
				}
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		// create item for the first time
		if (itemExists) return;
		
		long listTypeId = 0l;
		try {
			listTypeId = counterLocalService.increment("list-item-id");
		} catch (SystemException e) {
			e.printStackTrace();
		}
		ListType listType = listTypePersistence.create((int)listTypeId);
		
		listType.setName(name);
		listType.setType(prefix + type);
		
		try {
			listTypePersistence.update(listType);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
	
	public int getListTypeId(String listName, String suffix) {
		int listTypeId = 0;
		try {
			List<ListType> listTypes = listTypePersistence.findByType(Profile.class.getName() + StringPool.PERIOD + listName);
			for (ListType listType: listTypes) {
				if (listType.getName().endsWith(suffix)) {
					listTypeId = listType.getListTypeId();
					break;
				}
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return listTypeId;
	}
}