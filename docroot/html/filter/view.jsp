<%@ include file="/html/filter/init.jsp" %>

<portlet:actionURL var="criteriaAddURL" name="add">
<%-- <portlet:param name="<%= ActionRequest.ACTION_NAME %>" value="criteriaAdd"/> --%>
</portlet:actionURL> 



<aui:form action="<%= criteriaAddURL.toString() %>">
<liferay-ui:panel-container accordion="true" extended="true">
	
	<liferay-ui:panel title="Age Criteria" collapsible="true" defaultState="close" helpMessage="Search by age">
		<%@ include file="/html/filter/age-criteria.jspf" %>
	</liferay-ui:panel>
		
	<liferay-ui:panel title="Height Criteria" collapsible="true" defaultState="close" helpMessage="Search by height">
		<%@ include file="/html/filter/height-criteria.jspf" %>
	</liferay-ui:panel>
	
<aui:button name="" type="submit" value="Search"/>
</liferay-ui:panel-container>


</aui:form>	