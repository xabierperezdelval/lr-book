package com.mpower.job;

import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.mpower.util.InvitationUtil;

public class ReminderInvitationJob extends BaseMessageListener {

	protected void doReceive(Message message) throws Exception {

		long userId = 10195l;
		
		long companyId = CompanyLocalServiceUtil.getCompanyIdByUserId(userId);
		
		Group group = GroupLocalServiceUtil.fetchGroup(companyId, GroupConstants.GUEST);
		
		System.out.println("createAccountURL ==> " + InvitationUtil.getCreateAccountURL(group.getGroupId(), "", userId));
		
		
		
	}
}