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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fingence.IConstants;
import com.fingence.slayer.model.Asset;
import com.fingence.slayer.model.Bond;
import com.fingence.slayer.model.Dividend;
import com.fingence.slayer.model.History;
import com.fingence.slayer.model.MyResult;
import com.fingence.slayer.model.Rating;
import com.fingence.slayer.model.impl.HistoryModelImpl;
import com.fingence.slayer.service.CurrencyServiceUtil;
import com.fingence.slayer.service.base.MyResultServiceBaseImpl;
import com.fingence.slayer.service.persistence.MyResultFinderImpl;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.TextFormatter;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Country;
import com.liferay.portal.service.CountryServiceUtil;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetVocabulary;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil;
import com.liferay.util.dao.orm.CustomSQLUtil;

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

	static String QUERY = MyResultFinderImpl.class.getName() + ".findResults";
	static String QUERY2 = MyResultFinderImpl.class.getName() + ".getDistinct";
	static String[] bucketNames = {"7 to 12 Months", "1 to 2 Years", "2 to 5 Years", "5 to 10 Years", "More than 10 Years"};
	
	static double yldToMaturityRange[][] =
		{{0, 1},{1, 3},{3, 5},{5, 7},{7, 9},{9, 11},{11, 90}};
	
	public static double durationRange[][] =
		{{0, 2},{2, 4},{4, 6},{6, 8},{8, 10},{10, 15},{15, 90}};
	
	public List<MyResult> getMyResults(String portfolioIds) {
		return getMyResults(portfolioIds, 0);
	}
	
	public List<MyResult> getMyResults(String portfolioIds, int allocationBy) {
				
		List<MyResult> myResults = myResultFinder.findResults(portfolioIds);
						
		for (MyResult myResult: myResults) {
			
			String baseCurrency = myResult.getBaseCurrency();
			
			if (!baseCurrency.equalsIgnoreCase("USD")) {
				double conversionFactor = CurrencyServiceUtil.getConversion(baseCurrency);
				myResult.setPurchasedMarketValue(myResult.getPurchasedMarketValue() * conversionFactor);
				myResult.setCurrentMarketValue(myResult.getCurrentMarketValue() * conversionFactor);
				myResult.setGain_loss(myResult.getGain_loss() * conversionFactor);
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
				myResult.setCountryOfRiskName(IConstants.UN_SPECIFIED);
			}
			
			if (Validator.isNull(myResult.getCurrency_())) {
				myResult.setCurrency_(IConstants.UN_SPECIFIED);
			}
			
			if (Validator.isNull(myResult.getIndustry_sector())) {
				myResult.setIndustry_sector(IConstants.UN_SPECIFIED);
			}
						
			if (allocationBy != IConstants.BREAKUP_BY_CURRENCY) {
				setCategoryFields(myResult, allocationBy);
			}
			
			calculateIncome(myResult);
			
		}
				
		return myResults;
	}

	private void calculateIncome(MyResult myResult) {
		long assetId = myResult.getAssetId();
		double income = 0l;
		try {
			List<Dividend> dividends = dividendPersistence.findByAssetId(assetId);
			for(Dividend dividend : dividends) {
				if(Validator.isNotNull(dividend.getPayableDate()) && Validator.isNotNull(myResult.getPurchaseDate())) {
					if(myResult.getPurchaseDate().after(dividend.getPayableDate())) {
						income += dividend.getAmount();
					}
				}
			}
			// Coupon
			
			
		} catch (SystemException e) {
			e.printStackTrace();
		}
		myResult.setIncome(income);
	}

	private void setCategoryFields(MyResult myResult, int allocationBy) {
				
		long assetId = myResult.getAssetId();
		
		long entryId = 0l;
		
		try {
			AssetEntry assetEntry = AssetEntryLocalServiceUtil.fetchEntry(Asset.class.getName(), assetId);
			entryId = assetEntry.getEntryId();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (entryId == 0l) return;
		
		List<AssetCategory> assetCategories = null;
		try {
			assetCategories = AssetCategoryLocalServiceUtil.getAssetEntryAssetCategories(entryId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(assetCategories)) return;
		
		for (AssetCategory assetCategory: assetCategories) {		
			
			String vocabularyName = StringPool.BLANK;
			try {
				AssetVocabulary assetVocabulary = AssetVocabularyLocalServiceUtil.fetchAssetVocabulary(assetCategory.getVocabularyId());
				vocabularyName = assetVocabulary.getName();
			} catch (SystemException e) {
				e.printStackTrace();
			}		
			
			if (vocabularyName.equalsIgnoreCase("BB_Security") && (allocationBy == IConstants.BREAKUP_BY_SECURITY_CLASS || allocationBy == IConstants.BREAKUP_BY_RISK_COUNTRY)) {				
				myResult.setSecurity_typ(assetCategory.getName());
				
				try {
					AssetCategory parentCategory = AssetCategoryLocalServiceUtil.fetchAssetCategory(assetCategory.getParentCategoryId());
					myResult.setSecurity_class(parentCategory.getName());
				} catch (SystemException e) {
					e.printStackTrace();
				}
			} else if (vocabularyName.equalsIgnoreCase("BB_Industry") && (allocationBy == IConstants.BREAKUP_BY_INDUSTRY_SECTOR)) {
				myResult.setSecurity_class(assetCategory.getName());
				
				try {
					AssetCategory industrySector = AssetCategoryLocalServiceUtil.fetchAssetCategory(assetCategory.getParentCategoryId());
					myResult.setIndustry_sector(industrySector.getName());
				} catch (SystemException e) {
					e.printStackTrace();
				}				
			} else if (vocabularyName.equalsIgnoreCase("BB_Asset_Class") && (allocationBy == IConstants.BREAKUP_BY_ASSET_CLASS)) {
				
				AssetCategory grantParentCategory = null;
				AssetCategory parentCategory = null;
				
				try {
					parentCategory = AssetCategoryLocalServiceUtil.fetchAssetCategory(assetCategory.getParentCategoryId());
					
					if (parentCategory.getParentCategoryId() > 0l) {
						grantParentCategory = AssetCategoryLocalServiceUtil.fetchAssetCategory(parentCategory.getParentCategoryId());
						myResult.setSecurity_class(grantParentCategory.getName());
						myResult.setAsset_class(parentCategory.getName());
						myResult.setAsset_sub_class(assetCategory.getName());
					} else {
						myResult.setSecurity_class(parentCategory.getName());
						myResult.setAsset_class(assetCategory.getName());
					}
				} catch (SystemException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public List<MyResult> getBondsByMaturity(String bucketName, String portfolioIds) {
		
		String[] tokens = {"[$PORTFOLIO_IDS$]", "[$FING_BOND_COLUMNS$]", "[$FING_BOND_TABLE$]", "[$FING_BOND_WHERE_CLAUSE$]"};
		
		int[][] ranges = {
			{210, 365},
			{366, 730},
			{731, 1825},
			{1826, 3650},
			{3651, 10000}
		};
				
		int index = -1; 
		for (int i=0; i<bucketNames.length; i++) {
			if (bucketName.equalsIgnoreCase(bucketNames[i])) {
				index = i;
				break;
			}
		}
				
		StringBuilder sb = new StringBuilder();
		sb.append(" and a.assetId = f.assetId");
		sb.append(" and DATEDIFF(f.maturity_dt,now()) between ").append(ranges[index][0]).append(" and ").append(ranges[index][1]);
		String[] replacements = {portfolioIds, ",f.*, DATEDIFF(f.maturity_dt,now()) AS maturing_after", ",fing_Bond f", sb.toString()};
		
		List<MyResult> myResults = myResultFinder.findResults(portfolioIds, tokens, replacements);
		
		return myResults;
	}
	
	public List<MyResult> getBondsByCollateral(String bucketName, String portfolioIds) {
		
		String[] tokens = {"[$PORTFOLIO_IDS$]", "[$FING_BOND_COLUMNS$]", "[$FING_BOND_TABLE$]", "[$FING_BOND_WHERE_CLAUSE$]"};
		
		StringBuilder sb = new StringBuilder();
		sb.append(" and a.assetId = f.assetId");
		sb.append(" and f.collat_typ = '").append(bucketName).append("'");
		String[] replacements = {portfolioIds, ",f.*", ",fing_Bond f", sb.toString()};
		
		List<MyResult> myResults = myResultFinder.findResults(portfolioIds, tokens, replacements);
		
		return myResults;
	}	
	
	public List<MyResult> getBondsByYldToMaturity(String index, String portfolioIds) {
		
		String[] tokens = {"[$PORTFOLIO_IDS$]", "[$FING_BOND_COLUMNS$]", "[$FING_BOND_TABLE$]", "[$FING_BOND_WHERE_CLAUSE$]"};
		
		StringBuilder sb = new StringBuilder();
		sb.append(" and a.assetId = f.assetId");
		
		String[] parts = index.split(StringPool.COLON);
		int i = Integer.parseInt(parts[0]);
		int j = Integer.parseInt(parts[1]);
		
		sb.append(" and yld_ytm_bid between ").append(yldToMaturityRange[i][0]).append(" and ").append(yldToMaturityRange[i][1]);
		sb.append(" and dur_mid between ").append(durationRange[j][0]).append(" and ").append(durationRange[j][1]);
		
		String[] replacements = {portfolioIds, ",f.*", ",fing_Bond f", sb.toString()};
		
		List<MyResult> myResults = myResultFinder.findResults(portfolioIds, tokens, replacements);
		
		return myResults;
	}
	
	public List<MyResult> getBondsByCpnTypVsMtyTyp(String combo, String portfolioIds) {
		
		String[] tokens = {"[$PORTFOLIO_IDS$]", "[$FING_BOND_COLUMNS$]", "[$FING_BOND_TABLE$]", "[$FING_BOND_WHERE_CLAUSE$]"};
		
		StringBuilder sb = new StringBuilder();
		sb.append(" and a.assetId = f.assetId");
		
		String[] parts = combo.split(StringPool.COLON);
		String mtyTyp = parts[0];
		String cpnTyp = parts[1];
		
		sb.append(" and mty_typ ='").append(mtyTyp).append("'");
		sb.append(" and cpn_typ ='").append(cpnTyp).append("'");
		
		String[] replacements = {portfolioIds, ",f.*", ",fing_Bond f", sb.toString()};
		
		List<MyResult> myResults = myResultFinder.findResults(portfolioIds, tokens, replacements);
		
		return myResults;
	}	
	
	public List<MyResult> getBondsByQuality(String bucketName, String portfolioIds) {
		
		List<MyResult> myResults = myResultFinder.findResults(portfolioIds);
		
		List<MyResult> results = new ArrayList<MyResult>();
		
		for (MyResult myresult: myResults) {
			// get the bond
			Bond bond = null;
			try {
				bond = bondPersistence.fetchByPrimaryKey(myresult.getAssetId());
			} catch (SystemException e) {
				e.printStackTrace();
			}
			
			if (Validator.isNull(bond)) continue;
			
			// get the rating
			Rating rating = null;
			try {
				rating = ratingPersistence.fetchBySP_Moody(bond.getRtg_sp(), bond.getRtg_moody());
			} catch (SystemException e) {
				e.printStackTrace();
			}
						
			if (Validator.isNotNull(rating) && rating.getDescription().equalsIgnoreCase(bucketName)) {
				results.add(myresult);
			} else if (Validator.isNull(rating) && bucketName.equalsIgnoreCase("No Rating Available")) {
				results.add(myresult);
			}
		}
		
		return results;
	}

	public JSONArray getCollateralBreakdown(String portfolioIds) {
		
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();
		
		Connection conn = null;
		try {
			conn = DataAccess.getConnection();
			
			String[] tokens = {"[$PORTFOLIO_IDS$]", "[$FING_BOND_COLUMNS$]", "[$FING_BOND_TABLE$]", "[$FING_BOND_WHERE_CLAUSE$]"};
			String[] replacements = {portfolioIds, ",f.*, DATEDIFF(f.maturity_dt,now()) AS maturing_after", ",fing_Bond f", "and a.assetId = f.assetId"};
					
			String sql = StringUtil.replace(CustomSQLUtil.get(QUERY), tokens, replacements);
			
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			double totalMarketValue = getTotalMarketValue(portfolioIds);
			double totalValueOfBonds = 0.0;
						
			while (rs.next()) {
				String collatTyp = rs.getString("collat_typ");
				double currentMarketValue = rs.getDouble("currentMarketValue");
				totalValueOfBonds += currentMarketValue;
				
				JSONObject jsonObj = null;
				if (jsonArray.length() > 0) {
					for (int i=0; i<jsonArray.length(); i++) {
						JSONObject _jsonObj = jsonArray.getJSONObject(i);
						if (_jsonObj.getString("bucket").equalsIgnoreCase(collatTyp)) {
							jsonObj = _jsonObj;
							break;
						}
					}									
				} 
				
				if (Validator.isNull(jsonObj)) {
					jsonObj = JSONFactoryUtil.createJSONObject();
					jsonObj.put("bucket", collatTyp);
					jsonObj.put("market_value", 0.0);
					jsonObj.put("bond_holdings_percent", 0.0);
					jsonObj.put("total_holdings_percent", 0.0);
					jsonArray.put(jsonObj);	
				}
				
				jsonObj.put("market_value", jsonObj.getDouble("market_value") + currentMarketValue);
				jsonObj.put("total_holdings_percent", jsonObj.getDouble("total_holdings_percent") + currentMarketValue*100/totalMarketValue);
			}
			
			rs.close();
			stmt.close();
			
			for (int i=0; i<jsonArray.length(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				jsonObj.put("bond_holdings_percent", jsonObj.getDouble("market_value")*100/totalValueOfBonds);
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataAccess.cleanUp(conn);
		}
		
		return jsonArray;		
	}
	
	public JSONArray getCashFlow(String portfolioIds) {
		
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();
		
		Connection conn = null;
		try {
			conn = DataAccess.getConnection();
			
			String[] tokens = {"[$PORTFOLIO_IDS$]", "[$FING_BOND_COLUMNS$]", "[$FING_BOND_TABLE$]", "[$FING_BOND_WHERE_CLAUSE$]"};
			String[] replacements = {portfolioIds, ",b.portfolioName ,f.*", ", fing_Bond f", "and a.assetId = f.assetId"};
			
			String sql = StringUtil.replace(CustomSQLUtil.get(QUERY), tokens, replacements);
			
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				String portfolioName = rs.getString("portfolioName");
				
				Date date = new Date();
				double calcTyp = rs.getDouble("calc_typ");
				String collatTyp = rs.getString("collat_typ");
				String isBondNoCalcTyp = rs.getString("is_bond_no_calctyp");
				if (calcTyp > 0.0d && Validator.isNotNull(collatTyp) && Validator.isNotNull(isBondNoCalcTyp)) {
					Date issueDate = rs.getDate("issue_dt");
					List<History> histories = null;
					try {
						//System.out.println("Entered If, assetId :: " + rs.getLong("assetId"));
						OrderByComparator comparator = OrderByComparatorFactoryUtil.create(HistoryModelImpl.TABLE_NAME, "logDate", true);
						histories = historyPersistence.findByAssetId_Type(rs.getLong("assetId"), 3, 0, historyPersistence.countByAssetId(rs.getLong("assetId")), comparator);
						
						for(History history:histories) {
							//System.out.println("Asset Id :: " + rs.getLong("assetId") + ", History Log Date :: " + history.getLogDate() + ", issueDate :: " + issueDate);
						}
						
						//date = history.getLogDate();
					} catch (SystemException e) {
						e.printStackTrace();
					}
				} else {
					//date = rs.getDate("purchaseDate");
					//System.out.println("Entered Else");
				}
				
				// if (calcTyp > 0.0d && Validator.isNotNull(collatTyp) && Validator.isNotNull(isBondNoCalcTyp)) {
				//    History
				//    Query with assetId and Type = 3 (Cash Flow Type)
				//    First record after issue date
				// }
				// Non- Bond
				// Query dividend table with asset ID
				// get first which is after the purchaseDate
				
				String nameSecurityDes = rs.getString("name") + " " + rs.getString("security_des");
				String idIsin = rs.getString("id_isin");
				double purchaseQty = rs.getDouble("purchaseQty");
				double most_recent_reported_factor = rs.getDouble("amount_outstanding") / rs.getDouble("amount_issued");
				double amountOutstanding = purchaseQty * most_recent_reported_factor;
				//String transaction = rs.getString("");
				double cashFlow = amountOutstanding * (rs.getDouble("cpn") / (rs.getDouble("cpn_freq") * 100) );
				String currency = rs.getString("currencyDesc");
				String currencySymbol = rs.getString("currencySymbol");
				double currencyConversionRate = rs.getDouble("current_fx");
				double cashFlowCurrency = cashFlow * currencyConversionRate;
				
				JSONObject jsonObj = JSONFactoryUtil.createJSONObject();;
				jsonObj.put("portfolioName", portfolioName);
				jsonObj.put("date", date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getYear());
				jsonObj.put("nameSecurityDes", nameSecurityDes);
				jsonObj.put("securityID", idIsin);
				jsonObj.put("purchaseQty", purchaseQty);
				jsonObj.put("amountOutstanding", amountOutstanding);
				jsonObj.put("transaction", "");
				jsonObj.put("cashFlow", cashFlow);
				jsonObj.put("currency", currency);
				jsonObj.put("currencySymbol", currencySymbol);
				jsonObj.put("currencyConversionRate", currencyConversionRate);
				jsonObj.put("cashFlowCurrency", cashFlowCurrency);
				
				jsonArray.put(jsonObj);
			}
			
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataAccess.cleanUp(conn);
		}
		
		return jsonArray;		
	}
	
	public JSONArray getBondsMaturing(String portfolioIds) {
		
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();
		
		// initialization of JSONArray with default values
		for (int i=0; i<bucketNames.length; i++) {
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
			jsonObject.put("bucket", bucketNames[i]);
			jsonObject.put("market_value", 0.0);
			jsonObject.put("bond_holdings_percent", 0.0);
			jsonObject.put("total_holdings_percent", 0.0);
			jsonArray.put(jsonObject);
		}
		
		Connection conn = null;
		try {
			conn = DataAccess.getConnection();
			
			String[] tokens = {"[$PORTFOLIO_IDS$]", "[$FING_BOND_COLUMNS$]", "[$FING_BOND_TABLE$]", "[$FING_BOND_WHERE_CLAUSE$]"};
			String[] replacements = {portfolioIds, ",f.*, DATEDIFF(f.maturity_dt,now()) AS maturing_after", ",fing_Bond f", "and a.assetId = f.assetId"};
					
			String sql = StringUtil.replace(CustomSQLUtil.get(QUERY), tokens, replacements);
			
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			double totalMarketValue = getTotalMarketValue(portfolioIds);
			double totalValueOfBonds = 0.0;
						
			while (rs.next()) {
				int maturingAfter = rs.getInt("maturing_after");
				double currentMarketValue = rs.getDouble("currentMarketValue");
				totalValueOfBonds += currentMarketValue;
				
				if (maturingAfter < 210) continue; // less than 7 months
				
				int index = 0;
				if (maturingAfter > 366 && maturingAfter < 730) {
					index = 1;
				} else if (maturingAfter > 731 && maturingAfter < 1825) {
					index = 2;
				} else if (maturingAfter > 1826 && maturingAfter < 3650) {
					index = 3;
				} else if (maturingAfter > 3651) {
					index = 4;
				}
				
				JSONObject jsonObj = jsonArray.getJSONObject(index);
				
				jsonObj.put("market_value", jsonObj.getDouble("market_value") + currentMarketValue);
				jsonObj.put("total_holdings_percent", jsonObj.getDouble("total_holdings_percent") + currentMarketValue*100/totalMarketValue);
			}
			
			rs.close();
			stmt.close();
			
			for (int i=0; i<jsonArray.length(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				jsonObj.put("bond_holdings_percent", jsonObj.getDouble("market_value")*100/totalValueOfBonds);
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataAccess.cleanUp(conn);
		}
		
		return jsonArray;		
	}
	
	public JSONArray getYldToMaturity(String portfolioIds) {
		
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();
				
		for (int i=0; i<yldToMaturityRange.length; i++) {
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
			if (i<=5) {
				jsonObject.put("yldToMaturityRange", yldToMaturityRange[i][0] + StringPool.DASH + yldToMaturityRange[i][1]);
			} else {
				jsonObject.put("yldToMaturityRange", yldToMaturityRange[i][0] + StringPool.PLUS);
			}
			for (int j=0; j<durationRange.length; j++) {
				jsonObject.put((int)durationRange[j][0] + StringPool.DASH + (int)durationRange[j][1] + " Yrs", 0.0d);
				jsonObject.put("index"+j, (i + StringPool.COLON + j));
			}
			jsonArray.put(jsonObject);
		}
		
		Connection conn = null;
		try {
			conn = DataAccess.getConnection();
			
			String[] tokens = {"[$PORTFOLIO_IDS$]", "[$FING_BOND_COLUMNS$]", "[$FING_BOND_TABLE$]", "[$FING_BOND_WHERE_CLAUSE$]"};
			String[] replacements = {portfolioIds, ",f.*", ",fing_Bond f", "and a.assetId = f.assetId"};
					
			String sql = StringUtil.replace(CustomSQLUtil.get(QUERY), tokens, replacements);
			
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			double totalValueOfBonds = 0.0;
						
			while (rs.next()) {
				double dur_mid = rs.getDouble("dur_mid");
				double yld_ytm_bid = rs.getDouble("yld_ytm_bid");
				
				double currentMarketValue = rs.getDouble("currentMarketValue");
				totalValueOfBonds += currentMarketValue;
				
				for (int i=0; i<yldToMaturityRange.length; i++) {
					if (yld_ytm_bid > yldToMaturityRange[i][0] && yld_ytm_bid <= yldToMaturityRange[i][1]) {
						JSONObject jsonObj = jsonArray.getJSONObject(i);
						for (int j=0; j<durationRange.length; j++) {
							if (dur_mid > durationRange[j][0] && dur_mid <= durationRange[j][1]) {
								String key = (int)durationRange[j][0] + StringPool.DASH + (int)durationRange[j][1] + " Yrs";
								jsonObj.put(key, jsonObj.getDouble(key) + currentMarketValue);
							}
						}
					}
				}
			}
			
			rs.close();
			stmt.close();
			
			for (int i=0; i<yldToMaturityRange.length; i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				for (int j=0; j<durationRange.length; j++) {
					String key = (int)durationRange[j][0] + StringPool.DASH + (int)durationRange[j][1] + " Yrs";
					jsonObj.put(key, jsonObj.getDouble(key)*100/totalValueOfBonds);
				}
			}
			
			// append a summary row
			JSONObject summary = JSONFactoryUtil.createJSONObject();
			summary.put("summary", true);
			summary.put("yldToMaturityRange", "Total");
			for (int i=0; i<yldToMaturityRange.length; i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				for (int j=0; j<durationRange.length; j++) {
					String key = (int)durationRange[j][0] + StringPool.DASH + (int)durationRange[j][1] + " Yrs";
					if (Double.isNaN(summary.getDouble(key))) {
						summary.put(key, jsonObj.getDouble(key));
					} else {
						summary.put(key, summary.getDouble(key) + jsonObj.getDouble(key));
					}
				}
			}
			
			jsonArray.put(summary);
				
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataAccess.cleanUp(conn);
		}
		
		return jsonArray;		
	}
	
	public JSONArray getCpnTypVsMtyTyp(String portfolioIds) {
		
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();
		
		List<String> cpnTypes = getDistinctValues("cpn_typ", portfolioIds);
		List<String> mtyTypes = getDistinctValues("mty_typ", portfolioIds);
		
		for (String cpnType: cpnTypes) {
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
			
			jsonObject.put("cpnType", cpnType);
			int i=0;
			for (String mtyType: mtyTypes) {
				jsonObject.put(mtyType, 0.0d);
				jsonObject.put(cpnType + ++i, 
					mtyType + StringPool.COLON + cpnType);
				
			}
			jsonObject.put("grandTotal", 0.0d);
			
			jsonArray.put(jsonObject);
		}
		
		Connection conn = null;
		try {
			conn = DataAccess.getConnection();
			
			String[] tokens = {"[$PORTFOLIO_IDS$]", "[$FING_BOND_COLUMNS$]", "[$FING_BOND_TABLE$]", "[$FING_BOND_WHERE_CLAUSE$]"};
			String[] replacements = {portfolioIds, ",f.*", ",fing_Bond f", "and a.assetId = f.assetId"};
					
			String sql = StringUtil.replace(CustomSQLUtil.get(QUERY), tokens, replacements);
						
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			double totalValueOfBonds = 0.0;
						
			while (rs.next()) {
				String cpn_typ = rs.getString("cpn_typ");
				String mty_typ = rs.getString("mty_typ");
				
				double currentMarketValue = rs.getDouble("currentMarketValue");
				totalValueOfBonds += currentMarketValue;
				
				for (int i=0; i<cpnTypes.size(); i++) {
					if (cpn_typ.equalsIgnoreCase(cpnTypes.get(i))) {
						JSONObject jsonObj = jsonArray.getJSONObject(i);
						for (String mtyType: mtyTypes) {
							if (mtyType.equalsIgnoreCase(mty_typ)) {
								jsonObj.put(mtyType, jsonObj.getDouble(mtyType) + currentMarketValue);
							}
						}
					}
				}
			}
			
			rs.close();
			stmt.close();
			
			for (int i=0; i<cpnTypes.size(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				for (String mtyType: mtyTypes) {
					jsonObj.put(mtyType, jsonObj.getDouble(mtyType)*100/totalValueOfBonds);
					if (Double.isNaN(jsonObj.getDouble("grandTotal"))) {
						jsonObj.put("grandTotal", jsonObj.getDouble(mtyType));
					} else {
						jsonObj.put("grandTotal", jsonObj.getDouble(mtyType) + jsonObj.getDouble("grandTotal"));
					}
				}
			}
			
			// append a summary row
			JSONObject summary = JSONFactoryUtil.createJSONObject();
			summary.put("summary", true);
			summary.put("cpnType", "Grand Total");
			for (int i=0; i<cpnTypes.size(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				for (String mtyType: mtyTypes) {
					if (Double.isNaN(summary.getDouble(mtyType))) {
						summary.put(mtyType, jsonObj.getDouble(mtyType));
					} else {
						summary.put(mtyType, summary.getDouble(mtyType) + jsonObj.getDouble(mtyType));
					}
					
					if (Double.isNaN(summary.getDouble("grandTotal"))) {
						summary.put("grandTotal", summary.getDouble(mtyType));
					} else {
						summary.put("grandTotal", summary.getDouble(mtyType) + summary.getDouble("grandTotal"));
					}
				}
			}
			
			jsonArray.put(summary);
				
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataAccess.cleanUp(conn);
		}
		
		return jsonArray;		
	}	
	
	public JSONArray getBondsQuality(String portfolioIds) {
		
		String[] categories = {"Investment", "Non Investment", "Others"};
		
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();
		
		// initialization of JSONArray with default values
		for (int i=0; i<categories.length; i++) {
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
			jsonObject.put("category", categories[i]);
			jsonObject.put("children", JSONFactoryUtil.createJSONArray());
			jsonArray.put(jsonObject);
		}
		
		Connection conn = null;
		try {
			conn = DataAccess.getConnection();
			
			String[] tokens = {"[$PORTFOLIO_IDS$]", "[$FING_BOND_COLUMNS$]", "[$FING_BOND_TABLE$]", "[$FING_BOND_WHERE_CLAUSE$]"};
			String[] replacements = {portfolioIds, ",f.*", ",fing_Bond f", "and a.assetId = f.assetId"};
					
			String sql = StringUtil.replace(CustomSQLUtil.get(QUERY), tokens, replacements);
			
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			double totalMarketValue = getTotalMarketValue(portfolioIds);
			double totalValueOfBonds = 0.0;
						
			while (rs.next()) {
				double currentMarketValue = rs.getDouble("currentMarketValue");
				totalValueOfBonds += currentMarketValue;
				
				String spRating = rs.getString("rtg_sp");
				String moodyRating = rs.getString("rtg_moody");
								
				Rating rating = null;
				try {
					rating = ratingPersistence.fetchBySP_Moody(spRating, moodyRating);
				} catch (SystemException e) {
					e.printStackTrace();
				}
				
				// identify the object 
				int index = 2;
				String description = "No Rating Available";
				if (Validator.isNotNull(rating)) {
					String category = rating.getCategory();
					description = rating.getDescription();
					
					for (int i=0; i<categories.length; i++) {
						if (categories[i].equalsIgnoreCase(category)) {
							index = i;
						}
					}
				}
				
				JSONArray children = jsonArray.getJSONObject(index).getJSONArray("children");
				
				// identify the child within the parent
				JSONObject child = null;
								
				if (children.length() == 0) {
					child = JSONFactoryUtil.createJSONObject();
					child.put("bucket", description);
					child.put("market_value", 0.0);
					child.put("bond_holdings_percent", 0.0);
					child.put("total_holdings_percent", 0.0);					
					children.put(child);
				} 
								
				for (int i=0; i<children.length(); i++) {
					child = children.getJSONObject(i);
					if (child.getString("bucket").equalsIgnoreCase(description)) {
						child.put("market_value", child.getDouble("market_value") + currentMarketValue);
						child.put("total_holdings_percent", child.getDouble("total_holdings_percent") + currentMarketValue*100/totalMarketValue);
					}
				}
			}
			
			rs.close();
			stmt.close();
			
			for (int i=0; i<jsonArray.length(); i++) {
				JSONObject parent = jsonArray.getJSONObject(i);
				JSONArray children = parent.getJSONArray("children");
								
				for (int j=0; j<children.length(); j++) {
					JSONObject child = children.getJSONObject(j);
					child.put("bond_holdings_percent", child.getDouble("market_value")*100/totalValueOfBonds);
				}					
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataAccess.cleanUp(conn);
		}
		
		return jsonArray;		
	}	
	
	public double getTotalMarketValue(String portfolioIds) {
		double totalMarketValue = 0.0;
		
		List<MyResult> myResults = myResultFinder.findResults(portfolioIds);
		
		for (MyResult myResult: myResults) {
			totalMarketValue += myResult.getCurrentMarketValue();
		}
		
		return totalMarketValue;
	}
	
	public List<String> getDistinctValues(String columnName, String portfolioIds) {
		
		List<String> distinctValues = new ArrayList<String>();
		Connection conn = null;
		try {
			conn = DataAccess.getConnection();
			
			String[] tokens = {"[$COLUMN_NAME$]", "[$PORTFOLIO_IDS$]"};
			String[] replacements = {columnName, portfolioIds};
					
			String sql = StringUtil.replace(CustomSQLUtil.get(QUERY2), tokens, replacements);
			
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
						
			while (rs.next()) {
				distinctValues.add(rs.getString(1));	
			}
			
			rs.close();
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataAccess.cleanUp(conn);
		}
		return distinctValues;
	}
}