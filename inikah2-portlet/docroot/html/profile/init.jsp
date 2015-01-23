<%@page import="com.slayer.service.ProfileLocalServiceUtil"%>
<%@include file="/html/init.jsp" %>

<%

	Profile profile = null;

	long profileId = ParamUtil.getLong(request, "profileId");

	if (profileId > 0l) {
		profile = ProfileLocalServiceUtil.fetchProfile(profileId);
	} else {
		profile = ProfileLocalServiceUtil.init(user.getUserId());
	}
%>