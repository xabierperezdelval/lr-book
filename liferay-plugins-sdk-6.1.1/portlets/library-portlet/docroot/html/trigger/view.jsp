<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>
<%@page import="com.liferay.portal.util.PortalUtil"%>
<%@page import="com.util.LMSUtil"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="com.liferay.portlet.PortletURLFactoryUtil"%>
<%@page import="javax.portlet.PortletSession"%>
<%@include file="/html/library/init.jsp"%>

<portlet:actionURL var="quickAddURL" name="quickAdd" />

<aui:form action="<%= quickAddURL.toString() %>">
	<aui:input name="bookTitle"/>
	<aui:input name="author"/>
	<aui:button type="submit" />
</aui:form>

<liferay-portlet:actionURL var="quickAddNewURL" 
	name="<%= LibraryConstants.ACTION_UPDATE_BOOK %>" 
	portletName="library_WAR_libraryportlet" />

<aui:form action="<%= quickAddNewURL %>" useNamespace="false">
	<aui:input name="bookTitle"/>
	<aui:input name="author"/>
	<aui:button type="submit" />
</aui:form>

<hr/>
<portlet:actionURL var="setGreetingURL" name="setGreeting" />

<aui:form action="<%= setGreetingURL.toString() %>">
	<aui:input name="dailyGreeting"/>
	<aui:input name="LIFERAY_SHARED_HELLO" label="sharedHello" />
	<aui:button type="submit" />
</aui:form>

<hr/>
<aui:form name="frm1">
	<aui:input name="sayHello"/>
	<aui:button value="Poke" 
		onClick="javascript:sayHello();"/>
</aui:form>

<script>
	function sayHello() {
		var frm = document.<portlet:namespace/>frm1;
		var data = frm.<portlet:namespace/>sayHello.value;
		var payload = {helloTo : data};
		Liferay.fire("sayHelloEvent", payload);
	}
</script>

<hr/>
<liferay-portlet:renderURL plid="<%= plid %>" var="addBookURL"
		portletName="library_WAR_libraryportlet">
	<portlet:param name="jspPage" 
		value="<%= LibraryConstants.PAGE_UPDATE %>" />
</liferay-portlet:renderURL>	
<aui:a href="<%= addBookURL.toString() %>" label="Add New Book" />

<br/>
<%
	long targetPlid = 	LMSUtil.getPlidByName(
			themeDisplay.getScopeGroupId(), themeDisplay.getLocale(), 
			"<target page name>", false);
	PortletURL listPageURL = PortletURLFactoryUtil.create(
		request, "library_WAR_libraryportlet", 
		targetPlid, PortletRequest.RENDER_PHASE);
	listPageURL.setParameter("jspPage", LibraryConstants.PAGE_LIST);
%>
<aui:a href="<%= listPageURL.toString() %>" label="Show Books &raquo;"/>

<aui:script>
	AUI().ready('liferay-portlet-url', function(A){
		var portletURL = new Liferay.PortletURL();
		portletURL.setParameter("key1", "value");
		portletURL.setPortletId(80);
	});	
</aui:script>

<%
	boolean showAddressBlock = GetterUtil.getBoolean(
		PortalUtil.getOriginalServletRequest(request)
			.getParameter("show-address"), false);
%>

<c:if test="<%= showAddressBlock %>">
	<h2>The address block is shown ....</h2>
</c:if>
