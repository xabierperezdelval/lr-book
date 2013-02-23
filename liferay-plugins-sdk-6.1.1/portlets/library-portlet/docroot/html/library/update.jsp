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
	
	<aui:field-wrapper label="Permissions">
		<liferay-ui:input-permissions
			modelName="<%= LMSBook.class.getName() %>" />
	</aui:field-wrapper>
	
	<%@include file="/html/library/contact-info.jspf" %>
	
	<aui:button type="submit" value="Save" />
	
	<% String functionName = 
		renderResponse.getNamespace() + "invokeJSONWS();"; %>
	
	<aui:button value="Save thru JSON" 
		onClick="<%= functionName %>"/>
</aui:form>

<br/><a href="<portlet:renderURL/>">&laquo; Go Back</a>

<aui:script>
	Liferay.Util.focusFormField(document.<portlet:namespace/>fm.<portlet:namespace/>bookTitle);
	
	<portlet:namespace/>invokeJSONWS=function() {
		var frm = document.<portlet:namespace/>fm;
		
		// create the payload in JSON format
		var payload = {
			bookTitle: frm.<portlet:namespace/>bookTitle.value, 
			author: frm.<portlet:namespace/>author.value
		};
		
		// invoke the Web Service
		Liferay.Service(
			'/library-portlet#lmsbook/insert-book',
			payload,
		  	function(obj) {
		   		console.log(obj);
		  	}
		);

		// clear values in the input fields
		frm.<portlet:namespace/>bookTitle.value = '';
		frm.<portlet:namespace/>author.value = '';
	}
</aui:script>