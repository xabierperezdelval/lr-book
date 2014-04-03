package com.fingence;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import com.fingence.slayer.model.Portfolio;
import com.fingence.slayer.service.PortfolioItemServiceUtil;
import com.fingence.slayer.service.PortfolioLocalServiceUtil;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class RegisterPortlet
 */
public class ReportPortlet extends MVCPortlet {

	public void serveResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws IOException,
			PortletException {

		String cmd = ParamUtil.getString(resourceRequest, Constants.CMD);
		PortletSession portletSession = resourceRequest.getPortletSession();

		if (cmd.equalsIgnoreCase(IConstants.CMD_SET_PORTFOLIO_ID)) {
			long portfolioId = ParamUtil
					.getLong(resourceRequest, "portfolioId");
			portletSession.setAttribute("PORTFOLIO_ID",
					String.valueOf(portfolioId),
					PortletSession.APPLICATION_SCOPE);
		} else if (cmd.equalsIgnoreCase(IConstants.CMD_SET_ALLOCATION_BY)) {
			int allocationBy = ParamUtil.getInteger(resourceRequest,
					"allocationBy");
			portletSession.setAttribute("ALLOCATION_BY",
					String.valueOf(allocationBy),
					PortletSession.APPLICATION_SCOPE);
		} else if (cmd.equalsIgnoreCase(IConstants.CMD_CHECK_DUPLICATE_PORTFOLIO)) {
			
			long portfolioId = ParamUtil.getLong(resourceRequest, "portfolioId", 0l);
			String portfolioName = ParamUtil.getString(resourceRequest, "portfolioName");
			
			long userId = PortalUtil.getUserId(resourceRequest);
			List<Portfolio> userPortfolios = PortfolioLocalServiceUtil.getPortfolios(userId);
			
			boolean flag = false;
			for (Portfolio portfolio: userPortfolios) {
				if ((portfolioId == 0l && portfolio.getPortfolioName().equalsIgnoreCase(portfolioName)) 
						|| (portfolioId > 0l && portfolioId != portfolio.getPortfolioId() && portfolio.getPortfolioName().equalsIgnoreCase(portfolioName))) {
					flag = true;
					break;
				}
			}
			
			PrintWriter writer = resourceResponse.getWriter();
			writer.println(flag);
		}
	}

	public void savePortfolio(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {

		UploadPortletRequest uploadPortletRequest = PortalUtil
				.getUploadPortletRequest(actionRequest);

		long userId = PortalUtil.getUserId(actionRequest);
		String portfolioName = ParamUtil.getString(uploadPortletRequest, "portfolioName");
		String baseCurrency = ParamUtil.getString(uploadPortletRequest, "baseCurrency");

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
				trial, relationshipManagerId, social,baseCurrency, excelFile);
		
		PortletSession portletSession = actionRequest.getPortletSession();
		portletSession.setAttribute("MENU_ITEM", IConstants.PAGE_REPORTS_HOME, PortletSession.APPLICATION_SCOPE);
		
		actionResponse.sendRedirect("/reports");
	}

	public void updatePortfolioItem(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
				
		String isinId = ParamUtil.getString(actionRequest, "isinId");
		long portfolioItemId = ParamUtil.getLong(actionRequest, "itemId");
		long portfolioId = ParamUtil.getLong(actionRequest, "portfolioId");
		String ticker = ParamUtil.getString(actionRequest, "ticker");
		double purchasePrice = ParamUtil.getDouble(actionRequest, "purchasePrice");
		String purchaseDate = ParamUtil.getString(actionRequest, "purchaseDate");
		double purchaseQty = ParamUtil.getDouble(actionRequest, "purchaseQty");
		double purchasedFx = ParamUtil.getDouble(actionRequest, "purchasedFx");
				
		PortfolioItemServiceUtil.updateItem(portfolioItemId, portfolioId,
				isinId, ticker, purchasePrice, purchaseQty, purchasedFx,
				purchaseDate);		
	}
}