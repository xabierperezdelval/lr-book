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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fingence.slayer.model.MyResult;
import com.fingence.slayer.service.base.MyResultServiceBaseImpl;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
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
		
		Map<String, Double> fxRates = getFxRates();
		
		for (MyResult myResult: myResults) {
			
			double updatedCurrentValueinUSD = myResult.getCurrentMarketValue()
					+ ((myResult.getCurrent_fx() - myResult.getPurchasedFx()) * myResult.getPurchaseQty());
			
			if(!myResult.getCurrency_().equals("USD")){
				//Updated Purchased and Curent Value in USD
				if (!myResult.getBaseCurrency().equalsIgnoreCase("USD")) {
					myResult = getValueInBaseCurrency(myResult, fxRates, updatedCurrentValueinUSD);
				} else {
					try{
						double updatedCurrentMarketValue = myResult.getCurrentMarketValue() * myResult.getCurrent_fx();
						
						double gain_loss = updatedCurrentMarketValue - myResult.getPurchasedMarketValue();
						
						myResult.setCurrentMarketValue(updatedCurrentMarketValue);
						myResult.setGain_loss(gain_loss);
						myResult.setGain_loss_percent(gain_loss/myResult.getPurchasedMarketValue() * 100);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
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
			} else {
				myResult.setCountryOfRiskName("Un-Specified");
			}
		}
		return myResults;
	}
	private MyResult getValueInBaseCurrency(MyResult myResult, Map<String, Double> fxRates, double updatedCurrentValueinUSD){
		
		if (!myResult.getBaseCurrency().equalsIgnoreCase("USD")) {
			if(Validator.isNotNull(myResult.getBaseCurrency())){
				double conversionRate = fxRates.get(myResult.getBaseCurrency());
				double purchasedValueinBaseCurrency = myResult.getPurchasedMarketValue() * (1/conversionRate);
				double currentValueinBaseCurrency = updatedCurrentValueinUSD * (1/conversionRate);
				double gainLoss = currentValueinBaseCurrency - purchasedValueinBaseCurrency;
				myResult.setPurchasedMarketValue(purchasedValueinBaseCurrency);
				myResult.setCurrentMarketValue(currentValueinBaseCurrency);
				myResult.setGain_loss(gainLoss);
				if(purchasedValueinBaseCurrency > 0){
					myResult.setGain_loss_percent(gainLoss/purchasedValueinBaseCurrency * 100);
				} else {
					myResult.setGain_loss_percent(0);
				}
				
			}
		}
		return myResult;
	}
	
	private Map<String, Double> getFxRates() {
		
		Map<String, Double> fxRates = new HashMap<String, Double>();
		Connection conn = null;
		try {
			conn = DataAccess.getConnection();
			
			Statement stmt = conn.createStatement();
			
			String sql = "SELECT distinct currency_, conversion FROM fing_CountryExt where currency_ != 'USD'";
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				String currency = rs.getString(1);
				double fxRate = rs.getDouble(2);
				
				fxRates.put(currency, fxRate);
			}
			
		} catch (SQLException sqle) {
			
		} finally {
			DataAccess.cleanUp(conn);
		}
		
		return fxRates;
	}
}