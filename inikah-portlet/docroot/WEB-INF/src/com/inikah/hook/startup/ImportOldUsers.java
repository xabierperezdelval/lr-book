package com.inikah.hook.startup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextThreadLocal;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;

public class ImportOldUsers extends SimpleAction {
	public void run(String[] arg0) throws ActionException {
		
		File file = new File("old-users.csv");
		
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
				
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		
		long companyId = PortalUtil.getDefaultCompanyId();
		long creatorUserId = 0l;
		try {
			creatorUserId = UserLocalServiceUtil.getDefaultUserId(companyId);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}		
		
		try {
			String line = StringPool.BLANK;
			while (Validator.isNotNull(line = br.readLine())) {
				insertOldUser(companyId, creatorUserId, line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//file.delete();
	}
	
	private void insertOldUser(long companyId, long creatorUserId, String line) {
		
		String parts[] = line.split(StringPool.COMMA);
		
		boolean autoPassword = false;
		String password = parts[2];
		boolean autoScreenName = true;
		String screenName = null;
		String emailAddress = parts[1];
		long facebookId = 0l;
		String openId = StringPool.BLANK;
		String firstName = parts[3];
		String middleName = StringPool.BLANK;
		String lastName = StringPool.BLANK;
		int prefixId = 0;
		int suffixId = 0;
		boolean male = parts[6].equalsIgnoreCase("1");
		int birthdayMonth = 0;
		int birthdayDay = 0;
		int birthdayYear = 0;
		String jobTitle = StringPool.BLANK;
		long[] groupIds = null;
		long[] organizationIds = null;
		long[] roleIds = null;
		long[] userGroupIds = null;
		boolean sendEmail = false;
		
		ServiceContext serviceContext = ServiceContextThreadLocal.getServiceContext();
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy hh:mm");
		
		try {
			User user = UserLocalServiceUtil.addUser(creatorUserId, companyId,
					autoPassword, password, password, autoScreenName, screenName,
					emailAddress, facebookId, openId, Locale.ENGLISH, firstName,
					middleName, lastName, prefixId, suffixId, male, birthdayMonth,
					birthdayDay, birthdayYear, jobTitle, groupIds, organizationIds,
					roleIds, userGroupIds, sendEmail, serviceContext);
			
			String createDate = parts[0];
			if (Validator.isNotNull(createDate)) {
				try {
					user.setCreateDate(dateFormat.parse(createDate));
				} catch (ParseException e) {
					e.printStackTrace();
				}				
			}
			
			String lastLoginDate = parts[5];
			if (Validator.isNotNull(lastLoginDate)) {
				
				try {
					user.setLastLoginDate(dateFormat.parse(lastLoginDate));
				} catch (ParseException e) {
					e.printStackTrace();
				}				
			}
			
			user.setGreeting(parts[2]);
			
			user = UserLocalServiceUtil.updateUser(user);
			
			_log.debug("User successfully created ==> " + user);
			
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
	}

	private static Log _log = LogFactoryUtil.getLog(
			LanguageConfig.class);
}