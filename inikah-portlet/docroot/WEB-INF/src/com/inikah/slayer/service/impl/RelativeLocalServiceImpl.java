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
import com.liferay.portal.service.ServiceContext;

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
	
	public Relative addRelative(long userId, long profileId, String name, boolean married,
			boolean passedAway, String phone, String emailAddress,
			int category, int occupation, String occupationOther,
			String comments, boolean owner, int relationship, ServiceContext serviceContext) {
	
		long relativeId = 0l;
		try {
			relativeId = counterLocalService.increment(Relative.class.getName());
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		Relative relative = createRelative(relativeId);
		
		relative.setCategory(category);
		relative.setName(name);
		relative.setComments(comments);
		relative.setRelationship(relationship);
		relative.setOccupation(occupation);
		relative.setOccupationOther(occupationOther);
		relative.setOwner(owner);
		relative.setPassedAway(passedAway);
		relative.setProfileId(profileId);
		relative.setMarried(married);
		
		relative.setCreateDate(new java.util.Date());
		
		try {
			relative = addRelative(relative);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		long classPK = relative.getRelativeId();
		String className = Relative.class.getName();
		
		bridgeLocalService.addPhone(userId, className, classPK, phone, "91", true);
		bridgeLocalService.addEmail(userId, className, classPK, emailAddress, true);
		
		return relative;
	}
}