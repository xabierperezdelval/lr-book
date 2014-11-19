<%@include file="/html/investor/init.jsp"%>

<%
	int allocationBy = GetterUtil.getInteger(portletPreferences.getValue("allocationBy", "0"));
%>

<aui:script>
	AUI().ready(function(A) {
		Liferay.Service(
  			'/fingence-portlet.portfolio/get-snapshot',
  			{
    			portfolioIds: '1,2',
    			allocationBy: <%= allocationBy %>
  			},
  			function(data) {
  				console.log(JSON.stringify(data));
  			}
		);
	});
</aui:script>

<c:choose>
	<c:when test="<%= (allocationBy == IConstants.BREAKUP_BY_CURRENCY) %>">
		<%@include file="/html/investor/snapshot/currency.jspf"%>
	</c:when>
	
	<c:when test="<%= (allocationBy == IConstants.BREAKUP_BY_INDUSTRY_SECTOR) %>">
		show breakup up industry sector. 
	</c:when>	
	
	<c:when test="<%= (allocationBy == IConstants.BREAKUP_BY_RISK_COUNTRY) %>">
		show breakup up risk country. 
	</c:when>
	
	<c:when test="<%= (allocationBy == IConstants.BREAKUP_BY_ASSET_CLASS) %>">
		show breakup up asset class. 
	</c:when>

	<c:otherwise>
		Configure allocation type.  
	</c:otherwise>
</c:choose>