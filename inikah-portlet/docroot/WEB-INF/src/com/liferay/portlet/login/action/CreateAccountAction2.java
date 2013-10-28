package com.liferay.portlet.login.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

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
	}
	
	private void profileQuickAdd(ActionRequest actionRequest) {
				
		// Don't do anything if not custom registration
		boolean customRegistration = ParamUtil.getBoolean(actionRequest, "customRegistration", false);
		
		if (!customRegistration) return;

		String emailAddress = ParamUtil.getString(actionRequest, "emailAddress");
		String profileName = ParamUtil.getString(actionRequest, "profileName");
		
		boolean bride = ParamUtil.getBoolean(actionRequest, "bride");
		boolean creatingForSelf = ParamUtil.getBoolean(actionRequest, "creatingForSelf", false);

		ServiceContext serviceContext = null;
		try {
			serviceContext = ServiceContextFactory.getInstance(actionRequest);
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