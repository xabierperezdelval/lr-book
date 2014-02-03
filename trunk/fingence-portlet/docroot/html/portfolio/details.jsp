<%@ include file="/html/portfolio/init.jsp"%>



<%
	long portfolioId = ParamUtil.getLong(renderRequest, "portfolioId");
%>

<h1>This is the details page for <%= portfolioId %></h1>