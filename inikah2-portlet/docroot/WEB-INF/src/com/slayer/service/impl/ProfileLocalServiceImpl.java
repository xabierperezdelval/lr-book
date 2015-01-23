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

import com.liferay.portal.kernel.exception.SystemException;
import com.slayer.model.Profile;
import com.slayer.service.base.ProfileLocalServiceBaseImpl;

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
		
		try {
			profile = addProfile(profile);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return profile;
	}
}