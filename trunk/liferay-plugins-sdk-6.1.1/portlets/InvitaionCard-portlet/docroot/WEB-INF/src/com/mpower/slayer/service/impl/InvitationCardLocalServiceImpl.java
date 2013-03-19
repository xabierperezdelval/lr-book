/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.mpower.slayer.service.impl;

import java.util.List;
import javax.portlet.ActionRequest;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.mpower.slayer.NoSuchInvitationCardException;
import com.mpower.slayer.model.InvitationCard;
import com.mpower.slayer.model.UserRank;
import com.mpower.slayer.service.InvitationCardLocalServiceUtil;
import com.mpower.slayer.service.base.InvitationCardLocalServiceBaseImpl;
import com.mpower.slayer.service.persistence.InvitationCardFinderUtil;


/**
 * The implementation of the invitation card local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.mpower.slayer.service.InvitationCardLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.mpower.slayer.service.base.InvitationCardLocalServiceBaseImpl
 * @see com.mpower.slayer.service.InvitationCardLocalServiceUtil
 */
public class InvitationCardLocalServiceImpl extends InvitationCardLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.mpower.slayer.service.InvitationCardLocalServiceUtil} to access the invitation card local service.
	 */
	 public boolean notAnExistingUser(String emailAdress ,ActionRequest actionrequest) {
		 
		 ThemeDisplay themeDisplay = (ThemeDisplay) actionrequest.getAttribute(WebKeys.THEME_DISPLAY);
		 boolean check;
		 User  user=null;
		 try {
			 user= UserLocalServiceUtil.getUserByEmailAddress(themeDisplay.getCompanyId(), emailAdress);
			 check = false;
		}  catch (Exception e) {
			check=true;
		}
		 return check;
	 }
	 
	 public boolean notAlreadyInvited(String emailAdress ,ActionRequest actionrequest ){
		 
		 boolean hasAlreadyInvited=true;
		 InvitationCard invitationCard=null;
		 
		 ThemeDisplay themeDisplay = (ThemeDisplay) actionrequest.getAttribute(WebKeys.THEME_DISPLAY); 
		 
		 try {
			 invitationCard=InvitationCardLocalServiceUtil.findByUserIdAndInviteeEmail(themeDisplay.getUserId(), emailAdress);
			 hasAlreadyInvited=false;
		} catch (Exception e) {
			 hasAlreadyInvited=true;
		}
		 return hasAlreadyInvited;
		 
	 }
	 
	public InvitationCard  findByUserIdAndInviteeEmail(long userId,String inviteeEmail) throws SystemException, NoSuchInvitationCardException{
				InvitationCard invitationCard = invitationCardPersistence.findByuserId_InviteeEmail(userId, inviteeEmail);
			return invitationCard;
			
		}
		
	
	public List<InvitationCard> findbystatus(int status) throws SystemException{
			
			List<InvitationCard> invitationCard=invitationCardPersistence.findByfindByStatus(status);
			return invitationCard;
			
		}
	
	public List< InvitationCard> findByUserIdSatus(long userId,int status) throws  SystemException{
		
		List<InvitationCard> Invitationdetails=invitationCardPersistence.findByuserId_Status(userId, status);
		return Invitationdetails;
	}
	
	public List<UserRank> findByCount() throws SystemException{
		return InvitationCardFinderUtil.findCountBy();
	}
	
	public InvitationCard  userId(long userId,String inviteeEmail) throws SystemException, NoSuchInvitationCardException{
		
		InvitationCard invitationcard=invitationCardPersistence.findByuserId_InviteeEmail(userId, inviteeEmail);
		return invitationcard;
		
	}
	
	public InvitationCard invitationAccepted(long inviteeId,String emailAddress) throws SystemException{
		InvitationCard invitationCard=null;
		try {
			invitationCard =InvitationCardLocalServiceUtil.userId(inviteeId, emailAddress);
		} catch (NoSuchInvitationCardException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNotNull(invitationCard)) {
			invitationCard.setStatus(2);
			invitationCard.setInviteeNewUserId(inviteeId);
			InvitationCardLocalServiceUtil.updateInvitationCard(invitationCard);
		}
		return invitationCard;
	}
}