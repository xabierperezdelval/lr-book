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

import com.inikah.slayer.service.base.ProfileServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Contact;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ContactLocalServiceUtil;

/**
 * The implementation of the profile remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.inikah.slayer.service.ProfileService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.inikah.slayer.service.base.ProfileServiceBaseImpl
 * @see com.inikah.slayer.service.ProfileServiceUtil
 */
public class ProfileServiceImpl extends ProfileServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.inikah.slayer.service.ProfileServiceUtil} to access the profile remote service.
	 */
	
	public void updateUserInfo(long userId, String userName, boolean female, String occupation, String additionalInfo) {
		// update user info
				
		User user = null;
		try {
			user = userLocalService.fetchUser(userId);
		} catch (SystemException e) {
			e.printStackTrace();
		}

		if (Validator.isNull(user)) return;
		
		user.setFirstName(userName);
		user.setJobTitle(occupation);
		user.setComments(additionalInfo);
		user.setLdapServerId(-1);
		
		try {
			userLocalService.updateUser(user);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (female) {
			try {
				Contact contact = user.getContact();
				contact.setMale(false);
				ContactLocalServiceUtil.updateContact(contact);
			} catch (PortalException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}						
		}
	}
}