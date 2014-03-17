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

import com.fingence.slayer.model.MyResult;
import com.fingence.slayer.service.CurrencyServiceUtil;
import com.fingence.slayer.service.base.MyResultServiceBaseImpl;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.TextFormatter;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Country;
import com.liferay.portal.service.CountryServiceUtil;

/**
 * The implementation of the my result remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.fingence.slayer.service.MyResultService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.fingence.slayer.service.base.MyResultServiceBaseImpl
 * @see com.fingence.slayer.service.MyResultServiceUtil
 */
public class MyResultServiceImpl extends MyResultServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.fingence.slayer.service.MyResultServiceUtil} to access the my result remote service.
	 */
	
	public List<MyResult> getMyResults(long portfolioId) {
		
		List<MyResult> myResults = myResultFinder.findResults(portfolioId);
		
		for (MyResult myResult: myResults) {
			
			String baseCurrency = myResult.getBaseCurrency();
			
			if (!baseCurrency.equalsIgnoreCase("USD")) {
				double conversionFactor = CurrencyServiceUtil.getConversion(baseCurrency);
				myResult.setPurchasedMarketValue(myResult.getPurchasedMarketValue() * conversionFactor);
				myResult.setCurrentMarketValue(myResult.getCurrentMarketValue() * conversionFactor);
				myResult.setGain_loss(myResult.getGain_loss() * conversionFactor);
			}
			
			long countryOfRisk = myResult.getCountryOfRisk();
			
			if (countryOfRisk > 0l) {
				Country country = null;
				try {
					country = CountryServiceUtil.fetchCountry(countryOfRisk);
				} catch (SystemException e) {
					e.printStackTrace();
				}
				
				if (Validator.isNotNull(country)) {
					myResult.setCountryOfRiskName(TextFormatter.format(country.getName(), TextFormatter.J));
				}
			}		
		}
		
		return myResults;
	}
}