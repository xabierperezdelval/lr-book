package com.mpower.util;

public interface InvitationConstants {
	static final int STATUS_PENDING = 0;
	
	static final int STATUS_INVITED = 1;
	
	static final int STATUS_ACCEPTED = 2;
	
	static final int STATUS_ALL = -1;
	
	static final String KEY_MESSAGE_SUCCESS = "key.message.success";
	
	static final String KEY_MESSAGE_INVALID_EMAILS = "key.message.invalid.emails";
	
	static final String KEY_MESSAGE_INVITED_EMAILS = "key.message.invited.emails";
	
	static final String EMAIL_TEMPLATE_PATH = "/com/mpower/listener/invitation.tmpl";
	
	static final String KEY_INVITER_ID = "inviterId";
	
	static final String KEY_EMAIL_ADDRESS = "emailAddress";
	
	static final String EMAIL_BODY_TEXT = "EMAIL_BODY_TEXT";
	
	static final String TAB_MY_INVITATIONS = "my-invitations";
	
	static final String TAB_STATISTICS = "statistics";
	
	static final String TAB_INVITE_FRIENDS = "invite-friends";
	
	static final String TAB_CONFIG_TEMPLATE = "invitation-template";
}