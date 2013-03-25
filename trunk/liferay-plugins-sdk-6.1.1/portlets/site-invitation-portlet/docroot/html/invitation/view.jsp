<%@page import="com.liferay.portal.kernel.dao.search.SearchContainer"%>

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
		<%-- @include file="/html/invitation/statistics.jspf"--%>
		<c:choose>
			<c:when test="<%= SiteInvitationLocalServiceUtil.userHasInvitations(user.getUserId()) %>">
				<%
					int sentInvitations = SiteInvitationLocalServiceUtil.getUserInvitationsCount(user.getUserId(), InvitationConstants.STATUS_ALL);
					int acceptedInvitations = SiteInvitationLocalServiceUtil.getUserInvitationsCount(user.getUserId(), InvitationConstants.STATUS_ACCEPTED);
					int pendingInvitations = sentInvitations - acceptedInvitations;
				%>
				<aui:layout>
					<aui:column first="true" cssClass="aui-w25">
						<liferay-ui:message key="total-invitations-sent"/>: <b><%= sentInvitations %></b> 
						<br/><liferay-ui:message key="invitations-accepted"/>: <b><%= acceptedInvitations %></b>
						<br/><liferay-ui:message key="acceptance-ratio"/>: <b><%= Math.round(((float) acceptedInvitations / (float)sentInvitations) * 100) %>%</b>
						<br/><liferay-ui:message key="total-points-earned"/>: <b><%= SiteInvitationLocalServiceUtil.getTotalPoints(user.getUserId(), pointsForInviting, pointsForAccepting) %></b>
						<br/><liferay-ui:message key="your-rank"/>: <b><%= SiteInvitationLocalServiceUtil.getUserRank(user.getUserId()) %></b>
					</aui:column>
					<aui:column last="true" cssClass="aui-w75">
						<script type="text/javascript" src="https://www.google.com/jsapi"></script>
					    <script type="text/javascript">
					    	google.load("visualization", "1", {packages:["corechart"]});
					      	google.setOnLoadCallback(drawChart);
					      	function drawChart() {
					        	var data = google.visualization.arrayToDataTable([
									['Status', 'Count'],                                              
					          		['Pending Invitations', '<%= pendingInvitations %>'],
					          		['Accepted Invitations', '<%= acceptedInvitations %>']
								]);
					
					        	var options = {
					          		title: 'Statitsics'
					        	};
					
					        	var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
					        	chart.draw(data, options);
					      	}
					    </script>
					    <div id="chart_div" style="width: 900px; height: 500px;"></div>
					</aui:column>
				</aui:layout>
				<hr/>
				<i>
				<liferay-ui:message key="points-for-inviting"/>: <b><%= pointsForInviting %></b>, 
				<liferay-ui:message key="points-for-accepting"/>: <b><%= pointsForAccepting %></b>
				</i>
			</c:when>
			<c:otherwise>
				<liferay-ui:message key="no-invitations-sent" />
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>