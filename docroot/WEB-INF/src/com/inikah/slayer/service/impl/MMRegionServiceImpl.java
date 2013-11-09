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

import com.inikah.slayer.model.MMRegion;
import com.inikah.slayer.service.base.MMRegionServiceBaseImpl;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

/**
 * The implementation of the m m region remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.slayer.service.MMRegionService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.slayer.service.base.MMRegionServiceBaseImpl
 * @see com.slayer.service.MMRegionServiceUtil
 */
public class MMRegionServiceImpl extends MMRegionServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.slayer.service.MMRegionServiceUtil} to access the m m region remote service.
	 */
	
	public MMRegion getRegion(long countryId, String isoCode, String name) {
		
		MMRegion mmRegion = null;
		try {
			mmRegion = mmRegionPersistence.fetchByCountryId_isoCode(countryId, isoCode);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNotNull(mmRegion)) return mmRegion;
		
		long regionId = 0l;
		try {
			regionId = counterLocalService.increment();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		mmRegion = mmRegionPersistence.create(regionId);
		
		mmRegion.setCountryId(countryId);
		mmRegion.setIsoCode(isoCode);
		
		if (name.startsWith("State Of")) {
			name = StringUtil.replace(name, "State Of ", StringPool.BLANK);
		}
		
		mmRegion.setName(name);
		
		try {
			mmRegion = mmRegionPersistence.update(mmRegion);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return mmRegion;
	}
	
	public List<MMRegion> getRegions(long countryId) {
		List<MMRegion> regions = null;
		try {
			regions = mmRegionPersistence.findByCountryId(countryId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return regions;
	}
}