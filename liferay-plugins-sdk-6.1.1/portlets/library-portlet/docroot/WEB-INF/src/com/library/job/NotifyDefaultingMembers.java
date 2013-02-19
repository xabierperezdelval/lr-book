package com.library.job;

import java.io.UnsupportedEncodingException;

import javax.mail.internet.InternetAddress;
import javax.portlet.PortletPreferences;

import com.liferay.mail.service.MailServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.mail.MailMessage;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.MessageListenerException;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.service.PortalPreferencesLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.slayer.model.LMSBorrowing;

public class NotifyDefaultingMembers implements MessageListener {

	@Override
	public void receive(Message message) 
			throws MessageListenerException {

		System.out.println("inside receive method....");
		
	}
	
	private void notifyMember(LMSBorrowing borrowing, 
			String subjectTemplate, String bodyTemplate) {
		
		User member = null;
		try {
			member = 
				UserLocalServiceUtil.fetchUser(borrowing.getMemberId());
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNotNull(member)) return;
		
		long companyId = member.getCompanyId();
			
		InternetAddress fromAddress = getFromAddress(companyId); 
		MailMessage mailMessage = new MailMessage();
		mailMessage.setFrom(fromAddress);
		mailMessage.setSubject(subjectTemplate);
		mailMessage.setBody(bodyTemplate);
		
		String toEmail = member.getEmailAddress();
		String toName = member.getFullName();		
		try {
			mailMessage.setTo(
				new InternetAddress(toEmail, toName));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		MailServiceUtil.sendEmail(mailMessage);
	}

	private InternetAddress getFromAddress(long companyId) {
		PortletPreferences portletPreferences = null;
		try {
			portletPreferences = 
				PortalPreferencesLocalServiceUtil.getPreferences(
					companyId, companyId, 1);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		String key1 = PropsKeys.ADMIN_EMAIL_FROM_ADDRESS;
		String fromEmail = portletPreferences.getValue(key1, 
							PropsUtil.get(key1));
		
		String key2 = PropsKeys.ADMIN_EMAIL_FROM_NAME;
		String fromName = portletPreferences.getValue(key2, 
				PropsUtil.get(key2));
		
		InternetAddress fromAddress = new InternetAddress(); 
		fromAddress.setAddress(fromEmail); 
		try {
			fromAddress.setPersonal(fromName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return fromAddress;
	}
}