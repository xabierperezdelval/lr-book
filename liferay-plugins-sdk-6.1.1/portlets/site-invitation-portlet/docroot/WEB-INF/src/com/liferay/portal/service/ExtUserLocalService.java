package com.liferay.portal.service;

import java.util.Locale;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.model.User;
import com.mpower.slayer.service.SiteInvitationLocalServiceUtil;
import com.mpower.util.InvitationConstants;

public class ExtUserLocalService extends UserLocalServiceWrapper {

	public ExtUserLocalService(UserLocalService userLocalService) {
		super(userLocalService);
	}

	public User addUserWithWorkflow(long creatorUserId, long companyId,
			boolean autoPassword, String password1, String password2,
			boolean autoScreenName, String screenName, String emailAddress,
			long facebookId, String openId, Locale locale, String firstName,
			String middleName, String lastName, int prefixId, int suffixId,
			boolean male, int birthdayMonth, int birthdayDay, int birthdayYear,
			String jobTitle, long[] groupIds, long[] organizationIds,
			long[] roleIds, long[] userGroupIds, boolean sendEmail,
			ServiceContext serviceContext) throws PortalException,
			SystemException {

		User user = super.addUserWithWorkflow(creatorUserId, companyId,
				autoPassword, password1, password2, autoScreenName, screenName,
				emailAddress, facebookId, openId, locale, firstName,
				middleName, lastName, prefixId, suffixId, male, birthdayMonth,
				birthdayDay, birthdayYear, jobTitle, groupIds, organizationIds,
				roleIds, userGroupIds, sendEmail, serviceContext);

		updateInvitation(user, serviceContext);

		return user;
	}

	private void updateInvitation(User user, ServiceContext serviceContext) {

		long inviterId = GetterUtil.getLong(
				serviceContext.getAttribute(InvitationConstants.KEY_INVITER_ID), 0l);
		String inviteeEmail = user.getEmailAddress();

		if (inviterId > 0l) {
			SiteInvitationLocalServiceUtil.setInvitationAccepted(inviterId,
					inviteeEmail, user.getUserId());
		}
	}
}