<%@include file="/html/library/init.jsp"%>

<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>

<%       
	ResultRow row = (ResultRow) 
       	request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);		
	LMSBook book = (LMSBook) row.getObject();
	PortletURL editBookURL = renderResponse.createRenderURL(); 
	editBookURL.setParameter("bookId", Long.toString(book.getBookId()));
	editBookURL.setParameter("jspPage", LibraryConstants.PAGE_UPDATE);
	
	PortletURL viewBookURL = renderResponse.createRenderURL();
%>

<liferay-ui:icon-menu>
	<liferay-ui:icon image="edit" message="Edit Book"
		url="<%= editBookURL.toString() %>" />
	<liferay-ui:icon image="view" message="View Details"
		url="<%= viewBookURL.toString() %>" />
</liferay-ui:icon-menu>