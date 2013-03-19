package com.mail.util;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import com.liferay.mail.service.MailServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.mail.MailMessage;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;

public class Mail {
	public static String getContent(String articleName,long groupId) {
		String emailTemplate = StringPool.BLANK;
		JournalArticle journal = null;
		try {
			journal = JournalArticleLocalServiceUtil.getArticleByUrlTitle(
					groupId, articleName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (Validator.isNotNull(journal)) {
			emailTemplate = journal.getContent();
			emailTemplate = emailTemplate.replace("<![CDATA[", "");
			emailTemplate = emailTemplate.replace("]]>", "");
		}
		return emailTemplate;
	}
	
	public static void sendMail(String mailId, long groupId,String Invitedfrom, String invitationUrls ,long userId)
			throws PortalException, SystemException, AddressException
	{
	
		String InvitationTemplate = StringPool.BLANK;
		InvitationTemplate = getContent("InvitationTemplate", groupId);
		String subject = " Invitation";
		InvitationTemplate = InvitationTemplate.replace("[$invitationUrls]", String.valueOf(invitationUrls));
		String from = Invitedfrom;
		InternetAddress fromAddress = new InternetAddress(from);
		InternetAddress to = new InternetAddress(mailId);
        MailMessage message = new MailMessage(fromAddress, to, subject, InvitationTemplate, true);
        message.setSubject(subject);
        message.setBody(invitationUrls);
        
        MailServiceUtil.sendEmail(message);
	}

}

	
	


