package com.fingence.hook;

import java.util.List;

import com.fingence.slayer.service.impl.AssetHelper;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetVocabulary;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil;
import com.liferay.util.portlet.PortletProps;

public class CreateReportTypes extends SimpleAction {
	public CreateReportTypes() {
		super();
	}

	public void run(String[] arg0) throws ActionException {
		ServiceContext serviceContext = new ServiceContext();
		serviceContext.setScopeGroupId(AssetHelper.getGuestGroupId());
		long companyId = PortalUtil.getDefaultCompanyId(); // Long.parseLong(arg0[0])
		long defaultUserId = 0l;
		try {
			defaultUserId = UserLocalServiceUtil.getDefaultUserId(companyId);
		} catch (PortalException | SystemException e1) {
			e1.printStackTrace();
		}
		
		long vocabularyId = 0l;
		String vocabularyName = PortletProps.get("report.types.vocabulary.name");
	
		vocabularyId = checkTreeVocabulary(vocabularyName);
		
		if (vocabularyId == 0l) {
			vocabularyId = createTreeVocabulary(vocabularyName, defaultUserId, serviceContext);
		}
		
		String[] parentReportNames = PortletProps.getArray("report.types.parent.categories");
		for (int i = 0; i < parentReportNames.length; i++) {
			long treeItemId = 0l;
			treeItemId = checkTreeItem(vocabularyId, parentReportNames[i]);
			
			if (treeItemId == 0l) {
				treeItemId = createTreeItem(vocabularyId, 0, parentReportNames[i], defaultUserId, serviceContext);
			}
			
			String childReportPath = "report.types.children." + parentReportNames[i].toLowerCase().trim().replace(" ", ".");
			String[] childReportNames = PortletProps.getArray(childReportPath);
			
			for (int j = 0; j < childReportNames.length; j++) {
				long treeChildItemId = 0l;
				treeChildItemId = checkTreeItem(vocabularyId, childReportNames[j]);
				
				if (treeChildItemId == 0l) {
					treeChildItemId = createTreeItem(vocabularyId, treeItemId, childReportNames[j], defaultUserId, serviceContext);
				}
			}
		}
	}

	private long checkTreeVocabulary(String vocabularyName) {
		AssetVocabulary assetVocabulary = null;
		try {
			assetVocabulary = AssetVocabularyLocalServiceUtil.getGroupVocabulary(AssetHelper.getGuestGroupId(), vocabularyName);
		} catch (PortalException | SystemException e) {
			e.printStackTrace();
		}
		return assetVocabulary.getVocabularyId();
	}

	private long createTreeVocabulary(String vocabularyName, long defaultUserId, ServiceContext serviceContext) {
		AssetVocabulary vocabulary = null;
		try {
			vocabulary = AssetVocabularyLocalServiceUtil.addVocabulary(defaultUserId, vocabularyName, serviceContext);
		} catch (SystemException | PortalException e) {
			e.printStackTrace();
		}
		return vocabulary.getVocabularyId();
	}
	
	private long checkTreeItem(long vocabularyId, String itemName) {
		long assetCategoryId = 0l;
		List<AssetCategory> assetCategories = null;
		try {
			assetCategories = AssetCategoryLocalServiceUtil.getVocabularyCategories(vocabularyId, 0, AssetCategoryLocalServiceUtil.getAssetCategoriesCount(), null);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		for(AssetCategory assetCategory:assetCategories) {
			if(assetCategory.getName().equalsIgnoreCase(itemName)) {
				assetCategoryId = assetCategory.getCategoryId();
			}
		}
		return assetCategoryId;
	}
	
	private long createTreeItem(long vocabularyId, long parentItemId, String itemName, long defaultUserId, ServiceContext serviceContext) {
		AssetCategory assetCategory = null;
		try {
			assetCategory = AssetCategoryLocalServiceUtil.addCategory(defaultUserId, itemName, vocabularyId, serviceContext);
		} catch (SystemException | PortalException e) {
			e.printStackTrace();
		}
		assetCategory.setParentCategoryId(parentItemId);
		try {
			AssetCategoryLocalServiceUtil.updateAssetCategory(assetCategory);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return assetCategory.getCategoryId();
	}
	
}