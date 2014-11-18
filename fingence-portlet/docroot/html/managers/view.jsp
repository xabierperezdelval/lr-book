<%@ include file="/html/init.jsp"%>

<%
	List<User> managers = BridgeServiceUtil.getUsersByTargetType(user.getUserId(), IConstants.USER_TYPE_REL_MANAGER);

	for (User manager: managers) {
		%>
			div....
		<%
	}
%>