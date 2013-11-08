<%@ include file="/html/common/init.jsp" %>
<%@ include file="/html/payment/init.jsp" %>

<%
	long profileId = ParamUtil.getLong(request, "profileId");	
	long planId = ParamUtil.getLong(request, "planId");
	
	Profile profile = ProfileLocalServiceUtil.fetchProfile(profileId);
	
	long paymentId = (Long) request.getAttribute("paymentId");
	
	double finalPrice = GetterUtil.getDouble(portletSession.getAttribute("FINAL_AMOUNT"));
%>

<liferay-ui:panel-container accordion="<%= true %>">
	<liferay-ui:panel title="abcd" extended="<%= true %>" collapsible="<%= true %>" >
		<%@ include file="/html/payment/paypal-direct.jspf" %>
	</liferay-ui:panel>
	
	<liferay-ui:panel title="1234" extended="<%= true %>" collapsible="<%= true %>" defaultState="close">
		<%@ include file="/html/payment/paypal-signin.jspf" %>
	</liferay-ui:panel>	
	
	<liferay-ui:panel title="xxxx" extended="<%= true %>" collapsible="<%= true %>" defaultState="close">
		<%@ include file="/html/payment/wire-transfer.jspf" %>
	</liferay-ui:panel>		
	
	<liferay-ui:panel title="xxxx" extended="<%= true %>" collapsible="<%= true %>" defaultState="close">
		<%@ include file="/html/payment/wire-transfer.jspf" %>
	</liferay-ui:panel>	

	<liferay-ui:panel title="xxxx" extended="<%= true %>" collapsible="<%= true %>" defaultState="close">
		<%@ include file="/html/payment/ccavenue.jspf" %>
	</liferay-ui:panel>		
	
	<liferay-ui:panel title="xxxx" extended="<%= true %>" collapsible="<%= true %>" defaultState="close">
		<%@ include file="/html/payment/cheque-dd.jspf" %>
	</liferay-ui:panel>			
	
	<liferay-ui:panel title="xxxx" extended="<%= true %>" collapsible="<%= true %>" defaultState="close">
		<%@ include file="/html/payment/pay-at-door.jspf" %>
	</liferay-ui:panel>		
</liferay-ui:panel-container>