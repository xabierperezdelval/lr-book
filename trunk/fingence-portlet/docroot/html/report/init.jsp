<%@ include file="/html/init.jsp"%>

<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="com.liferay.portal.kernel.util.StringPool"%>
<%@page import="com.fingence.slayer.model.impl.PortfolioImpl"%>
<%@page import="com.fingence.slayer.model.impl.AssetImpl"%>
<%@page import="com.fingence.slayer.model.Asset"%>

<%@page import="com.fingence.slayer.model.Portfolio"%>
<%@page import="com.fingence.slayer.service.AssetLocalServiceUtil"%>
<%@page import="com.fingence.slayer.service.PortfolioLocalServiceUtil"%>
<%@page import="com.fingence.slayer.service.PortfolioItemLocalServiceUtil"%>
<%@page import="com.fingence.slayer.model.impl.PortfolioItemImpl"%>
<%@page import="com.fingence.slayer.model.PortfolioItem"%>


<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">

<%
	String layoutName = StringPool.BLANK;
	if(portletSession.getAttribute("navigationParam",PortletSession.APPLICATION_SCOPE) != null){
		layoutName = (String)portletSession.getAttribute("navigationParam",PortletSession.APPLICATION_SCOPE);
	} else{
		layoutName = layout.getName(locale);
	}
	
%>

<script src="//code.jquery.com/jquery-1.9.1.js"></script>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>

<script type="text/javascript" src="http://code.jquery.com/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.form.js"></script>

<c:if test="<%= !layoutName.equalsIgnoreCase(IConstants.PAGE_REPORTS_HOME) %>">
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<script src="//code.highcharts.com/highcharts.js"></script>
	<script src="//ajax.googleapis.com/ajax/libs/mootools/1.4.5/mootools-yui-compressed.js"></script>
	<script src="//code.highcharts.com/adapters/mootools-adapter.js"></script>
</c:if>
<script src="//underscorejs.org/underscore-min.js"></script>