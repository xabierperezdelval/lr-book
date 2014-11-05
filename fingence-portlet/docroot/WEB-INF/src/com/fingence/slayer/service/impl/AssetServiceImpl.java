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

import java.util.ArrayList;
import java.util.List;

import com.fingence.slayer.model.Asset;
import com.fingence.slayer.model.impl.AssetImpl;
import com.fingence.slayer.service.base.AssetServiceBaseImpl;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.OrderFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

/**
 * The implementation of the asset remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.fingence.slayer.service.AssetService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.fingence.slayer.service.base.AssetServiceBaseImpl
 * @see com.fingence.slayer.service.AssetServiceUtil
 */
public class AssetServiceImpl extends AssetServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.fingence.slayer.service.AssetServiceUtil} to access the asset remote service.
	 */
	
	@SuppressWarnings("unchecked")
	public List<Asset> getAssets(String pattern, boolean ticker) {
		
		List<Asset> results = new ArrayList<Asset>();
		
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(AssetImpl.class);
		dynamicQuery.add(RestrictionsFactoryUtil.like(
				ticker? "security_ticker" : "id_isin", StringPool.PERCENT
						+ pattern + StringPool.PERCENT));
		dynamicQuery.addOrder(OrderFactoryUtil.asc(ticker? "security_ticker" : "id_isin"));
		
		try {
			results = assetLocalService.dynamicQuery(dynamicQuery);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	public boolean isAssetPresent(String id_isin, String security_ticker) {
		
		boolean present = false;
		
		try {
			Asset asset = assetPersistence.fetchByIdISIN_Ticker(id_isin, security_ticker);
			present = Validator.isNotNull(asset);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return present;
	}
}