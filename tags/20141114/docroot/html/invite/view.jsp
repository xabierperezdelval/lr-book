<%@ include file="/html/invite/init.jsp" %>

 <%
	String tabNames = "invite-friends,my-invitations,statistics";
	String tabs2 = renderRequest.getParameter("tabs2");
%>

<liferay-ui:tabs names="<%= tabNames %>" refresh="<%= false %>" value="<%= tabs2 %>">
	<liferay-ui:section>
		<%@ include file="/html/invite/invite-friends.jspf" %>
	</liferay-ui:section>
	
	<liferay-ui:section>
		<%@ include file="/html/invite/my-invitations.jspf" %>
	</liferay-ui:section>
	
	<liferay-ui:section>
		<%@ include file="/html/invite/statistics.jspf" %>
	</liferay-ui:section>		
</liferay-ui:tabs>	