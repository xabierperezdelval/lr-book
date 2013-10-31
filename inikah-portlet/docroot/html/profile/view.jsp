<%@ include file="/html/common/init.jsp" %>

<%	
	String pageName = layout.getName(locale).toLowerCase();
	String jspfPath = "/html/profile/profile-" + pageName + StringPool.PERIOD + "jsp";
%>

<jsp:include page="<%= jspfPath %>" flush="false"/>