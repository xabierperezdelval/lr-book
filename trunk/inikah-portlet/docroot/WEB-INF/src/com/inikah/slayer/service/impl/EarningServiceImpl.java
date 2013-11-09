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

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import com.inikah.slayer.model.Earning;
import com.inikah.slayer.service.base.EarningServiceBaseImpl;
import com.liferay.portal.kernel.exception.SystemException;

/**
 * The implementation of the earning remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.inikah.slayer.service.EarningService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.inikah.slayer.service.base.EarningServiceBaseImpl
 * @see com.inikah.slayer.service.EarningServiceUtil
 */
public class EarningServiceImpl extends EarningServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.inikah.slayer.service.EarningServiceUtil} to access the earning remote service.
	 */
	
	public double getEarnings(long userId) {
		
		double earnings = 2.0d;
		try {
			List<Earning> items = earningPersistence.findByUserId(userId);
			
			for (Earning item: items) {
				if (item.isDebit()) {
					earnings -= item.getAmount();
				} else {
					earnings += item.getAmount();
				}
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return earnings;
	}
	
	public String getEarningsText(long userId) {
		double earnings =  getEarnings(userId);
		
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		
		return decimalFormat.format(earnings);
	}
	
	public Earning debit(long userId, double amount, String details) {
		return addEntry(userId, amount, details, true);
	}
	
	public Earning credit(long userId, double amount, String details) {
		return addEntry(userId, amount, details, false);
	}
	
	private Earning addEntry(long userId, double amount, String details, boolean debit) {
		long earningId = 0l;
		try {
			earningId = counterLocalService.increment(Earning.class.getName());
		} catch (SystemException e) {
			e.printStackTrace();
		}
		Earning earning = earningLocalService.createEarning(earningId);
		
		earning.setCreateDate(new Date());
		earning.setDebit(debit);
		earning.setUserId(userId);
		earning.setAmount(amount);
		earning.setDetails(details);
		
		try {
			earningLocalService.addEarning(earning);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return earning;
	}
}