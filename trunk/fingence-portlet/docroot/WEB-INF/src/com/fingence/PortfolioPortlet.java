package com.fingence;

import java.io.File;
import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import com.fingence.slayer.service.PortfolioLocalServiceUtil;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class PortfolioPortlet
 */
public class PortfolioPortlet extends MVCPortlet {
 
	public void savePortfolio(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {

		UploadPortletRequest uploadPortletRequest = PortalUtil.getUploadPortletRequest(actionRequest);
		
		long userId = PortalUtil.getUserId(actionRequest);
		String portfolioName = ParamUtil.getString(uploadPortletRequest, "porfolioName");
		
		long portfolioId = ParamUtil.getLong(uploadPortletRequest, "portfolioId");
		long investorId = ParamUtil.getLong(uploadPortletRequest, "investorId");
		long institutionId = ParamUtil.getLong(uploadPortletRequest, "institutionId");
		long wealthAdvisorId = ParamUtil.getLong(uploadPortletRequest, "wealthAdvisorId");
		long relationshipManagerId = ParamUtil.getLong(uploadPortletRequest, "relationshipManagerId");
		
		boolean trial = ParamUtil.getBoolean(uploadPortletRequest, "trial", false);
		boolean social = ParamUtil.getBoolean(uploadPortletRequest, "social", false);

		File excelFile = uploadPortletRequest.getFile("excelFile");
		
		PortfolioLocalServiceUtil.updatePortfolio(portfolioId, userId,
				portfolioName, investorId, institutionId, wealthAdvisorId,
				trial, relationshipManagerId, social, true, excelFile);
	}
}