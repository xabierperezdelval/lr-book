<%@ include file="/html/common/init.jsp" %>

<% 
	ResultRow row = (ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
	Profile match = (Profile) row.getObject();
%>

<%= match.getProfileName() %> 

<a href="javascript:void();" onclick="javascript:expandDiv(<%= match.getProfileId() %>);"><liferay-ui:message key="show-details"/></a>

<div id="<%= match.getProfileId() %>_details" hidden="<%= true %>">
	Show here the details....
</div>

<script type="text/javascript">
	function expandDiv(profileId) {
		var div = document.getElementById(profileId + "_details");
		alert(div);
		
		div.style.display = 'block';
	}
</script>