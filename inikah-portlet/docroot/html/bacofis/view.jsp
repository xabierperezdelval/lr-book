<%@ include file="/html/bacofis/init.jsp" %>

<%
	String status = ParamUtil.getString(request, "status", IConstants.BACOFIS_STATUS_PAID);
	List<Profile> profiles = ProfileLocalServiceUtil.getProfilesWithStatus(status);
%>

<liferay-ui:search-container delta="10" emptyResultsMessage="bacofis-no-profiles">

	<liferay-ui:search-container-results 
		total="<%= profiles.size() %>"
		results="<%= ListUtil.subList(profiles, searchContainer.getStart(), searchContainer.getEnd()) %>"/>
		
	<liferay-ui:search-container-row className="com.inikah.slayer.model.Profile">
		<liferay-ui:search-container-column-jsp path="/html/matches/match.jsp"/>
	</liferay-ui:search-container-row>
	
	<liferay-ui:search-iterator searchContainer="<%= searchContainer %>" />
</liferay-ui:search-container>