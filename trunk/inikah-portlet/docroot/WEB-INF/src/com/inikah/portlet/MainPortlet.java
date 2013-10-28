package com.inikah.portlet;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;


/**
 * Portlet implementation class MainPortlet
 */
public class MainPortlet extends MVCPortlet {

	@Override
	public void doView(RenderRequest renderRequest,
			RenderResponse renderResponse) throws IOException, PortletException {

		Layout layout = (Layout)renderRequest.getAttribute(WebKeys.LAYOUT);
		
		
		
		System.out.println("The current page is...." + layout.getName(PortalUtil.getLocale(renderRequest)));
		
		HttpServletRequest httpServletRequest = PortalUtil.getHttpServletRequest(renderRequest);
		
		HttpServletRequest originalHttpServletRequest = PortalUtil.getOriginalServletRequest(httpServletRequest);
		
		System.out.println(ParamUtil.getLong(originalHttpServletRequest, "id", 0l));
		
		
		

		super.doView(renderRequest, renderResponse);
	}
	
	@Override
	public void render(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		
		System.out.println("This is render.....");
		
		super.render(request, response);
	}
}