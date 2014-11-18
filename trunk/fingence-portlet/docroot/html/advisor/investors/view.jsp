<%@page import="com.fingence.util.SecurityUtil"%>
<%@ include file="/html/init.jsp"%>

<%
	List<User> investors = BridgeServiceUtil.getUsersByTargetType(user.getUserId(), IConstants.USER_TYPE_INVESTOR);

	%><div class="row-fluid"><%
	for (User investor: investors) {
		String targetURL = "/investor?investorId=" + SecurityUtil.getEncrypted(investor.getUserId());
		%>
			<aui:a href="<%= targetURL %>">
				<div class="span4">
					<img class="thumbnail" src="<%= investor.getPortraitURL(themeDisplay) %>"/>
					<%= investor.getFullName() %>
				</div>
			</aui:a>
		<%
	}
	%></div><%
%>