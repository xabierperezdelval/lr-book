<%@ include file="/html/init.jsp"%>

<%
	List<User> investors = BridgeServiceUtil.getUsersByTargetType(user.getUserId(), IConstants.USER_TYPE_INVESTOR);

	%><div class="row-fluid"><%
	for (User investor: investors) {
		%>
			<div class="span4">
				<img class="thumbnail" src="<%= investor.getPortraitURL(themeDisplay) %>"/>
				<%= investor.getFullName() %>
			</div>
		<%
	}
	%></div><%
%>