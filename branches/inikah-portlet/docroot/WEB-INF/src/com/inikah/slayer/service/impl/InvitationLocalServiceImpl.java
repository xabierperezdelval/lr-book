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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.inikah.invite.InviteConstants;
import com.inikah.slayer.NoSuchInvitationException;
import com.inikah.slayer.model.Invitation;
import com.inikah.slayer.service.base.InvitationLocalServiceBaseImpl;
import com.inikah.slayer.service.persistence.InvitationFinderUtil;
import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;

/**
 * The implementation of the invitation local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.inikah.slayer.service.InvitationLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.inikah.slayer.service.base.InvitationLocalServiceBaseImpl
 * @see com.inikah.slayer.service.InvitationLocalServiceUtil
 */
public class InvitationLocalServiceImpl extends InvitationLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.inikah.slayer.service.InvitationLocalServiceUtil} to access the invitation local service.
	 */
	
	/**
	 * 
	 */
	public String getInviteeEmail(long invitationId) {
		String inviteeEmail = StringPool.BLANK;
		try {
			Invitation invitation = fetchInvitation(invitationId);
			
			if (Validator.isNotNull(invitation)) {
				inviteeEmail = invitation.getInviteeEmail();
				 // update status
				if (invitation.getStatus() == InviteConstants.STATUS_CREATED) {
					updateInvitation(invitation);
					invitation.setStatus(InviteConstants.STATUS_CLICKED);
					invitation.setModifiedDate(new Date());
					updateInvitation(invitation);
				}
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return inviteeEmail;
	}
	
	/**
	 * 
	 */
	public void updateInviation(long invitationId, String emailAddress) {
		
		Invitation invitation = null;
		try {
			invitation = fetchInvitation(invitationId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(invitation)) return;
		System.out.println(!emailAddress.equalsIgnoreCase(invitation.getInviteeEmail()));
		
		if (!emailAddress.equalsIgnoreCase(invitation.getInviteeEmail())) {
			invitation.setRegisteredEmail(emailAddress);
		}
		
		invitation.setStatus(InviteConstants.STATUS_JOINED);
		invitation.setModifiedDate(new Date());
		
		try {
			updateInvitation(invitation);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param inviterId
	 * @param emailAddress
	 */
	public Invitation initInvitation(long inviterId, String emailAddress, int status) {
		
		long invitationId = 0l ,companyId = 0l;
		try {
			invitationId = counterLocalService.increment(Invitation.class.getName());
			companyId = UserLocalServiceUtil.fetchUser(inviterId).getCompanyId();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		Invitation invitation = createInvitation(invitationId);
		
		invitation.setCompanyId(companyId);
		invitation.setUserId(inviterId);
		invitation.setCreateDate(new Date());
		invitation.setInviteeEmail(emailAddress);
		
		invitation.setStatus(status);
		
		try {
			addInvitation(invitation);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return invitation;
	}
	
	public void sendInvitation(long inviterId, String inviteeName, String emailAddress) {
		User inviter = null;
		try {
			inviter = userLocalService.fetchUser(inviterId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(inviter)) return;
		
		Invitation invitation = initInvitation(inviterId, emailAddress, 0);
		invitation.setUserName(inviteeName);
		try {
			updateInvitation(invitation);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	
		Message message = new Message();
		/*message.setPayload(invitation);*/
		message.put("inviterName", inviter.getFullName());
		message.put("invitationId", invitation.getInvitationId());
		message.put("inviteeName", invitation.getUserName());
		message.put("inviteeEmail",invitation.getInviteeEmail());
		MessageBusUtil.sendMessage("destinationBus", message);
		
	

		// email the actual invitation
		String inviterName = inviter.getFirstName();
		String inviterEmail = inviter.getEmailAddress();
		System.out.println("inviter name>>>>>>>>>>>>"+inviterName);
		System.out.println("inviter Email>>>>>>>>>>>>"+inviterEmail);
	}
	
	public void linkInvitation(User user) {
		
		String emailAddress = user.getEmailAddress();
		Invitation invitation = null;
		try {
			invitation = invitationPersistence.findByInviteeEmail(emailAddress);
			
		} catch (NoSuchInvitationException e) {
			try {
				invitation = invitationPersistence.findByRegisteredEmail(emailAddress);
			} catch (NoSuchInvitationException e1) {
				// ignore
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(invitation)) return;
		
		invitation.setInviteeNewUserId(user.getUserId());
		invitation.setStatus(InviteConstants.STATUS_LINKED);
		invitation.setModifiedDate(new Date());
		
		// email the actual inviter. 
		
		// set invitation chain.
		invitation.setInvitationChain(getChain(invitation.getUserId()));
		
		try {
			updateInvitation(invitation);
		} catch (SystemException e) {
			e.printStackTrace();
		}		
	}

	private String getChain(long inviterId) {

		StringBuilder sb = new StringBuilder(String.valueOf(inviterId));
		try {
			Invitation invitation = invitationPersistence.fetchByInviteeNewUserId(inviterId);
			
			if (Validator.isNotNull(invitation)) {
				sb.append(StringPool.COMMA);
				sb.append(getChain(invitation.getUserId()));
			} 
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	

	public boolean isNewEmail(long companyId, String emailAddress) {
		
		boolean flag = true;
		
		try {
			User user = userLocalService.getUserByEmailAddress(companyId, emailAddress);
			flag = !Validator.isNotNull(user);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public boolean alreadyInvited(long inviterId, String inviteeEmail) {

		boolean alreadyInvited = false;
		try {
			Invitation invitation = invitationPersistence.fetchByUserId_InviteeEmail(inviterId, inviteeEmail);
			alreadyInvited = Validator.isNotNull(invitation);
		} catch (SystemException e) {
			e.printStackTrace();
		}

		return alreadyInvited;
	}
	
	public void setInvitationAccepted(long inviterId, String inviteeEmail,
			long inviteeNewUserId) {

		Invitation invitation = null;
		try {
			invitation = invitationPersistence
					.fetchByUserId_InviteeEmail(inviterId, inviteeEmail);
		} catch (SystemException e) {
			e.printStackTrace();
		}

		if (Validator.isNull(invitation))
			return;

		invitation.setStatus(InviteConstants.STATUS_JOINED);
		invitation.setInviteeNewUserId(inviteeNewUserId);
		invitation.setModifiedDate(new Date());

		try {
			invitationPersistence.update(invitation);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
	}
	
	public List<Invitation> getUserInvitations(long inviterId, int status) 
			throws SystemException {
		
		List<Invitation> invitations = null;
		
		invitations = (status == -1)? 
			invitationPersistence.findByUserId(inviterId) : 
				invitationPersistence.findByUserId_Status(inviterId, status);
		
		return invitations;
	}
	public int getTodaysInvitationCount(long userId) throws SystemException
	{
		int count=0;
		List<Invitation> invitations = null;
		Date date = new Date();
		invitations = searchByTodaysDate(userId,date);
		count = invitations.size();
		return count;
		
	}
	public List<Invitation>searchByTodaysDate(long userId,Date date)
	   throws SystemException {
		
		SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd");
        String dt = (String)newDate.format(date);
		return InvitationFinderUtil.findTodaysInvitation(userId,dt+"%");
	}
	
	public int getUserInvitationsCount(long userId,int status) throws SystemException
	{
		
		int count=0;
		List<Invitation> invitations = null;
		invitations = getUserInvitations(userId, status);
		count = invitations.size();
		return count;
	}
	
	public boolean userHasInvitations(long userId)
	{
		int count=0;
		try {
			count = getUserInvitationsCount(userId,-1);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		if(count==0)
		{
		return false;
		}
		else
		return true;
	}
}