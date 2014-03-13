<%@ include file="/html/init.jsp"%>

<%@page import="com.liferay.portal.kernel.util.StringPool"%>
<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>

<%@page import="com.fingence.slayer.model.impl.PortfolioImpl"%>
<%@page import="com.fingence.slayer.model.impl.AssetImpl"%>
<%@page import="com.fingence.slayer.model.Asset"%>

<%@page import="com.fingence.slayer.model.Portfolio"%>
<%@page import="com.fingence.slayer.service.PortfolioLocalServiceUtil"%>
<%@page import="com.fingence.slayer.model.impl.PortfolioItemImpl"%>
<%@page import="com.fingence.slayer.model.PortfolioItem"%>

<%
	String layoutName = GetterUtil.getString(portletSession.getAttribute("MENU_ITEM",PortletSession.APPLICATION_SCOPE), IConstants.PAGE_REPORTS_HOME);
%>