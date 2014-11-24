<%@include file="/html/investor/init.jsp"%>

<liferay-portlet:actionURL var="configurationURL" portletConfiguration="true" />

<%
	int allocationBy = GetterUtil.getInteger(portletPreferences.getValue("allocationBy", "0"));
%>

<aui:form action="<%= configurationURL.toString() %>"> 
	<aui:input type="hidden" name="<%= Constants.CMD %>" value="<%= Constants.UPDATE %>" />
	<aui:select name="preferences--allocationBy--" showEmptyOption="true" required="true">
		<aui:option value="<%= IConstants.BREAKUP_BY_CURRENCY %>" label="Currency" selected="<%= allocationBy == IConstants.BREAKUP_BY_CURRENCY %>"/>
		<aui:option value="<%= IConstants.BREAKUP_BY_INDUSTRY_SECTOR %>" label="Industry Sector" selected="<%= allocationBy == IConstants.BREAKUP_BY_INDUSTRY_SECTOR %>"/>
		<aui:option value="<%= IConstants.BREAKUP_BY_RISK_COUNTRY %>" label="Risk Country" selected="<%= allocationBy == IConstants.BREAKUP_BY_RISK_COUNTRY %>"/>
		<aui:option value="<%= IConstants.BREAKUP_BY_ASSET_CLASS %>" label="Asset Class" selected="<%= allocationBy == IConstants.BREAKUP_BY_ASSET_CLASS %>"/>
	</aui:select>
	<aui:button type="submit" /> 
</aui:form>