<%@page import="com.fingence.slayer.service.AssetLocalServiceUtil"%>
<%@ include file="/html/fingence/init.jsp" %>

This is the <b>Main Portlet</b> portlet in View mode.

<%	
	AssetLocalServiceUtil.importFromExcel(user.getUserId(), null);
%>