<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.service.SubscriptionLocalServiceUtil"%>
<%@page import="com.liferay.portal.security.permission.ActionKeys"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="javax.portlet.PortletSession"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
<%@include file="/html/library/init.jsp"%>

<div id="LibraryWelcomeMessage"></div>
 	
<h1>
	<u>Greeting for Today</u>: 
	<%= ParamUtil.getString(renderRequest, "DAILY_GREETING", "Welcome") %>
</h1>
	
<portlet:renderURL var="updateBookURL">
	<portlet:param name="jspPage" value="<%= LibraryConstants.PAGE_UPDATE %>" />
</portlet:renderURL>

<% String addImagePath = themeDisplay.getPathThemeImage() + "/common/add.png"; %>
<img src="<%= addImagePath %>"/>

<br/>
<c:if test="<%= canAddBook %>">
	<a href="<%= updateBookURL %>">
		<liferay-ui:message key="add-new-book"/> &raquo;
	</a>
</c:if>

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
	<aui:button type="submit" value="<%= LanguageUtil.get(locale, LibraryConstants.KEY_SEARCH) %>" />
</aui:form>

<h1>Limit:<%= portletConfig.getInitParameter("max-books-limit") %></h1>

<aui:script use='aui-io-request'>
	AUI().ready(function(A){
		Liferay.on("sayHelloEvent", function(payload){
			alert('I am in the other Porltet:' + payload.helloTo);
		});
		
		// loading the welcome article
		var url = '/c/journal/view_article_content?'+
				'groupId=<%= themeDisplay.getScopeGroupId() %>'+
				'&articleId=LIBRARY_WELCOME_MESSAGE';
		A.io.request(url, {
			method: 'GET', 
			on: {
				success: function() {
					var data = this.get("responseData");
					A.one('#LibraryWelcomeMessage').html(data);
				}
			} 
		});		
	});
</aui:script>

<%-- <%@include file="/html/library/social/subscription.jspf" %> --%>

<library-ui:subscribe 
	classPK="<%= themeDisplay.getScopeGroupId() %>" 
	className="<%= LMSBook.class.getName() %>"/>