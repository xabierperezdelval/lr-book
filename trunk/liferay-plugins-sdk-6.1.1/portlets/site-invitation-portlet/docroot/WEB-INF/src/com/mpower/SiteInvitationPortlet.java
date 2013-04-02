package com.mpower;

import java.io.IOException;
import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.portal.kernel.language.LanguageUtil;
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
		
		String currentTab = ParamUtil.getString(actionRequest, "tabs1", 
				InvitationConstants.TAB_INVITE_FRIENDS);
		actionResponse.setRenderParameter("tabs1", currentTab);
		
		// 1. Extract List of valid emails
		String textWithEmails = ParamUtil.getString(actionRequest, "textWithEmails");
		List<String> validEmails = InvitationUtil.extractEmails(textWithEmails);
		if (Validator.isNull(validEmails) || validEmails.isEmpty()) {
			SessionErrors.add(actionRequest, InvitationConstants.KEY_MESSAGE_INVALID_EMAILS);
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
		
		for (String inviteeEmail: validEmails) {
			if (SiteInvitationLocalServiceUtil.isAnExistingUser(inviteeEmail, companyId)) {
				alreadyMember++;
			} else if (SiteInvitationLocalServiceUtil.alreadyInvited(inviterId, inviteeEmail)) {
				alreadyInvited++;
			} else {
				validInvitations = true;
				SiteInvitationLocalServiceUtil.saveSiteInvitation(
					inviteeEmail, inviterId, companyId, themeDisplay.getScopeGroupId());
			}
		}
		
		String [] arguments = {
				String.valueOf(totalInvited), 
				String.valueOf(alreadyMember),
				String.valueOf(alreadyInvited),
				String.valueOf(totalInvited - (alreadyMember+alreadyInvited))};
		
		String pattern = LanguageUtil.get(getPortletConfig(), themeDisplay.getLocale(), InvitationConstants.KEY_MESSAGE_INVITED_EMAILS);
		
		String successMessage = themeDisplay.translate(pattern, arguments);
		SessionMessages.add(actionRequest, InvitationConstants.KEY_MESSAGE_SUCCESS, successMessage);
		
		if (!validInvitations) return;
				
		// 3. Register in the MessageBus
		String createAccountURL = InvitationUtil.getCreateAccountURL(themeDisplay);
		Message message = new Message();
		message.put("inviterId", Long.valueOf(inviterId));
		message.put("createAccountURL", createAccountURL);
		
		PortletPreferences portletPreferences = actionRequest.getPreferences();
		
		String defaultTemplate = ContentUtil.get(InvitationConstants.EMAIL_TEMPLATE_PATH);
		message.put("emailBody", portletPreferences.getValue("invitation-email-template", defaultTemplate));
		
		User user = PortalUtil.getUser(actionRequest);
		InternetAddress fromAddress = new InternetAddress();
		fromAddress.setAddress(user.getEmailAddress());
		fromAddress.setPersonal(user.getFullName());
		message.put("fromAddress", fromAddress);
		MessageBusUtil.sendMessage("liferay/invitation", message);
	}
	
	public void render(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {

		// setting the configuration icon
		ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		themeDisplay.getPortletDisplay().setShowConfigurationIcon(true);
		
		setSortParams(request);
		super.render(request, response);
	}

	private void setSortParams(RenderRequest request) {
		if (!ParamUtil.getString(request, "tabs1").equalsIgnoreCase(InvitationConstants.TAB_MY_INVITATIONS)) return;
		
		String orderByCol = ParamUtil.getString(
				request, "orderByCol", "createDate");
		request.setAttribute("orderByCol", orderByCol);
		
		String orderByType = ParamUtil.getString(
				request, "orderByType", "desc");
		request.setAttribute("orderByType", orderByType);
	}
}