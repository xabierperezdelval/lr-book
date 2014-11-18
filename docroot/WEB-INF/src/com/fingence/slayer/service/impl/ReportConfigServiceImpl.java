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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fingence.slayer.model.ReportConfig;
import com.fingence.slayer.service.ReportConfigLocalServiceUtil;
import com.fingence.slayer.service.base.ReportConfigServiceBaseImpl;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.ClassNameLocalServiceUtil;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;

/**
 * The implementation of the report config remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.fingence.slayer.service.ReportConfigService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.fingence.slayer.service.base.ReportConfigServiceBa
 * seImpl
 * @see com.fingence.slayer.service.ReportConfigServiceUtil
 */
public class ReportConfigServiceImpl extends ReportConfigServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.fingence.slayer.service.ReportConfigServiceUtil} to access the report config remote service.
	 */
	
	public JSONArray getTreeStructure(String title) {
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();
		long vocabularyId = AssetHelper.getVocabularyId(title);
				
		if (vocabularyId == 0l) return jsonArray;
		
		return generateJSON(vocabularyId, 0);	
	}
	
	private JSONArray generateJSON(long vocabularyId, long parentCategoryId) {
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();
		List<AssetCategory> assetCategories = null;
		try {
			assetCategories = AssetCategoryLocalServiceUtil.getVocabularyCategories(
					parentCategoryId, 
					vocabularyId, 0, AssetCategoryLocalServiceUtil.getAssetCategoriesCount(), null);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		for (AssetCategory assetCategory: assetCategories) {
			
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
			jsonObject.put("label", assetCategory.getName());
			jsonObject.put("id",String.valueOf(assetCategory.getCategoryId()));
			jsonObject.put("expanded", true);
			jsonObject.put("leaf", true);
			jsonObject.put("type", "task");
			
			ReportConfig reportConfig = null;
			try {
				reportConfig = reportConfigPersistence
						.fetchByReportId_ClassPK_ClassNameId_Enabled(
								assetCategory.getCategoryId(), 
								AssetHelper.getGuestGroupId(),
								ClassNameLocalServiceUtil.getClassNameId(Group.class), true);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			
			jsonObject.put("checked", Validator.isNotNull(reportConfig));
			jsonObject.put("children", generateJSON(vocabularyId, assetCategory.getCategoryId()));
			jsonArray.put(jsonObject);
		}

		return jsonArray;
	}
	
	public ReportConfig getReportConfig(long reportId) {
		ReportConfig reportConfig = null;
		try {
			reportConfig = reportConfigPersistence
					.fetchByReportId_ClassPK_ClassNameId(
							reportId, 
							AssetHelper.getGuestGroupId(),
							ClassNameLocalServiceUtil.getClassNameId(Group.class));
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return reportConfig;
	}
	
	public ReportConfig setReportConfig(long reportId, boolean enabled) {		
		ReportConfig reportConfig = null;
		try {
			reportConfig = reportConfigPersistence
					.fetchByReportId_ClassPK_ClassNameId(
							reportId, 
							AssetHelper.getGuestGroupId(),
							ClassNameLocalServiceUtil.getClassNameId(Group.class));
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(reportConfig)) {
			long recId = 0l;
			try {
				recId = counterLocalService.increment(ReportConfig.class.getName());
			} catch (SystemException e) {
				e.printStackTrace();
			}
			reportConfig = ReportConfigLocalServiceUtil.createReportConfig(recId);
			reportConfig.setReportId(reportId);
			reportConfig.setClassPK(AssetHelper.getGuestGroupId());
			reportConfig.setClassNameId(ClassNameLocalServiceUtil.getClassNameId(Group.class));
			try {
				reportConfig = reportConfigLocalService.addReportConfig(reportConfig);
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		
		if (reportConfig.isEnabled() != enabled) {
			reportConfig.setEnabled(enabled);
			try {
				reportConfig = reportConfigLocalService.updateReportConfig(reportConfig);
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		
		return reportConfig;
	}
	
	public Map<Long, String> getMenuItems(long parentCategoryId) {
		Map<Long, String> menuItems = new HashMap<Long, String>();
		List<AssetCategory> assetCategories = null;
		try {
			assetCategories = AssetCategoryLocalServiceUtil.getChildCategories(parentCategoryId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		for (AssetCategory assetCategory : assetCategories) {
			ReportConfig reportConfig = getReportConfig(assetCategory.getCategoryId());
			if(Validator.isNotNull(reportConfig)) {
				if(reportConfig.getEnabled()) {
					menuItems.put(assetCategory.getCategoryId(), assetCategory.getName());
				}
			}
		}
		return menuItems;
	}
}