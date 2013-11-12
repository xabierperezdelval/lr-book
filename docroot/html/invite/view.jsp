<%@page import="javax.portlet.ActionRequest"%>
<%@ include file="/html/common/init.jsp" %>

 <%
	Profile profile = (Profile) request.getAttribute("PROFILE");

	String tabNames = "Invite Friends,Pending Requests,Statistics";
	
	String tabs2 = renderRequest.getParameter("tabs2");
	
%>

<liferay-ui:tabs names="<%= tabNames %>" refresh="<%= false %>" value="<%=tabs2 %>">
	<liferay-ui:section>
		<%@ include file="/html/invite/invite-friends.jspf" %>
	</liferay-ui:section>
	
	<liferay-ui:section>
		<%@ include file="/html/invite/pending-requests.jspf" %>
	</liferay-ui:section>
	
	<liferay-ui:section>
		<%@ include file="/html/invite/statistics.jspf" %>
	</liferay-ui:section>		
</liferay-ui:tabs>	
