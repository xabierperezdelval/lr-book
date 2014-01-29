package com.fingence;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import com.fingence.slayer.service.BridgeServiceUtil;
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
		boolean male = ParamUtil.getBoolean(actionRequest, "male");
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
		} else if (userType == IConstants.USER_TYPE_WEALTH_ADVISOR) {
			String firmName = ParamUtil.getString(actionRequest, "firmName");
			BridgeServiceUtil.addWealthAdvisorFirm(firmName, user.getUserId());
		}
	}
}