<%@taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme"%>

<%@page import="javax.portlet.PortletURL"%> 
<%@page import="javax.portlet.ActionRequest"%>

<%@page import="com.library.LibraryConstants"%>

<%@page import="java.util.List"%>
<%@page import="com.slayer.model.LMSBook"%>
<%@page import="com.slayer.service.LMSBookLocalServiceUtil"%>

<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>

<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>

<portlet:defineObjects/>
<liferay-theme:defineObjects/>