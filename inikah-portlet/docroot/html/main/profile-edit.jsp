<%@ include file="/html/init.jsp" %>

<%
	Profile profile = (Profile) request.getAttribute("PROFILE");

	String tabNames = "Step 1,Step 2,Step 3";
	int _currentStep = (profile.getStepsCompleted() % 3) + 1;
	String currentStep = "Step " + String.valueOf(_currentStep);
	String nextLabel = "Next";
	
	if (_currentStep == 3) {
		nextLabel = "Finish";
	}
%>

<h3>Update: <%= profile.getTitle() %></h3>

<portlet:actionURL var="saveProfileURL" name="saveProfile"/>
<portlet:actionURL var="previousStepURL" name="gotoPreviousStep">
	<portlet:param name="" value=""/>
</portlet:actionURL>

<aui:form action="<%= saveProfileURL %>">

	<aui:input name="profileId" type="hidden" value="<%= profile.getProfileId() %>"/>
	<aui:input name="currentStep" type="hidden" value="<%= _currentStep %>"/>
	<liferay-ui:tabs names="<%= tabNames %>" refresh="<%= false %>" value="<%= currentStep %>">
		<liferay-ui:section>
			<%@ include file="/html/main/edit/step1.jspf" %>
		</liferay-ui:section>
		
		<liferay-ui:section>
			<%@ include file="/html/main/edit/step2.jspf" %>
		</liferay-ui:section>
		
		<liferay-ui:section>
			<%@ include file="/html/main/edit/step3.jspf" %>
		</liferay-ui:section>		
	</liferay-ui:tabs>
	
	<c:if test="<%= _currentStep > 1 %>">
		<aui:button type="button" value="Prev" />
	</c:if>
	<aui:button type="submit" value="<%= nextLabel %>"/>

</aui:form>