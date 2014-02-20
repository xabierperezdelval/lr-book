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
import com.fingence.slayer.model.Portfolio;
import com.fingence.slayer.model.PortfolioItem;
import com.fingence.slayer.service.base.PortfolioServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
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
	
	public void makePrimary(long portfolioId) {
		portfolioLocalService.makePrimary(portfolioId);
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
		return getPortfolio(portfolioId).getPortfolioName();
	}
	
	public JSONArray getPortfolioSummary(long userId) {
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();
		
		double totalPurchasedValue = 0d;
		double totalCurrentValue = 0d;

		int userType = bridgeService.getUserType(userId);

		List<Portfolio> portfolios = portfolioLocalService
				.getPortfolios(userId);

		for (Portfolio portfolio : portfolios) {
			long portfolioId = portfolio.getPortfolioId();
			
			double portfolioPurchasedPrice = 0d;
			double portfolioCurrentPrice = 0d;

			JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

			jsonObject.put("manager", bridgeService.getUserName(portfolio
					.getRelationshipManagerId()));
			jsonObject.put("portfolioId", portfolioId);
			jsonObject.put("portfolioName", portfolio.getPortfolioName());

			if (userType == IConstants.USER_TYPE_WEALTH_ADVISOR) {
				jsonObject.put("investorOrAdvisor",
						bridgeService.getUserName(portfolio.getInvestorId()));
			} else if (userType == IConstants.USER_TYPE_INVESTOR) {
				jsonObject.put("investorOrAdvisor", bridgeService
						.getUserName(portfolio.getWealthAdvisorId()));
			}
			try {
				List<PortfolioItem> portfolioItems = portfolioItemPersistence
						.findByPortfolioId(portfolioId);
				for (PortfolioItem portfolioItem : portfolioItems) {
					Asset asset = null;
					try {
						asset = assetLocalService.getAsset(portfolioItem
								.getAssetId());
						portfolioPurchasedPrice += portfolioItem
								.getPurchasePrice();
						portfolioCurrentPrice += asset.getCurrent_price();
					} catch (PortalException e) {
						e.printStackTrace();
					}
				}
			} catch (SystemException e) {
				e.printStackTrace();
			}
			totalPurchasedValue += portfolioPurchasedPrice;
			totalCurrentValue += portfolioCurrentPrice;

			jsonObject.put("purchasePrice", portfolioPurchasedPrice);
			jsonObject.put("currentPrice", portfolioCurrentPrice);
			jsonObject.put("performance", 0d);

			jsonArray.put(jsonObject);
		}
		
		// To Add the a final row for Total
		JSONObject jsonTotal = JSONFactoryUtil.createJSONObject();
		jsonTotal.put("portfolioName", "<b>Total</b>");
		jsonTotal.put("manager", "");
		jsonTotal.put("investorOrAdvisor", "");
		jsonTotal.put("purchasePrice", "<b>" + totalPurchasedValue + "</b>");
		jsonTotal.put("currentPrice", "<b>" + totalCurrentValue + "</b>");
		jsonTotal.put("performance", 0d);
		
		jsonArray.put(jsonTotal);

		return jsonArray;
	}
}