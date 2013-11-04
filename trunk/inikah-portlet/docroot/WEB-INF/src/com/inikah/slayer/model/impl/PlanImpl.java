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

package com.inikah.slayer.model.impl;

import com.inikah.slayer.model.Profile;
import com.inikah.slayer.service.ProfileLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Address;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;

/**
 * The extended model implementation for the Plan service. Represents a row in the &quot;inikah_Plan&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * Helper methods and all application logic should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.inikah.slayer.model.Plan} interface.
 * </p>
 *
 * @author Ahmed Hasan
 */
public class PlanImpl extends PlanBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. All methods that expect a plan model instance should use the {@link com.inikah.slayer.model.Plan} interface instead.
	 */
	public PlanImpl() {
	}
	
	//private double price;
	
	public String getPriceText(long profileId) {
		
		return String.valueOf(getPrice(profileId));
		
	}
	
	public double getPrice(long profileId) {
		
		long countryId = 0l;
		User user = null;
		Profile profile = null;
		try {
			profile = ProfileLocalServiceUtil.fetchProfile(profileId);
			try {
				user = UserLocalServiceUtil.getUser(profile.getUserId());
				
				Address address = ProfileLocalServiceUtil.getMaxMindAddress(user);
				countryId = address.getCountryId();
			} catch (PortalException e) {
				e.printStackTrace();
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (countryId > 0l) {
			long countryOfProfile = profile.getResidingCountry();
			
			
		}
		
		
		
		return 0.0d;
	}
}