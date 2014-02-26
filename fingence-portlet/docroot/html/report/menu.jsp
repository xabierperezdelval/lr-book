<%@ include file="/html/report/init.jsp" %>

<liferay-ui:header title="Navigate"/>

<ul>
<%
	for (int i=0; i<IConstants.REPORT_MENU_ITEMS.length; i++) {
		String item = IConstants.REPORT_MENU_ITEMS[i];	
		%>
			<li>
				<a href="javascript:void(0);" onClick="javascript:triggerRequest('<%= item %>');"><%= TextFormatter.format(item, TextFormatter.J) %></a>
			</li>
		<%
	}
%>
</ul>

<hr/>

<aui:script>
	function triggerRequest(item) {
		var ajaxURL = Liferay.PortletURL.createActionURL();
		ajaxURL.setPortletId('menu_WAR_fingenceportlet');
		ajaxURL.setParameter('MENU_ITEM', item);
		ajaxURL.setName('setNavigation');
		ajaxURL.setWindowState('<%= LiferayWindowState.EXCLUSIVE.toString() %>');
		
		AUI().io.request('<%= themeDisplay.getURLPortal() %>' + ajaxURL, {
			sync: true,
			on: {
				success: function() {
					Liferay.Portlet.refresh('#p_p_id_report_WAR_fingenceportlet_');
				}
			}
		});				
	}
</aui:script>