<%@page import="com.fingence.slayer.service.PortfolioItemServiceUtil"%>

<%@ include file="/html/report/init.jsp"%>

<%
	long portfolioId = ParamUtil.getLong(renderRequest, "portfolioId");
	Portfolio portfolio = PortfolioLocalServiceUtil.fetchPortfolio(portfolioId);
	String backURL = ParamUtil.getString(request, "backURL");
	String managerName = BridgeServiceUtil.getUserName(portfolio.getRelationshipManagerId());
	
	int portfolioItemCount = PortfolioItemServiceUtil.getPortfolioItems(portfolioId).size();
%>
	
<aui:fieldset>
	<aui:row>
		<aui:column columnWidth="25"><b>Investor</b></aui:column>
		<aui:column columnWidth="25"><b>Managed By</b></aui:column>
		<aui:column columnWidth="25"><b>Wealth Advisor</b></aui:column>
		<aui:column columnWidth="25"><b>Institution</b></aui:column>
	</aui:row>
	
	<aui:row>
		<aui:column columnWidth="25"><%= BridgeServiceUtil.getUserName(portfolio.getInvestorId()) %></aui:column>
		<aui:column columnWidth="25"><%= (Validator.isNull(managerName) ? "Not Assigned" : managerName) %></aui:column>
		<aui:column columnWidth="25"><%= BridgeServiceUtil.getUserName(portfolio.getWealthAdvisorId()) %></aui:column>
		<aui:column columnWidth="25"><%= BridgeServiceUtil.getOrganizationName(portfolio.getInstitutionId())  %></aui:column>
	</aui:row>	
</aui:fieldset>

<br/><aui:a href="javascript:void(0);" onClick="javasript:updateItem(0)" label="Add Asset"/><hr/>

<div id="myDataTable"></div>

<aui:script>
	<c:if test="<%= portfolioItemCount > 0 %>">
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
	                    	{key: 'name', label: 'Security Name'},
	                        {key: 'security_ticker', label: 'TICKER'},
	                        {
	                        	key: 'purchasedMarketValue', 
	                        	label: 'Purchased Value',
	                        	formatter: function(obj) {
					 				obj.value = accounting.formatMoney(obj.value);
					 			}
	                       	},
	                        {
	                        	key: 'currentMarketValue', 
	                        	label: 'Current Value',
		                        formatter: function(obj) {
					 				obj.value = accounting.formatMoney(obj.value);
					 			}
				 			},
	                        {
	                        	key: 'purchaseQty',
	                         	label: 'Quantity',
	                         	formatter: function(obj) {
					 				obj.value = obj.value.toFixed(2);
					 			}
	                       	},
	                       	{
	                        	key: 'gain_loss',
	                         	label: 'Gain/Loss',
	                         	formatter: function(obj) {
									obj.value = display(obj.value, 'amount') + '(' +display(obj.value/obj.data.purchasedMarketValue *100, 'percent')   + ')';
								},
					 			allowHTML: true
	                       	},
	                        {
	                             key: 'itemId',
	                             label: 'Actions',
	                             formatter: function(obj) {
	                             
	                              	obj.value = '<a href="javascript:void(0);" title="Update Asset" onclick="javascript:updateItem(' + obj.value + ');"><img src="<%= themeDisplay.getPathThemeImages() + IConstants.THEME_ICON_EDIT %>"/></a>&nbsp;' +
	                             			'<a href="javascript:void(0);" title="Delete Asset" onclick="javascript:deleteItem(' + obj.value + ');"><img src="<%= themeDisplay.getPathThemeImages() + IConstants.THEME_ICON_DELETE %>"/></a>' +
	                             			'<a id="viewMoreDetails" href="javascript:void(0);" onmouseover="javascript:showMoreDetails(' + obj.value + ',' + obj.data.purchasedFx + ','  + obj.data.current_fx + ');"><img src="<%= themeDisplay.getPathThemeImages() + IConstants.THEME_ICON_MORE_DETAILS %>"/></a>';
	                             			
                       			},
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
		
	    function deleteItem(portfolioItemId) {
	        if (confirm('Are you sure to delete this item from portfolio?')) {
	            Liferay.Service(
	                '/fingence-portlet.portfolioitem/delete-item',
	                {
	                    portfolioItemId: portfolioItemId
	                },
	                function(obj) {
	                    Liferay.Portlet.refresh('#p_p_id<portlet:namespace/>');
	                }
	            );
	        }
	    }		
	</c:if>
    
 	function updateItem(portfolioItemId) {
		var ajaxURL = Liferay.PortletURL.createRenderURL();
		ajaxURL.setPortletId('report_WAR_fingenceportlet');
		ajaxURL.setParameter('jspPage', '/html/report/update-item.jsp');
		ajaxURL.setParameter('portfolioItemId', portfolioItemId);
		ajaxURL.setParameter('portfolioId', '<%= portfolioId %>');
		ajaxURL.setWindowState('<%= LiferayWindowState.POP_UP.toString() %>');	 
    
        AUI().use('aui-dialog', function(A) {
			Liferay.Util.openWindow({
            	dialog: {
                	centered: true,
                    modal: true,
                    width: 600,
                    height: 400,
                    destroyOnHide: true,
                    resizable: false           
               	},
                id: '<portlet:namespace/>editPortfolioItemPopup',
                title: 'Edit Portfolio Item',
               	uri: ajaxURL
           	}); 
           	Liferay.provide(
            	window, '<portlet:namespace/>reloadPortlet', function() {
                	Liferay.Portlet.refresh('#p_p_id<portlet:namespace />');
                }
            );
        });
    }
    
    function showMoreDetails(assetId, purchasedFx, currentFx){
    	AUI().ready(
		  'aui-tooltip',
		  function(A) {
		    new A.Tooltip(
		      {
		        align: {
		          points: ['bc', 'tc']
		        },
		        bodyContent: '<div>' +
		        			 	'<ul>' +
		        			 		'<li>Purchased Fx: ' + purchasedFx + '</li>' +	
		        				'</ul>' +
		        			 '</div>',
		        trigger: '#viewMoreDetails'
		      }
		    ).render();
		  }
		);
    }
</aui:script>