<%@ include file="/html/common/init.jsp" %>

<%@page import="com.inikah.slayer.model.MatchCriteria"%>
<%@page import="com.inikah.slayer.service.MatchCriteriaLocalServiceUtil"%>

<%
	long profileId1 = profile.getProfileId();
	boolean bride = profile.isBride();
	MatchCriteria matchCriteria = MatchCriteriaLocalServiceUtil.fetchMatchCriteria(profileId1);
%>

