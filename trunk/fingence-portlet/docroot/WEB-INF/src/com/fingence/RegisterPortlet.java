package com.fingence;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import com.fingence.slayer.service.BridgeServiceUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class RegisterPortlet
 */
public class RegisterPortlet extends MVCPortlet {
 
	public void register(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		
		String emailAddress = ParamUtil.getString(actionRequest, "emailAddress");
		String firstName = ParamUtil.getString(actionRequest, "firstName");
		String lastName = ParamUtil.getString(actionRequest, "lastName");
		boolean male = ParamUtil.getBoolean(actionRequest, "gender");
		String jobTitle = ParamUtil.getString(actionRequest, "jobTitle");
		long countryId = ParamUtil.getLong(actionRequest, "countryId");
		
		long creatorUserId = PortalUtil.getUserId(actionRequest);
		
		User user = BridgeServiceUtil.addUser(creatorUserId, firstName,
				lastName, emailAddress, male, countryId, jobTitle);

		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest
				.getAttribute(WebKeys.THEME_DISPLAY);

		int userType = ParamUtil.getInteger(actionRequest, "userType",
				IConstants.USER_TYPE_INVESTOR);

		boolean isWealthAdvisor = (BridgeServiceUtil.getUserType(creatorUserId) == IConstants.USER_TYPE_WEALTH_ADVISOR);
				
		if (themeDisplay.isSignedIn() && isWealthAdvisor) {
			BridgeServiceUtil.assignRole(creatorUserId, user.getUserId(), userType);
			PortletSession portletSession = actionRequest.getPortletSession();
			portletSession.setAttribute("MENU_ITEM", IConstants.PAGE_REPORTS_HOME, PortletSession.APPLICATION_SCOPE);
			actionResponse.sendRedirect(ParamUtil.getString(actionRequest, "redirectURL"));
		} else if (userType == IConstants.USER_TYPE_WEALTH_ADVISOR) {
			String firmName = ParamUtil.getString(actionRequest, "firmName");
			BridgeServiceUtil.addWealthAdvisorFirm(firmName, user.getUserId());
			actionResponse.sendRedirect(themeDisplay.getURLSignIn());
		}
	}
	
	public void serveResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws IOException,
			PortletException {
		
		String cmd = ParamUtil.getString(resourceRequest, Constants.CMD);
		
		if (cmd.equalsIgnoreCase(IConstants.CMD_CHECK_DUPLICATE)) { 
			long companyId = PortalUtil.getCompanyId(resourceRequest);
	        String fieldValue = ParamUtil.getString(resourceRequest, "fieldValue");
	        String fieldName = ParamUtil.getString(resourceRequest, "fieldName");
	        PrintWriter writer = resourceResponse.getWriter();
	        
	        if (fieldName.equalsIgnoreCase("email")) {
	        	writer.println(BridgeServiceUtil.isEmailExists(companyId, fieldValue));
	        } else if (fieldName.equalsIgnoreCase("firm")) {
	        	writer.println(BridgeServiceUtil.isFirmExists(companyId, fieldValue));
	        }
	        
	        writer.flush();
		}
    }
}