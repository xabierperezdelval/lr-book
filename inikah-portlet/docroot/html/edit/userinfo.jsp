<%@ include file="/html/edit/init.jsp" %>

<portlet:actionURL var="updateUserInfoURL" name="updateUserInfo" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>"/>

<aui:form action="<%= updateUserInfoURL %>">
	<aui:input name="userName" label="your-full-name"/>
	<aui:input type="checkbox" lable="i-am-a-female" name="female"/>
	<aui:button type="submit"/>
</aui:form>