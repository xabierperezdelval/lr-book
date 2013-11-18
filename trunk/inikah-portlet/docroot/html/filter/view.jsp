<%@ include file="/html/filter/init.jsp" %>

<portlet:actionURL var="saveCriteriaURL" name="saveCriteria"/>

<aui:form action="<%= saveCriteriaURL %>">
	<liferay-ui:panel-container accordion="true" extended="true">
		<liferay-ui:panel title="basic-info" collapsible="true" defaultState="close">
			<%@ include file="/html/filter/basic-info.jspf" %>
		</liferay-ui:panel>
	</liferay-ui:panel-container>	
	<aui:button name="" type="submit" value="apply"/>
</aui:form>