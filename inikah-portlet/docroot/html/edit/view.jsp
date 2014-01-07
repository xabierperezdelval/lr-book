<%@ include file="/html/edit/init.jsp" %>

<portlet:actionURL var="saveProfileURL" name="saveProfile"/>

<%
	String enctype = StringPool.BLANK;
	if (profile.getStatus() == IConstants.PROFILE_STATUS_STEP3_DONE) {
		enctype = "multipart/form-data";
	}
%>

<aui:form action="<%= saveProfileURL %>" enctype="<%= enctype %>">
	<c:choose>
		<c:when test="<%= editMode %>">
			<%@ include file="/html/edit/edit-mode.jspf" %>
		</c:when>
		<c:otherwise>
			<%@ include file="/html/edit/add-mode.jspf" %>
		</c:otherwise>
	</c:choose>
</aui:form>