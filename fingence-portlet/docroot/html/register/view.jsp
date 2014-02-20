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
			<aui:select name="male" label="gender">
				<aui:option value="true" label="male"/>
				<aui:option value="false" label="female"/>
			</aui:select>
		</aui:column>
	
		<aui:select name="countryId" required="true" showEmptyOption="true" label="residing-country">
			<% 
				List<Country> countries = CountryServiceUtil.getCountries(true);
				for (Country country: countries) {
					%><aui:option value="<%= country.getCountryId() %>" label="<%= TextFormatter.format(country.getName(), TextFormatter.J) %>"/><%
				}
			%>
		</aui:select>
	</aui:row>
	
	<aui:row>
		<aui:column>
			<aui:input name="emailAddress" required="true">
				<aui:validator name="email"/>
				<aui:validator name="custom" errorMessage="email-already-exists">
					function() {
						return notExists('email');					
					}
				</aui:validator>
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
					<aui:input name="firmName" readonly="true" value="<%= BridgeServiceUtil.getFirmName(userId) %>"/>
				</c:when>
				<c:otherwise>
					<aui:input name="firmName" required="true">
						<aui:validator name="custom" errorMessage="firm-already-exists">
							function() {
								return notExists('firm');					
							}
						</aui:validator>					
					</aui:input>
				</c:otherwise>
			</c:choose>
		</aui:column>
	</aui:row>	

	<c:choose>
		<c:when test="<%= themeDisplay.isSignedIn() %>">
			<aui:button value="create" onClick="javascript:addUser();"/>
		</c:when>
		<c:otherwise>
			<aui:button type="submit" />
		</c:otherwise>
	</c:choose>
</aui:form>

<aui:script>
	function notExists(fld) {
		var frm = document.<portlet:namespace/>fm;
		
		var ele = frm.<portlet:namespace/>emailAddress;
		if (fld == 'firm') {
			ele = frm.<portlet:namespace/>firmName;
		}
	
		var ajaxURL = Liferay.PortletURL.createResourceURL();
		ajaxURL.setPortletId('register_WAR_fingenceportlet');
		ajaxURL.setParameter('<%= Constants.CMD %>', '<%= IConstants.CMD_CHECK_DUPLICATE %>');
		ajaxURL.setParameter('fieldName', fld);
		ajaxURL.setParameter('fieldValue', ele.value);
		ajaxURL.setWindowState('<%= LiferayWindowState.EXCLUSIVE.toString() %>');
		
		var notExists = true;
		AUI().io.request('<%= themeDisplay.getURLPortal() %>' + ajaxURL, {
			sync: true,
			on: {
				success: function() {
					notExists = (!(eval(this.get('responseData'))));
				}
			}
		});
		
		return notExists;
	}
	
	<c:if test="<%= themeDisplay.isSignedIn() %>">
		function addUser(){
	        AUI().io.request('<%= registerURL %>',{
	
	            sync: true,
	            method: 'POST',
	            form: { id: '<portlet:namespace/>fm' },
	            on: {
	                success: function() {
	                    Liferay.Util.getWindow('_portfolio_WAR_fingenceportlet_addUserPopup').destroy();
	                    Liferay.Util.getOpener()._portfolio_WAR_fingenceportlet_reloadPortlet();
	                }
	              }
	         });
	    }	
	</c:if>
</aui:script>