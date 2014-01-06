<%@page import="com.inikah.util.NotifyUtil"%>
<%@ include file="/html/common/init.jsp" %>

<%
	List<Profile> profiles = ProfileLocalServiceUtil.getProfilesForUser(user.getUserId());
%>

<liferay-ui:search-container delta="10" emptyResultsMessage="You don't have any profiles yet">

	<liferay-ui:search-container-results
		total="<%= profiles.size() %>"
		results="<%= ListUtil.subList(profiles, searchContainer.getStart(), searchContainer.getEnd()) %>"/>
		
	<liferay-ui:search-container-row className="Profile" modelVar="profile1">
		<liferay-ui:search-container-column-jsp 
			path="<%= IConstants.OWNER_VIEW %>"  />
	</liferay-ui:search-container-row>
	
	<liferay-ui:search-iterator searchContainer="<%= searchContainer %>" />
	
</liferay-ui:search-container>

<%
	long targetPlId = PageUtil.getPageLayoutId(scopeGroupId, "pay", locale);
%>

<liferay-portlet:renderURL plid="<%= targetPlId %>" portletName="payment_WAR_inikahportlet" refererPlid="<%= plid %>" var="payNowURL">
	<liferay-portlet:param name="profileId" value="<%= String.valueOf(14017) %>" />
</liferay-portlet:renderURL>

<aui:a href="<%= payNowURL %>">Pay Now</aui:a>

<%
	long matchesPlId = PageUtil.getPageLayoutId(scopeGroupId, "match", locale);
%>

<liferay-portlet:renderURL plid="<%= matchesPlId %>" portletName="matches_WAR_inikahportlet" refererPlid="<%= plid %>" var="matchNowURL">
	<liferay-portlet:param name="profileId" value="<%= String.valueOf(14017) %>" />
</liferay-portlet:renderURL>

<aui:a href="<%= matchNowURL %>">Match Now</aui:a>

<%
	long editPlId = PageUtil.getPageLayoutId(scopeGroupId, "edit", locale);
%>

<liferay-portlet:renderURL plid="<%= editPlId %>" portletName="edit_WAR_inikahportlet" refererPlid="<%= plid %>" var="editNowURL">
	<liferay-portlet:param name="profileId" value="<%= String.valueOf(14017) %>" />
</liferay-portlet:renderURL>

<aui:a href="<%= editNowURL %>">Edit Now</aui:a>