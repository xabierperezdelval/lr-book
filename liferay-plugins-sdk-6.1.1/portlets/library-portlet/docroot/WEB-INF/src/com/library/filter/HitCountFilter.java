package com.library.filter;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.RenderFilter;

public class HitCountFilter implements RenderFilter {

	private static int count = 0;

	public void init(FilterConfig filterConfig) throws PortletException {
		// empty
	}

	public void doFilter(RenderRequest renderRequest,
			RenderResponse renderResponse, FilterChain filterChain)
			throws IOException, PortletException {
		System.out.println("inside doFilter method..." + (++count));
		filterChain.doFilter(renderRequest, renderResponse);
	}

	public void destroy() {
		// empty
	}
}