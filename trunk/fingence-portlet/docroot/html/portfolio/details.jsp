<%@ include file="/html/portfolio/init.jsp"%>

<%
	long portfolioId = ParamUtil.getLong(renderRequest, "portfolioId");
	Portfolio portfolio = PortfolioLocalServiceUtil.fetchPortfolio(portfolioId);
	String backURL = ParamUtil.getString(request, "backURL");
%>

<liferay-ui:header backLabel="back-to-list"
	title="portfolio-details" backURL="<%= backURL %>" />
	
<aui:fieldset>
	<aui:row>
		<aui:column columnWidth="25"><b>Investor</b></aui:column>
		<aui:column columnWidth="25"><b>Managed By</b></aui:column>
		<aui:column columnWidth="25"><b>Wealth Advisor</b></aui:column>
		<aui:column columnWidth="25"><b>Institution</b></aui:column>
	</aui:row>
	
	<aui:row>
		<aui:column columnWidth="25"><%= BridgeServiceUtil.getUserName(portfolio.getInvestorId()) %></aui:column>
		<aui:column columnWidth="25"><%= BridgeServiceUtil.getUserName(portfolio.getRelationshipManagerId()) %></aui:column>
		<aui:column columnWidth="25"><%= BridgeServiceUtil.getUserName(portfolio.getWealthAdvisorId()) %></aui:column>
		<aui:column columnWidth="25"><%= BridgeServiceUtil.getOrganizationName(portfolio.getInstitutionId())  %></aui:column>
	</aui:row>	
</aui:fieldset>

<hr/>

<div id="myDataTable"></div>

<aui:script>
	YUI().use(
		'aui-base','aui-datatable',
		function(Y) {
			Liferay.Service(
				'/fingence-portlet.myresult/get-my-results',
				{
					portfolioId : '<%= portfolioId %>'
				},
					
				function(data) {
					var columns = [
                    	{key: 'name', label: 'Name'},
                        {key: 'security_ticker', label: 'TICKER'},
                        {key: 'purchasedMarketValue', label: 'Purchased Value'},
                        {key: 'currentMarketValue', label: 'Current Value'},
                        {key: 'current_price', label: 'Current Price'},
                        {key: 'purchaseQty', label: 'Quantity'}
					];	
								 	
					new Y.DataTable.Base({
						columnset: columns,
					    recordset: data
					}).render('#myDataTable');
				}
			);
		}
	);
</aui:script>