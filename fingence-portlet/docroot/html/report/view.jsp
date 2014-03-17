<%@ include file="/html/report/init.jsp"%>

<%@page import="com.fingence.slayer.service.PortfolioServiceUtil"%>

<%

	int portfolioCount = PortfolioServiceUtil.getPortoliosCount(userId);

	if (portfolioCount == 0 && (userType == IConstants.USER_TYPE_INVESTOR || userType == IConstants.USER_TYPE_WEALTH_ADVISOR) && !layoutName.equalsIgnoreCase(IConstants.ADD_USER)) {
		layoutName = IConstants.ADD_PORTFOLIO;
	}

	long portfolioId = GetterUtil.getLong(portletSession.getAttribute(
			"PORTFOLIO_ID", PortletSession.APPLICATION_SCOPE),
			PortfolioServiceUtil.getDefault(userId));
	
	int allocationBy = GetterUtil.getInteger(portletSession.getAttribute(
			"ALLOCATION_BY", PortletSession.APPLICATION_SCOPE),
			IConstants.BREAKUP_BY_RISK_COUNTRY);
	
	boolean showAllocationSwitch = layoutName.equalsIgnoreCase(IConstants.PAGE_ASSET_REPORT);
%>

<c:if test="<%= !layoutName.equalsIgnoreCase(IConstants.PAGE_REPORTS_HOME) && !layoutName.equalsIgnoreCase(IConstants.ADD_PORTFOLIO)  && !layoutName.equalsIgnoreCase(IConstants.ADD_USER)%>">

	<aui:row>
		<aui:column columnWidth="30">
			<h4>Base Currency: <%= PortfolioLocalServiceUtil.getPortfolio(portfolioId).getBaseCurrency() %></h4>
		</aui:column>
		<aui:column>
			<c:choose>
				<c:when test="<%= portfolioCount == 1 %>">
					<h4><%= PortfolioServiceUtil.getPortfolioName(portfolioId) %></h4>
				</c:when>
				<c:when test="<%= portfolioCount == 2 %>">
					<%
						String otherPortolio = null;
						long otherPortfolioId = 0l;
						List<Portfolio> _portfolios = PortfolioLocalServiceUtil.getPortfolios(userId);
						for (Portfolio _portfolio: _portfolios) {
							if (portfolioId != _portfolio.getPortfolioId()) {
								otherPortfolioId = _portfolio.getPortfolioId();
								otherPortolio = _portfolio.getPortfolioName();
							}
						}
					%>
					<a href="javascript:void(0);" onClick="javascript:changePortfolio('<%= otherPortfolioId %>');">Show Reports for <%= otherPortolio %> &raquo;</a>
				</c:when>			
				<c:otherwise>
					<aui:select name="portfolioList" onChange="javascript:changePortfolio(this.value);"/>
				</c:otherwise>
			</c:choose>
		</aui:column>
		
		<c:if test="<%= showAllocationSwitch %>">
			<aui:column>
				<aui:select name="allocationBy" onChange="javascript:switchAllocationBy(this.value);">
					<aui:option value="<%=IConstants.BREAKUP_BY_RISK_COUNTRY%>"
						label="<%=IConstants.LBL_BREAKUP_BY_RISK_COUNTRY%>"
						selected="<%=(allocationBy == IConstants.BREAKUP_BY_RISK_COUNTRY)%>" />
					<aui:option value="<%=IConstants.BREAKUP_BY_CURRENCY%>"
						label="<%=IConstants.LBL_BREAKUP_BY_CURRENCY%>"
						selected="<%=(allocationBy == IConstants.BREAKUP_BY_CURRENCY)%>" />
					<aui:option value="<%=IConstants.BREAKUP_BY_SECURITY_TYPE%>"
						label="<%=IConstants.LBL_BREAKUP_BY_SECURITY_TYPE%>"
						selected="<%=(allocationBy == IConstants.BREAKUP_BY_SECURITY_TYPE)%>" />
					<aui:option value="<%=IConstants.BREAKUP_BY_SECTOR%>"
						label="<%=IConstants.LBL_BREAKUP_BY_SECTOR%>"
						selected="<%=(allocationBy == IConstants.BREAKUP_BY_SECTOR)%>" />
				</aui:select>
			</aui:column>
		</c:if>
	</aui:row>
