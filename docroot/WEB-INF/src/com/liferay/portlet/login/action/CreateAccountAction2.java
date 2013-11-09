package com.liferay.portlet.login.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import com.inikah.invite.InviteConstants;
import com.inikah.slayer.service.ProfileLocalServiceUtil;
import com.inikah.slayer.service.InvitationLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.struts.BaseStrutsPortletAction;
import com.liferay.portal.kernel.struts.StrutsPortletAction;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.util.PortalUtil;

public class CreateAccountAction2  extends BaseStrutsPortletAction  {
	public void processAction(StrutsPortletAction originalStrutsPortletAction,
			PortletConfig portletConfig, ActionRequest actionRequest,
			ActionResponse actionResponse) throws Exception {
		
		setValuesForSelfProfile(actionRequest);
			
		originalStrutsPortletAction.processAction(
			originalStrutsPortletAction, portletConfig, actionRequest, actionResponse);
				
		profileQuickAdd(actionRequest);
		
		checkInvitationStatus(actionRequest);
	}

	/*
	private void setExtraInfo(ActionRequest actionRequest) {

		long invitationId = ParamUtil.getLong(actionRequest, "invitationId", 0l);
		
		ServiceContext serviceContext = null;
		try {
			serviceContext = ServiceContextFactory.getInstance(actionRequest);
			//serviceContext = ServiceContextFactory.getInstance(
					//User.class.getName(), actionRequest);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (invitationId > 0l) {
			serviceContext.setAttribute("INVITATION_ID", String.valueOf(invitationId));
		}
		
		boolean customRegistration = ParamUtil.getBoolean(actionRequest, "customRegistration", false);
		
		if (customRegistration) {
			String profileName = ParamUtil.getString(actionRequest, "profileName");
			String emailAddress = ParamUtil.getString(actionRequest, "emailAddress");
			boolean bride = ParamUtil.getBoolean(actionRequest, "bride");
			boolean creatingForSelf = ParamUtil.getBoolean(actionRequest, "creatingForSelf", false);
			
			long profileId = 0l;
			try {
				profileId = CounterLocalServiceUtil.increment();
			} catch (SystemException e) {
				e.printStackTrace();
			}
			Profile profile = ProfileLocalServiceUtil.createProfile(profileId);
			profile.setProfileName(profileName);
			profile.setEmailAddress(emailAddress);
			profile.setBride(bride);
			if (creatingForSelf) {
				profile.setCreatedFor(MyListUtil.getListTypeId(IConstants.LIST_CREATED_FOR, "self"));
			}
			
			serviceContext.setAttribute("PROFILE", profile);
		}
	}
	*/

	private void setValuesForSelfProfile(PortletRequest portletRequest) {
		
		HttpServletRequest httpServletRequest = PortalUtil.getHttpServletRequest(portletRequest);
				
		boolean creatingForSelf = ParamUtil.getBoolean(portletRequest, "creatingForSelf", false);
		
		if (creatingForSelf) {
			String profileName = ParamUtil.getString(portletRequest, "profileName");
			httpServletRequest.setAttribute("_58_firstName", profileName);
			
			boolean bride = ParamUtil.getBoolean(portletRequest, "bride", false);
			if (bride) {
				httpServletRequest.setAttribute("_58_male", "0");
			}
		}		
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
					InviteConstants.STATUS_REGISTERED);
		}
	}

	/**
	 * 
	 * @param actionRequest
	 */
	private void profileQuickAdd(PortletRequest portletRequest) {
				
		// Don't do anything if not custom registration
		boolean customRegistration = ParamUtil.getBoolean(portletRequest, "customRegistration", false);
		
		if (!customRegistration) return;

		String emailAddress = ParamUtil.getString(portletRequest, "emailAddress");
		String profileName = ParamUtil.getString(portletRequest, "profileName");
		
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
		
		ProfileLocalServiceUtil.init(bride, emailAddress, profileName, creatingForSelf, serviceContext);
	}

	public String render(StrutsPortletAction originalStrutsPortletAction,
			PortletConfig portletConfig, RenderRequest renderRequest,
			RenderResponse renderResponse) throws Exception {
		
		return originalStrutsPortletAction.render(
			originalStrutsPortletAction, portletConfig, renderRequest, renderResponse);
	}
}