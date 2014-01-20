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
	
	public Relative updateRelative(long userId, long relativeId,
			long profileId, String name, boolean unMarried, boolean passedAway,
			String phone, String emailAddress, String profession,
			long residingIn, boolean owner, int relationship, boolean younger,
			int age) {
		
		String className = Relative.class.getName();
		
		boolean update = (relativeId > 0l);
		
		System.out.println("this is update ==> " + update);
		
		Relative relative = null;
		
		if (update) {
			try {
				relative = fetchRelative(relativeId);
			} catch (SystemException e) {
				e.printStackTrace();
			}
		} else {
			try {
				relativeId = counterLocalService.increment(className);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			relative = createRelative(relativeId);
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
		relative.setProfileId(profileId);
		
		if (update) {
			relative.setModifiedDate(new java.util.Date());
		} else {
			relative.setCreateDate(new java.util.Date());
		}
		
		try {
			relative = updateRelative(relative);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (update) {
			bridgeLocalService.updatePhone(className, relativeId, phone,
					bridgeService.getIdd(residingIn), true);			
		} else {
			bridgeLocalService.addPhone(userId, className, relativeId, phone,
					bridgeService.getIdd(residingIn), true);	
		}

		return relative;
	}
}