package com.fingence;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class RegisterPortlet
 */
public class ReportPortlet extends MVCPortlet {
	
	public void serveResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws IOException,
			PortletException {
		
		String cmd = ParamUtil.getString(resourceRequest, Constants.CMD);
		PortletSession portletSession = resourceRequest.getPortletSession();
		
		if (cmd.equalsIgnoreCase(IConstants.CMD_SET_PORTFOLIO_ID)) {
			long portfolioId = ParamUtil.getLong(resourceRequest, "portfolioId");
			portletSession.setAttribute("PORTFOLIO_ID",
					String.valueOf(portfolioId),
					PortletSession.APPLICATION_SCOPE);
		} else if (cmd.equalsIgnoreCase(IConstants.CMD_SET_ALLOCATION_BY)) {
			int allocationBy = ParamUtil.getInteger(resourceRequest, "allocationBy");
			portletSession.setAttribute("ALLOCATION_BY",
					String.valueOf(allocationBy),
					PortletSession.APPLICATION_SCOPE);			
		}
    }
}