<%@ include file="/html/common/init.jsp" %>

<%
	String tgtPortlet = "backofis_WAR_inikahportlet"; 
	String tgtNamespace = StringPool.UNDERLINE + tgtPortlet + StringPool.UNDERLINE;
%>

<liferay-ui:header title="bacofis-profiles-pending"/>

<liferay-portlet:renderURL portletName="<%= tgtPortlet %>" var="targetURL" />

<% 
	String link = targetURL + StringPool.AMPERSAND + tgtNamespace + "status" + StringPool.EQUAL;
%>

<ul>
	<li><aui:a href="<%= link + IConstants.BACOFIS_STATUS_PAID %>" label="bacofis-profiles-paid"/>
		&nbsp;(<%= ProfileLocalServiceUtil.getProfilesWithStatusCount(IConstants.BACOFIS_STATUS_PAID) %>)
			
	<li><aui:a href="<%= link + IConstants.BACOFIS_STATUS_PLAN_PICKED %>" label="bacofis-plan-picked" />
		&nbsp;(<%= ProfileLocalServiceUtil.getProfilesWithStatusCount(IConstants.BACOFIS_STATUS_PLAN_PICKED) %>)
	
	<li><aui:a href="<%= link + IConstants.BACOFIS_STATUS_MODE_PICKED %>" label="bacofis-mode-picked" /> 
		&nbsp;(<%= ProfileLocalServiceUtil.getProfilesWithStatusCount(IConstants.BACOFIS_STATUS_MODE_PICKED) %>)
	
	<li><aui:a href="<%= link + IConstants.BACOFIS_STATUS_IN_PROGRESS %>" label="bacofis-in-progress" /> 
		&nbsp;(<%= ProfileLocalServiceUtil.getProfilesWithStatusCount(IConstants.BACOFIS_STATUS_IN_PROGRESS) %>)
</ul>