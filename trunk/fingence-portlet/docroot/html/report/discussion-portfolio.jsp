<%@ include file="/html/report/init.jsp"%>

<%
	long _portfolioId = ParamUtil.getLong(request, "portfolioId");
	Portfolio portfolio = null;
	if (_portfolioId > 0l) {
		portfolio = PortfolioLocalServiceUtil.fetchPortfolio(_portfolioId);
	}
%>

<portlet:actionURL var="discussionURL" name="discussOnPortfolio" />

<liferay-ui:discussion classPK="<%= portfolio.getPortfolioId() %>"
	userId="<%= userId %>"
	className="<%= Portfolio.class.getName() %>"
	formAction="<%= discussionURL %>" 
	ratingsEnabled="<%= true %>" 
	subject="<%= portfolio.getPortfolioName() %>"
/>