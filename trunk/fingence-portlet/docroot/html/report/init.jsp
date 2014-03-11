<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>
<%@ include file="/html/init.jsp"%>

<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="com.liferay.portal.kernel.util.StringPool"%>
<%@page import="com.fingence.slayer.model.impl.PortfolioImpl"%>
<%@page import="com.fingence.slayer.model.impl.AssetImpl"%>
<%@page import="com.fingence.slayer.model.Asset"%>
<%@page import="com.fingence.util.CellUtil"%>
<%@page import="com.fingence.util.ConversionUtil"%>


<%@page import="com.fingence.slayer.model.Portfolio"%>
<%@page import="com.fingence.slayer.service.AssetLocalServiceUtil"%>
<%@page import="com.fingence.slayer.service.PortfolioLocalServiceUtil"%>
<%@page import="com.fingence.slayer.service.PortfolioItemLocalServiceUtil"%>
<%@page import="com.fingence.slayer.model.impl.PortfolioItemImpl"%>
<%@page import="com.fingence.slayer.model.PortfolioItem"%>

<%
	String layoutName = GetterUtil.getString(portletSession.getAttribute("MENU_ITEM",PortletSession.APPLICATION_SCOPE), IConstants.PAGE_REPORTS_HOME);
%>