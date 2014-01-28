<%@ include file="/html/portfolio/init.jsp"%>

<portlet:renderURL var="defaultViewURL"/>

<portlet:actionURL name="savePortfolio" var="savePortfolioURL"/>

<aui:form action="<%= savePortfolioURL %>" enctype="multipart/form-data">

	<aui:row>
		<aui:column>
			<aui:input name="porfolioName" required="true" autoFocus="true"/>
		</aui:column>
		
		<aui:column cssClass="display-down">
			<aui:input type="checkbox" name="trial" label="this-is-a-trial"/>
		</aui:column>		
	</aui:row>

	<aui:row>
		<aui:column>
			<aui:select name="investorId" label="investor" required="true" showEmptyOption="true">
				<%
					List<User> users = BridgeServiceUtil.getInvestors(user.getUserId());
					for (User user: users) {
						%><aui:option value="<%= user.getUserId() %>" label="<%= user.getFullName() %>"/><% 
					}
				%>
			</aui:select>			
		</aui:column>
		
		<aui:column>
			<aui:input name="wealthAdvisorId" label="wealth-advisor"/>
		</aui:column>		
	</aui:row>
	
	<aui:row>
		<aui:column>
			<aui:input name="relationshipManagerId" label="relationship-manager"/>
		</aui:column>
		
		<aui:column>
			<aui:select name="institutionId" label="institution" required="true" showEmptyOption="true">
				<%
					List<Organization> institutions = BridgeServiceUtil.getInstitutions();
					for (Organization institution: institutions) {
						%><aui:option value="<%= institution.getOrganizationId() %>" label="<%= institution.getName() %>"/><% 
					}
				%>
			</aui:select>
		</aui:column>		
	</aui:row>	
	
	<aui:row>
		<aui:column>
			<aui:input type="file" name="excelFile" label="portfolio-assets"/>
		</aui:column>		
	</aui:row>		

	<aui:button-row>
		<aui:button type="submit" />
		<aui:a href="<%= defaultViewURL %>" label="cancel"/>
	</aui:button-row>
</aui:form>