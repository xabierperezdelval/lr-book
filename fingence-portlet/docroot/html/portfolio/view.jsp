<%@page import="com.liferay.portal.util.PortalUtil"%>
<%@ include file="/html/portfolio/init.jsp"%>

<%
	List<Portfolio> portfolios = PortfolioLocalServiceUtil.getPortfolios(user.getUserId());
	
	PortletURL detailsURL = renderResponse.createRenderURL();
	detailsURL.setParameter("jspPage", "/html/portfolio/details.jsp");
	detailsURL.setParameter("backURL", themeDisplay.getURLCurrent());
%>

<portlet:renderURL var="addPortfolioURL">
	<portlet:param name="jspPage" value="/html/portfolio/update.jsp"/>
</portlet:renderURL>

<c:if test="<%= (userType != IConstants.USER_TYPE_BANK_ADMIN) %>">
	<aui:button-row>
		<aui:button href="<%= addPortfolioURL %>" value="add-portfolio" />
		<c:if test="<%= (userType == IConstants.USER_TYPE_WEALTH_ADVISOR) %>">
			<aui:button href="<%= PortalUtil.getCreateAccountURL(request, themeDisplay) %>" value="add-user" />
		</c:if>
	</aui:button-row>
</c:if>

<liferay-ui:search-container delta="5" emptyResultsMessage="no-portfolios">
	<liferay-ui:search-container-results results="<%=ListUtil.subList(portfolios, searchContainer.getStart(),searchContainer.getEnd()) %>" total="<%= portfolios.size() %>"/>
	<liferay-ui:search-container-row className="com.fingence.slayer.model.Portfolio" modelVar="portfolio">
		<%
			detailsURL.setParameter("portfolioId", String.valueOf(portfolio.getPortfolioId()));
		%>
		<liferay-ui:search-container-column-text name="porfolioName" href="<%= detailsURL.toString() %>"/>
		<liferay-ui:search-container-column-text name="institution">
			<%= BridgeServiceUtil.getOrganizationName(portfolio.getInstitutionId()) %>
		</liferay-ui:search-container-column-text>
		<liferay-ui:search-container-column-text name="assets">
			<%= PortfolioLocalServiceUtil.getAssetsCount(portfolio.getPortfolioId()) %>
		</liferay-ui:search-container-column-text>
		<liferay-ui:search-container-column-text name="Last Updated">
			<fmt:formatDate value="<%= portfolio.getModifiedDate() %>" pattern="dd/MMM/yyyy"/>
		</liferay-ui:search-container-column-text>
       <liferay-ui:search-container-column-text name="primary"/>
	
		</liferay-ui:search-container-row>
		<liferay-ui:search-iterator searchContainer="<%= searchContainer %>"/>
</liferay-ui:search-container>