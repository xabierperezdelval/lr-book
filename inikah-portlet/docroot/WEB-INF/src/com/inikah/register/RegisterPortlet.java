package com.inikah.register;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import com.inikah.slayer.service.BridgeLocalServiceUtil;
import com.inikah.util.IConstants;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class RegisterPortlet extends MVCPortlet {
	public void serveResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws IOException,
			PortletException {
		
		String cmd = ParamUtil.getString(resourceRequest, Constants.CMD);
		
		if (cmd.equalsIgnoreCase(IConstants.CMD_CHECK_DUPLICATE)) { 
			long companyId = PortalUtil.getCompanyId(resourceRequest);

			String emailAddress = ParamUtil.getString(resourceRequest, "emailAddress");
	        PrintWriter writer = resourceResponse.getWriter();
	        writer.println(BridgeLocalServiceUtil.isEmailExists(companyId, emailAddress));
	        writer.flush();
		}
    }
}