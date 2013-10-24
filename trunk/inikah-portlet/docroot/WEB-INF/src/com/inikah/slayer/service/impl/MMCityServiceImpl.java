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

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Country;
import com.liferay.portal.service.CountryServiceUtil;
import com.inikah.slayer.model.MMCity;
import com.inikah.slayer.model.MMRegion;
import com.inikah.slayer.service.base.MMCityServiceBaseImpl;

/**
 * The implementation of the m m city remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.slayer.service.MMCityService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.slayer.service.base.MMCityServiceBaseImpl
 * @see com.slayer.service.MMCityServiceUtil
 */
public class MMCityServiceImpl extends MMCityServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.slayer.service.MMCityServiceUtil} to access the m m city remote service.
	 */
	
	public MMCity getCity(long regionId, String name) {
		
		MMCity mmCity = null;
		
		try {
			mmCity = mmCityPersistence.fetchByRegionId_Name(regionId, name);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNotNull(mmCity)) return mmCity;
		
		long cityId = 0;
		try {
			cityId = counterLocalService.increment();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		mmCity = mmCityPersistence.create(cityId);
		
		mmCity.setName(name);
		mmCity.setRegionId(regionId);
		
		try {
			mmCity = mmCityPersistence.update(mmCity);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return mmCity;
	}
	
	public List<MMCity> getCities(long regionId) {
		List<MMCity> cities = null;
		try {
			cities = mmCityPersistence.findByRegionId(regionId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return cities;
	}
	
	public String getDisplayInfo(long cityId) {
		StringBuilder sb = new StringBuilder();

		try {
			MMCity mmCity = mmCityPersistence.fetchByPrimaryKey(cityId);
			sb.append(mmCity.getName()).append(StringPool.COMMA).append(StringPool.SPACE);
			
			MMRegion mmRegion = mmRegionPersistence.fetchByPrimaryKey(mmCity.getRegionId());
			sb.append(mmRegion.getName()).append(StringPool.COMMA).append(StringPool.SPACE);
			
			Country country = CountryServiceUtil.fetchCountry(mmRegion.getCountryId());
			sb.append(country.getName());
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
}