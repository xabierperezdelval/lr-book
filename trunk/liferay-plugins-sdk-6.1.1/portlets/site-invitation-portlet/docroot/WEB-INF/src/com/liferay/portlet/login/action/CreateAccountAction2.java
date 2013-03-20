package com.liferay.portlet.login.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.struts.BaseStrutsPortletAction;
import com.liferay.portal.kernel.struts.StrutsPortletAction;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.mpower.util.InvitationConstants;

public class CreateAccountAction2  extends BaseStrutsPortletAction  {
	public void processAction(StrutsPortletAction originalStrutsPortletAction,
			PortletConfig portletConfig, ActionRequest actionRequest,
			ActionResponse actionResponse) throws Exception {
		
		String inviterId = ParamUtil.getString(
			actionRequest, InvitationConstants.KEY_INVITER_ID, StringPool.BLANK);
		ServiceContext serviceContext = ServiceContextFactory.getInstance(
				User.class.getName(), actionRequest);
		serviceContext.setAttribute(InvitationConstants.KEY_INVITER_ID, inviterId);
			
		originalStrutsPortletAction.processAction(
			originalStrutsPortletAction, portletConfig, actionRequest, actionResponse);
		
		if (SessionErrors.size(actionRequest) > 0) {
			actionResponse.setRenderParameter(InvitationConstants.KEY_INVITER_ID, inviterId);
			actionResponse.setRenderParameter(InvitationConstants.KEY_EMAIL_ADDRESS, 
				ParamUtil.getString(actionRequest, InvitationConstants.KEY_EMAIL_ADDRESS));
		}
	}
	
	public String render(StrutsPortletAction originalStrutsPortletAction,
			PortletConfig portletConfig, RenderRequest renderRequest,
			RenderResponse renderResponse) throws Exception {
		
		return originalStrutsPortletAction.render(
			originalStrutsPortletAction, portletConfig, renderRequest, renderResponse);
	}
}