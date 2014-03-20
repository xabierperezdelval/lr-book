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
		AUI().ready(function(A) {
				Liferay.Service(
					'/fingence-portlet.myresult/get-my-results',
					{
						portfolioId : '<%= portfolioId %>'
					},
					function(data) {
						displayPortfolioItemDetails(data, '#myDataTable');
					}
				);
		});
		
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
</aui:script>