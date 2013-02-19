<%@include file="/html/library/init.jsp" %>

<h1>Portlet Settings</h1>

<portlet:actionURL var="setPreferencesURL" name="setPreferences"/>

<% 	String maxBooksLimit = portletPreferences.getValue("maxBooksLimit", ""); %>

<aui:form action="<%= setPreferencesURL.toString() %>">
	<aui:input name="maxBooksLimit" 
		value="<%= maxBooksLimit %>"/>
	<aui:button type="submit" value="Set Preferences" />
</aui:form>