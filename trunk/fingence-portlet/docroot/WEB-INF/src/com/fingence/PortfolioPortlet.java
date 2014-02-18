package com.fingence;

import java.io.File;
import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import com.fingence.slayer.service.PortfolioItemServiceUtil;
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
		String portfolioName = ParamUtil.getString(uploadPortletRequest, "portfolioName");
		
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
				trial, relationshipManagerId, social, excelFile);
	}
	
	public void updatePortfolioItem(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		
		System.out.println("coming inside....");
				
		String isinId = ParamUtil.getString(actionRequest, "isinId");
		long portfolioItemId = ParamUtil.getLong(actionRequest, "portfolioItemId");
		long portfolioId = ParamUtil.getLong(actionRequest, "portfolioId");
		String ticker = ParamUtil.getString(actionRequest, "ticker");
		Double purchasePrice = ParamUtil.getDouble(actionRequest, "purchasePrice");
		String purchaseDate = ParamUtil.getString(actionRequest, "purchaseDate");
		int purchaseQty = ParamUtil.getInteger(actionRequest, "purchaseQty");

		PortfolioItemServiceUtil.updateItem(portfolioItemId, portfolioId, isinId,
				ticker, purchasePrice, purchaseQty, null);		
	}	
}