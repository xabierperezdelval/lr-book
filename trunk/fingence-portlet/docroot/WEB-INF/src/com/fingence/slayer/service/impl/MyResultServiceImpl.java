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
import java.util.List;

import com.fingence.IConstants;
import com.fingence.slayer.model.Asset;
import com.fingence.slayer.model.Bond;
import com.fingence.slayer.model.MyResult;
import com.fingence.slayer.model.Rating;
import com.fingence.slayer.service.CurrencyServiceUtil;
import com.fingence.slayer.service.base.MyResultServiceBaseImpl;
import com.fingence.slayer.service.persistence.MyResultFinderImpl;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
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
	static String[] bucketNames = {"7 to 12 Months", "1 to 2 Years", "2 to 5 Years", "5 to 10 Years", "More than 10 Years"};
	
	public List<MyResult> getMyResults(String portfolioIds) {
				
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
			
			if (Validator.isNull(myResult.getSecurity_class())) {
				myResult.setSecurity_class(IConstants.UN_SPECIFIED);
			}
			
			if (Validator.isNull(myResult.getIndustry_sector())) {
				myResult.setIndustry_sector(IConstants.UN_SPECIFIED);
			}
			
			setCategoryFields(myResult);
		}
				
		return myResults;
	}
	
	private void setCategoryFields(MyResult myResult) {
		
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
			
			if (vocabularyName.equalsIgnoreCase("BB_Security")) {
				myResult.setSecurity_typ(assetCategory.getName());
				
				try {
					AssetCategory parentCategory = AssetCategoryLocalServiceUtil.fetchAssetCategory(assetCategory.getParentCategoryId());
					myResult.setSecurity_class(parentCategory.getName());
				} catch (SystemException e) {
					e.printStackTrace();
				}
			} else if (vocabularyName.equalsIgnoreCase("BB_Industry")) {
				myResult.setIndustry_subgroup(assetCategory.getName());
				
				try {
					AssetCategory industryGroup = AssetCategoryLocalServiceUtil.fetchAssetCategory(assetCategory.getParentCategoryId());
					myResult.setIndustry_group(industryGroup.getName());
					
					AssetCategory industrySector = AssetCategoryLocalServiceUtil.fetchAssetCategory(industryGroup.getParentCategoryId());
					myResult.setIndustry_sector(industrySector.getName());
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
			{3651, 10000},
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
			
			if (Validator.isNull(rating)) continue;
			
			if (rating.getDescription().equalsIgnoreCase(bucketName)) {
				results.add(myresult);
			}
		}
		
		return results;
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
}