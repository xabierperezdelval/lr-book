<%@ include file="/html/init.jsp" %>

<% 
	Profile profile = (Profile) request.getAttribute("PROFILE");
%>

<h3>Update: <%= profile.getTitle() %></h3>