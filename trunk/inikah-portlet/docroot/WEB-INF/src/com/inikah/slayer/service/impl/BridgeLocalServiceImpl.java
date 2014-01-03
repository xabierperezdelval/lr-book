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

import com.inikah.slayer.service.base.BridgeLocalServiceBaseImpl;
import com.inikah.util.IConstants;
import com.inikah.util.SMSUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Phone;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.service.ClassNameLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextThreadLocal;
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
		
		ServiceContext serviceContext = ServiceContextThreadLocal.getServiceContext();
		
		long phoneId = 0l;
		try {
			Phone phone = phoneLocalService.addPhone(userId, className, classPK, number, extension, IConstants.PHONE_UNVERIFIED, primary, serviceContext);
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
	
		
	@SuppressWarnings("unchecked")
	private List<Phone> getPhones(long phoneId, String number, String extension) {
		
		List<Phone> phones = null;
		
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(Phone.class, PortalClassLoaderUtil.getClassLoader());
		dynamicQuery.add(RestrictionsFactoryUtil.eq("number", number));
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
	
	public void updatePhone(String className, long classPK, String number, String extension, boolean primary) {
		
		Phone phone = null;
		
		long companyId = CompanyThreadLocal.getCompanyId();
		long classNameId = ClassNameLocalServiceUtil.getClassNameId(className);
		try {
			List<Phone> phones = phonePersistence.findByC_C_C(companyId, classNameId, classPK);
			for (Phone _phone: phones) {
				phone = _phone;
				break;
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(phone)) return;
		
		phone.setNumber(number);
		phone.setExtension(extension);
		phone.setPrimary(primary);
		
		try {
			phoneLocalService.updatePhone(phone);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
}