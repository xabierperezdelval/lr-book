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

import java.util.List;

import com.fingence.slayer.model.Currency;
import com.fingence.slayer.service.base.CurrencyServiceBaseImpl;
import com.liferay.portal.kernel.exception.SystemException;

/**
 * The implementation of the currency remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.fingence.slayer.service.CurrencyService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.fingence.slayer.service.base.CurrencyServiceBaseImpl
 * @see com.fingence.slayer.service.CurrencyServiceUtil
 */
public class CurrencyServiceImpl extends CurrencyServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.fingence.slayer.service.CurrencyServiceUtil} to access the currency remote service.
	 */
	
	public double getConversion(String currencyCode) {		
		return getCurrency(currencyCode).getConversion();
	}
	
	public List<Currency> getCurrencies() {
		
		List<Currency> currencies = null;
		
		try {
			currencies = currencyPersistence.findAll();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return currencies;
	}
	
	public Currency getCurrency(String currencyCode) {
		Currency currency = null;
		
		try {
			currency = currencyPersistence.fetchByCurrencyCode(currencyCode);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return currency;		
	}
}