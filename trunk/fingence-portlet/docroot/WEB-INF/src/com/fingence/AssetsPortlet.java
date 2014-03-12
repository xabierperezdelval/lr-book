package com.fingence;

import java.io.File;
import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import com.fingence.slayer.service.AssetLocalServiceUtil;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class AssetsPortlet
 */
public class AssetsPortlet extends MVCPortlet {
 

	public void uploadAssets(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {

		UploadPortletRequest uploadPortletRequest = PortalUtil
				.getUploadPortletRequest(actionRequest);
		
		File excelFile = uploadPortletRequest.getFile("assetsMaster");
		long userId = PortalUtil.getUserId(uploadPortletRequest);
		AssetLocalServiceUtil.importFromExcel(userId, excelFile);
	}
}