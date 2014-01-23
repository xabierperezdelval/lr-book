<%@ include file="/html/payment/init.jsp" %>

<%@page import="com.inikah.slayer.model.Plan"%>
<%@page import="com.inikah.slayer.service.PlanLocalServiceUtil"%>

<c:choose>
	<c:when test="<%= profile.getCurrentPlan() == 0 %>">
		<%@ include file="/html/payment/plan-selection.jspf" %>
	</c:when>
</c:choose>

<h1><%= profile.getCurrentPlan() %></h1>