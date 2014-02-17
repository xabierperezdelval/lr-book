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
import com.fingence.slayer.model.Portfolio;
import com.fingence.slayer.model.PortfolioItem;
import com.fingence.slayer.service.base.PortfolioServiceBaseImpl;
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
	
	public void updatePortfolio(long portfolioId, long userId,
			String portfolioName, long investorId, long institutionId,
			long wealthAdvisorId, boolean trial, long relationshipManagerId,
			boolean social) {
		
		portfolioLocalService.updatePortfolio(portfolioId, userId,
				portfolioName, investorId, institutionId, wealthAdvisorId,
				trial, relationshipManagerId, social, null);
	}
	
	public JSONArray getPortfolioSummary(long userId) {
		
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();
				
		List<Portfolio> portfolios = portfolioLocalService.getPortfolios(userId);
		
		for (Portfolio portfolio: portfolios) {
			
			long portfolioId = portfolio.getPortfolioId();
			
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
			jsonObject.put("Manager", bridgeService.getUserName(portfolio.getRelationshipManagerId()));
			jsonObject.put("portfolioId", portfolioId);
			jsonObject.put("portfolioName", portfolio.getPortfolioName());
			
			try {
				List<PortfolioItem> portfolioItems = portfolioItemPersistence.findByPortfolioId(portfolioId);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			
			jsonArray.put(jsonObject);			
		}
		
		return jsonArray;
	}
}