<%@ include file="/html/common/init.jsp" %>

<% 
	ResultRow row = (ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
	Profile profile1 = (Profile) row.getObject();
%>

<%= profile1.getProfileName() %>

<%
	long targetPlId = PageUtil.getPageLayoutId(scopeGroupId, "pay", locale);
%>

<liferay-portlet:renderURL plid="<%= targetPlId %>" portletName="payment_WAR_inikahportlet" refererPlid="<%= plid %>" var="payNowURL">
	<liferay-portlet:param name="profileId" value="<%= String.valueOf(profile1.getProfileId()) %>" />
</liferay-portlet:renderURL>

<aui:a href="<%= payNowURL %>">Pay Now</aui:a>

<%
	long matchesPlId = PageUtil.getPageLayoutId(scopeGroupId, "match", locale);
%>

<liferay-portlet:renderURL plid="<%= matchesPlId %>" portletName="matches_WAR_inikahportlet" refererPlid="<%= plid %>" var="matchNowURL">
	<liferay-portlet:param name="profileId" value="<%= String.valueOf(profile1.getProfileId()) %>" />
</liferay-portlet:renderURL>

<aui:a href="<%= matchNowURL %>">Match Now</aui:a>

<aui:a href="<%= URLUtil.editURL(profile1.getProfileId()) %>">Edit Now</aui:a>