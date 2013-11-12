package com;

import javax.mail.internet.InternetAddress;

import com.liferay.mail.service.MailServiceUtil;
import com.liferay.portal.kernel.mail.MailMessage;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.util.ContentUtil;




public class InvitationMail extends BaseMessageListener {

	protected void doReceive(Message m) throws Exception 
	{	System.out.println("in do receive");
		/*Invitation invitation = (Invitation) m.getPayload();*/
		String inviterName = m.getString("inviterName");
		String inviteeName = m.getString("inviteeName");
		String inviteeEmail = m.getString("inviteeEmail");
		String invitationId = m.getString("invitationId");
		/*User u = UserLocalServiceUtil.getUser(invitation.getUserId());*/
		
		System.out.println(" Email sent to=========>"+ inviteeName );
		System.out.println(" Email sent to=========>"+inviteeEmail );
		System.out.println("Invitation sent by======>"+inviterName);
		System.out.println("Invitation ID is======>"+invitationId);
		
		String fromEmail = PropsUtil.get(PropsKeys.ADMIN_EMAIL_FROM_ADDRESS);
		String fromName = PropsUtil.get(PropsKeys.ADMIN_EMAIL_FROM_NAME);
		System.out.println(fromEmail);
		System.out.println(fromName);
		InternetAddress fromAddress = new InternetAddress();
		fromAddress.setAddress(fromEmail);
		fromAddress.setPersonal(fromName);
						
		StringBuilder subject = new StringBuilder();
		subject.append(inviteeName+" , you are invited");
		
		
	/*	========================== template code==================*/
		
		StringBuffer link = new StringBuffer();
		link.append("http://localhost:8080/home?invitationId=");
		link.append(invitationId);
		
		String finalLink = String.valueOf(link);
		String messageBody = ContentUtil.get("/content/record-added-email.tmpl");
		
		MailMessage mailMessage = new MailMessage();
		mailMessage.setFrom(fromAddress);
		mailMessage.setSubject(subject.toString());							
		String[] tags = {"[$INVITEE_NAME$]","[$INVITEE_EMAIL$]","[$INVITER_NAME$]","[$INVITATION_LINK$]"};
		String[] values={inviteeName,inviteeEmail,inviterName,finalLink};
		
		messageBody = StringUtil.replace(messageBody, tags, values);
		mailMessage.setBody(messageBody);
		mailMessage.setTo(new InternetAddress(inviteeEmail, inviteeName));					
		mailMessage.setHTMLFormat(true);									
		MailServiceUtil.sendEmail(mailMessage);
		System.out.println("Mail Sent to .."+inviteeEmail);
		
	}
								
								
}

