<%@page import="javax.portlet.PortletRequest"%>
<%@page import="com.liferay.portal.util.PortalUtil"%>
<%@page import="com.liferay.portlet.PortletURLFactoryUtil"%>
<%@ include file="/html/init.jsp" %>

<%
	String[] pageLayouts = {"Asset Report","Fixed Income Report","Performance Report","Risk Report","Violation Report"};

	for(String pageLayout : pageLayouts){
		String link = "<a href=\"javascript:void(0);\" onClick=\"trigerRequest('" +pageLayout+ "'\")>" + pageLayout +  "</a>";
%>
		<%=link %><br/>
<%

	}
%>



<aui:script>
	function trigerRequest(param){
		
		alert(param);
		var ajaxURL = Liferay.PortletURL.createResourceURL();
		ajaxURL.setPortletId('fingenceMenu_WAR_fingenceportlet');
		ajaxURL.setParameter('navigationMenuItem', param);
		
		var notExists = true;
		AUI().io.request('<%= themeDisplay.getURLPortal() %>' + ajaxURL, {
			sync: true,
			on: {
				success: function() {
					notExists = (!(eval(this.get('responseData'))));
				}
			}
		});
		
	}
</aui:script>