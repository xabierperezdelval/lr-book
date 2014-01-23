<%@ include file="/html/common/init.jsp" %>
<%@ include file="/html/payment/init.jsp" %>

<%
	long planId = ParamUtil.getLong(request, "planId");
	
	long paymentId = (Long) request.getAttribute("paymentId");
	
	double finalPrice = GetterUtil.getDouble(portletSession.getAttribute("FINAL_AMOUNT"));
%>

<liferay-ui:panel-container accordion="<%= true %>">
	<liferay-ui:panel title="payment-option-use-credits" extended="<%= true %>" collapsible="<%= true %>" >
		<%@ include file="/html/payment/use-credits.jspf" %>
	</liferay-ui:panel>
	
	<liferay-ui:panel title="payment-option-paypal-direct" extended="<%= true %>" collapsible="<%= true %>" defaultState="close">
		<%@ include file="/html/payment/paypal-direct.jspf" %>
	</liferay-ui:panel>
	
	<liferay-ui:panel title="payment-option-paypal-signin" extended="<%= true %>" collapsible="<%= true %>" defaultState="close">
		<%@ include file="/html/payment/paypal-signin.jspf" %>
	</liferay-ui:panel>	
	
	<liferay-ui:panel title="payment-option-wire-transfer" extended="<%= true %>" collapsible="<%= true %>" defaultState="close">
		<%@ include file="/html/payment/wire-transfer.jspf" %>
	</liferay-ui:panel>		

	<liferay-ui:panel title="payment-option-cc-avenue" extended="<%= true %>" collapsible="<%= true %>" defaultState="close">
		<%@ include file="/html/payment/ccavenue.jspf" %>
	</liferay-ui:panel>		
	
	<liferay-ui:panel title="payment-option-cheque-dd" extended="<%= true %>" collapsible="<%= true %>" defaultState="close">
		<%@ include file="/html/payment/cheque-dd.jspf" %>
	</liferay-ui:panel>			
	
	<liferay-ui:panel title="payment-option-at-door" extended="<%= true %>" collapsible="<%= true %>" defaultState="close">
		<%@ include file="/html/payment/pay-at-door.jspf" %>
	</liferay-ui:panel>		
</liferay-ui:panel-container>