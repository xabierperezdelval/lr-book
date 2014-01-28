<%@page import="com.fingence.slayer.service.AssetLocalServiceUtil"%>
<%@ include file="/html/portfolio/init.jsp"%>

<%
	List<Portfolio> portfolios = PortfolioLocalServiceUtil.getPortfolios(user.getUserId(), BridgeServiceUtil.getUserType(user.getUserId()));
	AssetLocalServiceUtil.importFromExcel(user.getUserId(), null);
%>

<portlet:renderURL var="addPortfolioURL">
	<portlet:param name="jspPage" value="/html/portfolio/update.jsp"/>
</portlet:renderURL>

<aui:button-row>
	<aui:button href="<%= addPortfolioURL %>" value="add-portfolio" />
</aui:button-row>

<liferay-ui:search-container delta="5" emptyResultsMessage="no-portfolios">
	<liferay-ui:search-container-results results="<%=ListUtil.subList(portfolios, searchContainer.getStart(),searchContainer.getEnd()) %>" total="<%= portfolios.size() %>"/>
	<liferay-ui:search-container-row className="com.fingence.slayer.model.Portfolio" modelVar="portfolio">
		<liferay-ui:search-container-column-text name="porfolioName"/>
		<liferay-ui:search-container-column-text name="Bank Name">
			<%= portfolio.getInstitutionId() %>
		</liferay-ui:search-container-column-text>
		<liferay-ui:search-container-column-text name="Relation Manager">
			<%= portfolio.getRelationshipManagerId() %>
		</liferay-ui:search-container-column-text>
		<liferay-ui:search-container-column-text name="Last Updated">
			<%= portfolio.getModifiedDate() %>
		</liferay-ui:search-container-column-text>
       <liferay-ui:search-container-column-text name="primary"/>
	
		</liferay-ui:search-container-row>
		<liferay-ui:search-iterator searchContainer="<%=searchContainer %>"/>
</liferay-ui:search-container>