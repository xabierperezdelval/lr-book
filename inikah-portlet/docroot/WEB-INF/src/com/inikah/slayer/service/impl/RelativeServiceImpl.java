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

import com.inikah.slayer.model.Relative;
import com.inikah.slayer.service.base.RelativeServiceBaseImpl;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;

/**
 * The implementation of the relative remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.inikah.slayer.service.RelativeService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.inikah.slayer.service.base.RelativeServiceBaseImpl
 * @see com.inikah.slayer.service.RelativeServiceUtil
 */
public class RelativeServiceImpl extends RelativeServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.inikah.slayer.service.RelativeServiceUtil} to access the relative remote service.
	 */
	
	public Relative updateRelative(long userId, long relativeId,
			long profileId, String name, boolean unMarried, boolean passedAway,
			String phone, String emailAddress, String profession,
			long residingIn, boolean owner, int relationship, boolean younger,
			int age) {
		
		return relativeLocalService.updateRelative(userId, relativeId, profileId, name,
				unMarried, passedAway, phone, emailAddress, profession,
				residingIn, owner, relationship, younger, age);
	}
	
	public int isRelativeAdded(long profileId, int relationship) {
		
		int flag = 0;
		
		try {
			List<Relative> relatives = relativePersistence.findByRelationship(profileId, relationship);
			if (Validator.isNotNull(relatives) && !relatives.isEmpty()) {
				flag = relatives.get(0).getRelationship();
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public List<Relative> getRelatives(long profileId) {
		
		List<Relative> relatives = null;
		
		try {
			relatives = relativePersistence.findByProfileId(profileId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return relatives;
	}
}