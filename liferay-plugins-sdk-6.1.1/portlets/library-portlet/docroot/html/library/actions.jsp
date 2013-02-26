<%@page import="com.library.security.LibraryPermissionUtil"%>
<%@page import="javax.portlet.WindowState"%>
<%@include file="/html/library/init.jsp"%>

<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>

<%@ taglib uri="http://liferay.com/tld/security" 
			prefix="liferay-security" %>
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

<liferay-security:permissionsURL 
	modelResource="<%= LMSBook.class.getName() %>" 
	modelResourceDescription="<%= book.getBookTitle() %>" 
	resourcePrimKey="<%= Long.toString(book.getBookId()) %>" 
	var="permissionsURL" />
	
<portlet:renderURL var="attachFilesURL">
	<portlet:param name="jspPage" 
			value="<%= LibraryConstants.PAGE_UPLOAD %>"/>
	<portlet:param name="redirectURL" 
			value="<%= themeDisplay.getURLCurrent() %>"/>
	<portlet:param name="bookId" 
			value="<%= Long.toString(book.getBookId()) %>"/>
</portlet:renderURL>	

<liferay-ui:icon-menu>

	<c:if test="<%= LibraryPermissionUtil.canEditThisBook(
			themeDisplay.getScopeGroupId(), 
			permissionChecker, book.getBookId()) %>">
		<liferay-ui:icon image="edit" message="Edit Book"
			url="<%= editBookURL.toString() %>" />
	</c:if>	
		
	<liferay-ui:icon image="view" message="View Details"
		url="<%= popup %>" />
	<liferay-ui:icon image="permissions" url="<%= permissionsURL %>"/>
	
	<liferay-ui:icon image="add_article" 
			message="add-attachments" url="<%= attachFilesURL %>"/>	
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