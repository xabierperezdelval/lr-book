package com.inikah.portlet;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import com.inikah.slayer.model.Profile;
import com.inikah.slayer.service.ProfileLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;


/**
 * Portlet implementation class MainPortlet
 */
public class MainPortlet extends MVCPortlet {
	
	@Override
	public void render(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		// TODO Auto-generated method stub
				
		viewTemplate = "/html/main/view.jsp";
		
		Layout layout = (Layout) request.getAttribute(WebKeys.LAYOUT);
		String page = layout.getName(PortalUtil.getLocale(request));

		if (!page.equalsIgnoreCase("mine")) {
			HttpServletRequest httpServletRequest = PortalUtil.getHttpServletRequest(request);
			
			long profileId = GetterUtil.getLong(PortalUtil.getOriginalServletRequest(httpServletRequest).getParameter("id"), 0l);
			
			if (profileId == 0l) {
				viewTemplate = "/html/error/invalid-id.jsp";
			} else {
				Profile profile = null;
				try {
					profile = ProfileLocalServiceUtil.fetchProfile(profileId);
				} catch (SystemException e) {
					e.printStackTrace();
				}
				
				if (Validator.isNull(profile)) {
					request.setAttribute("PROFILE_ID", String.valueOf(profileId));
					viewTemplate = "/html/error/no-profile.jsp";
				} else {
					
					// check ownership
					long userId = PortalUtil.getUserId(request);
					if (ProfileLocalServiceUtil.isOwner(userId, profileId)) {
						request.setAttribute("PROFILE", profile);
					} else {
						viewTemplate = "/html/error/not-owner.jsp";
					}
				}
			}			
		}
		
		super.render(request, response);
	}
}