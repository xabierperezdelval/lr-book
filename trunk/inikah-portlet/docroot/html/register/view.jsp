<%@ include file="/html/common/init.jsp" %>

<%@page import="com.inikah.slayer.service.InvitationLocalServiceUtil"%>
<%@page import="com.inikah.slayer.model.Invitation"%>

<% 
	String emailAddress = StringPool.BLANK;
	long invitationId = GetterUtil.getLong(PortalUtil.getOriginalServletRequest(request).getParameter("invitationId"), 0l);
	if (invitationId > 0l) {
		emailAddress = InvitationLocalServiceUtil.getInviteeEmail(invitationId);
	}
	long inviterId = GetterUtil.getLong(PortalUtil.getOriginalServletRequest(request).getParameter("inviterId"), 0l);
%>

<portlet:actionURL name="createAccount" var="createAccountURL"/>

<aui:form action="<%= createAccountURL %>" onSubmit="javascript:setValues();">
	
	<!-- birth day -->
	<aui:input name="male" type="hidden" value="1" />
	<aui:input name="firstName" type="hidden" value="<%= IConstants.PENDING_USR_FIRST_NAME %>" />
	<aui:input name="invitationId" type="hidden" value="<%= String.valueOf(invitationId) %>" />
	<aui:input name="inviterId" type="hidden" value="<%= String.valueOf(inviterId) %>" />
	
	<aui:fieldset>
		<aui:column>
			<aui:input autoFocus="true" name="profileName" label="profile-name" required="<%= true %>" />
			
			<aui:input name="emailAddress" required="<%= true %>" value="<%= emailAddress %>">
				<aui:validator name="email"/>
				<aui:validator name="custom" errorMessage="email-already-exists">
					function() {
						return emailNotExists();					
					}
				</aui:validator>				
			</aui:input>		
		</aui:column>
		
		<aui:column cssClass="mini-selection">
			<aui:select name="bride" label="profile-type" 
					showEmptyOption="<%= true %>" required="<%= true %>"> 
				<aui:option value="1" label="bride"/>
				<aui:option value="0" label="groom"/>
			</aui:select>
			
			<aui:select name="creatingForSelf" label="creating-for" 
					showEmptyOption="<%= true %>" required="<%= true %>">
				<aui:option value="1" label="self"/>
				<aui:option value="0" label="others"/>
			</aui:select>						
		</aui:column>		
	</aui:fieldset>
	
	<aui:input name="" type="checkbox" required="<%= true %>" label="i-agree-to-terms-and-conditions" />
	
	<aui:button type="submit" value="Register" />
</aui:form>

<aui:script>
	function setValues() {
		var frm = document.<portlet:namespace/>fm;
		var creatingForSelf = frm.<portlet:namespace/>creatingForSelf.value;
		if (creatingForSelf == 1) {
			frm.<portlet:namespace/>firstName.value = frm.<portlet:namespace/>profileName.value;
			
			if (frm.<portlet:namespace/>bride.value == 1) {
				frm.<portlet:namespace/>male.value = '0';
			} else {
				frm.<portlet:namespace/>male.value = '1';
			}
		} else {
			frm.<portlet:namespace/>firstName.value = '<%= IConstants.PENDING_USR_FIRST_NAME %>';
		}
	}

	function emailNotExists() {
		var frm = document.<portlet:namespace/>fm;
		var ele = frm.<portlet:namespace/>emailAddress;
	
		var ajaxURL = Liferay.PortletURL.createResourceURL();
		ajaxURL.setPortletId('register_WAR_inikahportlet');
		ajaxURL.setParameter('<%= Constants.CMD %>', '<%= IConstants.CMD_CHECK_DUPLICATE %>');
		ajaxURL.setParameter('emailAddress', ele.value);
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
</aui:script>