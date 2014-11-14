package com.inikah.util;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.internet.InternetAddress;

import com.inikah.slayer.model.Location;
import com.inikah.slayer.model.Profile;
import com.inikah.slayer.service.LocationLocalServiceUtil;
import com.liferay.mail.service.MailServiceUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.OrderFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.mail.MailMessage;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;

public class NotifyUtil {
	
	public static void newCityCreated(Location location) {
		
		MailMessage mailMessage = new MailMessage();
		mailMessage.setFrom(getFrom());
		mailMessage.setTo(getAdmin());
		mailMessage.setSubject("New City Created: " 
				+ LocationLocalServiceUtil.getDisplayInfo(location.getLocationId()));
		MailServiceUtil.sendEmail(mailMessage);
	}
	
	public static void newProfileCreated(Profile profile) {
		
	}
	
	private static InternetAddress getFrom() {
		InternetAddress addr = new InternetAddress();
		addr.setAddress(PropsUtil.get(PropsKeys.ADMIN_EMAIL_FROM_ADDRESS));
		try {
			addr.setPersonal(PropsUtil.get(PropsKeys.ADMIN_EMAIL_FROM_NAME));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return addr;
	}
	
	private static InternetAddress getAdmin() {
		InternetAddress addr = new InternetAddress();
		addr.setAddress("hasan@mpowerglobal.com");
		try {
			addr.setPersonal("Ahmed Hasan");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return addr;
	}
	
	private static InternetAddress getTo(String inviteeEmail, String inviteeName) {
		InternetAddress addr = new InternetAddress();
		addr.setAddress(inviteeEmail);
		try {
			addr.setPersonal(inviteeName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return addr;
	}
	
	public static void sendInvitation(long invitationId, String inviterEmail, String inviterName, String inviteeEmail, String inviteeName) {
		
		MailMessage mailMessage = new MailMessage();
		mailMessage.setFrom(getFrom());
		mailMessage.setTo(getTo(inviteeEmail, inviteeName));
		
		InternetAddress[] replyTo = new InternetAddress[]{getTo(inviterEmail, inviterName)};
		mailMessage.setReplyTo(replyTo);
		
		StringBuilder sb = new StringBuilder();
		sb.append("Inviation from ").append(inviterName).append("to join hasnain");
		mailMessage.setSubject(sb.toString());
		
		String mailBody = getEmailTemplate("PORTAL_INVITATION");
		
		String[] tokens = {"[$INVITER_NAME$]", "[$INVITATION_ID$]"};
		String[] replacements = {inviterName, String.valueOf(invitationId)};
		
		mailBody = StringUtil.replace(mailBody, tokens, replacements);
		
		mailMessage.setBody(mailBody);
		
		MailServiceUtil.sendEmail(mailMessage);
	}
	
	public static String getEmailTemplate(String articleId) {
		
		long companyId = CompanyThreadLocal.getCompanyId();
		
		String emailTemplate = StringPool.BLANK;
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
				JournalArticle.class, PortalClassLoaderUtil.getClassLoader());
		dynamicQuery.add(RestrictionsFactoryUtil.eq("companyId", companyId));
		dynamicQuery.add(RestrictionsFactoryUtil.eq("articleId", articleId));
		dynamicQuery.addOrder(OrderFactoryUtil.desc("version"));

		try {
			@SuppressWarnings("unchecked")
			List<JournalArticle> articles = JournalArticleLocalServiceUtil.dynamicQuery(dynamicQuery);
			
			for (JournalArticle journalArticle: articles) {
				try {
					emailTemplate = JournalArticleLocalServiceUtil.getArticleContent(
							journalArticle, null, null, "en_US", null);
					break;
				} catch (PortalException e) {
					e.printStackTrace();
				}
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return emailTemplate;
	}
}