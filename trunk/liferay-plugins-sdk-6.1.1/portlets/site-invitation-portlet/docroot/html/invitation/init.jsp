<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>
<%@page import="com.liferay.util.ContentUtil"%>
<%@page import="com.liferay.portlet.PortletPreferencesFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>

<%@taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui"%>
<%@taglib uri="http://liferay.com/tld/aui" prefix="aui"%>
<%@taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme"%>
<%@taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@page import="java.util.List"%>

<%@page import="javax.portlet.PortletPreferences"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="javax.portlet.PortletURL"%>

<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="com.liferay.portal.kernel.util.JavaConstants"%>
<%@page import="com.liferay.portal.kernel.servlet.SessionMessages"%>
<%@page import="com.liferay.portal.kernel.util.ListUtil"%>

<%@page import="com.mpower.util.InvitationConstants"%>

<%@page import="com.mpower.slayer.model.SiteInvitation"%>
<%@page import="com.mpower.slayer.service.SiteInvitationLocalServiceUtil"%>

<portlet:defineObjects />
<liferay-theme:defineObjects />

<%
	PortletRequest portletRequest = (PortletRequest)request.getAttribute(JavaConstants.JAVAX_PORTLET_REQUEST);

	PortletPreferences preferences = renderRequest.getPreferences();
	String portletResource = ParamUtil.getString(request, "portletResource");
	if (Validator.isNotNull(portletResource)) {
		preferences = PortletPreferencesFactoryUtil.getPortletSetup(request, portletResource);
	}
	
	String messageBody = ContentUtil.get(InvitationConstants.EMAIL_TEMPLATE_PATH);
	String EMAIL_BODY_TEXT = preferences.getValue(InvitationConstants.EMAIL_BODY_TEXT, messageBody);
	
	int pointsForInviting =  GetterUtil.getInteger(preferences.getValue("points-for-inviting", "2"));
	int pointsForAccepting =  GetterUtil.getInteger(preferences.getValue("points-for-accepting", "5"));
%>