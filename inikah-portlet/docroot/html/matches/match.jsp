<%@ include file="/html/common/init.jsp" %>

<% 
	ResultRow row = (ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
	Profile match = (Profile) row.getObject();
%>

<%= match.getProfileName() %> 

<portlet:renderURL var="ajaxURL" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>">
	<portlet:param name="jspPage" value="/html/matches/detail.jsp"/>
	<portlet:param name="matchingProfileId" value="<%= String.valueOf(match.getProfileId()) %>"/>
</portlet:renderURL>

<a href="javascript:void();" onclick="javascript:expandDiv(<%= match.getProfileId() %>);">
	<liferay-ui:message key="show-details"/>
</a>

<div id="<%= match.getProfileId() %>_details"></div>