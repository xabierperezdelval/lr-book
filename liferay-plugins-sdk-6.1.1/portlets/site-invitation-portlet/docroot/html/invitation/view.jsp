<%@include file="/html/invitation/init.jsp"%>
 
<%
	String tabs1 = ParamUtil.getString(request, "tabs1", InvitationConstants.TAB_STATISTICS);
%>

<portlet:renderURL var="portletURL" />

<liferay-ui:tabs names="statistics,invite-friends,my-invitations" 
	param="tabs1" refresh="<%= true %>"
	url="<%= portletURL %>"
/>

<c:choose>
	<c:when test="<%= tabs1.equalsIgnoreCase(InvitationConstants.TAB_INVITE_FRIENDS) %>">
		<%@include file="/html/invitation/invite-friends.jspf"%>
	</c:when>
	
	<c:when test="<%= tabs1.equalsIgnoreCase(InvitationConstants.TAB_MY_INVITATIONS) %>">
		<%@include file="/html/invitation/my-invitations.jspf"%>
	</c:when>
	
	<c:otherwise>
		<%@include file="/html/invitation/statistics.jspf"%>
	</c:otherwise>
</c:choose>