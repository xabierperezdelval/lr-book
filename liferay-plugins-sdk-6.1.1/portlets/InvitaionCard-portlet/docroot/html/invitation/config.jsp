<%@include file="/html/invitation/init.jsp"%>

<%
	PortletPreferences preferences = renderRequest.getPreferences();
	String portletResource = ParamUtil.getString(request, "portletResource");
	if (Validator.isNotNull(portletResource)) {
		preferences = PortletPreferencesFactoryUtil.getPortletSetup(
				request, portletResource);
	}

	String newMessageBody = preferences.getValue("newMessageBody", "");
	String invitationAcceptedPoint = preferences.getValue("accept", "");
	String invitationSentPoint = preferences.getValue("sent", "");
	String tabs1 = ParamUtil.getString(request, "tabs1", "general");
%>

<!-- Requirement starts -->

<liferay-portlet:renderURL portletConfiguration="true" var="portletURL">
	<portlet:param name="tabs1" value="<%=tabs1%>" />
</liferay-portlet:renderURL>

<liferay-portlet:actionURL var="configurationURL"
	portletConfiguration="true" />

<aui:form action="<%=configurationURL.toString()%>"
	onSubmit='saveConfiguration();'>
	<aui:input type="hidden" name="<%=Constants.CMD%>"
		value="<%=Constants.UPDATE%>" />
	<aui:input name="tabs1" type="hidden" value="<%=tabs1%>" />

	<liferay-ui:tabs names="general,invitation-notifications" param="tabs1"
		url="<%=portletURL%>" />

	<c:choose>

		<c:when test='<%=tabs1.equals("invitation-notifications")%>'>
			<aui:fieldset>
				<aui:input label="subject" name="invitation" value="Invitation" />
				<aui:field-wrapper label="body">
					<liferay-ui:input-editor initMethod="initEditor" />
					<aui:input name="preferences--newMessageBody--"
						id="newMessageBodyId" type="hidden" />
				</aui:field-wrapper>
			</aui:fieldset>

			<div class="definition-of-terms">
				<h4>
					<liferay-ui:message key="definition-of-terms" />
				</h4>
				<dl>
					<dt>[$FROM_INVITER$]</dt>
					<dd>Inviter Name</dd>
					<dt>[$INVITEE_NAME$]</dt>
					<dd>Invitee Name</dd>
					<dt>[$INVITATION_URL$]</dt>
					<dd>Create account URL so the Invitee can register himself in
						liferay.</dd>
				</dl>
			</div>
		</c:when>

		<c:otherwise>
			<h1>
				Points Scored After Invitation Accepted:<%=invitationAcceptedPoint%></h1>
			<h1>
				Points Scored After Invitation Sent:<%=invitationSentPoint%></h1>
		</c:otherwise>
	</c:choose>
	<aui:button type="submit" value="Save Settings" />
</aui:form>


<aui:script>
				function <portlet:namespace />initEditor() {
					return "<%=UnicodeFormatter.toString(newMessageBody)%>";
				}

				 function saveConfiguration() {
					document.getElementById("<portlet:namespace />newMessageBodyId").value = window.<portlet:namespace />editor.getHTML();
				}

				
</aui:script>
