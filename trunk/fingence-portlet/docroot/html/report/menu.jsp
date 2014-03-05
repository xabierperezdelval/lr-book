<%@ include file="/html/report/init.jsp" %>

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
	<hr/>
	<c:choose>
		<c:when test = "<%= (userType == IConstants.USER_TYPE_WEALTH_ADVISOR || userType == IConstants.USER_TYPE_WEALTH_ADVISOR) %>">
			<li>
				<a href="javascript:void(0);" onClick="javascript:triggerRequest('<%= IConstants.ADD_PORTFOLIO %>');"><%= TextFormatter.format(IConstants.ADD_PORTFOLIO, TextFormatter.J) %></a>
			</li>
		</c:when>
	</c:choose>
	<c:choose>
		<c:when test = "<%= (userType == IConstants.USER_TYPE_WEALTH_ADVISOR) %>">
			<li>
				<a href="javascript:void(0);" onClick="javascript:triggerRequest('<%= IConstants.ADD_USER %>');"><%= TextFormatter.format(IConstants.ADD_USER, TextFormatter.J) %></a>
			</li>
		</c:when>
	</c:choose>
	
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