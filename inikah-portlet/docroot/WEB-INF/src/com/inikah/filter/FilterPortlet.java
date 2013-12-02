package com.inikah.filter;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;

import org.apache.commons.lang.StringUtils;

import com.inikah.slayer.model.MatchCriteria;
import com.inikah.slayer.model.Profile;
import com.inikah.slayer.service.MatchCriteriaLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class FilterPortlet
 */
public class FilterPortlet extends MVCPortlet {
	public void saveCriteria(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		
		Profile profile = (Profile) actionRequest.getPortletSession().getAttribute("SEL_PROFILE", PortletSession.APPLICATION_SCOPE);
		
		MatchCriteria matchCriteria = null;
		try {
			matchCriteria = MatchCriteriaLocalServiceUtil.fetchMatchCriteria(profile.getProfileId());
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(matchCriteria)) return;
		
		// setting Age range
		matchCriteria.setMinAge(ParamUtil.getInteger(actionRequest, "minAge"));
		matchCriteria.setMaxAge(ParamUtil.getInteger(actionRequest, "maxAge"));
		
		// setting Height range
		matchCriteria.setMinHeight(ParamUtil.getInteger(actionRequest, "minHeight"));
		matchCriteria.setMaxHeight(ParamUtil.getInteger(actionRequest, "maxHeight"));
		
		// marital status
		String[] maritalStatusArray = ParamUtil.getParameterValues(actionRequest, "maritalStatus");
		String maritalStatus = StringUtils.join(maritalStatusArray, StringPool.COMMA);
		matchCriteria.setMaritalStatus(maritalStatus);
		
		String[] motherTongueArray = ParamUtil.getParameterValues(actionRequest, "motherTongue");
		String motherTongue = StringUtils.join(motherTongueArray, StringPool.COMMA);
		matchCriteria.setMotherTongue(motherTongue);
		
		// profiles with Childen (applicable ONLY for non-single profiles)
		matchCriteria.setWithChildren(ParamUtil.getBoolean(actionRequest, "withChildren", false));
		
		try {
			matchCriteria = MatchCriteriaLocalServiceUtil.updateMatchCriteria(matchCriteria);
		} catch (SystemException e) {
			e.printStackTrace();
		}		
	} 
}