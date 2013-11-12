package com.inikah.invite;

import java.io.IOException;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import com.inikah.slayer.model.Invitation;
import com.inikah.slayer.model.Profile;
import com.inikah.slayer.model.impl.InvitationImpl;
import com.inikah.slayer.service.InvitationLocalService;
import com.inikah.slayer.service.InvitationLocalServiceUtil;
import com.inikah.slayer.service.ProfileLocalServiceUtil;
import com.inikah.slayer.service.persistence.InvitationPersistence;
import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
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
				System.out.println("Invitee name--"+inviteeName);
				
				String inviteeEmail = ParamUtil.getString(actionRequest, "inviteeEmail" + emailsIndex);
				System.out.println("Email Id--"+inviteeEmail);
				
				if (InvitationLocalServiceUtil.isNewEmail(companyId, inviteeEmail))
				{
					System.out.println("in loop");
					InvitationLocalServiceUtil.sendInvitation(inviterId, inviteeName, inviteeEmail);
				}
				else
				{
					System.out.println("ALREADY PRESENt!!");
				}
				
							
			
	}

}
}
