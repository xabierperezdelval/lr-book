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

import com.fingence.slayer.NoSuchAssetException;
import com.fingence.slayer.model.Asset;
import com.fingence.slayer.model.PortfolioItem;
import com.fingence.slayer.service.base.PortfolioItemServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;

/**
 * The implementation of the portfolio item remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.fingence.slayer.service.PortfolioItemService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.fingence.slayer.service.base.PortfolioItemServiceBaseImpl
 * @see com.fingence.slayer.service.PortfolioItemServiceUtil
 */
public class PortfolioItemServiceImpl extends PortfolioItemServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.fingence.slayer.service.PortfolioItemServiceUtil} to access the portfolio item remote service.
	 */
	
	public List<PortfolioItem> getPortfolioItems(long portfolioId) {
		
		List<PortfolioItem> portfolioItems = null;
		
		try {
			portfolioItems = portfolioItemPersistence.findByPortfolioId(portfolioId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return portfolioItems;
	}
	
	public void deleteItem(long itemId) {
		try {
			portfolioItemLocalService.deletePortfolioItem(itemId);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
	
	public void updateItem(long itemId, long portfolioId, String isinId, String ticker, double purchasePrice, int purchaseQty, Date purchaseDate) {
		PortfolioItem portfolioItem = null;
		
		if (itemId > 0l) {
			try {
				portfolioItem = portfolioItemLocalService.fetchPortfolioItem(itemId);
				portfolioItem.setModifiedDate(new java.util.Date());
			} catch (SystemException e) {
				e.printStackTrace();
			}
		} else {
			try {
				itemId = counterLocalService.increment(PortfolioItem.class.getName());
			} catch (SystemException e) {
				e.printStackTrace();
			}
			portfolioItem = portfolioItemLocalService.createPortfolioItem(itemId);
			portfolioItem.setCreateDate(new java.util.Date());
			try {
				portfolioItem = portfolioItemLocalService.addPortfolioItem(portfolioItem);
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		
		Asset asset = null;
		try {
			asset = assetPersistence.findByIdISIN(isinId);
		} catch (NoSuchAssetException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(asset)) {
			long assetId = 0l;
			try {
				assetId = counterLocalService.increment(Asset.class.getName());
			} catch (SystemException e) {
				e.printStackTrace();
			}
			asset = assetLocalService.createAsset(assetId);
			asset.setCreateDate(new java.util.Date());
			asset.setId_isin(isinId);
			asset.setSecurity_ticker(ticker);
			
			// invoke XIGNITE web service to get all other information
			try {
				asset = assetLocalService.addAsset(asset);
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		
		portfolioItem.setAssetId(asset.getAssetId());
		portfolioItem.setPortfolioId(portfolioId);
		portfolioItem.setPurchaseDate(purchaseDate);
		portfolioItem.setPurchasePrice(purchasePrice);
		portfolioItem.setPurchaseQty(purchaseQty);
	}
}