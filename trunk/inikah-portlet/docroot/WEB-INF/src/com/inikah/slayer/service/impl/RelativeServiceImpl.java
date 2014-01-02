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
import com.inikah.slayer.service.base.RelativeServiceBaseImpl;

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
	
	public Relative addRelative(long userId, long profileId, String name, boolean married,
			boolean passedAway, String phone, String emailAddress,
			int category, int occupation, String occupationOther,
			String comments, boolean owner, int relationship, int age) {
		
		return relativeLocalService.addRelative(userId, profileId, name,
				married, passedAway, phone, emailAddress, category, occupation,
				occupationOther, comments, owner, relationship, age);
	}
	
	public Relative updateRelative(long relativeId, String name, boolean married,
			boolean passedAway, String phone, String emailAddress,
			int category, int occupation, String occupationOther,
			String comments, boolean owner, int relationship, int age) {
		
		return relativeLocalService.updateRelative(relativeId, name,
				married, passedAway, phone, emailAddress, category, occupation,
				occupationOther, comments, owner, relationship, age);
	}
}