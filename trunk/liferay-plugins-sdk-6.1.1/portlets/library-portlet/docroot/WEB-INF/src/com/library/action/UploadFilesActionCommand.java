package com.library.action;

import java.io.File;
import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.ActionCommand;
import com.slayer.service.LMSBookLocalServiceUtil;

public class UploadFilesActionCommand implements ActionCommand {

	public boolean processCommand(PortletRequest portletRequest, 
		PortletResponse portletResponse)
			throws PortletException {
				
		ActionRequest actionRequest = (ActionRequest) portletRequest;
		ActionResponse actionResponse = (ActionResponse) portletResponse;
		
		UploadPortletRequest uploadRequest = 
				PortalUtil.getUploadPortletRequest(actionRequest);
			
		File coverImage = uploadRequest.getFile("coverImage");
		
		if (coverImage.getTotalSpace() > 0) {
			long bookId = ParamUtil.getLong(uploadRequest, "bookId");
			try {
				ServiceContext serviceContext = 
					ServiceContextFactory
						.getInstance(actionRequest);
				
				serviceContext.setAttribute("COVER_IMAGE", coverImage);
				LMSBookLocalServiceUtil
						.attachFiles(bookId, serviceContext);
			} catch (PortalException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		
		// redirecting to original list page
		try {
			actionResponse.sendRedirect(
				ParamUtil.getString(uploadRequest, "redirectURL"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}
}