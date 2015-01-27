<%@include file="/html/init.jsp" %>

<%@page import="com.slayer.service.ProfileLocalServiceUtil"%>
<%@page import="com.portlet.MyListUtil"%>

<%
	Profile profile = null;

	long profileId = ParamUtil.getLong(request, "profileId");

	if (profileId > 0l) {
		profile = ProfileLocalServiceUtil.fetchProfile(profileId);
	} else {
		boolean bride = ParamUtil.getBoolean(request, "bride");
		profile = ProfileLocalServiceUtil.init(user.getUserId(), bride);
	}
%>