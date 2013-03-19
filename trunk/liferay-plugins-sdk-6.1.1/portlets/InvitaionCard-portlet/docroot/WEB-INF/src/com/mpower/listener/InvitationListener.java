package com.mpower.listener;

import java.util.List;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.mail.util.Mail;
import com.mpower.slayer.model.InvitationCard;
import com.mpower.slayer.service.InvitationCardLocalServiceUtil;

public class InvitationListener  extends BaseMessageListener {
	
	protected void doReceive(Message msg) throws Exception {
		
		List<InvitationCard> invitationcard=null;
		long inviterId =0l;
		
		inviterId = msg.getLong("inviterId");
		String createAccountURLS = (String)msg.get("createAccountURL");
		String emailBody = (String)msg.get("emailBody");
		//String from= (String)msg.get("from");
		User user = UserLocalServiceUtil.getUser(inviterId);
		invitationcard=InvitationCardLocalServiceUtil.findByUserIdSatus(inviterId, com.mpower.util.InvitationConstants.STATUS_PENDING);
		
		for(InvitationCard invitecards:invitationcard){
			
			
		String createAcounts=	createAccountURLS+"&"+"inviteeEmail="+invitecards.getInviteeEmail()+"&"+"inviterId="+inviterId;
		emailBody = emailBody.replace("[$INVITATION_URL$]", createAcounts);
		emailBody = emailBody.replace("[$FROM_INVITER$]",invitecards.getUserName());
		/*MailMessage mailMessage = new MailMessage();
		System.out.println("Mails to be sent"+invitecards.getInviteeEmail());
		InternetAddress fromAddress = new InternetAddress();
		fromAddress.setAddress("ushaswami@gmail.com");
		fromAddress.setPersonal(from);

		mailMessage.setTo(new InternetAddress(invitecards.getInviteeEmail(), ""));
		mailMessage.setFrom(fromAddress);
		MailServiceUtil.sendEmail(mailMessage);*/
		
		Mail.sendMail(invitecards.getInviteeEmail(),invitecards.getGroupId() , user.getEmailAddress(), emailBody, inviterId);
		
		invitecards.setStatus(com.mpower.util.InvitationConstants.STATUS_INVITED);
		InvitationCardLocalServiceUtil.updateInvitationCard(invitecards);
			}
		}

	}


