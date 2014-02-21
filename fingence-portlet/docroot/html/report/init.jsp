<%@ include file="/html/init.jsp"%>

<%@page import="com.fingence.slayer.service.AssetLocalServiceUtil"%>
<%@page import="com.fingence.slayer.service.PortfolioLocalServiceUtil"%>
<%@page import="com.fingence.slayer.model.Portfolio"%>

<%
	String layoutName = layout.getName(locale);
%>

<c:if test="<%= !layoutName.equalsIgnoreCase(IConstants.PAGE_REPORTS_HOME) %>">
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<script src="//code.highcharts.com/highcharts.js"></script>
	<script src="//ajax.googleapis.com/ajax/libs/mootools/1.4.5/mootools-yui-compressed.js"></script>
	<script src="//code.highcharts.com/adapters/mootools-adapter.js"></script>
</c:if>
<script src="//underscorejs.org/underscore-min.js"></script>