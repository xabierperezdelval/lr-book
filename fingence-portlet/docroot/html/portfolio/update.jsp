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
			<aui:input name="investorId" label="investor"/>
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
			<aui:input name="institutionId" label="institution"/>
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