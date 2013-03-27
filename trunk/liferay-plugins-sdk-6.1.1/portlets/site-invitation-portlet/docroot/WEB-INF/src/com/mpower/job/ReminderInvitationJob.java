package com.mpower.job;

import java.util.List;

import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.mpower.slayer.model.SiteInvitation;
import com.mpower.slayer.service.SiteInvitationLocalServiceUtil;
import com.mpower.util.InvitationUtil;

public class ReminderInvitationJob extends BaseMessageListener {

	protected void doReceive(Message message) throws Exception {
		
		List<SiteInvitation> siteInvitations = SiteInvitationLocalServiceUtil.getListForSendingReminder();
		
		for (SiteInvitation siteInvitation: siteInvitations) {
			long userId = siteInvitation.getUserId();
			long companyId = CompanyLocalServiceUtil.getCompanyIdByUserId(userId);
			
			Group group = GroupLocalServiceUtil.fetchGroup(companyId, GroupConstants.GUEST);
			
			String portalURL = ""; // ???????
			System.out.println("createAccountURL ==> " + 
					InvitationUtil.getCreateAccountURL(group.getGroupId(), portalURL, userId));
			
			
		}
	}
}