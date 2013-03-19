package com.mpower;

import java.util.Date;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletSession;
import javax.servlet.http.HttpServletRequest;
import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.mpower.slayer.model.InvitationCard;
import com.mpower.slayer.service.InvitationCardLocalServiceUtil;
import com.mpower.util.InvitationConstants;

public class InvitationPortlet extends MVCPortlet {
	
	public void processInvitations(ActionRequest actionRequest,ActionResponse actionResponse) throws Exception {
		
		List<String> validEmails=null;
		String userName=null;
		User user=null;
		User checkAlreadyExisted=null;
		InvitationCard alreadyBeenInvited=null;
		List<User> alreadyMemberList=null;
		HttpServletRequest httpRequest=null;
		String createAccountURL=null;
		
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		PortletPreferences config =actionRequest.getPreferences();
		String emailBody=config.getValue("newMessageBody", "");
		String email=ParamUtil.getString(actionRequest, "emails");
		validEmails=com.mpower.util.InvitationUtil.extractEmails(email);
		long groupId = themeDisplay.getLayout().getGroupId();
		user = UserLocalServiceUtil.getUser(themeDisplay.getUserId());
		userName = user.getFullName();
		for(String emails:validEmails){
			if(InvitationCardLocalServiceUtil.notAnExistingUser(emails, actionRequest)&&InvitationCardLocalServiceUtil.notAlreadyInvited(emails, actionRequest)){
				saveInvitationdetails(emails,themeDisplay.getUserId(), themeDisplay.getCompanyId(),groupId,userName);
			}
			else {
					try{
						checkAlreadyExisted=UserLocalServiceUtil.fetchUserByEmailAddress(themeDisplay.getCompanyId(), emails);
						alreadyMemberList.add(checkAlreadyExisted);
					}catch (Exception e) {
					}
					try {
						alreadyBeenInvited=InvitationCardLocalServiceUtil.findByUserIdAndInviteeEmail(themeDisplay.getCompanyId(), emails);
						alreadyMemberList.add(UserLocalServiceUtil.getUser( alreadyBeenInvited.getUserId()));
					} catch (Exception e) {
					}
					SessionErrors.add(actionRequest, "Alreadyused");
					PortletSession session = actionRequest.getPortletSession();
					session.setAttribute("alreadyMemberList", alreadyMemberList,PortletSession.APPLICATION_SCOPE);
			}
				
			}

		httpRequest = PortalUtil.getHttpServletRequest(actionRequest);
		createAccountURL = PortalUtil.getCreateAccountURL(httpRequest,themeDisplay);
		Message message = new Message();
		message.put("inviterId", Long.valueOf(themeDisplay.getUserId()));
		message.put("createAccountURL", createAccountURL.toString());
		message.put("emailBody", emailBody);
		message.put("from", userName);
		MessageBusUtil.sendMessage("liferay/invitation", message);
	}
		
	private static void saveInvitationdetails(String mailId,
			 long userId, long companyId, long groupId, String userName) throws SystemException {
			InvitationCard invitationCard=null;;
				invitationCard = InvitationCardLocalServiceUtil
						.createInvitationCard((CounterLocalServiceUtil.increment()));
				invitationCard.setInviteeEmail(mailId);
				invitationCard.setUserId(userId);
				invitationCard.setUserName(userName);
				invitationCard.setCompanyId(companyId);
				invitationCard.setGroupId(groupId);
				invitationCard.setCreateDate((new Date()));
				invitationCard.setStatus(InvitationConstants.STATUS_PENDING);
				InvitationCardLocalServiceUtil.addInvitationCard(invitationCard);

			}
}

					

