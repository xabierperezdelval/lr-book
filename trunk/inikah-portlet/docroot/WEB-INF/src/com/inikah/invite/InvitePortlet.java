package com.inikah.invite;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import com.inikah.slayer.service.InvitationLocalServiceUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class InvitePortlet
 */
public class InvitePortlet extends MVCPortlet {

	public void invite(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {

		String inviteeEmail = ParamUtil.getString(actionRequest, "inviteeEmail");
		String inviteeName = ParamUtil.getString(actionRequest, "inviteeName");
		
		long inviterId = PortalUtil.getUserId(actionRequest);
		long companyId = PortalUtil.getCompanyId(actionRequest);
		
		if (InvitationLocalServiceUtil.isNewEmail(companyId, inviteeEmail)) {
			
			InvitationLocalServiceUtil.sendInvitation(inviterId, inviteeName, inviteeEmail);
		}
	}
}