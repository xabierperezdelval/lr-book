<%@page import="com.liferay.portal.kernel.util.TextFormatter"%>
<%@page import="com.liferay.portal.model.Country"%>
<%@page import="com.liferay.portal.service.CountryServiceUtil"%>

<%@ include file="/html/init.jsp"%>

<portlet:actionURL var="registerURL" name="register" />

<aui:form action="<%= registerURL %>">
	<aui:fieldset>
		<aui:column>
			<aui:input name="firstName" required="true" autoFocus="true"/>

			<aui:input name="lastName" />
			
			<aui:input name="emailAddress" required="true">
				<aui:validator name="email"/>
			</aui:input>
		</aui:column>
		
		<aui:column>
			<aui:select name="male">
				<aui:option value="true" label="male"/>
				<aui:option value="false" label="female"/>
			</aui:select>
			
			<aui:select name="countryId" required="true" showEmptyOption="true" label="residing-country">
				<% 
					List<Country> countries = CountryServiceUtil.getCountries(true);
					for (Country country: countries) {
						%><aui:option value="<%= country.getCountryId() %>" label="<%= TextFormatter.formatName(country.getName()) %>"/><%
					}
				%>
			</aui:select>
			
			<aui:input name="jobTitle" />
				
			<aui:input name="firmName" required="true"/>
			
		</aui:column>
	</aui:fieldset>
	<aui:button type="submit" />
</aui:form>