<%@page import="com.liferay.portal.kernel.util.UnicodeFormatter"%>
<%@page import="com.liferay.util.portlet.PortletProps"%>
<%@page import="com.liferay.portal.kernel.util.StringPool"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>

<%@include file="/html/invitation/init.jsp"%>

<liferay-portlet:actionURL portletConfiguration="true" var="configActionURL" />
<liferay-portlet:renderURL portletConfiguration="true" var="configRenderURL" />

<%
	String tabs2 = ParamUtil.getString(request, "tabs2", "general");
	String emailSubject = preferences.getValue("email-subject", 
			PortletProps.get("default-email-subject") + 
				StringPool.SPACE + themeDisplay.getURLPortal());
	
	String emailBody = preferences.getValue("email-body-template", ContentUtil.get(InvitationConstants.EMAIL_TEMPLATE_PATH));
%>

<aui:form action="<%= configActionURL %>" onSubmit="saveConfiguration();">
	<aui:input type="hidden" name="<%= Constants.CMD %>" value="<%= Constants.UPDATE %>" />
	
	<liferay-ui:tabs names="general,invitation-template" param="tabs2" url="<%= configRenderURL %>" refresh="<%= true %>"/>
	
	<c:choose>
		<c:when test="<%= tabs2.equals(InvitationConstants.TAB_CONFIG_TEMPLATE) %>">
			<aui:input type="hidden" name="tabs2" value="<%= InvitationConstants.TAB_CONFIG_TEMPLATE %>" />
			<aui:input label="subject" name="preferences--email-subject--" value="<%= emailSubject %>" size="70">
				<aui:validator name="required"/>
			</aui:input>
			<aui:field-wrapper label="email-body">
				<liferay-ui:input-editor initMethod="initEditor" />
				<aui:input name="preferences--email-body-template--" id="messageBodyId"
						type="hidden" />
			</aui:field-wrapper>
			
			<aui:script>
				function <portlet:namespace />initEditor() {
					return "<%= UnicodeFormatter.toString(emailBody) %>";
				}
				
				function saveConfiguration() {
					document.getElementById("<portlet:namespace/>messageBodyId").value = window.<portlet:namespace/>editor.getHTML();
				}
			</aui:script>

			<div class="definition-of-terms">
				<h4>
					<liferay-ui:message key="definition-of-terms" />
				</h4>
				<dl>
					<dt>[$FROM_INVITER$]</dt>
					<dd>Inviter Name</dd>
					<dt>[$INVITEE_EMAIL$]</dt>
					<dd>Invitee EMAIL</dd>
					<dt>[$INVITATION_URL$]</dt>
					<dd>Create account URL</dd>
				</dl>
			</div>
		</c:when>
		<c:otherwise>
			<aui:input type="hidden" name="tabs2" value="general" />
			<aui:input label="points-for-inviting" name="preferences--points-for-inviting--" value="<%= pointsForInviting %>" inlineLabel="true" size="3">
				<aui:validator name="required" />
				<aui:validator name="digits" />
				<aui:validator name="range">[1,50]</aui:validator>
			</aui:input>
			<br/>
			<aui:input label="points-for-accepting" name="preferences--points-for-accepting--"  value="<%= pointsForAccepting %>" inlineLabel="true" size="3">
				<aui:validator name="required" />
				<aui:validator name="digits" />
				<aui:validator name="range">[1,100]</aui:validator>
				<aui:validator name="custom" errorMessage="accepting-more-than-inviting">

				</aui:validator>
			</aui:input>
		</c:otherwise>
	</c:choose>
	<aui:button type="submit"/>
</aui:form>