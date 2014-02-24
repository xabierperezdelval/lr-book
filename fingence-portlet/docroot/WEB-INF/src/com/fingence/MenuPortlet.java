package com.fingence;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class FingenceMenu
 */
public class MenuPortlet extends MVCPortlet {
	
	public void setNavigation(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		
		PortletSession portletSession = actionRequest.getPortletSession();
		String menuItem =  ParamUtil.getString(actionRequest, "MENU_ITEM");
				
		portletSession.setAttribute("MENU_ITEM", menuItem, PortletSession.APPLICATION_SCOPE);
	}
}
