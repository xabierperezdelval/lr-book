<%@ include file="/html/investor/init.jsp"%>

<%
	List<Portfolio> portfolios = PortfolioLocalServiceUtil.getPortfolios(userId);
%>

<c:if test="<%= Validator.isNotNull(portfolios) %>">
	There are portfolios for this investor	
</c:if>

<br>Link to create a portfolio