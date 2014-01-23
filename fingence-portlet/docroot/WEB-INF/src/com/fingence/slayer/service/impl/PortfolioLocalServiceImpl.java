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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fingence.IConstants;
import com.fingence.slayer.NoSuchAssetException;
import com.fingence.slayer.model.Asset;
import com.fingence.slayer.model.Portfolio;
import com.fingence.slayer.model.PortfolioItem;
import com.fingence.slayer.service.base.PortfolioLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.security.auth.CompanyThreadLocal;

/**
 * The implementation of the portfolio local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.fingence.slayer.service.PortfolioLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.fingence.slayer.service.base.PortfolioLocalServiceBaseImpl
 * @see com.fingence.slayer.service.PortfolioLocalServiceUtil
 */
public class PortfolioLocalServiceImpl extends PortfolioLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.fingence.slayer.service.PortfolioLocalServiceUtil} to access the portfolio local service.
	 */
	
	public void addPortfolio(long userId, String porfolioName, long investorId, long bankId,
			long wealthAdvisorId, boolean trial, long relationshipManagerId,
			boolean social, boolean primary, File excel) {	
		
		long companyId = CompanyThreadLocal.getCompanyId();
		
		long portfolioId = 0l;
		try {
			portfolioId = counterLocalService.increment(Portfolio.class.getName());
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		Portfolio portfolio = createPortfolio(portfolioId);
		
		portfolio.setPorfolioName(porfolioName);
		portfolio.setInvestorId(investorId);
		portfolio.setWealthAdvisorId(wealthAdvisorId);
		portfolio.setRelationshipManagerId(relationshipManagerId);
		portfolio.setBankId(bankId);
		portfolio.setTrial(trial);
		portfolio.setPrimary(primary);
		portfolio.setSocial(social);
		portfolio.setCreateDate(new java.util.Date());
		portfolio.setCompanyId(companyId);
		portfolio.setUserId(userId);
		
		try {
			addPortfolio(portfolio);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		InputStream is = null;
		try {
			is = new FileInputStream(excel);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		 
        //Create Workbook instance holding reference to .xlsx file
        XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

        //Get first/desired sheet from the workbook
        XSSFSheet sheet = workbook.getSheetAt(0);
        
        //Iterate through each rows one by one
        Iterator<Row> rowIterator = sheet.iterator();
        
        while (rowIterator.hasNext()) {
        	// get the individual columns. 
        	
        	Row row = rowIterator.next();
        	
        	String id_isin = row.getCell(1).getStringCellValue();
        	
        	long itemId = 0l;
			try {
				itemId = counterLocalService.increment(PortfolioItem.class.getName());
			} catch (SystemException e) {
				e.printStackTrace();
			}
        	
        	PortfolioItem portfolioItem = portfolioItemLocalService.createPortfolioItem(itemId);
        	portfolioItem.setPortfolioId(portfolioId);
        	
        	try {
				Asset asset = assetPersistence.findByIdISIN(id_isin);
				portfolioItem.setAssetId(asset.getAssetId());
			} catch (NoSuchAssetException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}
        	
        	//portfolioItem.setPurchaseDate(purchaseDate);
        	//portfolioItem.setPurchasePrice(purchasePrice);
        	//portfolioItem.setPurchaseQty(purchaseQty);
        	//portfolioItem.setPurchasedMarketValue(purchasedMarketValue);
        	//portfolioItem.setPurchasedConversionRate(purchasedConversionRate);
        	
        	try {
				portfolioItemLocalService.addPortfolioItem(portfolioItem);
			} catch (SystemException e) {
				e.printStackTrace();
			}
        }
	}
	
	public List<Portfolio> getPortfolios(long finderKey, int userType) {
		
		List<Portfolio> portfolios = null;
		
		switch (userType) {
			case IConstants.USER_TYPE_INVESTOR:
			try {
				portfolios = portfolioPersistence.findByInvestorId(finderKey);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			break;
			
			case IConstants.USER_TYPE_WEALTH_ADVISOR:
			try {
				portfolios = portfolioPersistence.findByWealthAdvisorId(finderKey);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			break;
			
			case IConstants.USER_TYPE_BANK_ADMIN:
			try {
				portfolios = portfolioPersistence.findByBankId(finderKey);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			break;	
			
			case IConstants.USER_TYPE_REL_MANAGER:
			try {
				portfolios = portfolioPersistence.findByRelationshipManagerId(finderKey);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			break;	
		}
		
		return portfolios;
	}
}