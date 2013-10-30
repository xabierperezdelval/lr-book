package com.liferay.portal.service;

import java.util.Locale;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;

public class ExtUserLocalService extends UserLocalServiceWrapper {
	/* (non-Java-doc)
	 * @see com.liferay.portal.service.UserLocalServiceWrapper#UserLocalServiceWrapper(UserLocalService userLocalService)
	 */
	public ExtUserLocalService(UserLocalService userLocalService) {
		super(userLocalService);
	}
	
	@Override
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
		
		User user = super.addUserWithWorkflow(creatorUserId, companyId, autoPassword,
				password1, password2, autoScreenName, screenName, emailAddress,
				facebookId, openId, locale, firstName, middleName, lastName, prefixId,
				suffixId, male, birthdayMonth, birthdayDay, birthdayYear, jobTitle,
				groupIds, organizationIds, roleIds, userGroupIds, sendEmail,
				serviceContext);
		
		//processExtraSteps(user, serviceContext);
		
		return user;
	}

	@Override
	public User addUser(long creatorUserId, long companyId,
			boolean autoPassword, String password1, String password2,
			boolean autoScreenName, String screenName, String emailAddress,
			long facebookId, String openId, Locale locale, String firstName,
			String middleName, String lastName, int prefixId, int suffixId,
			boolean male, int birthdayMonth, int birthdayDay, int birthdayYear,
			String jobTitle, long[] groupIds, long[] organizationIds,
			long[] roleIds, long[] userGroupIds, boolean sendEmail,
			ServiceContext serviceContext) throws PortalException,
			SystemException {

		User user = super.addUser(creatorUserId, companyId, autoPassword, password1,
				password2, autoScreenName, screenName, emailAddress, facebookId,
				openId, locale, firstName, middleName, lastName, prefixId, suffixId,
				male, birthdayMonth, birthdayDay, birthdayYear, jobTitle, groupIds,
				organizationIds, roleIds, userGroupIds, sendEmail, serviceContext);
		
		//processExtraSteps(user, serviceContext);
		
		return user;
	}
}