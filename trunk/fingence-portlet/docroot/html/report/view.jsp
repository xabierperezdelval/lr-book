<%@page import="com.fingence.slayer.service.PortfolioServiceUtil"%>
<%@page import="com.fingence.slayer.service.PortfolioLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>

<%@ include file="/html/report/init.jsp"%>

<%
	String layoutName = layout.getName(locale);
	long portfolioId = GetterUtil.getLong(portletSession.getAttribute(
			"PORTFOLIO_ID", PortletSession.APPLICATION_SCOPE),
			PortfolioServiceUtil.getDefault(user.getUserId()));
%>

<aui:row>
	<aui:column columnWidth="50">
		<h3><%= layoutName.toUpperCase() + " for " + portfolioId %></h3>
	</aui:column>
	<aui:column>
		<aui:select name="portfolioList" onClick="javascript:changePortfolio(this);"/>
	</aui:column>
</aui:row>

<c:choose>

	<c:when test="<%= layoutName.equalsIgnoreCase(IConstants.PAGE_ASSET_REPORT) %>">
		<%@ include file="/html/report/asset-reports.jspf"%>
	</c:when>
	
	<c:when test="<%= layoutName.equalsIgnoreCase(IConstants.PAGE_FIXED_INCOME_REPORT) %>">
		<%@ include file="/html/report/security-holding.jspf"%>
	</c:when>
	
	<c:when test="<%= layoutName.equalsIgnoreCase(IConstants.PAGE_RISK_REPORT) %>">
		<%@ include file="/html/report/risk-report.jspf"%>
	</c:when>
	
	<c:when test="<%= layoutName.equalsIgnoreCase(IConstants.PAGE_PERFORMANCE_REPORT) %>">
		<%@ include file="/html/report/performance-report.jspf"%>
	</c:when>		
	
	<c:otherwise>
		<%@ include file="/html/report/violations-report.jspf"%>
	</c:otherwise>
	
</c:choose>

<aui:script>

	function changePortfolio(list) {
		var ajaxURL = Liferay.PortletURL.createResourceURL();
		ajaxURL.setPortletId('report_WAR_fingenceportlet');
		ajaxURL.setParameter('<%= Constants.CMD %>', '<%= IConstants.CMD_SET_PORTFOLIO_ID %>');
		ajaxURL.setParameter('portfolioId', list.value);
		ajaxURL.setWindowState('<%= LiferayWindowState.EXCLUSIVE.toString() %>');
		
		AUI().io.request('<%= themeDisplay.getURLPortal() %>' + ajaxURL, {
			on: {
				success: function() {
					Liferay.Portlet.refresh('#p_p_id<portlet:namespace />');
				}
			}
		});	
	}

	AUI().ready(function(A) {
	
		var list = document.getElementById('<portlet:namespace/>portfolioList');
		Liferay.Service(
  			'/fingence-portlet.portfolio/get-portfolios',
  			{
    			userId: '<%= user.getUserId() %>'
  			},
  			function(data) {
    			for (var i=0; i<(data.length); i++) {
    				var obj = data[i];
    				list.options[i] = new Option(obj.portfolioName, obj.portfolioId);
    				list.options[i].selected = (obj.portfolioId == '<%= portfolioId %>');
    			}
  			}
		);
	});
</aui:script>