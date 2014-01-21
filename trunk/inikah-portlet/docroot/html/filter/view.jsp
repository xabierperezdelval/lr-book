<%@ include file="/html/filter/init.jsp" %>

<c:choose>
	<c:when test="<%= Validator.isNull(matchCriteria) %>">
		<liferay-ui:message key="sorry-no-match-criteria-set"/>
	</c:when>
	
	<c:otherwise>
		<portlet:actionURL var="saveCriteriaURL" name="saveCriteria"/>
		
		<aui:form action="<%= saveCriteriaURL %>" onSubmit="populateCSVLists();">
			<liferay-ui:panel-container accordion="true" extended="true">
				<liferay-ui:panel title="basic-info" collapsible="true" defaultState="close">
					<%@ include file="/html/filter/basic-info.jspf" %>
				</liferay-ui:panel>
				
				<liferay-ui:panel title="social-info" collapsible="true" defaultState="close">
					<%@ include file="/html/filter/social-info.jspf" %>
				</liferay-ui:panel>		
			</liferay-ui:panel-container>
			<aui:button name="" type="submit" value="apply"/>
		</aui:form>	
	</c:otherwise>
</c:choose>