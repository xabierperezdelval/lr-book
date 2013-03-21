<%@page import="com.liferay.portal.kernel.servlet.SessionMessages"%>
<%@include file="/html/invitation/init.jsp"%>
 
<%
	String tabs1 = ParamUtil.getString(request, "tabs1", InvitationConstants.TAB_STATISTICS);
%>
	
<c:if test="<%= SessionMessages.contains(portletRequest, InvitationConstants.KEY_MESSAGE_SUCCESS) %>">
	<div class="portlet-msg-success">
		<%= SessionMessages.get(portletRequest, InvitationConstants.KEY_MESSAGE_SUCCESS) %>
	</div>
</c:if>	
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