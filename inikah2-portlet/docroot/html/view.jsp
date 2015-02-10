<%@page import="com.slayer.service.ProfileLocalServiceUtil"%>

<%@include file="/html/init.jsp" %>

<c:choose>
	<c:when test="<%= ProfileLocalServiceUtil.hasIncompleteProfiles(userId) %>">
		<div class="alert">
			<liferay-ui:message key="incomplete-profile-message"/>
		</div>	
	</c:when>
	<c:otherwise>
		<portlet:actionURL var="addBrideURL" name="startProfile">
			<portlet:param name="bride" value="true"/>
		</portlet:actionURL>
		
		<portlet:actionURL var="addGroomURL" name="startProfile">
			<portlet:param name="bride" value="false"/>
		</portlet:actionURL>
		
		<aui:a href="<%= addBrideURL %>" label="add-bride"/> | <aui:a href="<%= addGroomURL %>" label="add-groom"/>	
	</c:otherwise>
</c:choose>

<%
	List<Profile> userProfiles = ProfileLocalServiceUtil.getUserProfiles(user.getUserId());
%>

<c:choose>
	<c:when test="<%= Validator.isNull(userProfiles) || userProfiles.isEmpty() %>">
		<hr/>
		<liferay-ui:journal-article articleId="USER_WELCOME"
			groupId="<%= themeDisplay.getScopeGroupId() %>" />
	</c:when>
	
	<c:otherwise>
		<liferay-ui:search-container emptyResultsMessage="no-user-profiles" delta="4">
			<liferay-ui:search-container-results 
				total="<%= userProfiles.size() %>" 
				results="<%= ListUtil.subList(userProfiles, searchContainer.getStart(), searchContainer.getEnd()) %>" />
			
			<liferay-ui:search-container-row className="com.slayer.model.Profile" modelVar="profile">
				<liferay-ui:search-container-column-text property="profileName" name="profile-name"/>
				
				<liferay-ui:search-container-column-jsp path="/html/actions.jsp" name="actions"/>
			</liferay-ui:search-container-row>
			
			<liferay-ui:search-iterator/>
		</liferay-ui:search-container>	
	</c:otherwise>
</c:choose>