package com.inikah.filter;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.xml.namespace.QName;

import com.inikah.slayer.model.MatchCriteria;
import com.inikah.slayer.model.impl.MatchCriteriaImpl;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class Filter
 */
public class Filter extends MVCPortlet {
 
	
	public void add(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {

		 
		 
		 int minAge =ParamUtil.getInteger(actionRequest, "minAge");
		 int maxAge =ParamUtil.getInteger(actionRequest, "maxAge");
		 int minHeight =ParamUtil.getInteger(actionRequest, "minHeight");
		 int maxHeight =ParamUtil.getInteger(actionRequest, "maxHeight");
		 
		 MatchCriteria filter = new MatchCriteriaImpl();
		 filter.setMinAge(minAge);
		 filter.setMaxAge(maxAge);
		 filter.setMinHeight(minHeight);
		 filter.setMaxHeight(maxHeight);
		 System.out.println(minAge+" "+maxAge+" "+minHeight+" "+maxHeight);
		 
		 QName qName = new QName("http://liferay.com", "filter", "x");
		 actionResponse.setEvent(qName,filter);
	}

}
