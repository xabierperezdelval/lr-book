<%@include file="/html/library/init.jsp"%>

<portlet:actionURL var="quickAddURL" name="quickAdd" />

<aui:form action="<%= quickAddURL.toString() %>">
	<aui:input name="bookTitle"/>
	<aui:input name="author"/>
	<aui:button type="submit" />
</aui:form>

<hr/>
<portlet:actionURL var="setGreetingURL" name="setGreeting" />

<aui:form action="<%= setGreetingURL.toString() %>">
	<aui:input name="dailyGreeting"/>
	<aui:input name="LIFERAY_SHARED_HELLO" label="sharedHello" />
	<aui:button type="submit" />
</aui:form>