<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>

<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Iterator"%>

<%@page import="javax.portlet.PortletURL"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="javax.portlet.PortletMode"%>
<%@page import="javax.portlet.WindowState"%>
<%@page import="javax.portlet.PortletSession"%>
<%@page import="javax.portlet.ActionRequest"%>

<%@page import="com.liferay.portal.util.PortalUtil"%>

<%@page import="com.liferay.portlet.PortletURLFactoryUtil"%>

<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.kernel.util.StringPool"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="com.liferay.portal.kernel.util.ListUtil"%>
<%@page import="com.liferay.portal.kernel.util.KeyValuePair"%>
<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="com.liferay.portal.kernel.util.CharPool"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>

<%@page import="com.liferay.portal.model.ListType"%>

<%@page import="com.inikah.util.IConstants"%>
<%@page import="com.inikah.slayer.model.Profile"%>
<%@page import="com.inikah.slayer.model.Photo"%>
<%@page import="com.inikah.util.MyListUtil"%>
<%@page import="com.inikah.util.PageUtil"%>

<%@page import="com.inikah.slayer.service.BridgeServiceUtil"%>
<%@page import="com.inikah.slayer.service.ProfileLocalServiceUtil"%>
<%@page import="com.inikah.slayer.service.LocationLocalServiceUtil"%>
<%@page import="com.inikah.slayer.service.CurrencyLocalServiceUtil"%>

<portlet:defineObjects />
<liferay-theme:defineObjects/>

<%
	Profile profile = (Profile) portletSession.getAttribute("SEL_PROFILE", PortletSession.APPLICATION_SCOPE);

	if (layout.getName(locale).equalsIgnoreCase("mine")) {
		portletSession.removeAttribute("SEL_PROFILE", PortletSession.APPLICATION_SCOPE);
	} else if (Validator.isNull(profile)) {
		long profileId = ParamUtil.getLong(renderRequest, "profileId", 0l);
		
		if (profileId == 0l) {
			HttpServletRequest origServletRequest = PortalUtil.getOriginalServletRequest(request);
			profileId = GetterUtil.getLong(origServletRequest.getParameter("id"), 0l);			
			renderRequest.setAttribute("profileId", String.valueOf(profileId));
		}
		
		if (profileId > 0l) {
			profile = ProfileLocalServiceUtil.fetchProfile(profileId);
			portletSession.setAttribute("SEL_PROFILE", profile, PortletSession.APPLICATION_SCOPE);			
		}
	}
%>