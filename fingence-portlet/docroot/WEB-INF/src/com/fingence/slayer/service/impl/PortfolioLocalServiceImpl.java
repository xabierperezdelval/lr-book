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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fingence.IConstants;
import com.fingence.slayer.NoSuchAssetException;
import com.fingence.slayer.model.Asset;
import com.fingence.slayer.model.Portfolio;
import com.fingence.slayer.model.PortfolioItem;
import com.fingence.slayer.service.base.PortfolioLocalServiceBaseImpl;
import com.fingence.util.CellUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Organization;
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
	
	public void updatePortfolio(long portfolioId, long userId,
			String portfolioName, long investorId, long institutionId,
			long wealthAdvisorId, boolean trial, long relationshipManagerId,
			boolean social, String baseCurrency, File excelFile) {
		
		Portfolio portfolio = getPortfolioObj(portfolioId, userId);
		
		portfolioId = portfolio.getPortfolioId();
		portfolio.setPortfolioName(portfolioName);
		portfolio.setInvestorId(investorId);
		portfolio.setWealthAdvisorId(wealthAdvisorId);
		portfolio.setRelationshipManagerId(relationshipManagerId);
		portfolio.setInstitutionId(institutionId);
		portfolio.setTrial(trial);
		portfolio.setPrimary(isFirstPortfolio(investorId));
		portfolio.setSocial(social);
		portfolio.setBaseCurrency(baseCurrency);
		
		try {
			portfolio = updatePortfolio(portfolio);
		} catch (SystemException e) {
			e.printStackTrace();
		}
				
		if (Validator.isNull(excelFile)) return;
		
		InputStream is = null;
		try {
			is = new FileInputStream(excelFile);
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
        	if (row.getRowNum()==0) continue;
        	
        	String id_isin = CellUtil.getString(row.getCell(0));
        	
        	Asset asset = null;
        	try {
				asset = assetPersistence.findByIdISIN(id_isin);
			} catch (NoSuchAssetException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}
        	
        	if (Validator.isNull(asset)) continue;
        	
        	long assetId = asset.getAssetId();
        	
        	PortfolioItem portfolioItem = null;
        	try {
				portfolioItem = portfolioItemPersistence.fetchByAssetId_PortfolioId(assetId, portfolioId);
			} catch (SystemException e) {
				e.printStackTrace();
			}
        	
        	if (Validator.isNull(portfolioItem)) {
        		long itemId = 0l;
    			try {
    				itemId = counterLocalService.increment(PortfolioItem.class.getName());
    			} catch (SystemException e) {
    				e.printStackTrace();
    			}        		
    			portfolioItem = portfolioItemLocalService.createPortfolioItem(itemId);
    			portfolioItem.setCreateDate(new java.util.Date());
				portfolioItem.setPortfolioId(portfolioId);
				portfolioItem.setAssetId(assetId);
		
    			try {
					portfolioItemLocalService.addPortfolioItem(portfolioItem);
				} catch (SystemException e) {
					e.printStackTrace();
				}
        	} else {
        		portfolioItem.setModifiedDate(new java.util.Date());
        	}
       
			portfolioItem.setPurchaseDate(CellUtil.getDate(row.getCell(2)));
			portfolioItem.setPurchasePrice(CellUtil.getDouble(row.getCell(3)));
			portfolioItem.setPurchaseQty(CellUtil.getDouble(row.getCell(4)));
			portfolioItem.setPurchasedFx(CellUtil.getDouble(row.getCell(5)));
			
        	try {
				portfolioItemLocalService.updatePortfolioItem(portfolioItem);
			} catch (SystemException e) {
				e.printStackTrace();
			}
        }
        
        if (Validator.isNotNull(excelFile)) {
            // invoke JMS
            Message message = new Message();
            message.put("MESSAGE_NAME", "setConvertionRate");
            message.put("portfolioId", portfolioId);
            MessageBusUtil.sendMessage("fingence/destination", message);        	
        }
	}
	
	public void applyConversion(long portfolioId) {
		
		List<PortfolioItem> portfolioItems = null;
		try {
			portfolioItems = portfolioItemPersistence.findByPortfolioId(portfolioId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(portfolioItems)) return;
		
		for (PortfolioItem portfolioItem: portfolioItems) {
			Asset asset = null;
			try {
				asset = assetLocalService.fetchAsset(portfolioItem.getAssetId());
			} catch (SystemException e) {
				e.printStackTrace();
			}
			
			if (Validator.isNull(asset)) continue;
			
			String currency = asset.getCurrency();
			
			double conversion = 1.0d;
			if (!currency.equalsIgnoreCase("USD") && portfolioItem.getPurchasedFx() == 0.0d) {
				conversion = getConversion(currency, portfolioItem.getPurchaseDate());
			}
		
			portfolioItem.setPurchasedFx(conversion);
			try {
				portfolioItemLocalService.updatePortfolioItem(portfolioItem);
			} catch (SystemException e) {
				e.printStackTrace();
			}			
		}
	}
	
	private double getConversion(String currency, Date purchaseDate) {

		double conversion = 1.0d;

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		StringBuilder sb = new StringBuilder()
			.append("http://currencies.apps.grandtrunk.net/getrate/")
			.append(formatter.format(purchaseDate))
			.append(StringPool.SLASH)
			.append(currency)
			.append(StringPool.SLASH)
			.append("usd");
			
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(sb.toString());

		try {
			client.executeMethod(method);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		InputStream inputStream = null;
		try {
			inputStream = method.getResponseBodyAsStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			conversion = Double.parseDouble(IOUtils.toString(inputStream));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return conversion;
	}

	private boolean isFirstPortfolio(long investorId) {
		
		boolean first = false;
		
		try {
			int count = portfolioPersistence.countByInvestorId(investorId);
			first = (count == 0);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return first;
	}

	public List<Portfolio> getPortfolios(long userId) {
		
		List<Portfolio> portfolios = null;
		
		int userType = bridgeService.getUserType(userId);
		
		switch (userType) {
			case IConstants.USER_TYPE_INVESTOR:
			try {
				portfolios = portfolioPersistence.findByInvestorId(userId);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			break;
			
			case IConstants.USER_TYPE_WEALTH_ADVISOR:
			try {
				portfolios = portfolioPersistence.findByWealthAdvisorId(userId);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			break;
			
			case IConstants.USER_TYPE_BANK_ADMIN:
				
			long institutionId = getInstitutionId(userId);	
			try {
				portfolios = portfolioPersistence.findByInstitutionId(institutionId);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			break;	
			
			case IConstants.USER_TYPE_REL_MANAGER:
			try {
				portfolios = portfolioPersistence.findByRelationshipManagerId(userId);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			break;	
		}
		
		return portfolios;
	}
	
	private long getInstitutionId(long userId) {
		
		long institutionId = 0l;

		Organization organization = bridgeService.getCurrentOrganization(userId);
		
		if (Validator.isNotNull(organization)) {
			institutionId = organization.getOrganizationId();
		}
		
		return institutionId;
	}

	private Portfolio getPortfolioObj(long portfolioId, long userId) {
		
		long companyId = CompanyThreadLocal.getCompanyId();
		
		Portfolio portfolio = null;
		
		if (portfolioId > 0l) {
			try {
				portfolio = fetchPortfolio(portfolioId);
			} catch (SystemException e) {
				e.printStackTrace();
			}
		} else {
			try {
				portfolioId = counterLocalService.increment(Portfolio.class.getName());
			} catch (SystemException e) {
				e.printStackTrace();
			}
			
			portfolio = createPortfolio(portfolioId);
			portfolio.setCreateDate(new java.util.Date());
			portfolio.setUserId(userId);
			portfolio.setCompanyId(companyId);
			
			try {
				portfolio = addPortfolio(portfolio);
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		
		portfolio.setModifiedDate(new java.util.Date());
		return portfolio;
	}
	
	public int getAssetsCount(long portfolioId) {

		int count = 0;
		try {
			count = portfolioItemPersistence.countByPortfolioId(portfolioId);
		} catch (SystemException e) {
			e.printStackTrace();
		}

		return count;
	}

	public List<PortfolioItem> getAssets(long portfolioId) {

		List<PortfolioItem> assets = null;

		try {
			assets = portfolioItemPersistence.findByPortfolioId(portfolioId);
		} catch (SystemException e) {
			e.printStackTrace();
		}

		return assets;
	}
	
	public void makePrimary(long portfolioId) {
		
		Portfolio portfolio = null;
		try {
			portfolio = fetchPortfolio(portfolioId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNotNull(portfolio)) {
			try {
				List<Portfolio> portfolios = portfolioPersistence.findByInvestorId(portfolio.getInvestorId());
				
				for (Portfolio obj: portfolios) {
					obj.setPrimary(obj.getPortfolioId() == portfolioId);
					updatePortfolio(obj);
				}			
			} catch (SystemException e) {
				e.printStackTrace();
			}			
		}
	}
}