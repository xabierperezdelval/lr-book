<%@ include file="/html/portfolio/init.jsp"%>

<portlet:renderURL var="defaultViewURL"/>

<aui:form>

	

	<aui:button-row>
		<aui:button />
		<aui:a href="<%= defaultViewURL %>" label="cancel"/>
	</aui:button-row>
</aui:form>