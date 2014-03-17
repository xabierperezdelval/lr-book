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

import com.fingence.IConstants;
import com.fingence.slayer.model.Asset;
import com.fingence.slayer.model.Currency;
import com.fingence.slayer.model.Portfolio;
import com.fingence.slayer.model.PortfolioItem;
import com.fingence.slayer.service.CurrencyServiceUtil;
import com.fingence.slayer.service.base.PortfolioServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

/**
 * The implementation of the portfolio remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.fingence.slayer.service.PortfolioService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.fingence.slayer.service.base.PortfolioServiceBaseImpl
 * @see com.fingence.slayer.service.PortfolioServiceUtil
 */
public class PortfolioServiceImpl extends PortfolioServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.fingence.slayer.service.PortfolioServiceUtil} to access the portfolio remote service.
	 */
	
	public void makePrimary(long portfolioId, int userType) {
		portfolioLocalService.makePrimary(portfolioId, userType);
	}
	
	public List<Portfolio> getPortfolios(long userId) {
		return portfolioLocalService.getPortfolios(userId);
	}
	
	public long getDefault(long userId) {
		
		long portfolioId = 0l;
		
		List<Portfolio> portfolios = portfolioLocalService.getPortfolios(userId);
		
		if (Validator.isNotNull(portfolios) && portfolios.size() > 0) {
			if (portfolios.size() == 1) {
				portfolioId = portfolios.get(0).getPortfolioId();
			} else {
				int userType = bridgeService.getUserType(userId);
				for (Portfolio portfolio: portfolios) {
					if ((portfolio.isPrimary() && userType == IConstants.USER_TYPE_INVESTOR)
							|| (userType != IConstants.USER_TYPE_INVESTOR)) {
						portfolioId = portfolio.getPortfolioId();
						break;
					}
				}				
			}
		}
		
		return portfolioId;
	}	
	
	public int getPortoliosCount(long userId) {
		
		int count = 0;
		
		List<Portfolio> portfolios = getPortfolios(userId);
		
		if (Validator.isNotNull(portfolios)) {
			count = portfolios.size();
		}
		
		return count;
	}
	
	public Portfolio getPortfolio(long portfolioId) {
		
		Portfolio portfolio = null;
		
		try {
			portfolio = portfolioLocalService.fetchPortfolio(portfolioId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return portfolio;
	}
	
	public String getPortfolioName(long portfolioId) {
		
		String portfolioName = StringPool.BLANK;
		
		if (portfolioId > 0l) {
			portfolioName = getPortfolio(portfolioId).getPortfolioName();
		}
		
		return portfolioName;
	}
	
	public JSONArray getPortfolioSummary(long userId) {
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();
		
		int userType = bridgeService.getUserType(userId);

		List<Portfolio> portfolios = portfolioLocalService.getPortfolios(userId);

		double totalPurchasedValue = 0.0d;
		double totalCurrentValue = 0.0d;
		double totalGainLoss = 0.0d;
		
		for (Portfolio portfolio : portfolios) {
			long portfolioId = portfolio.getPortfolioId();
			
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

			if (userType == IConstants.USER_TYPE_WEALTH_ADVISOR) {
				jsonObject.put("investorOrAdvisor",
						bridgeService.getUserName(portfolio.getInvestorId()));
			} else if (userType == IConstants.USER_TYPE_INVESTOR) {
				jsonObject.put("investorOrAdvisor", bridgeService
						.getUserName(portfolio.getWealthAdvisorId()));
			}
			
			jsonObject.put("isPrimary", portfolio.isPrimary());
			
			String baseCurrency = portfolio.getBaseCurrency();
			jsonObject.put("baseCurrency", baseCurrency);
			
			List<PortfolioItem> portfolioItems = null;
			try {
				portfolioItems = portfolioItemPersistence
						.findByPortfolioId(portfolioId);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			
			double usdPurchasePrice = 0.0d;
			double usdCurrentPrice = 0.0d;

			for (PortfolioItem portfolioItem : portfolioItems) {
				
				double itemPurchasePrice = portfolioItem.getPurchasePrice() * portfolioItem.getPurchaseQty() * portfolioItem.getPurchasedFx();
				usdPurchasePrice += itemPurchasePrice;
				
				Asset asset = null;
				try {
					asset = assetLocalService.fetchAsset(portfolioItem.getAssetId());
				} catch (SystemException e) {
					e.printStackTrace();
				}
				
				if (Validator.isNotNull(asset)) {
					usdCurrentPrice += 	(asset.getCurrent_price() * portfolioItem.getPurchaseQty() * CurrencyServiceUtil.getConversion(asset.getCurrency()));
				}
			}
			
			double gainLoss = usdCurrentPrice - usdPurchasePrice;
			
			jsonObject.put("manager", bridgeService.getUserName(portfolio.getRelationshipManagerId()));
			jsonObject.put("portfolioId", portfolioId);
			jsonObject.put("portfolioName", portfolio.getPortfolioName());
			jsonObject.put("purchasePrice", usdPurchasePrice);
			jsonObject.put("currentPrice", usdCurrentPrice);
			jsonObject.put("gainLossAbsValue", Math.abs(gainLoss));
			jsonObject.put("gainOrLoss", (gainLoss > 0)? true : false);

			jsonArray.put(jsonObject);
			
			totalPurchasedValue += usdPurchasePrice;
			totalCurrentValue += usdCurrentPrice;
			totalGainLoss += gainLoss;
		}
		
		// To Add the a final row for Total
		JSONObject jsonTotal = JSONFactoryUtil.createJSONObject();
		jsonTotal.put("portfolioName", "<b>Total</b>");
		jsonTotal.put("portfolioId", "--");
		jsonTotal.put("manager", "--");
		jsonTotal.put("investorOrAdvisor", "--");
		jsonTotal.put("purchasePrice", totalPurchasedValue);
		jsonTotal.put("currentPrice", totalCurrentValue);
		jsonTotal.put("gainLossAbsValue", Math.abs(totalGainLoss));
		jsonTotal.put("gainOrLoss", (totalGainLoss > 0)? true : false);
		
		jsonArray.put(jsonTotal);
		
		return jsonArray;
	}
	
	public void deletePortfolio(long portfolioId) {

		List<PortfolioItem> portfolioItems = null;
		try {
			portfolioItems = portfolioItemPersistence.findByPortfolioId(portfolioId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		for(PortfolioItem portfolioItem : portfolioItems) {
			try {
				portfolioItemLocalService.deletePortfolioItem(portfolioItem.getItemId());
			} catch (SystemException e) {
				e.printStackTrace();
			} catch (PortalException e) {
				e.printStackTrace();
			}	
		}
		
		try {
			portfolioLocalService.deletePortfolio(portfolioId);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
	
	public String getBaseCurrency(long portfolioId) {
		
		String baseCurrency = StringPool.BLANK;
		
		try {
			Portfolio portfolio = portfolioLocalService.fetchPortfolio(portfolioId);
			
			if (Validator.isNotNull(portfolio)) {
				Currency currency = currencyService.getCurrency(portfolio.getBaseCurrency());
				baseCurrency = currency.getCurrencyCode() + StringPool.DASH + currency.getCurrencyDesc();
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return baseCurrency;
	}
}