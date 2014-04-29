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
import com.liferay.portal.service.CountryServiceUtil;

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
		if (country.getActive()) return country;
		
		country.setActive(true);
		
		try {
			country = countryPersistence.updateImpl(country);
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
	
	public List<ListType> getList(String listName) {
		
		
		List<ListType> listTypes = null;
		String _listName = Profile.class.getName() + StringPool.PERIOD + listName;
		
		try {
			listTypes = listTypeService.getListTypes(_listName);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return listTypes;
	}
	
	public void sendVerificationCode(long phoneId) {
		bridgeLocalService.sendVerificationCode(phoneId);
	}
	
	public boolean verifyPhone(long phoneId, String verificationCode) {
		return bridgeLocalService.verifyPhone(phoneId, verificationCode);
	}
	
	public void addPhone(long userId, String className, long classPK,
			String number, String extension, boolean primary) {
		bridgeLocalService.addPhone(userId, className, classPK, number,
				extension, primary);
	}
	
	public String getIdd(long countryId) {
		String idd = StringPool.BLANK;
		if (countryId > 0l) {
			try {
				Country country = CountryServiceUtil.fetchCountry(countryId);
				
				if (Validator.isNotNull(country)) {
					idd = country.getIdd();
				}
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		return idd;
	}
}