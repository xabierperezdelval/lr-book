<%@ include file="/html/matches/init.jsp" %>
<%@page import="com.inikah.slayer.service.MatchCriteriaLocalServiceUtil"%>

<%
	List<Profile> matchResults = MatchCriteriaLocalServiceUtil.getMatches(profile.getProfileId());
	System.out.println("@@@@ " + PortalUtil.getPathMain());
%>

<liferay-ui:search-container delta="7" emptyResultsMessage="You don't have any matches">

	<liferay-ui:search-container-results 
		total="<%= matchResults.size() %>"
		results="<%= ListUtil.subList(matchResults, searchContainer.getStart(), searchContainer.getEnd()) %>"/>
		
	<liferay-ui:search-container-row className="com.inikah.slayer.model.Profile" modelVar="match">
		<%--
		<liferay-ui:search-container-column-text name="Profile-Id" property="profileId">
			<%= match.getProfileId() %>			
		</liferay-ui:search-container-column-text>
		
		<liferay-ui:search-container-column-text name="Name" property="profileName">
			<%= match.getProfileName() %>			
		</liferay-ui:search-container-column-text>
		 --%>
		
		<liferay-ui:search-container-column-jsp path="/html/matches/match.jsp"/>
	</liferay-ui:search-container-row>
	
	<liferay-ui:search-iterator searchContainer="<%= searchContainer %>" />
</liferay-ui:search-container>

<script type="text/javascript">
	function expandDiv(profileId) {
		var ajaxURL = Liferay.PortletURL.createRenderURL();
		ajaxURL.setPortletId('matches_WAR_inikahportlet');
		ajaxURL.setParameter("jspPage", "/html/matches/detail.jsp");
		ajaxURL.setParameter("matchingProfileId", profileId);
		ajaxURL.setWindowState('<%= LiferayWindowState.EXCLUSIVE.toString() %>');
		AUI().one('#' + profileId + '_details').load('<%= themeDisplay.getURLPortal() %>'+ajaxURL);
	}
</script>