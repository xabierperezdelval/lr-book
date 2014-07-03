package com.fingence;

import java.io.File;
import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import com.fingence.slayer.service.AssetLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class AssetsPortlet
 */
public class AssetsPortlet extends MVCPortlet {
 
	public void uploadAssets(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		
		System.out.println("inside uploadAssets....");
		
		UploadPortletRequest uploadPortletRequest = PortalUtil
				.getUploadPortletRequest(actionRequest);
		
		File excelFile = uploadPortletRequest.getFile("assetsMaster");
		long userId = PortalUtil.getUserId(uploadPortletRequest);
		
		ServiceContext serviceContext = null;
		try {
			serviceContext = ServiceContextFactory.getInstance(actionRequest);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		AssetLocalServiceUtil.importFromExcel(userId, excelFile, serviceContext);
	}
}