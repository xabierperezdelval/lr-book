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

import com.inikah.slayer.model.Relative;
import com.inikah.slayer.service.base.RelativeLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Country;
import com.liferay.portal.service.CountryServiceUtil;

/**
 * The implementation of the relative local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.inikah.slayer.service.RelativeLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.inikah.slayer.service.base.RelativeLocalServiceBaseImpl
 * @see com.inikah.slayer.service.RelativeLocalServiceUtil
 */
public class RelativeLocalServiceImpl extends RelativeLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.inikah.slayer.service.RelativeLocalServiceUtil} to access the relative local service.
	 */
	
	public Relative addRelative(long userId, long profileId, String name, boolean unMarried,
			boolean passedAway, String phone, String emailAddress,
			String profession, long residingIn, boolean owner, 
			int relationship, boolean younger, int age) {
			
		long relativeId = 0l;
		try {
			relativeId = counterLocalService.increment(Relative.class.getName());
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		Relative relative = createRelative(relativeId);
		
		relative.setName(name);
		relative.setResidingIn(residingIn);
		relative.setRelationship(relationship);
		relative.setProfession(profession);
		relative.setOwner(owner);
		relative.setPassedAway(passedAway);
		relative.setProfileId(profileId);
		relative.setUnMarried(unMarried);
		relative.setEmailAddress(emailAddress);
		relative.setAge(age);
		relative.setYounger(younger);
		
		relative.setCreateDate(new java.util.Date());
		
		try {
			relative = addRelative(relative);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		String className = Relative.class.getName();
		
		bridgeLocalService.addPhone(userId, className, relativeId, phone, getIdd(residingIn), true);
				
		return relative;
	}
	
	public Relative updateRelative(long relativeId, String name, boolean unMarried,
			boolean passedAway, String phone, String emailAddress,
			String profession, long residingIn, 
			boolean owner, int relationship, boolean younger, int age) {
		
		Relative relative = null;
		try {
			relative = fetchRelative(relativeId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		relative.setName(name);
		relative.setResidingIn(residingIn);
		relative.setRelationship(relationship);
		relative.setProfession(profession);
		relative.setOwner(owner);
		relative.setPassedAway(passedAway);
		relative.setUnMarried(unMarried);
		relative.setAge(age);
		relative.setYounger(younger);
		
		relative.setModifiedDate(new java.util.Date());
		
		try {
			relative = updateRelative(relative);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		long classPK = relative.getRelativeId();
		String className = Relative.class.getName();
		

		
		bridgeLocalService.updatePhone(className, classPK, phone, getIdd(residingIn), true);
		
		return relative;
	}	
	
	private String getIdd(long countryId) {
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