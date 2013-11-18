<%@ include file="/html/edit/init.jsp" %>

<portlet:actionURL var="saveProfileURL" name="saveProfile"/>

<aui:form action="<%= saveProfileURL %>">	
	<c:choose>
		<c:when test="<%= editMode %>">
			<%@ include file="/html/edit/edit-mode.jspf" %>
		</c:when>
		<c:otherwise>
			<%@ include file="/html/edit/add-mode.jspf" %>
		</c:otherwise>
	</c:choose>
</aui:form>