<%@page import="com.liferay.portal.kernel.util.TextFormatter"%>
<%@page import="com.liferay.portal.model.Country"%>
<%@page import="com.liferay.portal.service.CountryServiceUtil"%>

<%@ include file="/html/init.jsp"%>

<portlet:actionURL var="registerURL" name="register" />

<aui:form action="<%= registerURL %>">

	<aui:row>
		<aui:column>
			<aui:input name="firstName" required="true" autoFocus="true"/>
		</aui:column>
		
		<aui:column>
			<aui:input name="lastName" />
		</aui:column>
	</aui:row>
	
	<aui:row>
		<aui:column>
			<aui:select name="male">
				<aui:option value="true" label="male"/>
				<aui:option value="false" label="female"/>
			</aui:select>
		</aui:column>
	
		<aui:select name="countryId" required="true" showEmptyOption="true" label="residing-country">
			<% 
				List<Country> countries = CountryServiceUtil.getCountries(true);
				for (Country country: countries) {
					%><aui:option value="<%= country.getCountryId() %>" label="<%= TextFormatter.formatName(country.getName()) %>"/><%
				}
			%>
		</aui:select>
	</aui:row>
	
	<aui:row>
		<aui:column>
			<aui:input name="emailAddress" required="true">
				<aui:validator name="email"/>
			</aui:input>
		</aui:column>
		
		<aui:column>
			<aui:input name="jobTitle" />
		</aui:column>		
	</aui:row>	
	
	<aui:row>
		<aui:column>
			<aui:select name="userType" showEmptyOption="true" required="true">
				<aui:option value="<%= IConstants.USER_TYPE_INVESTOR %>" label="investor"/>
				
				<c:choose>
					<c:when test="<%= themeDisplay.isSignedIn() %>">
						<aui:option value="<%= IConstants.USER_TYPE_REL_MANAGER %>" label="relationship-manager"/>
					</c:when>
					<c:otherwise>
						<aui:option value="<%= IConstants.USER_TYPE_WEALTH_ADVISOR %>" label="wealth-advisor"/>
					</c:otherwise>
				</c:choose>
			</aui:select>
		</aui:column>
		
		<aui:column>
			<c:choose>
				<c:when test="<%= themeDisplay.isSignedIn() %>">
					<aui:input name="firmName" readonly="true" value="<%= BridgeServiceUtil.getFirmName(user.getUserId()) %>"/>
				</c:when>
				<c:otherwise>
					<aui:input name="firmName" required="true" />
				</c:otherwise>
			</c:choose>
		</aui:column>		
	</aui:row>	

	<aui:button type="submit" />
</aui:form>