<%@ include file="/html/init.jsp" %>

<%	
	String pageName = layout.getName(locale);
	String jspfPath = "/html/main/profile-" + pageName + StringPool.PERIOD + "jspf";
%>

<jsp:include page="<%= jspfPath %>"/>