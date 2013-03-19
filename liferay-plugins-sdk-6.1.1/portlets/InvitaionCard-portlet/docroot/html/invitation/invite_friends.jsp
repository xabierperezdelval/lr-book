<%@include file="/html/invitation/init.jsp"%>

<script type="text/javascript">
	function change() {
		document.getElementById("<portlet:namespace/>mail").addEventListener(
				"click", function() {
					this.setAttribute("rows", "10");
					this.setAttribute("cols", "35");
				}, false)
		}
</script>

<%
	PortletURL emailDetailUrl = renderResponse.createActionURL();
	emailDetailUrl.setParameter(ActionRequest.ACTION_NAME, "processInvitations");
%>
<body onload=" change();">
	<aui:form method="post" action="<%=emailDetailUrl.toString()%>">
		<aui:input type="textarea" name="emails" id="mail" />
		<aui:button type="submit" />
	</aui:form>
</body>
