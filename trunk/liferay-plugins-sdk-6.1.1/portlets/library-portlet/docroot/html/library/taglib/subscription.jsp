<%@include file="/html/library/init.jsp" %>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.service.SubscriptionLocalServiceUtil"%>

<c:if test="<%= themeDisplay.isSignedIn() %>">
	<%@page import="javax.portlet.ResourceURL"%>
	<%
		boolean isSubscribed = 
			SubscriptionLocalServiceUtil.isSubscribed(
				company.getCompanyId(), user.getUserId(), 
				LMSBook.class.getName(), scopeGroupId);
		
		String label = isSubscribed? 
			Constants.UNSUBSCRIBE : Constants.SUBSCRIBE;
	%>
	
	<aui:a href="javascript:void();" label="<%= label %>" 
		onClick="javascript:toggleState(this);" />
	
	<aui:script>	
		AUI().ready('liferay-portlet-url', 'aui-io-request', 'aui-loading-mask', 
			function(A){
				var portletId = '<%= portletDisplay.getRootPortletId() %>';
				// form the URL
				var subscriptionURL = 
					Liferay.PortletURL.createResourceURL();
				subscriptionURL
					.setPortletId(portletId);
				subscriptionURL
					.setParameter('<%= Constants.CMD %>', '<%= label %>');
							
				toggleState = function(anchor) {
					// make the AJAX call
					A.io.request(subscriptionURL.toString(), {
						method: 'GET', 
						dataType: 'json',
						on: {
							success: function() {
							
								var layer = A.one('#portlet_'+portletId);
								if (typeof(layer.loadingmask) == 'undefined') {
									layer.plug(A.LoadingMask, { background: '#000' });
								}
								layer.loadingmask.toggle();
														
								var result = this.get("responseData").subscribed;
								var linkText = (result === true)? 'UnSubscribe' : 'Subscribe';
								A.one('#'+anchor.id).html(linkText);
								subscriptionURL.setParameter('<%= Constants.CMD %>'
									,linkText);
								setTimeout(function(){layer.loadingmask.toggle();},500);	
							}
						} 
					});
				} 
			}
		);
	</aui:script>
</c:if>