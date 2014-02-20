<%@ include file="/html/portfolio/init.jsp"%>

<%@page import="com.fingence.slayer.model.impl.PortfolioImpl"%>

<portlet:actionURL name="savePortfolio" var="savePortfolioURL"
	windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>" />

<%
	long portfolioId = ParamUtil.getLong(request, "portfolioId");

	Portfolio portfolio = new PortfolioImpl();
	if (portfolioId > 0l) {
		portfolio = PortfolioLocalServiceUtil.fetchPortfolio(portfolioId);
	}
%>

<aui:form action="<%=savePortfolioURL %>" enctype="multipart/form-data">
	<aui:input type="hidden" name="portfolioId" value="<%= portfolio.getPortfolioId() %>"/>
	<aui:row>
		<aui:column>
			<aui:input name="portfolioName" required="true" autoFocus="true" value="<%= portfolio.getPortfolioName() %>"/>
		</aui:column>
		
		<aui:column cssClass="display-down">
			<aui:input type="checkbox" name="trial" label="this-is-a-trial" value="<%= portfolio.isTrial() %>"/>
		</aui:column>		
	</aui:row>

	<aui:row>
		<aui:column>
			<aui:select name="investorId" label="investor" required="true" showEmptyOption="true">
				<%
					List<User> users = BridgeServiceUtil.getUsersByTargetType(userId, IConstants.USER_TYPE_INVESTOR);
					for (User _user: users) {
						%><aui:option value="<%= _user.getUserId() %>" label="<%= _user.getFullName() %>" selected="<%= (_user.getUserId() == portfolio.getInvestorId()) %>" /><% 
					}
				%>
			</aui:select>			
		</aui:column>
		
		<aui:column>
			<aui:select name="wealthAdvisorId" label="wealth-advisor" required="true" showEmptyOption="true">
				<%
					List<User> users = BridgeServiceUtil.getUsersByTargetType(userId, IConstants.USER_TYPE_WEALTH_ADVISOR);
					for (User _user: users) {
						%><aui:option value="<%= _user.getUserId() %>" label="<%= _user.getFullName() %>" selected="<%= (_user.getUserId() == portfolio.getWealthAdvisorId()) %>" /><% 
					}
				%>
			</aui:select>			
		</aui:column>		
	</aui:row>
	
	<aui:row>
		<aui:column>
			<aui:select name="relationshipManagerId" label="relationship-manager" required="true" showEmptyOption="true">
				<%
					List<User> users = BridgeServiceUtil.getUsersByTargetType(userId, IConstants.USER_TYPE_REL_MANAGER);
					for (User _user: users) {
						%><aui:option value="<%= _user.getUserId() %>" label="<%= _user.getFullName() %>" selected="<%= (_user.getUserId() == portfolio.getRelationshipManagerId()) %>" /><% 
					}
				%>
			</aui:select>			
		</aui:column>
		
		<aui:column>
			<aui:select name="institutionId" label="institution" required="true" showEmptyOption="true">
				<%
					List<Organization> institutions = BridgeServiceUtil.getInstitutions();
					for (Organization institution: institutions) {
						%><aui:option value="<%= institution.getOrganizationId() %>" label="<%= institution.getName() %>" selected="<%= (institution.getOrganizationId() == portfolio.getInstitutionId()) %>" /><% 
					}
				%>
			</aui:select>
		</aui:column>		
	</aui:row>
	
		<c:if test="<%= (portfolioId == 0l) %>">
			<aui:input type="file" name="excelFile" label="portfolio-assets" required="true"/>
		</c:if>	
		
		<c:choose>
			<c:when test="<%= (portfolioId == 0l) %>">
				<aui:button type="submit" />
			</c:when>
			<c:otherwise>
				<aui:button type="button" onClick="javascript:updateInfo();" value="save" cssClass="btn-primary"/>
			</c:otherwise>
		</c:choose>
		
		
</aui:form>

<aui:script>
	function updateInfo() {
		AUI().io.request('<%= savePortfolioURL %>',{
			method: 'POST',
			form: { id: '<portlet:namespace/>fm' },
			on: {
				success: function() {
					Liferay.Util.getWindow('<portlet:namespace/>editPortfolioPopup').destroy();
                	Liferay.Util.getOpener().<portlet:namespace/>reloadPortlet();
    			}
  			}
 		});
	}
</aui:script >