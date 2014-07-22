package com.fingence.hook;

import java.util.List;

import com.fingence.slayer.service.impl.AssetHelper;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
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
		
		long companyId = Long.parseLong(arg0[0]);
		long defaultUserId = 0l;
		try {
			defaultUserId = UserLocalServiceUtil.getDefaultUserId(companyId);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}

		String vocabularyName = PortletProps.get("report.types.vocabulary.name");
		long vocabularyId = checkTreeVocabulary(defaultUserId, vocabularyName, serviceContext);
		
		String[] parentReportNames = PortletProps.getArray("report.types.parent.categories");
		for (int i = 0; i < parentReportNames.length; i++) {
			long treeItemId = 0l;
			treeItemId = checkTreeItem(vocabularyId, parentReportNames[i]);
			
			if (treeItemId == 0l) {
				treeItemId = createTreeItem(vocabularyId, 0, parentReportNames[i], defaultUserId, serviceContext);
			}
			
			String childReportPath = "report.types.children." + parentReportNames[i].toLowerCase().trim().replaceAll(StringPool.SPACE, StringPool.PERIOD);
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

	private long checkTreeVocabulary(long userId, String vocabularyName, ServiceContext serviceContext) {
		AssetVocabulary assetVocabulary = null;

		try {
			assetVocabulary = AssetVocabularyLocalServiceUtil.getGroupVocabulary(AssetHelper.getGuestGroupId(), vocabularyName);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}

		if (Validator.isNull(assetVocabulary)) {
			try {
				assetVocabulary = AssetVocabularyLocalServiceUtil.addVocabulary(userId, vocabularyName, serviceContext);
			} catch (SystemException | PortalException e) {
				e.printStackTrace();
			}			
		}
		
		return assetVocabulary.getVocabularyId();
	}
	
	private long checkTreeItem(long vocabularyId, String itemName) {
		long assetCategoryId = 0l;
		List<AssetCategory> assetCategories = null;
		try {
			assetCategories = AssetCategoryLocalServiceUtil.getVocabularyCategories(vocabularyId, 0, AssetCategoryLocalServiceUtil.getAssetCategoriesCount(), null);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		for (AssetCategory assetCategory:assetCategories) {
			if (assetCategory.getName().equalsIgnoreCase(itemName)) {
				assetCategoryId = assetCategory.getCategoryId();
				break;
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