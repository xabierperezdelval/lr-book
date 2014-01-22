package com.fingence;

import java.io.IOException;
import java.util.Locale;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class RegisterPortlet
 */
public class RegisterPortlet extends MVCPortlet {
 
	public void register(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
			
		long creatorUserId = PortalUtil.getUserId(actionRequest);
		long companyId = PortalUtil.getCompanyId(actionRequest);
		
		String emailAddress = ParamUtil.getString(actionRequest, "emailAddress");
		String firstName = ParamUtil.getString(actionRequest, "firstName");
		String lastName = ParamUtil.getString(actionRequest, "lastName");
		boolean male = ParamUtil.getBoolean(actionRequest, "male");
		String jobTitle = ParamUtil.getString(actionRequest, "jobTitle");
		
		ServiceContext serviceContext = null;
		try {
			serviceContext = ServiceContextFactory.getInstance(actionRequest);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}

		User user = null;
		try {
			user = UserLocalServiceUtil.addUser(creatorUserId, companyId, true,
					null, null, true, null, emailAddress,
					0, null, Locale.US, firstName, StringPool.BLANK, lastName,
					0, 0, male, 1, 1,
					1, jobTitle, null, null, null,
					null, true, serviceContext);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		System.out.println("userId ==> " + user.getUserId());
				
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

		if (!themeDisplay.isSignedIn()) {
			
			// create organization
			
			
			
			
			actionResponse.sendRedirect(themeDisplay.getURLSignIn());
		} else {
			
		}
		
		// attach this user to the organization
	}
}