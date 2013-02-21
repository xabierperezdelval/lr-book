<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
<%@include file="/html/library/init.jsp"%>

<liferay-ui:journal-article articleId="LIBRARY_WELCOME_MESSAGE" 
	groupId="<%= themeDisplay.getScopeGroupId() %>"/>
	
<h1>
	<u>Greeting for Today</u>: 
	<%= ParamUtil.getString(renderRequest, "DAILY_GREETING", "Welcome") %>
</h1>
	
<portlet:renderURL var="updateBookURL">
	<portlet:param name="jspPage" value="<%= LibraryConstants.PAGE_UPDATE %>" />
</portlet:renderURL>

<% String addImagePath = themeDisplay.getPathThemeImage() + "/common/add.png"; %>
<img src="<%= addImagePath %>"/>

<br/><a href="<%= updateBookURL %>"><liferay-ui:message key="add-new-book"/> &raquo;</a>

<%
	PortletURL listBooksURL = renderResponse.createRenderURL();
	listBooksURL.setParameter("jspPage", "/html/library/list.jsp");
%>
&nbsp;|&nbsp;
<a href="<%= listBooksURL.toString() %>"><liferay-ui:message key="show-all-books"/> &raquo;</a>

<hr/>
<portlet:actionURL var="searchBooksURL" 
	name="<%= LibraryConstants.ACTION_SEARCH_BOOKS %>"  />
	
<aui:form action="<%= searchBooksURL.toString() %>">
	<aui:input name="searchTerm" label="enter-title-to-search"/>
	<aui:input name="" type="submit" value="<%= LanguageUtil.get(locale, LibraryConstants.KEY_SEARCH) %>" />
</aui:form>

<h1>Limit:<%= portletConfig.getInitParameter("max-books-limit") %><h1>