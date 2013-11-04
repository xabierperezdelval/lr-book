<%@ include file="/html/common/init.jsp" %>

<%@page import="com.inikah.slayer.model.Plan"%>
<%@page import="com.inikah.slayer.service.PlanLocalServiceUtil"%>

<%
	Profile profile = (Profile) portletSession.getAttribute("PROFILE");
	
	List<Plan> plans = PlanLocalServiceUtil.getPlans(company.getCompanyId());
%>

<liferay-ui:header title="<%= profile.getTitle() %>"/>

<aui:layout cssClass="plan-box">
	<%
		for (Plan plan: plans) {
			%>
				<aui:column columnWidth="25">
					<div><%= plan.getPlanName() %></div>
					<div>Validity: <%= plan.getValidity() %> Month(s)</div>
					<div>(<%= plan.getValidity() * 30 %> Days)</div>
					<div><%= plan.getPrice(profile.getProfileId()) %></div>
					<div>Discount <%= plan.getDiscount() %>%</div>
					<div>What you pay: </div>
				</aui:column>			
			<% 
		}
	%>
</aui:layout>