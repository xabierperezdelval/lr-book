<%@page import="com.liferay.portal.kernel.util.ListUtil"%>
<%@page import="com.slayer.service.ProfileLocalServiceUtil"%>

<%@include file="/html/init.jsp" %>

<portlet:renderURL var="addBrideURL">
	<portlet:param name="jspPage" value="/html/profile/step1.jsp"/>
	<portlet:param name="bride" value="true"/>
</portlet:renderURL>

<portlet:renderURL var="addGroomURL">
	<portlet:param name="jspPage" value="/html/profile/step1.jsp"/>
	<portlet:param name="bride" value="false"/>
</portlet:renderURL>

<aui:a href="<%= addBrideURL %>" label="add-bride"/> | <aui:a href="<%= addGroomURL %>" label="add-groom"/>

<%
	List<Profile> userProfiles = ProfileLocalServiceUtil.getUserProfiles(user.getUserId());
%>

<liferay-ui:search-container emptyResultsMessage="no-user-profiles" delta="4">
	<liferay-ui:search-container-results 
		total="<%= userProfiles.size() %>" 
		results="<%= ListUtil.subList(userProfiles, searchContainer.getStart(), searchContainer.getEnd()) %>" />
	
	<liferay-ui:search-container-row className="com.slayer.model.Profile" modelVar="profile">
		<liferay-ui:search-container-column-text property="profileName" name="profile-name"/>
	</liferay-ui:search-container-row>
	
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>