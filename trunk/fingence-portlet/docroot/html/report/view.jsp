<%@page import="com.fingence.slayer.model.Portfolio"%>
<%@page import="com.fingence.slayer.service.PortfolioServiceUtil"%>
<%@page import="com.fingence.slayer.service.PortfolioLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>

<%@ include file="/html/report/init.jsp"%>

<%
	String layoutName = layout.getName(locale);
	long portfolioId = GetterUtil.getLong(portletSession.getAttribute(
			"PORTFOLIO_ID", PortletSession.APPLICATION_SCOPE),
			PortfolioServiceUtil.getDefault(userId));
	int portfolioCount = PortfolioServiceUtil.getPortoliosCount(userId);
%>

<aui:row>
	<aui:column columnWidth="50">
		<h4><%= layoutName.toUpperCase() + " for " + portfolioId %></h4>
	</aui:column>
	<aui:column>
		<c:choose>
			<c:when test="<%= portfolioCount == 1 %>">
				&nbsp;
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
</aui:row>

<c:choose>

	<c:when test="<%= layoutName.equalsIgnoreCase(IConstants.PAGE_ASSET_REPORT) %>">
		<%@ include file="/html/report/asset-report.jspf"%>
	</c:when>
	
	<c:when test="<%= layoutName.equalsIgnoreCase(IConstants.PAGE_FIXED_INCOME_REPORT) %>">
		<%@ include file="/html/report/fixed-income-report.jspf"%>
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
						location.reload();
					}
				}
			});	
		}
	</c:if>		
	
	<c:if test="<%= (portfolioCount > 2) %>">	
		AUI().ready(function(A) {
			var list = document.getElementById('<portlet:namespace/>portfolioList');
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
		});
	</c:if>

	function showInMillions(figure){
		return accounting.formatMoney((figure / 1000000)) + ' Million';
	}
	
	function display(number, format) {
	
		var text = '';
		
		if (format == 'amount') {
			text = showInMillions(Math.abs(number));
		} else {
			text = (Math.abs(number)).toFixed(2) + '%';
		}
		
		if (number < 0) {
			text = text.fontcolor('red');
		}
		
		return text;
	}
</aui:script>