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

import com.inikah.slayer.model.Invitation;
import com.inikah.slayer.model.Payment;
import com.inikah.slayer.model.Plan;
import com.inikah.slayer.model.Profile;
import com.inikah.slayer.service.ProfileLocalServiceUtil;
import com.inikah.slayer.service.base.PaymentLocalServiceBaseImpl;
import com.inikah.util.IConstants;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ClassNameLocalServiceUtil;

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
		
		payment.setClassName(Profile.class.getName());
		payment.setClassPK(profileId);
		payment.setPlanId(planId);
		payment.setCreateDate(new java.util.Date());
		
		// calculate the amount as per the plan and the profile
		payment.setAmount(2.30d);
		
		try {
			payment = addPayment(payment);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return payment;
	}
	
	public void reward(long userId, long planId, double amount) {
		
		Invitation invitation = null;
		try {
			invitation = invitationPersistence.fetchByInviteeNewUserId(userId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(invitation)) return;
		
		String invitationChain = invitation.getInvitationChain();
		
		if (Validator.isNull(invitationChain)) return;
		
		String[] inviters = invitationChain.split(StringPool.COMMA);
				
		String[] commission = null;
		try {
			Plan plan = planLocalService.fetchPlan(planId);
			String referralBonus = plan.getReferralBonus();
			if (Validator.isNotNull(referralBonus)) {
				commission = referralBonus.split(StringPool.COMMA);
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		int loopCount = Math.min(inviters.length, commission.length);
		
		for (int i=0; i<loopCount; i++) {
			int percent = Integer.valueOf(commission[i]);
			
			double incentive = amount * (percent / 100);
			earningService.credit(Long.valueOf(inviters[i]), incentive, "some details");
		}
	}
	
	public boolean isPaymentPending(long profileId) {
		boolean paymentPending = true;
		
		try {
			List<Payment> payments = paymentPersistence
					.findByClassNameId_ClassPK(ClassNameLocalServiceUtil
							.getClassNameId(Profile.class), profileId);
			
			for (Payment payment: payments) {
				paymentPending = payment.isPaid();
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return paymentPending;
	}
	
	public int getCurrentPlan(long profileId) {
		
		int currentPlan = 0;
		
		try {
			List<Payment> payments = paymentPersistence
					.findByClassNameId_ClassPK(ClassNameLocalServiceUtil
							.getClassNameId(Profile.class), profileId);
			
			for (Payment payment: payments) {
				currentPlan = (int)payment.getPlanId();
				break;
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}		
		
		return currentPlan;
	}
	
	public Payment getCurrentPayment(long profileId) {
		Payment payment = null;
		
		try {
			List<Payment> payments = paymentPersistence
					.findByClassNameId_ClassPK(ClassNameLocalServiceUtil
							.getClassNameId(Profile.class), profileId);
			
			for (Payment _payment: payments) {
				payment = _payment;
				break;
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}	
		
		return payment;
	}
	
	public Payment paymentDone(long userId, long paymentId, int paymentMode, String details) {
		
		Payment payment = null;
		try {
			payment = fetchPayment(paymentId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(payment)) return payment;
		
		payment.setPaymentMode(paymentMode);
		payment.setDetails(details);
		payment.setModifiedDate(new java.util.Date());
		payment.setPaid(true);
		payment.setUserId(userId);
		
		try {
			payment = updatePayment(payment);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		ProfileLocalServiceUtil.updateStatus(payment.getClassPK(), IConstants.PROFILE_STATUS_PAYMENT_DONE);
		reward(userId, payment.getPlanId(), payment.getAmount());
		
		return payment;
	}
}