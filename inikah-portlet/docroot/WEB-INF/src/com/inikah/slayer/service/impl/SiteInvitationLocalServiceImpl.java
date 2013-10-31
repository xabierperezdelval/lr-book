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

import java.util.Date;

import com.inikah.invite.InvitationConstants;
import com.inikah.slayer.model.SiteInvitation;
import com.inikah.slayer.service.SiteInvitationLocalServiceUtil;
import com.inikah.slayer.service.base.SiteInvitationLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

/**
 * The implementation of the site invitation local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.inikah.slayer.service.SiteInvitationLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.inikah.slayer.service.base.SiteInvitationLocalServiceBaseImpl
 * @see com.inikah.slayer.service.SiteInvitationLocalServiceUtil
 */
public class SiteInvitationLocalServiceImpl
	extends SiteInvitationLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.inikah.slayer.service.SiteInvitationLocalServiceUtil} to access the site invitation local service.
	 */
	
	public String getInviteeEmail(long invitationId) {
		String inviteeEmail = StringPool.BLANK;
		try {
			SiteInvitation siteInvitation = fetchSiteInvitation(invitationId);
			
			if (Validator.isNotNull(siteInvitation)) {
				inviteeEmail = siteInvitation.getInviteeEmail();
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return inviteeEmail;
	}
	
	public void updateInviation(long invitationId, String emailAddress) {
		SiteInvitation siteInvitation = null;
		try {
			siteInvitation = SiteInvitationLocalServiceUtil.fetchSiteInvitation(invitationId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(siteInvitation)) return;
		
		if (!emailAddress.equalsIgnoreCase(siteInvitation.getInviteeEmail())) {
			siteInvitation.setAlternateEmail(emailAddress);
		}
		
		siteInvitation.setStatus(InvitationConstants.STATUS_ACCEPTED);
		siteInvitation.setModifiedDate(new Date());
		
		try {
			updateSiteInvitation(siteInvitation);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
}