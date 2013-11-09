<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>

<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@page import="java.util.List"%>
<%@page import="java.util.Calendar"%>

<%@page import="javax.portlet.PortletURL"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="javax.portlet.PortletMode"%>
<%@page import="javax.portlet.WindowState"%>

<%@page import="com.liferay.portal.util.PortalUtil"%>

<%@page import="com.liferay.portlet.PortletURLFactoryUtil"%>

<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.kernel.util.StringPool"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="com.liferay.portal.kernel.util.ListUtil"%>
<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>

<%@page import="com.inikah.util.IConstants"%>
<%@page import="com.inikah.slayer.model.Profile"%>
<%@page import="com.inikah.util.MyListUtil"%>

<%@page import="com.inikah.slayer.service.ProfileLocalServiceUtil"%>

<portlet:defineObjects />
<liferay-theme:defineObjects/>