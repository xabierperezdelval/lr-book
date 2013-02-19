<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@include file="/html/library/init.jsp"%>	

<liferay-portlet:actionURL var="configurationURL" 
				portletConfiguration="true"	/>

<%	String libraryName = preferences.getValue("libraryName", ""); %>

<aui:form action="<%= configurationURL.toString() %>">
	<aui:input type="hidden" name="<%= Constants.CMD %>" 
				value="<%= Constants.UPDATE %>" />
	<aui:input name="preferences--libraryName--" value="<%= libraryName %>"/>
	<aui:button type="submit" value="Save Settings"/>
</aui:form>