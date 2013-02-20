<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
<%@page import="com.liferay.portal.kernel.dao.search.RowChecker"%>
<%@page import="java.util.Collections"%>
<%@page import="org.apache.commons.beanutils.BeanComparator"%>
<%@page import="com.liferay.portal.kernel.util.ListUtil"%>
<%@include file="/html/library/init.jsp" %>

<h1>List of books in our Library</h1>

<%
	List<LMSBook> booksTemp = (List<LMSBook>) 
			request.getAttribute("SEARCH_RESULT");
	
	List<LMSBook> books = Validator.isNotNull(booksTemp)? 
			ListUtil.copy(booksTemp) : 
			LMSBookLocalServiceUtil.getLibraryBooks(
				company.getCompanyId(), themeDisplay.getScopeGroupId());
			
	// additional code for sorting the list
	String orderByCol = (String) request.getAttribute("orderByCol");
	String orderByType = (String) request.getAttribute("orderByType");

	BeanComparator comp = new BeanComparator(orderByCol);
	
	booksTemp = ListUtil.copy(books);
	Collections.sort(booksTemp, comp);
	
	if (orderByType.equalsIgnoreCase("desc")) {
		Collections.reverse(booksTemp);
	}
	books = ListUtil.copy(booksTemp);

	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("jspPage", LibraryConstants.PAGE_LIST);

	PortletURL deleteBookURL = renderResponse.createActionURL();
	deleteBookURL.setParameter(ActionRequest.ACTION_NAME,
			LibraryConstants.ACTION_DELETE_BOOK);
	deleteBookURL.setParameter("redirectURL", iteratorURL.toString());

	PortletURL bookDetailsURL = renderResponse.createRenderURL();
	bookDetailsURL.setParameter("jspPage",
			LibraryConstants.PAGE_DETAILS);
	bookDetailsURL
			.setParameter("backURL", themeDisplay.getURLCurrent());
%>

<c:if test="<%= !books.isEmpty() %>">
	<% String functionName = 
		renderResponse.getNamespace() + "submitFormForAction();"; %>
	<aui:button-row>
		<aui:button value="delete" cssClass="delete-books-button"
			onClick="<%= functionName %>"/>
	</aui:button-row>
</c:if>

<portlet:actionURL 
	name="<%= LibraryConstants.ACTION_DELETE_BOOKS %>" 
	var="deleteBooksURL">
	<portlet:param name="redirectURL" 
		value="<%= iteratorURL.toString() %>"/>
</portlet:actionURL>

<aui:form action="<%= deleteBooksURL.toString() %>">
	<aui:input name="bookIdsForDelete" type="hidden" />
	<liferay-ui:search-container delta="4" 
		emptyResultsMessage="Sorry. There are no items to display."
		iteratorURL="<%= iteratorURL %>" 
		orderByCol="<%= orderByCol %>" orderByType="<%= orderByType %>" 
		rowChecker="<%= new RowChecker(renderResponse) %>">
		<liferay-ui:search-container-results 
			total="<%= books.size() %>"
			results="<%= ListUtil.subList(books, 
							searchContainer.getStart(),
							searchContainer.getEnd()) %>"/>
							
		<liferay-ui:search-container-row modelVar="book"
			className="LMSBook">
			<% bookDetailsURL.setParameter("bookId", Long.toString(book.getBookId())); %>
			<liferay-ui:search-container-column-text name="Book Title"
				property="bookTitle" href="<%= bookDetailsURL.toString() %>" 
				orderable="true" orderableProperty="bookTitle"/>
			<liferay-ui:search-container-column-text name="Author"
				property="author" orderable="true" orderableProperty="author" />
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
</aui:form>

<br/><a href="<portlet:renderURL/>">&laquo; Go Back</a>

<aui:script use="aui-base">

	var deleteBooksBtn = A.one('.delete-books-button');
	
	if (deleteBooksBtn != 'undefined') {
		var toggleDisabled = function(disabled) {
	    	deleteBooksBtn.one(':button').attr('disabled', disabled);
	        deleteBooksBtn.toggleClass('aui-button-disabled', disabled);
		};
	
	    var resultsGrid = A.one('.results-grid');
	
	    if (resultsGrid) {
	    	resultsGrid.delegate(
	        	'click',
	            function(event) {
	            	var disabled = (resultsGrid.one(':checked') == null);
	                toggleDisabled(disabled);
	           	},
	            ':checkbox'
	      	);
	 	}
	
	    toggleDisabled(true);
	}

	Liferay.provide(
	       window,
	       '<portlet:namespace />submitFormForAction',
	       function() {	
			var accepted = confirm('<%= 
				UnicodeLanguageUtil.get(pageContext, 
				"are-you-sure-you-want-to-delete-selected-books") %>');
	
		    if (accepted) {
		    	var frm = document.<portlet:namespace/>fm;
		    	var hiddenField = frm.<portlet:namespace/>bookIdsForDelete;
		    	
		    	hiddenField.value = 
		     		Liferay.Util.listCheckedExcept(
		     			frm, "<portlet:namespace/>allRowIds");
		     				
		      	submitForm(frm);
		    }
		},
	    ['liferay-util-list-fields']
	);      
</aui:script>