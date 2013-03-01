<%@page import="com.util.LMSUtil"%>
<%@include file="/html/library/init.jsp"%>

<%
	LMSBook lmsBook = null;
	long bookId = ParamUtil.getLong(request, "bookId");
	if (bookId > 0L) {
		lmsBook = LMSBookLocalServiceUtil.fetchLMSBook(bookId);
	}
	String backURL = ParamUtil.getString(request, "backURL");
	boolean showHeader = ParamUtil.getBoolean(request, "showHeader", true);
%>

<c:if test="<%= showHeader %>">
	<liferay-ui:header
		backLabel="&laquo; Back to List" 
		title="Book Details" backURL="<%= backURL %>" />
</c:if>

<c:if test="<%= Validator.isNotNull(lmsBook) %>">

	<aui:field-wrapper label="Tags & Categories">
		<liferay-ui:asset-tags-summary
	   		className="<%= LMSBook.class.getName() %>"
	   		classPK="<%= lmsBook.getPrimaryKey() %>" />
	
		<liferay-ui:asset-categories-summary
		    className="<%= LMSBook.class.getName() %>"
		    classPK="<%= lmsBook.getPrimaryKey() %>" />
	</aui:field-wrapper>
	
	<table border="1">
		<tr>
			<td>Book Title</td>
			<td><%= lmsBook.getBookTitle() %></td>
		</tr>
		<tr>
			<td>Author</td>
			<td><%= lmsBook.getAuthor() %></td>
		</tr>
		<tr>
			<td>Date Added</td>
			<td><%= lmsBook.getCreateDate() %></td>
		</tr>
		<tr>
			<td>Last Modified</td>
			<td><%= lmsBook.getModifiedDate() %></td>
		</tr>						
	</table>
	
	<portlet:actionURL var="discussionURL" name="discussOnThisBook" />
	<liferay-ui:discussion 
		classPK="<%= lmsBook.getPrimaryKey() %>" 
		userId="<%= themeDisplay.getUserId() %>" 
		className="<%= LMSBook.class.getName() %>" 
		subject="<%= lmsBook.getBookTitle() %>"  
		formAction="<%= discussionURL %>" 
		ratingsEnabled="<%= true %>"
		redirect="<%= themeDisplay.getURLCurrent() %>"/>
</c:if>