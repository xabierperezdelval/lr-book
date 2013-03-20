package com.mpower;

import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.ContentUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.mpower.slayer.service.SiteInvitationLocalServiceUtil;
import com.mpower.util.InvitationConstants;
import com.mpower.util.InvitationUtil;

public class SiteInvitationPortlet extends MVCPortlet {

	public void saveInvitations(ActionRequest actionRequest,
			ActionResponse actionResponse) throws Exception {
		
		// 1. Extract List of valid emails
		String textWithEmails = ParamUtil.getString(actionRequest, "textWithEmails");
		List<String> validEmails = InvitationUtil.extractEmails(textWithEmails);
		if (Validator.isNotNull(validEmails) && !validEmails.isEmpty()) {
			SessionMessages.add(actionRequest, InvitationConstants.KEY_MESSAGE_INVITED_EMAILS);
		} else {
			SessionErrors.add(actionRequest, InvitationConstants.KEY_MESSAGE_INVITED_EMAILS);
			return;
		}
		
		// 2. Insert to the database
		boolean validInvitations = false;
		int totalInvited = validEmails.size();
		int alreadyMember = 0;
		int alreadyInvited = 0;
		
		long companyId = PortalUtil.getCompanyId(actionRequest);
		long inviterId = PortalUtil.getUserId(actionRequest);
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		
		System.out.println(validEmails);
		
		for (String inviteeEmail: validEmails) {
			if (SiteInvitationLocalServiceUtil.isAnExistingUser(inviteeEmail, companyId)) {
				System.out.println("existing user");
				alreadyMember++;
			} else if (SiteInvitationLocalServiceUtil.alreadyInvited(inviterId, inviteeEmail)) {
				System.out.println("already invited");
				alreadyInvited++;
			} else {
				System.out.println("new invitee");
				validInvitations = true;
				SiteInvitationLocalServiceUtil.saveSiteInvitation(
					inviteeEmail, inviterId, companyId, themeDisplay.getScopeGroupId());
			}
		}
		
		SessionMessages.add(actionRequest, "");
		
		if (!validInvitations) return;
				
		// 3. Register in the MessageBus
		HttpServletRequest httpServletRequest = PortalUtil.getHttpServletRequest(actionRequest);
		String createAccountURL = PortalUtil.getCreateAccountURL(httpServletRequest, themeDisplay);
		Message message = new Message();
		message.put("inviterId", Long.valueOf(inviterId));
		message.put("createAccountURL", createAccountURL);
		
		PortletPreferences portletPreferences = actionRequest.getPreferences();
		
		String defaultTemplate = ContentUtil.get(InvitationConstants.EMAIL_TEMPLATE_PATH);
		message.put("emailBody", portletPreferences.getValue("", defaultTemplate));
		
		User user = PortalUtil.getUser(actionRequest);
		InternetAddress fromAddress = new InternetAddress();
		fromAddress.setAddress(user.getEmailAddress());
		fromAddress.setPersonal(user.getFullName());
		message.put("fromAddress", fromAddress);
		MessageBusUtil.sendMessage("liferay/invitation", message);
	}
}