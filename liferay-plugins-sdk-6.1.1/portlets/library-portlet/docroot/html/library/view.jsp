<%@include file="/html/library/init.jsp"%>

<portlet:renderURL var="updateBookURL">
	<portlet:param name="jspPage" value="<%= LibraryConstants.PAGE_UPDATE %>" />
</portlet:renderURL>
<br /><a href="<%= updateBookURL %>">Add new Book &raquo;</a>

<%
	PortletURL listBooksURL = renderResponse.createRenderURL();
	listBooksURL.setParameter("jspPage", "/html/library/list.jsp");
%>
&nbsp;|&nbsp;
<a href="<%= listBooksURL.toString() %>">Show All Books &raquo;</a>

<hr/>
<portlet:actionURL var="searchBooksURL" 
	name="<%= LibraryConstants.ACTION_SEARCH_BOOKS %>"  />
	
<aui:form action="<%= searchBooksURL.toString() %>">
	<aui:input name="searchTerm" label="Enter Title to search"/>
	<aui:input name="" type="submit" value="Search"/>
</aui:form>	