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
                        {key: 'purchaseQty', label: 'Quantity'},
                        {
                             key: 'itemId',
                             label: 'Action',
                             formatter: '<a href="javascript:void(0);" onclick="javascript:updateItem({value});"><img src="<%= themeDisplay.getPathThemeImages() + IConstants.THEME_ICON_EDIT %>"/></a>&nbsp;<a href="javascript:void(0);" onclick="javascript:deleteItem({value});"><img src="<%= themeDisplay.getPathThemeImages() + IConstants.THEME_ICON_DELETE %>"/></a>',
                             allowHTML: true
                         }
                        
					];	
								 	
					new Y.DataTable.Base({
						columnset: columns,
					    recordset: data
					}).render('#myDataTable');
				}
			);
		}
	);
	
    function deleteItem(itemId) {
        if (confirm('Are you sure to delete this item from portfolio?')) {
            Liferay.Service(
                '/fingence-portlet.portfolioitem/delete-item',
                {
                    itemId: itemId
                },
                function(obj) {
                    Liferay.Portlet.refresh('#p_p_id<portlet:namespace/>');
                }
            );
        }
    }
    
 	function updateItem(itemId) {  
    
        AUI().use('aui-dialog', function(A) {
        
			var ajaxURL = Liferay.PortletURL.createRenderURL();
			ajaxURL.setPortletId('portfolio_WAR_fingenceportlet');
			ajaxURL.setParameter('jspPage', '/html/portfolio/update-item.jsp');
			ajaxURL.setParameter('itemId', itemId);
			ajaxURL.setWindowState('<%= LiferayWindowState.POP_UP.toString() %>');
			        
			Liferay.Util.openWindow({
            	dialog: {
                	centered: true,
                    modal: true,
                    width: 600,
                    height: 400                
               	},
                id: '<portlet:namespace/>editPortfolioItemPopup',
                title: 'Add/Edit Portfolio Item',
               	uri: ajaxURL
           	});        
        });
    }   
</aui:script>