</c:if>

<c:choose>
	<c:when test="<%= layoutName.equalsIgnoreCase(IConstants.PAGE_REPORTS_HOME) %>">
		<%@ include file="/html/report/reports-home.jspf"%>
	</c:when>
	
	<c:when test="<%= layoutName.equalsIgnoreCase(IConstants.PAGE_ASSET_REPORT) %>">
		<%@ include file="/html/report/asset-report.jspf"%>
	</c:when>
	
	<c:when test="<%= layoutName.equalsIgnoreCase(IConstants.PAGE_FIXED_INCOME) %>">
		<%@ include file="/html/report/fixed-income-report.jspf"%>
	</c:when>
	
	<c:when test="<%= layoutName.equalsIgnoreCase(IConstants.PAGE_RISK_REPORT) %>">
		<%@ include file="/html/report/risk-report.jspf"%>
	</c:when>
	
	<c:when test="<%= layoutName.equalsIgnoreCase(IConstants.PAGE_PERFORMANCE) %>">
		<%@ include file="/html/report/performance-report.jspf"%>
	</c:when>
	
	<c:when test="<%= layoutName.equalsIgnoreCase(IConstants.PAGE_VIOLATIONS) %>">
		<%@ include file="/html/report/violations-report.jspf"%>
	</c:when>	
	
	<c:when test="<%= layoutName.equalsIgnoreCase(IConstants.ADD_PORTFOLIO) %>">
		<%@ include file="/html/report/update.jspf"%>
	</c:when>
	
	<c:when test="<%= layoutName.equalsIgnoreCase(IConstants.ADD_USER) %>">
		<%@ include file="/html/register/register.jspf"%>
	</c:when>
</c:choose>

<aui:script>
	function formatNumber(data, number) {
		var text = data;
		
		if (number < 0 || number == false) {
			text = data.fontcolor('red');
		}
		
		return text;
	}
	
	<c:if test="<%= (portfolioCount > 1) %>">
		function changePortfolio(value) {
			var ajaxURL = Liferay.PortletURL.createResourceURL();
			ajaxURL.setPortletId('report_WAR_fingenceportlet');
			ajaxURL.setParameter('<%= Constants.CMD %>', '<%= IConstants.CMD_SET_PORTFOLIO_ID %>');
			ajaxURL.setParameter('portfolioId', value);
			ajaxURL.setWindowState('<%= LiferayWindowState.EXCLUSIVE.toString() %>');
			
			AUI().io.request('<%= themeDisplay.getURLPortal() %>' + ajaxURL, {
				sync: true,
				on: {
					success: function() {
						Liferay.Portlet.refresh('#p_p_id<portlet:namespace/>');
					}
				}
			});	
		}
	</c:if>		
	
	<c:if test="<%= (portfolioCount > 2) %>">	
		AUI().ready(function(A) {
			var list = document.getElementById('<portlet:namespace/>portfolioList');
			if (list != null){
				Liferay.Service(
		  			'/fingence-portlet.portfolio/get-portfolios',
		  			{
		    			userId: '<%= userId %>'
		  			},
		  			function(data) {
		    			for (var i=0; i<(data.length); i++) {
		    				var obj = data[i];
		    				list.options[i] = new Option(obj.portfolioName, obj.portfolioId);
		    				list.options[i].selected = (obj.portfolioId == '<%= portfolioId %>');
		    			}
		  			}
				);
			}
		});
	</c:if>
	
	<c:if test="<%= showAllocationSwitch %>">
		function switchAllocationBy(value) {
			var ajaxURL = Liferay.PortletURL.createResourceURL();
			ajaxURL.setPortletId('report_WAR_fingenceportlet');
			ajaxURL.setParameter('<%= Constants.CMD %>', '<%= IConstants.CMD_SET_ALLOCATION_BY %>');
			ajaxURL.setParameter('allocationBy', value);
			ajaxURL.setWindowState('<%= LiferayWindowState.EXCLUSIVE.toString() %>');
			
			AUI().io.request('<%= themeDisplay.getURLPortal() %>' + ajaxURL, {
				sync: true,
				on: {
					success: function() {
						Liferay.Portlet.refresh('#p_p_id<portlet:namespace/>');
					}
				}
			});
		}
	</c:if>	
</aui:script>