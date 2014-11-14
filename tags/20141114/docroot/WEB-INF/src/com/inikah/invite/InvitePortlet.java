package com.inikah.invite;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import com.inikah.slayer.service.InvitationLocalServiceUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class InvitePortlet
 */
public class InvitePortlet extends MVCPortlet {

	public void invite(ActionRequest actionRequest,ActionResponse response)
			throws Exception {
		
		long inviterId = PortalUtil.getUserId(actionRequest);
		long companyId = PortalUtil.getCompanyId(actionRequest);

		String emailsIndexesString = actionRequest.getParameter("emailsIndexes");

		int[] emailsIndexes = StringUtil.split(emailsIndexesString, 0);

		for (int emailsIndex : emailsIndexes) {
			String inviteeName = ParamUtil.getString(actionRequest, "inviteeName" + emailsIndex);
			String inviteeEmail = ParamUtil.getString(actionRequest, "inviteeEmail" + emailsIndex);

			if (InvitationLocalServiceUtil.isNewEmail(companyId, inviteeEmail)) {
				InvitationLocalServiceUtil.sendInvitation(inviterId,
						inviteeName, inviteeEmail);
			}
		}
	}	
}