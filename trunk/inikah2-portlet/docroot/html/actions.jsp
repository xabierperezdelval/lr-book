<%@include file="/html/init.jsp" %>

<%@page import="javax.portlet.PortletURL"%>
<%@page import="javax.portlet.ActionRequest"%>
<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>

<%
	ResultRow row = (ResultRow) 
	       	request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

	Profile _profile = (Profile) row.getObject();
%>

<portlet:actionURL var="deleteProfileURL" name="deleteProfile">
	<portlet:param name="profileId" value="<%= String.valueOf(_profile.getProfileId()) %>"/>
</portlet:actionURL>

<lui:icon-menu>
	<lui:icon image="delete" url="<%= deleteProfileURL %>"/>
</lui:icon-menu>