<%@page import="java.util.Map.Entry"%>
<%@page import="com.fingence.slayer.model.ReportConfig"%>
<%@page import="com.fingence.slayer.service.ReportConfigServiceUtil"%>
<%@ include file="/html/report/init.jsp" %>

<ul class="left-nav">
	<c:if test="<%= (userType == IConstants.USER_TYPE_WEALTH_ADVISOR || userType == IConstants.USER_TYPE_WEALTH_ADVISOR) %>">
		<li class="<%= (layoutName.equalsIgnoreCase(IConstants.PAGE_REPORTS_HOME))? IConstants.SELECTED : StringPool.BLANK %>" id="li_<%= IConstants.PAGE_REPORTS_HOME %>"><a href="javascript:void(0);"  onClick="javascript:triggerRequest('<%= IConstants.ADD_USER %>', '<%= IConstants.PAGE_REPORTS_HOME %>');"><%= TextFormatter.format(IConstants.PAGE_REPORTS_HOME, TextFormatter.J) %></a>
	</c:if>
	<%  
		for (Entry<Long, String> item : ReportConfigServiceUtil.getMenuItems(0).entrySet()) {
			%><li class="<%= (layoutName.equalsIgnoreCase(item.getValue()))? IConstants.SELECTED : StringPool.BLANK %>" id="li_<%= item.getKey() %>"><a href="javascript:void(0);" onClick="javascript:triggerRequest('<%= item.getKey() %>', '<%= item.getValue() %>');"><%= TextFormatter.format(item.getValue(), TextFormatter.J) %></a><%
		}
	%>
	<c:if test="<%= (userType == IConstants.USER_TYPE_WEALTH_ADVISOR || userType == IConstants.USER_TYPE_WEALTH_ADVISOR) %>">
		<li class="<%= (layoutName.equalsIgnoreCase(IConstants.ADD_PORTFOLIO))? IConstants.SELECTED : StringPool.BLANK %>" id="li_<%= IConstants.ADD_PORTFOLIO %>"><a href="javascript:void(0);"  onClick="javascript:triggerRequest('<%= IConstants.ADD_USER %>', '<%= IConstants.ADD_PORTFOLIO %>');"><%= TextFormatter.format(IConstants.ADD_PORTFOLIO, TextFormatter.J) %></a>
	</c:if>
	<c:if test="<%= (userType == IConstants.USER_TYPE_WEALTH_ADVISOR) %>">
		<li class="<%= (layoutName.equalsIgnoreCase(IConstants.ADD_USER))? IConstants.SELECTED : StringPool.BLANK %>" id="li_<%= IConstants.ADD_USER %>"><a href="javascript:void(0);" onClick="javascript:triggerRequest('<%= IConstants.ADD_USER %>', '<%= IConstants.ADD_USER %>');"><%= TextFormatter.format(IConstants.ADD_USER, TextFormatter.J) %></a>
	</c:if>
</ul>

<aui:script>
	function triggerRequest(itemID, itemName) {	
		var ajaxURL = Liferay.PortletURL.createActionURL();
		ajaxURL.setPortletId('menu_WAR_fingenceportlet');
		ajaxURL.setParameter('MENU_ITEM', itemName);
		ajaxURL.setParameter('MENU_ITEM_ID', itemID);
		ajaxURL.setName('setNavigation');
		ajaxURL.setWindowState('exclusive');
		
		AUI().io.request('<%= themeDisplay.getURLPortal() %>' + ajaxURL, {
			sync: true,
			on: {
				success: function() {
					Liferay.Portlet.refresh('#p_p_id_report_WAR_fingenceportlet_');
					Liferay.Portlet.refresh('#p_p_id_menu_WAR_fingenceportlet_');
				}
			}
		});
		
		// change the CSS of "li" tag
		$.each($("ul.left-nav li"), function(index, obj) {
 			if (obj.id == ('li_' + itemID)) {
 				obj.addClass('selected');
 			} else {
 				obj.removeClass('selected');
 			}
		});
	}
</aui:script>