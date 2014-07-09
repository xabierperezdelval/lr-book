package com.fingence;

import java.io.File;
import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ParamUtil;
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
		
		System.out.println("going to push to MessageBus...");
		
		Message message = new Message();
        message.put("EXCEL_FILE", excelFile);
        message.put("SERVICE_CONTEXT", serviceContext);
        message.put("USER_ID", userId);
		
		if (ParamUtil.getBoolean(uploadPortletRequest, "loadAssetData", false)) {
			message.put("MESSAGE_NAME", "loadAssetData");	
			MessageBusUtil.sendMessage("fingence/destination", message);
		}
		
		if (ParamUtil.getBoolean(uploadPortletRequest, "loadEquityPrice", false)) {
			message.put("MESSAGE_NAME", "loadEquityPrice");	
            MessageBusUtil.sendMessage("fingence/destination", message);			
		}
		
		if (ParamUtil.getBoolean(uploadPortletRequest, "loadBondPrice", false)) {
			message.put("MESSAGE_NAME", "loadBondPrice");	
            MessageBusUtil.sendMessage("fingence/destination", message);			
		}	
		
		if (ParamUtil.getBoolean(uploadPortletRequest, "loadBondCashflow", false)) {
			message.put("MESSAGE_NAME", "loadBondCashflow");	
            MessageBusUtil.sendMessage("fingence/destination", message);			
		}		
		
		if (ParamUtil.getBoolean(uploadPortletRequest, "loadDividends", false)) {
			message.put("MESSAGE_NAME", "loadDividends");	
            MessageBusUtil.sendMessage("fingence/destination", message);			
		}		
		
		System.out.println("Exiting portlet class after delegating to MessageBus...");
	}
}