<%@ include file="/html/init.jsp" %>

<%	
	String pageName = layout.getName(locale).toLowerCase();
	String jspfPath = "/html/main/profile-" + pageName + StringPool.PERIOD + "jsp";
	
	Profile profile = (Profile) request.getAttribute("PROFILE");
%>

<jsp:include page="<%= jspfPath %>" flush="false"/>