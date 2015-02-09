<%@include file="/html/profile/init.jsp" %>

<liferay-ui:header title="step2-title"/>

<portlet:actionURL var="saveProfileURL" name="saveProfile"/>

<aui:form action="<%= saveProfileURL %>">
	<aui:input name="step" type="hidden" value="2"/>
	<aui:input name="profileId" type="hidden" bean="<%= profile %>" />
	
	<c:if test="<%= contact.getParentContactId() == 0 %>">
		<%
			String label = "You are " + (profile.isBride()? "bride" : "groom") + StringPool.APOSTROPHE + "s"; 
		%>
		<c:choose>
			<c:when test="<%= (profile.getCreatedFor() == 2) || (profile.getCreatedFor() == 7) %>">
				<aui:select name="male" required="true" showEmptyOption="true" label="<%= label %>" inlineLabel="true">
					<aui:option value="true" label="relation-father"/>
					<aui:option value="false" label="relation-mother"/>
				</aui:select>
			</c:when>
			
			<c:when test="<%= (profile.getCreatedFor() == 3) || (profile.getCreatedFor() == 8) %>">
				<aui:select name="male" required="true" showEmptyOption="true" label="<%= label %>" inlineLabel="true">
					<aui:option value="true" label="relation-brother"/>
					<aui:option value="false" label="relation-sister"/>
				</aui:select>
			</c:when>
			
			<c:when test="<%= (profile.getCreatedFor() == 4) || (profile.getCreatedFor() == 9) %>">
				<aui:select name="male" required="true" showEmptyOption="true" label="<%= label %>" inlineLabel="true">
					<aui:option value="true" label="relation-grand-father"/>
					<aui:option value="false" label="relation-grand-mother"/>
				</aui:select>
			</c:when>
			
			<c:when test="<%= (profile.getCreatedFor() == 5) || (profile.getCreatedFor() == 10) %>">
				<aui:select name="male" required="true" showEmptyOption="true" label="<%= label %>" inlineLabel="true">
					<aui:option value="true" label="relation-nephew"/>
					<aui:option value="false" label="relation-niece"/>
				</aui:select>
			</c:when>
			
			<c:when test="<%= (profile.getCreatedFor() == 6) || (profile.getCreatedFor() == 11) %>">
				<aui:select name="male" required="true" showEmptyOption="true" label="<%= label %>" inlineLabel="true">
					<aui:option value="true" label="relation-uncle"/>
					<aui:option value="false" label="relation-aunt"/>
				</aui:select>
			</c:when>
			
			<c:when test="<%= profile.getCreatedFor() == 12 %>">
				<aui:select name="male" required="true" showEmptyOption="true" label="<%= label %>" inlineLabel="true">
					<aui:option value="true" label="relation-cousin-brother"/>
					<aui:option value="false" label="relation-cousin-sister"/>
				</aui:select>
			</c:when>
			
			<c:when test="<%= profile.getCreatedFor() > 12 %>">
				<aui:select name="male" required="true" showEmptyOption="true" label="<%= label %>" inlineLabel="true">
					<aui:option value="true" label="relation-brother-in-islam"/>
					<aui:option value="false" label="relation-sister-in-islam"/>
				</aui:select>
			</c:when>			
		</c:choose>
	</c:if>
	
	<aui:button type="submit" />
</aui:form>