<%@ include file="/html/common/init.jsp" %>

This is working....
<%
	long matchingProfileId = ParamUtil.getLong(renderRequest, "matchingProfileId");
%>

<h1><%= matchingProfileId %></h1>