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

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fingence.slayer.model.Asset;
import com.fingence.slayer.service.base.AssetLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.auth.CompanyThreadLocal;

/**
 * The implementation of the asset local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.fingence.slayer.service.AssetLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
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
	 * Never reference this interface directly. Always use {@link com.fingence.slayer.service.AssetLocalServiceUtil} to access the asset local service.
	 */
	
	public long importFromExcel(long userId, File excel) {
		
		int count = 0;
		
		long companyId = CompanyThreadLocal.getCompanyId();
		
		InputStream is = AssetLocalServiceImpl.class.getClassLoader().getResourceAsStream("data/fingence-data-sample.xlsx");
		
		/*
        InputStream is = null;
		try {
			is = new FileInputStream(excel);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		*/
		
		if (Validator.isNull(is)) return count;
        
        //Create Workbook instance holding reference to .xlsx file
        XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(workbook)) return count;

        //Get first/desired sheet from the workbook
        XSSFSheet sheet = workbook.getSheetAt(0);
        
        //Iterate through each rows one by one
        Iterator<Row> rowIterator = sheet.iterator();
        
        System.out.println("before looping....");
        
        while (rowIterator.hasNext()) {
        	Row row = rowIterator.next();
        	
        	System.out.println("row ==> " + row);
        	
        	double chg_pct_1m = row.getCell(21).getNumericCellValue();
        	double chg_pct_6m = row.getCell(23).getNumericCellValue();
        	double chg_pct_3m = row.getCell(22).getNumericCellValue();
        	double chg_pct_5d = row.getCell(20).getNumericCellValue();
        	double chg_pct_ytd = row.getCell(24).getNumericCellValue();
        	String security_typ= row.getCell(60).toString();
            String security_typ2= row.getCell(61).toString();
            String security_ticker = row.getCell(4).toString();
            String id_isin = row.getCell(6).toString();
            String id_cusip  = row.getCell(5).toString();
            String security_des= row.getCell(59).toString();
            String name = row.getCell(17).toString();
            String id_bb_global = row.getCell(7).toString();
            long id_bb_sec_num_src = (long) row.getCell(8).getNumericCellValue();
            double chg_pct_high_52week= row.getCell(52).getNumericCellValue();
            double chg_pct_low_52week= row.getCell(53).getNumericCellValue();
            //double chg_pct_mtd = row.getCell(arg0)

        	long assetId = 0l;
			try {
				assetId = counterLocalService.increment(Asset.class.getName());
			} catch (SystemException e) {
				e.printStackTrace();
			}
        	Asset asset = createAsset(assetId);
        	
        	//asset.setAsk_price(ask_price);
        	//asset.setBid_price(bid_price);
        	asset.setChg_pct_1m(chg_pct_1m);
        	//asset.setChg_pct_1yr(chg_pct_1yr);
        	asset.setChg_pct_3m(chg_pct_3m);
        	asset.setChg_pct_5d(chg_pct_5d);
        	asset.setChg_pct_6m(chg_pct_6m);
        	asset.setChg_pct_high_52week(chg_pct_high_52week);
        	asset.setChg_pct_low_52week(chg_pct_low_52week);
        	//asset.setChg_pct_mtd(chg_pct_mtd);
        	asset.setChg_pct_ytd(chg_pct_ytd);
        	//asset.setChg_trr_5yr(chg_trr_5yr);
        	//asset.setCurent_price(curent_price);
        	asset.setId_bb_global(id_bb_global);
        	asset.setId_bb_sec_num_src(id_bb_sec_num_src);
        	asset.setId_cusip(id_cusip);
        	asset.setId_isin(id_isin);
        	//asset.setLast_price(last_price);
        	asset.setName(name);
        	//asset.setParent_comp_name(parent_comp_name);
        	//asset.setSecurity_class(security_class);
        	asset.setSecurity_des(security_des);
        	asset.setSecurity_ticker(security_ticker);
        	asset.setSecurity_typ(security_typ);
        	asset.setSecurity_typ2(security_typ2);
        	
        	asset.setCompanyId(companyId);
        	asset.setCreateDate(new java.util.Date());
        	
        	try {
				addAsset(asset);
			} catch (SystemException e) {
				e.printStackTrace();
			}
        }
		
		return count;
	}
}