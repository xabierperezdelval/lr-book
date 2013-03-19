<%@include file="/html/invitation/init.jsp"%>

<portlet:renderURL var="inviteFriendsURL">
	<portlet:param  name="jspPage"  value="/html/invitation/invite_friends.jsp" />
</portlet:renderURL>

<portlet:renderURL var="myInvitationURL">
	<portlet:param name="jspPage" value="/html/invitation/my_invitation.jsp" />
</portlet:renderURL>

<portlet:renderURL var="statistics">
	<portlet:param name="jspPage" value="/html/invitation/statistics.jsp" />
</portlet:renderURL>

<%
	PortletSession pSession = renderRequest.getPortletSession();
	List<User> userList = (List<User>) pSession.getAttribute("alreadyMemberList", pSession.APPLICATION_SCOPE);
	if (SessionErrors.contains(renderRequest, "Alreadyused")) {
%>

<liferay-ui:error key="Alreadyused" message="invitation-fail"/>

<b>Rejected Users : </b>
<%
	if (Validator.isNotNull(userList)) {
				for (User users : userList) {
%>

	<li><%=users.getEmailAddress()%></li>
	
<%
			}
		}
		
	SessionErrors.clear(renderRequest);
}
	
%> 

<liferay-ui:tabs names="invite-friends,my-invitation,statistics"
	refresh="<%=false%>" url0="<%=inviteFriendsURL.toString()%>"
	url1="<%=myInvitationURL.toString()%>" url2="<%=statistics.toString()%>">
</liferay-ui:tabs>




