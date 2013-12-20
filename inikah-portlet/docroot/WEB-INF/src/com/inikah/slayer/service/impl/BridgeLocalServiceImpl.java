/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.inikah.slayer.service.impl;

import java.util.List;

import com.inikah.slayer.model.Location;
import com.inikah.util.IConstants;
import com.inikah.util.SMSUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Address;
import com.liferay.portal.model.EmailAddress;
import com.liferay.portal.model.Phone;
import com.liferay.portal.model.User;
import com.liferay.util.PwdGenerator;

/**
 * The implementation of the bridge local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.inikah.slayer.service.BridgeLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.inikah.slayer.service.base.BridgeLocalServiceBaseImpl
 * @see com.inikah.slayer.service.BridgeLocalServiceUtil
 */
public class BridgeLocalServiceImpl extends BridgeLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.inikah.slayer.service.BridgeLocalServiceUtil} to access the bridge local service.
	 */
	
	public long addPhone(long userId, String className, long classPK, String number, String extension, boolean primary) {
		
		List<Phone> phones = getPhones(0l, number, extension);
		boolean alreadyVerified = (Validator.isNotNull(phones) && phones.size() > 0);
		
		long phoneId = 0l;
		try {
			Phone phone = phoneLocalService.addPhone(userId, className, classPK, number, extension, IConstants.PHONE_UNVERIFIED, primary, null);
			phoneId = phone.getPhoneId();
			
			phone.setUserName(PwdGenerator.getPinNumber());
			if (alreadyVerified) {
				phone.setTypeId(IConstants.PHONE_VERIFIED);
			}
			
			phone = phoneLocalService.updatePhone(phone);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return phoneId;
	}
	
	public void sendVerificationCode(long phoneId) {
		
		Phone phone = null;
		try {
			phone = phoneLocalService.fetchPhone(phoneId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNotNull(phone) && phone.getTypeId() != IConstants.PHONE_VERIFIED) {
			String mobileNumber = phone.getExtension() + phone.getNumber();
			SMSUtil.sendVerificationCode(mobileNumber, phone.getUserName());
		}
	}
	
	public boolean verifyPhone(long phoneId, String verificationCode) {
		
		Phone phone = null;
		try {
			phone = phoneLocalService.fetchPhone(phoneId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		boolean verified = (Validator.isNotNull(phone) 
				&& phone.getUserName().equalsIgnoreCase(verificationCode)
				&& phone.getTypeId() == IConstants.PHONE_VERIFIED);
		
		if (!verified) {
			phone.setTypeId(IConstants.PHONE_VERIFIED);
			
			try {
				phoneLocalService.updatePhone(phone);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			
			// get all other phones like this and set verified
			List<Phone> phones = getPhones(phoneId, phone.getNumber(), phone.getExtension());
			for (Phone fone: phones) {
				fone.setTypeId(IConstants.PHONE_VERIFIED);
				
				try {
					phoneLocalService.updatePhone(fone);
				} catch (SystemException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		return verified;
	}
	
	public long addEmail(long userId, String className, long classPK, String address, boolean primary) {
		
		boolean alreadyVerified = false;
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(EmailAddress.class, PortalClassLoaderUtil.getClassLoader());
		dynamicQuery.add(RestrictionsFactoryUtil.eq("address", address));
		dynamicQuery.add(RestrictionsFactoryUtil.eq("type", IConstants.PHONE_VERIFIED));
		
		if (!alreadyVerified) {
			try {
				User user = userLocalService.fetchUser(userId);
				alreadyVerified = (Validator.isNotNull(user) 
						&& (user.getEmailAddress().equalsIgnoreCase(address) || user.getEmailAddressVerified()));
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		
		try {
			@SuppressWarnings("unchecked")
			List<EmailAddress> emails = emailAddressLocalService.dynamicQuery(dynamicQuery);
			alreadyVerified = (Validator.isNotNull(emails) && emails.size() > 0);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		long emailAddressId = 0l;
		try {
			EmailAddress emailAddress = EmailAddressLocalServiceUtil
					.addEmailAddress(userId, className, classPK, address,
							IConstants.PHONE_UNVERIFIED, primary, null);
			emailAddressId = emailAddress.getEmailAddressId();
			
			emailAddress.setUserName(PwdGenerator.getPinNumber());
			if (alreadyVerified) {
				emailAddress.setTypeId(IConstants.PHONE_VERIFIED);
			} else {
				// send verification email
			}
			
			emailAddress = emailAddressLocalService.updateEmailAddress(emailAddress);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return emailAddressId;
	}
	
	public boolean verifyEmail(long emailAddressId, String verificationCode) {
		
		EmailAddress emailAddress = null;
		try {
			emailAddress = emailAddressLocalService.fetchEmailAddress(emailAddressId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		boolean verified = (Validator.isNotNull(emailAddress) 
				&& emailAddress.getUserName().equalsIgnoreCase(verificationCode));
		
		if (verified && emailAddress.getTypeId() != IConstants.PHONE_VERIFIED) {
			emailAddress.setTypeId(IConstants.PHONE_VERIFIED);
			
			try {
				emailAddressLocalService.updateEmailAddress(emailAddress);
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		
		return verified;
	}
		
	private List<Phone> getPhones(long phoneId, String number, String extension) {
		
		List<Phone> phones = null;
		
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Phone.class, PortalClassLoaderUtil.getClassLoader());
		dynamicQuery.add(RestrictionsFactoryUtil.eq("number_", number));
		dynamicQuery.add(RestrictionsFactoryUtil.eq("extension", extension));
		
		if (phoneId > 0l) {
			dynamicQuery.add(RestrictionsFactoryUtil.ne("typeId", IConstants.PHONE_VERIFIED));
			dynamicQuery.add(RestrictionsFactoryUtil.ne("phoneId", phoneId));
		} else {
			dynamicQuery.add(RestrictionsFactoryUtil.eq("typeId", IConstants.PHONE_VERIFIED));
		}
		
		try {
			phones = phoneLocalService.dynamicQuery(dynamicQuery);
		} catch (SystemException e) {
			e.printStackTrace();
		}	
		
		return phones;
	}
	
	/**
	 * 
	 */
	public Address getLocation(User user) {
		
		Address address = null;
		
		try {
			List<Address> addresses = 
					addressLocalService.getAddresses(
							user.getCompanyId(), Location.class.getName(), user.getUserId());
			
			if (Validator.isNotNull(addresses) && !addresses.isEmpty()) {
				address = addresses.get(0);
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return address;
	}
	
	/**
	 * 
	 */
	public boolean isLocationSet(User user) {		
		return Validator.isNotNull(getLocation(user));
	}
}