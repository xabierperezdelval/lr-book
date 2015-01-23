<%@page import="com.liferay.portal.kernel.util.StringPool"%>
<%@include file="/html/profile/init.jsp" %>

<liferay-ui:header title="<%= profile.getCode() + StringPool.COMMA + StringPool.BLANK + profile.getProfileName() %>" />

<portlet:actionURL var="saveProfileURL" name="saveProfile"/>

<aui:form action="<%= saveProfileURL %>">
	<aui:input name="step" type="hidden" value="1"/>
	<aui:input name="profileId" type="hidden" bean="<%= profile %>"/>
	<aui:input name="field1" required="true" autoFocus="true" bean="<%= profile %>"/>
	<aui:input name="field2" required="true" bean="<%= profile %>"/>
	<aui:button type="submit" />
</aui:form>