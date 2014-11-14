<%@ include file="/html/matches/init.jsp" %>

<%@page import="com.inikah.slayer.service.InteractionServiceUtil"%>
<%@page import="com.inikah.slayer.service.InteractionLocalServiceUtil"%>

<%
	long matchingProfileId = ParamUtil.getLong(renderRequest, "matchingProfileId");

	InteractionServiceUtil.setViewed(profile.getProfileId(), matchingProfileId);
%>

<h1><%= matchingProfileId %></h1>