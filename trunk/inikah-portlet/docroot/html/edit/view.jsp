<%@ include file="/html/edit/init.jsp" %>

<h1><%= profile.getProfileName() %></h1>

<portlet:actionURL var="saveProfileURL" name="saveProfile"/>

<aui:form action="<%= saveProfileURL %>">

	<aui:input name="profileId" type="hidden" value="<%= profile.getProfileId() %>"/>
	<liferay-ui:tabs names="<%= tabNames %>" refresh="<%= false %>" value="step-1">
		<liferay-ui:section>
			<%@ include file="/html/profile/edit/step1.jspf" %>
		</liferay-ui:section>
		
		<liferay-ui:section>
			<%@ include file="/html/profile/edit/step2.jspf" %>
		</liferay-ui:section>
		
		<liferay-ui:section>
			<%@ include file="/html/profile/edit/step3.jspf" %>
		</liferay-ui:section>		
	</liferay-ui:tabs>

	<aui:button type="submit" value="Next &raquo;"/>

</aui:form>