<%@ include file="/html/common/init.jsp" %>

<%
	String portletId = "edit_WAR_inikahportlet";
	long targetPlId = PageUtil.getPageLayoutId(themeDisplay.getScopeGroupId(), "edit", locale);
	PortletURL startURL = PortletURLFactoryUtil.create(renderRequest, portletId, targetPlId, PortletRequest.ACTION_PHASE);
	startURL.setParameter(ActionRequest.ACTION_NAME , "start");
	startURL.setWindowState(WindowState.NORMAL);
	String portletNamespace = StringPool.UNDERLINE + portletId + StringPool.UNDERLINE;
%>

<aui:form action="<%= startURL.toString() %>" portletNamespace="<%= portletNamespace %>">
	<aui:input name="profileName"/>
	<aui:button type="submit" value="start"/>
</aui:form>