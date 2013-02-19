package com.library.job;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.portlet.PortletPreferences;

import com.library.LibraryConstants;
import com.liferay.mail.service.MailServiceUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.mail.MailMessage;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.MessageListenerException;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.service.PortalPreferencesLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.util.ContentUtil;
import com.liferay.util.portlet.PortletProps;
import com.slayer.model.LMSBook;
import com.slayer.model.LMSBorrowing;
import com.slayer.service.LMSBookLocalServiceUtil;

public class NotifyDefaultingMembers implements MessageListener {

	public void receive(Message message) 
			throws MessageListenerException {

		System.out.println("inside receive method....");
		
	}
	
	private void notifyMember(LMSBorrowing borrowing) {
		
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
		
		// getting the original book object.
		String bookTitle = null;
		try {
			LMSBook lmsBook = 
				LMSBookLocalServiceUtil.fetchLMSBook(borrowing.getBookId());
			bookTitle = lmsBook.getBookTitle();
		} catch (SystemException e1) {
			e1.printStackTrace();
		}
		
		// forming the actual subject line
		String subjectTemplate = PortletProps.get(
			LibraryConstants.PROP_DEFAULTING_REMINDER_EMAIL_SUBJECT);
		String subjectLine = 
			StringUtil.replace(subjectTemplate, "[$BOOK_TITLE$]", bookTitle);
		mailMessage.setSubject(subjectLine);
		
		// preparing the email body
		String messageBody = 
				ContentUtil.get("/template/reminder-email.tmpl");
		
		String[] tokens = 
			{"[$MEMBER_NAME$]", "[$DATE_BORROWED$]", "[[$RETURN_DATE$]]"};
		
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		String dateBorrowed = 
				dateFormat.format(borrowing.getDateBorrowed());
		String dateOfReturn = 
				dateFormat.format(borrowing.getDateOfReturn());
		String[] values =
			{member.getFullName(), dateBorrowed, dateOfReturn};
		
		messageBody = StringUtil.replace(messageBody, tokens, values);
		mailMessage.setBody(messageBody);
		
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
	
	private String getEmailTemplate(String articleId, User member) {
		
		String emailTemplate = StringPool.BLANK;
		
		DynamicQuery dynamicQuery = 
				DynamicQueryFactoryUtil.forClass(JournalArticle.class, 
						PortalClassLoaderUtil.getClassLoader());
		
		dynamicQuery.add(RestrictionsFactoryUtil.eq(
				"companyId", member.getCompanyId()));
		dynamicQuery.add(RestrictionsFactoryUtil.eq(
				"articleId", articleId));
		
		List<JournalArticle> articles = null;
		try {
			articles = 
				JournalArticleLocalServiceUtil.dynamicQuery(dynamicQuery);
		} catch (SystemException e) {
			e.printStackTrace();
		}
			
		JournalArticle journalArticle = null;
		if (Validator.isNotNull(articles) && !articles.isEmpty()) {
			journalArticle = articles.get(0);
		}
		
		if (Validator.isNull(journalArticle)) return emailTemplate;
			
		try {
			emailTemplate = 
				JournalArticleLocalServiceUtil.getArticleContent(
				journalArticle, null, null, member.getLanguageId(), null);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
			
		return emailTemplate;	
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