<%@page import="com.liferay.portal.kernel.util.ListUtil"%>
<%@include file="/html/library/init.jsp" %>

<h1>List of books in our Library</h1>

<%
	List<LMSBook> books = LMSBookLocalServiceUtil.getLMSBooks(0, -1);

	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("jspPage", LibraryConstants.PAGE_LIST);
	
	PortletURL deleteBookURL = renderResponse.createActionURL(); 
	deleteBookURL.setParameter(ActionRequest.ACTION_NAME, 
			LibraryConstants.ACTION_DELETE_BOOK);
	deleteBookURL.setParameter("redirectURL", iteratorURL.toString());
	
	PortletURL bookDetailsURL = renderResponse.createRenderURL();
	bookDetailsURL.setParameter("jspPage", LibraryConstants.PAGE_DETAILS);
	bookDetailsURL.setParameter("backURL", themeDisplay.getURLCurrent());
%>

<liferay-ui:search-container delta="4" 
	emptyResultsMessage="Sorry. There are no items to display."
	iteratorURL="<%= iteratorURL %>">
	<liferay-ui:search-container-results 
		total="<%= books.size() %>"
		results="<%= ListUtil.subList(books, 
						searchContainer.getStart(),
						searchContainer.getEnd()) %>"/>
						
	<liferay-ui:search-container-row modelVar="book"
		className="LMSBook">
		<% bookDetailsURL.setParameter("bookId", Long.toString(book.getBookId())); %>
		<liferay-ui:search-container-column-text name="Book Title"
			property="bookTitle" href="<%= bookDetailsURL.toString() %>" />
		<liferay-ui:search-container-column-text name="Author"
			property="author" />
		<liferay-ui:search-container-column-text name="Date Added">
			<fmt:formatDate value="<%= book.getCreateDate() %>"
				pattern="dd/MMM/yyyy" />
		</liferay-ui:search-container-column-text>
		
		<% deleteBookURL.setParameter("bookId", Long.toString(book.getBookId())); %>
		<liferay-ui:search-container-column-text name="Delete" 
			href="<%= deleteBookURL.toString() %>" value="delete &raquo;"/>
			
		<liferay-ui:search-container-column-jsp name="Actions"
			path="<%= LibraryConstants.PAGE_ACTIONS %>" />
	</liferay-ui:search-container-row>
	
	<liferay-ui:search-iterator 
		searchContainer="<%= searchContainer %>" />
</liferay-ui:search-container>

<br/><a href="<portlet:renderURL/>">&laquo; Go Back</a>