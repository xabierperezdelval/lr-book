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

import com.inikah.slayer.model.Location;
import com.inikah.slayer.service.base.LocationServiceBaseImpl;
import com.inikah.util.IConstants;

/**
 * The implementation of the location remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.inikah.slayer.service.LocationService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.inikah.slayer.service.base.LocationServiceBaseImpl
 * @see com.inikah.slayer.service.LocationServiceUtil
 */
public class LocationServiceImpl extends LocationServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.inikah.slayer.service.LocationServiceUtil} to access the location remote service.
	 */
	
	public List<Location> getRegions(long countryId) {
		return locationLocalService.getLocations(countryId, IConstants.LOC_TYPE_REGION);
	}
	
	public List<Location> getCities(long regionId) {
		return locationLocalService.getLocations(regionId, IConstants.LOC_TYPE_CITY);
	}
	
	public List<Location> getAreas(long cityId) {
		return locationLocalService.getLocations(cityId, IConstants.LOC_TYPE_AREA);
	}
	
	public List<Location> getMasaajid(long cityId) {
		return locationLocalService.getLocations(cityId, IConstants.LOC_TYPE_MASJID);
	}
	
	public List<Location> getLocations(long parentId, int locType) {
		return locationLocalService.getLocations(parentId, locType);
	}
}