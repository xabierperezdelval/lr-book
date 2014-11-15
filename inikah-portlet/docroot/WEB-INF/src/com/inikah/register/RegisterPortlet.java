package com.inikah.register;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import com.inikah.invite.InviteConstants;
import com.inikah.slayer.model.Profile;
import com.inikah.slayer.service.BridgeLocalServiceUtil;
import com.inikah.slayer.service.InvitationLocalServiceUtil;
import com.inikah.slayer.service.ProfileLocalServiceUtil;
import com.inikah.util.IConstants;
import com.inikah.util.PageUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.PortletURLFactoryUtil;
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
	
	public void createAccount(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {

		User user = profileQuickAdd(actionRequest);
		
		checkInvitationStatus(actionRequest);
		
		HttpServletRequest request = PortalUtil.getHttpServletRequest(actionRequest);		
		SessionMessages.add(request, "userAdded", user.getEmailAddress());
		
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest
				.getAttribute(WebKeys.THEME_DISPLAY);
		
		actionResponse.sendRedirect(themeDisplay.getURLSignIn());		
	}
	
	/**
	 * 
	 * @param actionRequest
	 */
	private void checkInvitationStatus(PortletRequest portletRequest) {
		// check for invitation Id in the request
		long invitationId = ParamUtil.getLong(portletRequest, "invitationId", 0l);
		long inviterId = ParamUtil.getLong(portletRequest, "inviterId", 0l);
		
		String emailAddress = ParamUtil.getString(portletRequest, "emailAddress");
		if (invitationId > 0l) {
			InvitationLocalServiceUtil.updateInviation(invitationId, emailAddress);
		} else if (inviterId > 0l) {
			InvitationLocalServiceUtil.initInvitation(inviterId, emailAddress, 
					InviteConstants.STATUS_JOINED);
		}
	}

	/**
	 * 
	 * @param actionRequest
	 */
	private User profileQuickAdd(PortletRequest portletRequest) {

		String emailAddress = ParamUtil.getString(portletRequest, "emailAddress");
		String profileName = ParamUtil.getString(portletRequest, "profileName");
		String firstName = ParamUtil.getString(portletRequest, "firstName");
		boolean male = ParamUtil.getBoolean(portletRequest, "gender", true);
		
		boolean bride = ParamUtil.getBoolean(portletRequest, "bride");
		boolean creatingForSelf = ParamUtil.getBoolean(portletRequest, "creatingForSelf", false);

		ServiceContext serviceContext = null;
		try {
			serviceContext = ServiceContextFactory.getInstance(portletRequest);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		long companyId = PortalUtil.getCompanyId(portletRequest);
		long creatorUserId = 0l;
		try {
			creatorUserId = UserLocalServiceUtil.getDefaultUserId(companyId);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}		
		
		User user = null;
		try {
			user = UserLocalServiceUtil.addUserWithWorkflow(creatorUserId,
					companyId, true, StringPool.BLANK, StringPool.BLANK, true,
					StringPool.BLANK, emailAddress, 0, StringPool.BLANK, Locale.US,
					firstName, StringPool.BLANK, StringPool.BLANK, 0, 0, male,
					1, 1, 1, StringPool.BLANK, null,
					null, null, null, true,
					serviceContext);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}		
		
		ProfileLocalServiceUtil.init(user, bride, emailAddress, profileName, creatingForSelf, serviceContext);
				
		return user;
	}	
	
	@Override
	public void render(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY);
		if (themeDisplay.isSignedIn()) {
			viewTemplate = "/html/register/signed-in.jsp";
		} else {
			viewTemplate = "/html/register/view.jsp";
		}
		
		super.render(request, response);
	}
	
	public void startProfile(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		
		boolean bride = ParamUtil.getBoolean(actionRequest, "bride", false);
		String profileName = ParamUtil.getString(actionRequest, "profileName", "Dummy");
		boolean createdForSelf = ParamUtil.getBoolean(actionRequest, "createdForSelf", false);

		User user = null;
		try {
			user = PortalUtil.getUser(actionRequest);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		ServiceContext serviceContext = null;
		try {
			serviceContext = ServiceContextFactory.getInstance(actionRequest);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		Profile profile = ProfileLocalServiceUtil.init(user, bride, user.getEmailAddress(), profileName, createdForSelf, serviceContext);
		
		PortletSession portletSession = actionRequest.getPortletSession();
		portletSession.setAttribute("SEL_PROFILE", profile, PortletSession.APPLICATION_SCOPE);
		
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		long targetPlId = PageUtil.getPageLayoutId(themeDisplay.getScopeGroupId(), Constants.EDIT, Locale.US);
		PortletURL portletURL = PortletURLFactoryUtil.create(actionRequest, "edit_WAR_inikahportlet", targetPlId, PortletRequest.RENDER_PHASE);
		portletURL.setParameter("profileId", String.valueOf(profile.getProfileId()));
		actionResponse.sendRedirect(portletURL.toString());
	}
}