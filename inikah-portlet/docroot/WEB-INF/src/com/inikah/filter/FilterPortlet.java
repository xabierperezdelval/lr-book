package com.inikah.filter;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import com.inikah.slayer.model.MatchCriteria;
import com.inikah.slayer.service.MatchCriteriaLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class FilterPortlet
 */
public class FilterPortlet extends MVCPortlet {
	public void saveCriteria(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {

		System.out.println("inside save criteria....");
		
		long profileId = ParamUtil.getLong(actionRequest, "profileId");
		
		MatchCriteria matchCriteria = null;
		try {
			matchCriteria = MatchCriteriaLocalServiceUtil.fetchMatchCriteria(profileId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(matchCriteria)) return;
		
		// setting Age range
		int minAge = ParamUtil.getInteger(actionRequest, "minAge");
		int maxAge = ParamUtil.getInteger(actionRequest, "maxAge");
		
		matchCriteria.setMinAge(minAge);
		matchCriteria.setMaxAge(maxAge);
		
		// setting Height range
		int minHeight = ParamUtil.getInteger(actionRequest, "minHeight");
		int maxHeight = ParamUtil.getInteger(actionRequest, "maxHeight");
		
		matchCriteria.setMinHeight(minHeight);
		matchCriteria.setMaxHeight(maxHeight);
		
		try {
			matchCriteria = MatchCriteriaLocalServiceUtil.updateMatchCriteria(matchCriteria);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		System.out.println("updated match criteria....");
	} 
}