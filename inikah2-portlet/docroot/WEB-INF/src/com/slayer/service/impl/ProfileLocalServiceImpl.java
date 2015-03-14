/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.slayer.service.impl;

import java.util.Date;
import java.util.List;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.OrderFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Address;
import com.liferay.portal.model.User;
import com.liferay.portal.service.AddressLocalServiceUtil;
import com.liferay.portal.service.ClassNameLocalServiceUtil;
import com.slayer.model.Location;
import com.slayer.model.Profile;
import com.slayer.service.base.ProfileLocalServiceBaseImpl;
import com.util.IConstants;

/**
 * The implementation of the profile local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.slayer.service.ProfileLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.slayer.service.base.ProfileLocalServiceBaseImpl
 * @see com.slayer.service.ProfileLocalServiceUtil
 */
public class ProfileLocalServiceImpl extends ProfileLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.slayer.service.ProfileLocalServiceUtil} to access the profile local service.
	 */
	
	public Profile init(long userId, boolean bride) {
		
		long profileId = 0l;
		try {
			profileId = counterLocalService.increment(Profile.class.getName());
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		Profile profile = createProfile(profileId);
		profile.setCreateDate(new Date());
		profile.setUserId(userId);
		profile.setBride(bride);
		profile.setOwnerLastLogin(new Date());
		
		setDefaultLocation(profile);
		
		try {
			profile = addProfile(profile);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return profile;
	}
	
	private void setDefaultLocation(Profile profile) {
					
		User user = null;
		try {
			user = userLocalService.fetchUser(profile.getUserId());
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
				Address.class, PortalClassLoaderUtil.getClassLoader());
		dynamicQuery.add(RestrictionsFactoryUtil.eq("userId", profile.getUserId()));
		dynamicQuery.add(RestrictionsFactoryUtil.eq("zip", user.getLastLoginIP()));
		dynamicQuery.add(RestrictionsFactoryUtil.eq("classNameId",
				ClassNameLocalServiceUtil.getClassNameId(Location.class)));
		
		try {
			@SuppressWarnings("unchecked")
			List<Address> addresses = AddressLocalServiceUtil.dynamicQuery(dynamicQuery);
			if (Validator.isNotNull(addresses) && !addresses.isEmpty()) {
				
				Address address = addresses.get(0);
				
				profile.setResidingCountry(address.getCountryId());
				profile.setResidingState(address.getRegionId());
				profile.setResidingCity(Long.valueOf(address.getCity()));
				
				profile.setCountryOfBirth(address.getCountryId());
				profile.setRegionOfBirth(address.getRegionId());
				profile.setCityOfBirth(Long.valueOf(address.getCity()));
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}							
	}	

	public boolean showSelfOption(long userId) {
		
		boolean showSelfOption = true;
		
		try {
			Profile profile = profilePersistence.fetchByCreatedForSelf(userId, IConstants.CREATED_FOR_SELF);
			showSelfOption = Validator.isNull(profile);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return showSelfOption;
	}
	
	public List<Profile> getUserProfiles(long userId) {
		List<Profile> userProfiles = null;
		
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Profile.class);
		dynamicQuery.add(RestrictionsFactoryUtil.eq("userId", userId));
		dynamicQuery.add(RestrictionsFactoryUtil.ne("status", IConstants.STATUS_DELETED));
		dynamicQuery.addOrder(OrderFactoryUtil.desc("modifiedDate"));
		
		try {
			userProfiles = dynamicQuery(dynamicQuery);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return userProfiles;
	}
	
	public List<Profile> getUserProfiles(long userId, int status) {
		List<Profile> userProfiles = null;
		
		try {
			userProfiles = profilePersistence.findByUserId_Status(userId, status);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return userProfiles;
	}
	
	public boolean hasIncompleteProfiles(long userId) {
		
		boolean flag = false;
		
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Profile.class);
		dynamicQuery.add(RestrictionsFactoryUtil.eq("userId", userId));
		dynamicQuery.add(RestrictionsFactoryUtil.lt("status", IConstants.STATUS_SUBMITTED));
		
		try {
			List<Profile> userProfiles = dynamicQuery(dynamicQuery);
			flag = (Validator.isNotNull(userProfiles) && !userProfiles.isEmpty());
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public void setOwnerLastLogin(long userId) {
		
		try {
			List<Profile> profiles = profilePersistence.findByUserId(userId);
			for (Profile profile: profiles) {
				profile.setOwnerLastLogin(new Date());
				updateProfile(profile);
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
}