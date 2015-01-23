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