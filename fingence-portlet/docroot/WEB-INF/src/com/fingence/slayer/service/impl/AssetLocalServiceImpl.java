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
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fingence.slayer.NoSuchAssetException;
import com.fingence.slayer.model.Asset;
import com.fingence.slayer.service.base.AssetLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.auth.CompanyThreadLocal;

/**
 * The implementation of the asset local service.
 * 
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link com.fingence.slayer.service.AssetLocalService} interface.
 * 
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 * 
 * @author Ahmed Hasan
 * @see com.fingence.slayer.service.base.AssetLocalServiceBaseImpl
 * @see com.fingence.slayer.service.AssetLocalServiceUtil
 */
public class AssetLocalServiceImpl extends AssetLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 * 
	 * Never reference this interface directly. Always use {@link
	 * com.fingence.slayer.service.AssetLocalServiceUtil} to access the asset
	 * local service.
	 */

	public void importFromExcel(long userId, File excel) {

		InputStream is = AssetLocalServiceImpl.class.getClassLoader()
				.getResourceAsStream("data/fingence-data-sample.xlsx");

		if (Validator.isNull(is)) return;

		// Create Workbook instance holding reference to .xlsx file
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (Validator.isNull(workbook)) return;

		// Get first/desired sheet from the workbook
		XSSFSheet sheet = workbook.getSheetAt(0);

		// Iterate through each rows one by one
		Iterator<Row> rowIterator = sheet.iterator();

		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (row.getRowNum() == 0) continue;

			String id_isin = getString(row.getCell(2));
			
			Asset asset = getAsset(userId, id_isin);
			
			asset.setSecurity_ticker(getString(row.getCell(0)));
			asset.setId_cusip(getString(row.getCell(1)));
			asset.setId_bb_global(getString(row.getCell(3)));
			asset.setId_bb_sec_num_src(getLong(row.getCell(4)));
			asset.setChg_pct_mtd(getDouble(row.getCell(15)));
			
			try {
				updateAsset(asset);
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Asset getAsset(long userId, String id_isin) {
		
		long companyId = CompanyThreadLocal.getCompanyId();
		
		Asset asset = null;
		
		long assetId = getAssetId(id_isin);
		
		if (assetId > 0l) {
			try {
				asset = fetchAsset(assetId);
				asset.setModifiedDate(new java.util.Date());
			} catch (SystemException e) {
				e.printStackTrace();
			}
		} else {
			try {
				assetId = counterLocalService.increment(Asset.class.getName());
				asset = createAsset(assetId);
				asset.setCompanyId(companyId);
				asset.setUserId(userId);
				asset.setCreateDate(new java.util.Date());
				addAsset(asset);
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		
		asset.setId_isin(id_isin);
		
		return asset;
	}

	private long getAssetId(String id_isin) {
		
		long assetId = 0l;
		
		try {
			Asset asset = assetPersistence.findByIdISIN(id_isin);
			
			if (Validator.isNotNull(asset)){
				assetId = asset.getAssetId();
			}
		} catch (NoSuchAssetException e) {
			System.out.println(e.getMessage());
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return assetId;
		
	}

	private long getLong(Cell cell) {
		
		long value = 0l;
		
		String cellValue = cell.toString();
		
		if (Validator.isNotNull(cellValue) && !cellValue.trim().equalsIgnoreCase(StringPool.DASH)) {
			try {
				value = Long.parseLong(cellValue.trim());
			} catch (NumberFormatException nfe) {
				System.out.println("Unable to convert to Long ==> " + cellValue);
			}
		}
		
		return value;
	}

	private double getDouble(Cell cell) {

		double value = 0;
		
		String cellValue = cell.toString();
		
		if (Validator.isNotNull(cellValue) && !cellValue.trim().equalsIgnoreCase(StringPool.DASH)) {
			try {
				value = Double.parseDouble(cellValue.trim());
				System.out.println("double value is ==> " + value);
			} catch (NumberFormatException nfe) {
				System.out.println("Unable to convert to double ==> " + cellValue);
			}
		}
		
		return value;

	}
	
	private String getString(Cell cell) {
		
		String value = StringPool.BLANK;
		
		String cellValue = cell.toString();
		
		if (Validator.isNotNull(cellValue) && !cellValue.trim().equalsIgnoreCase(StringPool.DASH)) {
			value = cellValue;
		}
		
		return value;
	}
}