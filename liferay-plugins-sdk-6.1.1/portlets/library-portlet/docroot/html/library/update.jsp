<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="com.slayer.model.impl.LMSBookImpl"%>
<%@include file="/html/library/init.jsp"%>

<h1>Add / Edit Form</h1>

<%
	PortletURL updateBookURL = renderResponse.createActionURL();
	updateBookURL.setParameter(
			ActionRequest.ACTION_NAME, LibraryConstants.ACTION_UPDATE_BOOK);
	
	LMSBook lmsBook = new LMSBookImpl();
	long bookId = ParamUtil.getLong(request, "bookId");
	
	if (bookId > 0l) {
		lmsBook = LMSBookLocalServiceUtil.fetchLMSBook(bookId);
	}	
%>

<aui:form name="fm"
	action="<%= updateBookURL.toString() %>">
	
	<aui:input name="bookId" type="hidden" value="<%= lmsBook.getBookId() %>"/>
	
	<aui:input name="bookTitle" label="Book Title" value="<%= lmsBook.getBookTitle() %>" >
		<aui:validator name="" errorMessage=""/>
	</aui:input>
	<aui:input name="author" helpMessage="Give the Author Name" value="<%= lmsBook.getAuthor() %>"/>
	
	<aui:button type="submit" value="Save" />
</aui:form>

<br/><a href="<portlet:renderURL/>">&laquo; Go Back</a>

<aui:script>
	Liferay.Util.focusFormField(document.<portlet:namespace/>fm.<portlet:namespace/>bookTitle);
</aui:script>