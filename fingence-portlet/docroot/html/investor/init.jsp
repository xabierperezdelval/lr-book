<%@ include file="/html/init.jsp"%>

<%@page import="com.fingence.slayer.model.Portfolio"%>
<%@page import="com.fingence.slayer.service.PortfolioLocalServiceUtil"%>

<%
	HttpServletRequest httpRequest = PortalUtil.getOriginalServletRequest(request);
	long investorId = ParamUtil.getLong(httpRequest, "investorId");
%>

<h1><%= investorId %></h1>