package com.inikah.payment;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.inikah.slayer.model.Profile;
import com.inikah.slayer.service.ProfileLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class PaymentPortlet
 */
public class PaymentPortlet extends MVCPortlet {
 
	@Override
	public void render(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {

		long profileId = ParamUtil.getLong(request, "profileId");
				
		PortletSession portletSession = request.getPortletSession();
		if (Validator.isNull(portletSession.getAttribute("PROFILE"))) {
			Profile profile = null;
			try {
				profile = ProfileLocalServiceUtil.fetchProfile(profileId);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			if (Validator.isNotNull(profile)) {
				portletSession.setAttribute("PROFILE", profile);
			}
		}
		
		super.render(request, response);
	}
}