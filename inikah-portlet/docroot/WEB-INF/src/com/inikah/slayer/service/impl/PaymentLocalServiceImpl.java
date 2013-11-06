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

import com.inikah.slayer.model.Payment;
import com.inikah.slayer.service.base.PaymentLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.SystemException;

/**
 * The implementation of the payment local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.inikah.slayer.service.PaymentLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.inikah.slayer.service.base.PaymentLocalServiceBaseImpl
 * @see com.inikah.slayer.service.PaymentLocalServiceUtil
 */
public class PaymentLocalServiceImpl extends PaymentLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.inikah.slayer.service.PaymentLocalServiceUtil} to access the payment local service.
	 */
	
	public Payment init(long profileId, long planId) {
		
		long paymentId = 0l;
		try {
			paymentId = counterLocalService.increment(Payment.class.getName());
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		Payment payment = createPayment(paymentId);
		
		payment.setProfileId(profileId);
		payment.setPlanId(planId);
		
		try {
			payment = addPayment(payment);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return payment;
	}
}