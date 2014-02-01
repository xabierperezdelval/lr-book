<%@page import="com.inikah.slayer.service.InvitationLocalServiceUtil"%>
<%@page import="com.inikah.slayer.model.Invitation"%>

<%@ include file="/html/common/init.jsp" %>

<% 
	PortletURL createAccountURL = PortletURLFactoryUtil.create(request, "58", plid, PortletRequest.ACTION_PHASE);
	createAccountURL.setWindowState(WindowState.NORMAL);
	createAccountURL.setPortletMode(PortletMode.VIEW);
	createAccountURL.setParameter("saveLastPath", "0");
	createAccountURL.setParameter("struts_action", "/login/create_account");
	
	String PORTLET_NSPACE = "_58_";
	
	String emailAddress = StringPool.BLANK;
	long invitationId = GetterUtil.getLong(PortalUtil.getOriginalServletRequest(request).getParameter("invitationId"), 0l);
	if (invitationId > 0l) {
		emailAddress = InvitationLocalServiceUtil.getInviteeEmail(invitationId);
	}
	long inviterId = GetterUtil.getLong(PortalUtil.getOriginalServletRequest(request).getParameter("inviterId"), 0l);
%>

<aui:form portletNamespace="<%= PORTLET_NSPACE %>" action="<%= createAccountURL.toString() %>" onSubmit="javascript:setValues();">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.ADD %>" />
	
	<!-- birth day -->
	<aui:input name="male" type="hidden" value="1" />
	<aui:input name="birthdayDay" type="hidden" value="1" />
	<aui:input name="firstName" type="hidden" value="<%= IConstants.PENDING_USR_FIRST_NAME %>" />
	<aui:input name="customRegistration" type="hidden" value="<%= true %>"/>
	<aui:input name="invitationId" type="hidden" value="<%= String.valueOf(invitationId) %>" />
	<aui:input name="inviterId" type="hidden" value="<%= String.valueOf(inviterId) %>" />
	
	<aui:fieldset>
		<aui:column>
			<aui:input autoFocus="true" name="profileName" label="profile-name" helpMessage="help-msg-profile-name" required="<%= true %>" />
			
			<aui:input name="emailAddress" required="<%= true %>" value="<%= emailAddress %>" helpMessage="help-msg-email-address">
				<aui:validator name="email"/>
			</aui:input>		
		</aui:column>
		
		<aui:column cssClass="mini-selection">
			<aui:select name="bride" label="profile-type" helpMessage="help-msg-profile-type"
					showEmptyOption="<%= true %>" required="<%= true %>" showRequiredLabel="<%= true %>"> 
				<aui:option value="1" label="bride"/>
				<aui:option value="0" label="groom"/>
			</aui:select>
			
			<aui:select name="creatingForSelf" label="creating-for" helpMessage="help-msg-creating-for"
					showEmptyOption="<%= true %>" required="<%= true %>" showRequiredLabel="<%= true %>">
				<aui:option value="1" label="self"/>
				<aui:option value="0" label="others"/>
			</aui:select>						
		</aui:column>		
	</aui:fieldset>
	
	<aui:input name="" type="checkbox" required="<%= true %>" label="i-agree-to-terms-and-conditions" showRequiredLabel="<%= false %>"/>
	
	<aui:button type="submit" value="Register" />
</aui:form>

<aui:script>
	function setValues() {
		var frm = document.<%= PORTLET_NSPACE %>fm;
		var creatingForSelf = frm.<%= PORTLET_NSPACE %>creatingForSelf.value;
		if (creatingForSelf == 1) {
			frm.<%= PORTLET_NSPACE %>firstName.value = frm.<%= PORTLET_NSPACE %>profileName.value;
			
			if (frm.<%= PORTLET_NSPACE %>bride.value == 1) {
				frm.<%= PORTLET_NSPACE %>male.value = '0';
			}
		}
	}
</aui:script>