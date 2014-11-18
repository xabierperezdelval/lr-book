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

package com.fingence.slayer.service.impl;

import java.util.Date;
import java.util.List;

import com.fingence.slayer.model.Dividend;
import com.fingence.slayer.service.base.DividendLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;

/**
 * The implementation of the dividend local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.fingence.slayer.service.DividendLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.fingence.slayer.service.base.DividendLocalServiceBaseImpl
 * @see com.fingence.slayer.service.DividendLocalServiceUtil
 */
public class DividendLocalServiceImpl extends DividendLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.fingence.slayer.service.DividendLocalServiceUtil} to access the dividend local service.
	 */
	
	public double getIncome(long assetId, Date purchaseDate, double purchaseQty) {
		
		double income = 0d;
		
		List<Dividend> dividends = null;
		try {
			dividends = dividendPersistence.findByAssetId(assetId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		for (Dividend dividend : dividends) {
			if (Validator.isNotNull(dividend.getPayableDate()) && Validator.isNotNull(purchaseDate)) {
				if(purchaseDate.after(dividend.getPayableDate())) {
					income += (dividend.getAmount() * purchaseQty);
				}
			}
		}		
		
		return income;
	}
}