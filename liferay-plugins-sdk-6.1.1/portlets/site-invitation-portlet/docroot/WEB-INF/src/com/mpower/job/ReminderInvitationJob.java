package com.mpower.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.mail.internet.InternetAddress;

import com.liferay.mail.service.MailServiceUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.mail.MailMessage;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.model.PortletPreferences;
import com.liferay.portal.model.User;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.util.ContentUtil;
import com.liferay.util.portlet.PortletProps;
import com.mpower.slayer.model.SiteInvitation;
import com.mpower.slayer.service.SiteInvitationLocalServiceUtil;
import com.mpower.util.InvitationConstants;
import com.mpower.util.InvitationUtil;

public class ReminderInvitationJob extends BaseMessageListener {

	protected void doReceive(Message message) throws Exception {
		
		// email template
		String portletId = (String)message.getValues().get("PORTLET_ID");
		
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(PortletPreferences.class, PortalClassLoaderUtil.getClassLoader());
		dynamicQuery.add(RestrictionsFactoryUtil.eq("portletId", portletId));
		List<PortletPreferences> portletPreferencesList = PortletPreferencesLocalServiceUtil.dynamicQuery(dynamicQuery);
		HashMap<Long, PortletPreferences> hashMap = new HashMap<Long, PortletPreferences>();
		
		for (PortletPreferences portletPreferences: portletPreferencesList) {
			long companyId = portletPreferences.getOwnerId();
			if (companyId > 0l && portletPreferences.getPlid() == 0l) {
				hashMap.put(companyId, portletPreferences);
				break;
			}
		}
		
		List<SiteInvitation> siteInvitations = SiteInvitationLocalServiceUtil.getListForSendingReminder();
		
		String[] tokens = {"[$CREATE_ACCOUNT_URL$]", "[$INVITER_NAME$]"};
		
		for (SiteInvitation siteInvitation: siteInvitations) {
			long userId = siteInvitation.getUserId();
			long companyId = CompanyLocalServiceUtil.getCompanyIdByUserId(userId);
			
			Group group = GroupLocalServiceUtil.fetchGroup(companyId, GroupConstants.GUEST);
			
			String inviteeEmail = siteInvitation.getInviteeEmail();
			String portalURL = siteInvitation.getPortalURL(); 
			String createAccountURL =  
					InvitationUtil.getCreateAccountURL(group.getGroupId(), portalURL, userId)  + "&inviteeEmail=" + inviteeEmail;
			
			// send the reminder
			
			User user = UserLocalServiceUtil.fetchUser(userId);
			String[] replacements = {createAccountURL, user.getFullName()};
			
			String emailBody = ContentUtil.get(InvitationConstants.EMAIL_TEMPLATE_PATH);
			PortletPreferences portletPreferences = hashMap.get(companyId);
			if (Validator.isNotNull(portletPreferences)) {
				javax.portlet.PortletPreferences portletPrefs = 
						PortletPreferencesLocalServiceUtil.getPreferences(
								companyId, portletPreferences.getOwnerId(), 
								portletPreferences.getOwnerType(), 
								portletPreferences.getPlid(), portletId);
				emailBody = portletPrefs.getValue(InvitationConstants.EMAIL_BODY_TEXT, emailBody);
			}
			
			String messageBody = StringUtil.replace(emailBody, tokens, replacements);
			
			InternetAddress fromAddress = new InternetAddress();
			fromAddress.setAddress(user.getEmailAddress());
			fromAddress.setPersonal(user.getFullName());
			
			InternetAddress toAddress = new InternetAddress();
			toAddress.setAddress(inviteeEmail);
			
			MailMessage mailMessage = new MailMessage();
			mailMessage.setFrom(fromAddress);
			mailMessage.setTo(toAddress);
			mailMessage.setBody(messageBody);
			
			String subject = PortletProps.get(InvitationConstants.PROP_DEFAULT_EMAIL_SUBJECT) + portalURL;
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