<%@ include file="/html/common/init.jsp" %>

<%
	long inCompleteProfileId = 0l;
	List<Profile> profiles = ProfileLocalServiceUtil.getProfilesForUser(user.getUserId());
	
	for (Profile _profile: profiles) {
		if (_profile.getStatus() <= IConstants.PROFILE_STATUS_STEP4_DONE) {
			inCompleteProfileId = _profile.getProfileId();
			break;
		}
	}
%>

<c:choose>
	<c:when test="<%= (inCompleteProfileId > 0l) %>">
		<liferay-portlet:renderURL var="editProfileURL" portletName="edit_WAR_inikahportlet" 
				plid="<%= PageUtil.getPageLayoutId(themeDisplay.getScopeGroupId(), Constants.EDIT, locale) %>">
			<portlet:param name="profileId" value="<%= String.valueOf(inCompleteProfileId) %>"/>
		</liferay-portlet:renderURL>
		One profile is in DRAFT state. <aui:a href="<%= editProfileURL %>" label="click-here"/> to complete.
	</c:when>
	
	<c:otherwise>
		<portlet:actionURL var="startURL" name="startProfile" />
		 
		<aui:form action="<%= startURL %>">
			<aui:input name="profileName" required="<%= true %>" />
			<aui:select name="bride" label="profile-type" 
					showEmptyOption="<%= true %>" required="<%= true %>"> 
				<aui:option value="1" label="bride"/>
				<aui:option value="0" label="groom"/>
			</aui:select>
			<aui:button type="submit" value="start"/>
		</aui:form>	
	</c:otherwise>
</c:choose>