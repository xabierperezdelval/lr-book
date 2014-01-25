<%@ include file="/html/bacofis/init.jsp" %>

<%
	String tgtPortlet = "bacofis_WAR_inikahportlet"; 
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

	<li><aui:a href="<%= link + IConstants.BACOFIS_STATUS_SENT_BACK %>" label="bacofis-sent-back" />
		&nbsp;(<%= ProfileLocalServiceUtil.getProfilesWithStatusCount(IConstants.BACOFIS_STATUS_SENT_BACK) %>)
	
	<li><aui:a href="<%= link + IConstants.BACOFIS_STATUS_INACTIVE %>" label="bacofis-inactive" />
		&nbsp;(<%= ProfileLocalServiceUtil.getProfilesWithStatusCount(IConstants.BACOFIS_STATUS_INACTIVE) %>)
		
	<li><aui:a href="<%= link + IConstants.BACOFIS_STATUS_EXPIRED %>" label="bacofis-expired" />
		&nbsp;(<%= ProfileLocalServiceUtil.getProfilesWithStatusCount(IConstants.BACOFIS_STATUS_EXPIRED) %>)
		
	<li><aui:a href="<%= link + IConstants.BACOFIS_STATUS_DELETE_REQUESTED %>" label="bacofis-delete-requested" /> 
		&nbsp;(<%= ProfileLocalServiceUtil.getProfilesWithStatusCount(IConstants.BACOFIS_STATUS_DELETE_REQUESTED) %>)
		
	<li><aui:a href="<%= link + IConstants.BACOFIS_STATUS_MODIFIED %>" label="bacofis-modified" /> 
		&nbsp;(<%= ProfileLocalServiceUtil.getProfilesWithStatusCount(IConstants.BACOFIS_STATUS_MODIFIED) %>)
		
	<li><aui:a href="<%= link + IConstants.BACOFIS_STATUS_EXPIRING %>" label="bacofis-expiring" /> 
		&nbsp;(<%= ProfileLocalServiceUtil.getProfilesWithStatusCount(IConstants.BACOFIS_STATUS_EXPIRING) %>)		
</ul>