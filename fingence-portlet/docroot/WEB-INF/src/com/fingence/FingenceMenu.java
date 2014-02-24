package com.fingence;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class FingenceMenu
 */
public class FingenceMenu extends MVCPortlet {
	
	public void serveResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws IOException,
			PortletException {

		PortletSession portletSession = resourceRequest.getPortletSession();
		String item =  ParamUtil.getString(resourceRequest, "navigationMenuItem");
		
		portletSession.setAttribute("navigationParam", item, PortletSession.APPLICATION_SCOPE);
	}
}
