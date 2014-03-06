<%@ include file="/html/report/init.jsp" %>

<ul class="left-nav">
<%
	for (int i=0; i<IConstants.REPORT_MENU_ITEMS.length; i++) {
		String item = IConstants.REPORT_MENU_ITEMS[i];	
		%><li class="<%= (layoutName.equalsIgnoreCase(item))? IConstants.SELECTED : StringPool.BLANK %>" id="li_<%= item %>"><a href="javascript:void(0);" onClick="javascript:triggerRequest('<%= item %>');"><%= TextFormatter.format(item, TextFormatter.J) %></a><%
	}
%>

<c:if test="<%= (userType == IConstants.USER_TYPE_WEALTH_ADVISOR || userType == IConstants.USER_TYPE_WEALTH_ADVISOR) %>">
	<li id="li_<%= IConstants.ADD_PORTFOLIO %>"><a href="javascript:void(0);" onClick="javascript:triggerRequest('<%= IConstants.ADD_PORTFOLIO %>');"><%= TextFormatter.format(IConstants.ADD_PORTFOLIO, TextFormatter.J) %></a>
</c:if>

<c:if test="<%= (userType == IConstants.USER_TYPE_WEALTH_ADVISOR) %>">
	<li id="li_<%= IConstants.ADD_USER %>"><a href="javascript:void(0);" onClick="javascript:triggerRequest('<%= IConstants.ADD_USER %>');"><%= TextFormatter.format(IConstants.ADD_USER, TextFormatter.J) %></a>
</c:if>
</ul>

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
		
		// change the CSS of "li" tag
		$.each($("ul.left-nav li"), function(index, obj) {
 			if (obj.id == ('li_' + item)) {
 				obj.addClass('selected');
 			} else {
 				obj.removeClass('selected');
 			}
		});
	}
</aui:script>