<%@include file="/html/init.jsp" %>

<portlet:renderURL var="addProfileURL">
	<portlet:param name="jspPage" value="/html/profile/step1.jsp"/>
</portlet:renderURL>

<aui:a href="<%= addProfileURL %>" label="add-profile"/>