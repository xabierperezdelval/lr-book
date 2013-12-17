package com.liferay.portlet.login.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.inikah.invite.InviteConstants;
import com.inikah.slayer.service.InvitationLocalServiceUtil;
import com.inikah.slayer.service.ProfileLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.struts.BaseStrutsPortletAction;
import com.liferay.portal.kernel.struts.StrutsPortletAction;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;

public class CreateAccountAction2  extends BaseStrutsPortletAction  {
	public void processAction(StrutsPortletAction originalStrutsPortletAction,
			PortletConfig portletConfig, ActionRequest actionRequest,
			ActionResponse actionResponse) throws Exception {
			
		originalStrutsPortletAction.processAction(
			originalStrutsPortletAction, portletConfig, actionRequest, actionResponse);
				
		profileQuickAdd(actionRequest);
		
		checkInvitationStatus(actionRequest);
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