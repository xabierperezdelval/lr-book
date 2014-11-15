<%@ include file="/html/common/init.jsp" %>

<portlet:actionURL var="startURL" name="startProfile" />
 
<aui:form action="<%= startURL %>">
	<aui:input name="profileName"/>
	<aui:button type="submit" value="start"/>
</aui:form>