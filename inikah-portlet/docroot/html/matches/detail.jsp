<%@page import="com.inikah.slayer.service.InteractionServiceUtil"%>
<%@page import="com.inikah.slayer.service.InteractionLocalServiceUtil"%>
<%@ include file="/html/common/init.jsp" %>

This is working....
<%
	long matchingProfileId = ParamUtil.getLong(renderRequest, "matchingProfileId");

	InteractionServiceUtil.setViewed(profile.getProfileId(), matchingProfileId);
%>

<h1><%= matchingProfileId %></h1>