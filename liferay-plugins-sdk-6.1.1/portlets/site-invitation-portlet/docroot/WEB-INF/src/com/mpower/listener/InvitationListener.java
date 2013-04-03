package com.mpower.listener;

import java.util.Date;
import java.util.List;

import javax.mail.internet.InternetAddress;

import com.liferay.mail.service.MailServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.mail.MailMessage;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.util.portlet.PortletProps;
import com.mpower.slayer.model.SiteInvitation;
import com.mpower.slayer.service.SiteInvitationLocalServiceUtil;
import com.mpower.util.InvitationConstants;

public class InvitationListener extends BaseMessageListener {

	protected void doReceive(Message message) throws Exception {
		
		long inviterId = message.getLong(InvitationConstants.KEY_INVITER_ID);
		
		List<SiteInvitation> siteInvitations = null;
		try {
			siteInvitations = SiteInvitationLocalServiceUtil.getUserInvitations(
				inviterId, InvitationConstants.STATUS_PENDING);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(siteInvitations) || siteInvitations.isEmpty()) return;
		
		String createAccountURL = message.getString("createAccountURL");
		InternetAddress fromAddress = (InternetAddress)message.get("fromAddress");
		String inviterName = message.getString("inviterName");
		
		String portalURL = message.getString("portalURL");
		String emailBody = message.getString("emailBody");
		String[] tokens = {"[$CREATE_ACCOUNT_URL$]", "[$INVITER_NAME$]"};		
		String subject = PortletProps.get(InvitationConstants.PROP_DEFAULT_EMAIL_SUBJECT) + portalURL;
		
		for (SiteInvitation siteInvitation : siteInvitations) {
			
			String inviteeEmail = siteInvitation.getInviteeEmail();
			String inviteeCreateAccountURL = createAccountURL + "&inviteeEmail=" + inviteeEmail;
			String[] replacements = {inviteeCreateAccountURL, inviterName};
			String messageBody = StringUtil.replace(emailBody, tokens, replacements);
			
			InternetAddress toAddress = new InternetAddress();
			toAddress.setAddress(inviteeEmail);
			
			MailMessage mailMessage = new MailMessage();
			mailMessage.setFrom(fromAddress);
			mailMessage.setTo(toAddress);
			mailMessage.setBody(messageBody);
			mailMessage.setSubject(subject);
			
			MailServiceUtil.sendEmail(mailMessage);
			
			siteInvitation.setStatus(InvitationConstants.STATUS_INVITED);
			siteInvitation.setModifiedDate(new Date());
			siteInvitation.setLastReminderDate(new Date());
			siteInvitation.setPortalURL(portalURL);
			
			try {
				SiteInvitationLocalServiceUtil.updateSiteInvitation(siteInvitation);
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
	}
}