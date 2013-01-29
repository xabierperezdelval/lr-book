<%@include file="/html/library/init.jsp"%>

<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>

<%       
	ResultRow row = (ResultRow) 
       	request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);		
	LMSBook book = (LMSBook) row.getObject();
	PortletURL editBookURL = renderResponse.createRenderURL(); 
	editBookURL.setParameter("bookId", Long.toString(book.getBookId()));
	editBookURL.setParameter("jspPage", LibraryConstants.PAGE_UPDATE);
	
	PortletURL viewBookURL = renderResponse.createRenderURL();
	viewBookURL.setWindowState(LiferayWindowState.POP_UP); 
	viewBookURL.setParameter("jspPage", LibraryConstants.PAGE_DETAILS);
	viewBookURL.setParameter("bookId",Long.toString(book.getBookId()));
	viewBookURL.setParameter("showHeader",Boolean.toString(false));
	
	String popup = "javascript:popup('"+ viewBookURL.toString()+"');";
%>

<liferay-ui:icon-menu>
	<liferay-ui:icon image="edit" message="Edit Book"
		url="<%= editBookURL.toString() %>" />
	<liferay-ui:icon image="view" message="View Details"
		url="<%= popup %>" />
</liferay-ui:icon-menu>

<aui:script>
   	function popup(url){
		AUI().use('aui-dialog', function(A) {
        	var dialog = new A.Dialog({
           		title: "Book Details",
           		centered: true,
           		modal: true,
           		width: 500,
           		height: 400,
           	}).plug(A.Plugin.IO, {uri: url}).render();
		});
	}
</aui:script>