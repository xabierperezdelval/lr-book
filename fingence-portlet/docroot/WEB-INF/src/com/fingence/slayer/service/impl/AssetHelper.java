package com.fingence.slayer.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;

import com.fingence.IConstants;
import com.fingence.slayer.model.Asset;
import com.fingence.slayer.model.Rating;
import com.fingence.slayer.service.RatingLocalServiceUtil;
import com.fingence.util.CellUtil;
import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Group;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetVocabulary;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil;

public class AssetHelper {
	
	public static long getVocabularyId(String title) {
		
		long vocabularyId = 0l;
		try {
			AssetVocabulary assetVocabulary = AssetVocabularyLocalServiceUtil.getGroupVocabulary(getGuestGroupId(), title);
			vocabularyId = assetVocabulary.getVocabularyId();
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return vocabularyId;
	}

	public static long getVocabularyId(long userId, String title, ServiceContext serviceContext) {
		
		long guestGroupId = getGuestGroupId();
		
		long originalScopeGroupId = serviceContext.getScopeGroupId();
		serviceContext.setScopeGroupId(guestGroupId);
		
		AssetVocabulary assetVocabulary = null;
		try {
			assetVocabulary = AssetVocabularyLocalServiceUtil.getGroupVocabulary(guestGroupId, title);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(assetVocabulary)) {
			try {
				assetVocabulary = AssetVocabularyLocalServiceUtil.addVocabulary(userId, title, serviceContext);
			} catch (PortalException e) {
				//e.printStackTrace();
			} catch (SystemException e) {
				//e.printStackTrace();
			}
		}
		
		serviceContext.setScopeGroupId(originalScopeGroupId);
		return assetVocabulary.getVocabularyId();
	}
	
	public static long getGroupId(String groupName) {
		
		long companyId = CompanyThreadLocal.getCompanyId();
		
		long guestGroupId = 0l;
		try {
			Group guestGroup = GroupLocalServiceUtil.getGroup(companyId, groupName);
			guestGroupId = guestGroup.getGroupId();
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return guestGroupId;
	}	
	
	public static long getGuestGroupId() {
		return getGroupId("Guest");
	}
	
	private static long getCategoryId(long userId, String name, ServiceContext serviceContext, long vocabularyId, long parentCategoryId) {
		
		if (Validator.isNull(name)) return 0l;
		
		long guestGroupId = getGuestGroupId();
		
		long originalScopeGroupId = serviceContext.getScopeGroupId();
		serviceContext.setScopeGroupId(guestGroupId);
		
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(AssetCategory.class, PortalClassLoaderUtil.getClassLoader());
		dynamicQuery.add(RestrictionsFactoryUtil.eq("vocabularyId", vocabularyId));
		dynamicQuery.add(RestrictionsFactoryUtil.eq("name", name));
		dynamicQuery.add(RestrictionsFactoryUtil.eq("parentCategoryId", parentCategoryId));
		dynamicQuery.add(RestrictionsFactoryUtil.eq("userId", userId));
		dynamicQuery.add(RestrictionsFactoryUtil.eq("groupId", guestGroupId));
		
		AssetCategory assetCategory = null;
		
		try {
			@SuppressWarnings("unchecked")
			List<AssetCategory> assetCategories = AssetCategoryLocalServiceUtil.dynamicQuery(dynamicQuery);
			
			if (Validator.isNotNull(assetCategories) && !assetCategories.isEmpty()) {
				assetCategory = assetCategories.get(0);
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(assetCategory)) {
			Map<Locale, String> titleMap = new HashMap<Locale, String>();
			titleMap.put(Locale.US, name);
			
			try {
				assetCategory = AssetCategoryLocalServiceUtil.addCategory(
						userId, parentCategoryId, titleMap, null, vocabularyId,
						null, serviceContext);
			} catch (PortalException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}
			
			serviceContext.setScopeGroupId(originalScopeGroupId);			
		}
		
		return assetCategory.getPrimaryKey();
	}	
	
	public static void assignCategories(Asset asset, long entryId, long userId, Row row,
			Map<String, Integer> columnNames, ServiceContext serviceContext,
			long bbSecurityVocabularyId, long bbIndustryVocabularyId, long bbAssetClassVocabularyId) {
		
		// Setting Security Class
		String securityClass = CellUtil.getString(row.getCell(columnNames.get("BPIPE_REFERENCE_SECURITY_CLASS")));
		String securityTyp = CellUtil.getString(row.getCell(columnNames.get("SECURITY_TYP")));
		String securityTyp2 = CellUtil.getString(row.getCell(columnNames.get("SECURITY_TYP2")));	
		
		if (securityClass.equalsIgnoreCase("FixedIncome")) {
			securityClass = "Fixed Income";
		}
		
		long securityClassId = getCategoryId(userId, securityClass, serviceContext, bbSecurityVocabularyId, 0l);
		long securityTypId = getCategoryId(userId, securityTyp, serviceContext, bbSecurityVocabularyId, securityClassId);
		
		if (Validator.isNotNull(securityTyp2) && !securityTyp2.equalsIgnoreCase(securityTyp)) {
			long securityTyp2Id = getCategoryId(userId, securityTyp2, serviceContext, bbSecurityVocabularyId, securityTypId);
		}
		
		if (securityTypId > 0l) {
			try {
				AssetCategoryLocalServiceUtil.addAssetEntryAssetCategory(entryId, securityTypId);
			} catch (SystemException e) {
				e.printStackTrace();
			}			
		}
		
		int assetType = asset.getSecurity_class();	
		
		// Setting Industry Sector
		String industrySector = StringPool.BLANK;
		
		switch (assetType) {
		case IConstants.SECURITY_CLASS_EQUITY:
		case IConstants.SECURITY_CLASS_FIXED_INCOME:
			industrySector = CellUtil.getString(row.getCell(columnNames.get("INDUSTRY_SECTOR")));
			break;
			
		case IConstants.SECURITY_CLASS_FUND:
			industrySector = CellUtil.getString(row.getCell(columnNames.get("INDUSTRY_SUBGROUP")));
			break;
		}
				
		long industrySectorId = getCategoryId(userId, industrySector, serviceContext, bbIndustryVocabularyId, 0l);
		long industryGroupId = getCategoryId(userId, securityClass, serviceContext, bbIndustryVocabularyId, industrySectorId);
		
		if (industryGroupId > 0l) {
			try {
				AssetCategoryLocalServiceUtil.addAssetEntryAssetCategory(entryId, industryGroupId);
			} catch (SystemException e) {
				e.printStackTrace();
			}			
		}
		
		// Setting Asset Class
		
		if (assetType > 0) {
			String assetClass = StringPool.BLANK;
			String assetSubClass = StringPool.BLANK;
			String assetSubClass2 = StringPool.BLANK;
			
			switch (assetType) {
			case IConstants.SECURITY_CLASS_EQUITY:
				assetClass = securityClass;
				assetSubClass = securityTyp2;
				break;
				
			case IConstants.SECURITY_CLASS_FIXED_INCOME:
				assetClass = securityClass;
				
				double calcTyp = CellUtil.getDouble(row.getCell(columnNames.get("CALC_TYP")));
				String collatTyp = CellUtil.getString(row.getCell(columnNames.get("COLLAT_TYP")));
				String isBondNoCalcTyp = CellUtil.getString(row.getCell(columnNames.get("IS_BOND_NO_CALCTYP")));
				
				if (calcTyp > 0.0d && Validator.isNotNull(collatTyp) && Validator.isNotNull(isBondNoCalcTyp)) {
					assetSubClass = "Bond";
					assetSubClass2 = "Not Rated";
					
					String bbComposite = CellUtil.getString(row.getCell(columnNames.get("BB_COMPOSITE")));
					if (Validator.isNotNull(bbComposite)) {
						Rating rating = RatingLocalServiceUtil.findByFitch(bbComposite);
						
						if (Validator.isNotNull(rating) && !rating.getDescription().equalsIgnoreCase("Not Rated")) {
							assetSubClass2 = rating.getDescription();
						}
					}
				} else if (Validator.isNull(assetSubClass)) {
					assetSubClass = "Non-Bond";
				}
								
				break;
				
			case IConstants.SECURITY_CLASS_FUND:
				assetClass = CellUtil.getString(row.getCell(columnNames.get("FUND_ASSET_CLASS_FOCUS")));
				assetSubClass = CellUtil.getString(row.getCell(columnNames.get("INDUSTRY_GROUP")));
				break;
			}
			
			if (Validator.isNotNull(assetClass)) {
				
				long assetClassId = getCategoryId(userId, assetClass, serviceContext, bbAssetClassVocabularyId, 0l);
				long assetSubClassId = getCategoryId(userId, assetSubClass, serviceContext, bbAssetClassVocabularyId, assetClassId);
				
				if (Validator.isNull(assetSubClass2)) {
					try {
						AssetCategoryLocalServiceUtil.addAssetEntryAssetCategory(entryId, assetSubClassId);
					} catch (SystemException e) {
						e.printStackTrace();
					}		
				} else {
					long assetSubClass2Id = getCategoryId(userId, assetSubClass2, serviceContext, bbAssetClassVocabularyId, assetSubClassId);
					try {
						AssetCategoryLocalServiceUtil.addAssetEntryAssetCategory(entryId, assetSubClass2Id);
					} catch (SystemException e) {
						e.printStackTrace();
					}					
				}
			}
		}
	}
	
	public static void assignBondCategory(long assetId, long entryId, long userId, Row row,
			Map<String, Integer> columnNames, ServiceContext serviceContext,
			long bondCPNVocabularyId) {
		
		String cpnType = CellUtil.getString(row.getCell(columnNames.get("CPN_TYP")));
		String mtyType = CellUtil.getString(row.getCell(columnNames.get("MTY_TYP")));
		
		long cpnTypeId = getCategoryId(userId, cpnType, serviceContext, bondCPNVocabularyId, 0l);
		long mtyTypeId = getCategoryId(userId, mtyType, serviceContext, bondCPNVocabularyId, cpnTypeId);
		
		if (mtyTypeId > 0l) {
			try {
				AssetCategoryLocalServiceUtil.addAssetEntryAssetCategory(entryId, mtyTypeId);
			} catch (SystemException e) {
				e.printStackTrace();
			}			
		}
	}	
	
	public static long updateAssetEntry(long assetId) {
		
		AssetEntry assetEntry = null;
		try {
			assetEntry = AssetEntryLocalServiceUtil.fetchEntry(Asset.class.getName(), assetId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(assetEntry)) {
			
			long entryId = 0l;
			try {
				entryId = CounterLocalServiceUtil.increment();
			} catch (SystemException e) {
				e.printStackTrace();
			}
			
			assetEntry = AssetEntryLocalServiceUtil.createAssetEntry(entryId);
			assetEntry.setClassName(Asset.class.getName());
			assetEntry.setClassPK(assetId);
			
			try {
				assetEntry = AssetEntryLocalServiceUtil.addAssetEntry(assetEntry);
			} catch (SystemException e) {
				e.printStackTrace();
			}	
		}
		
		return assetEntry.getEntryId();
	}
}