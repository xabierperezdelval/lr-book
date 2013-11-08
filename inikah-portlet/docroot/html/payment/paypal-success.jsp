<%@ include file="/html/common/init.jsp" %>
<%@ include file="/html/payment/init.jsp" %>

<portlet:renderURL var="thanksURL" windowState="<%= WindowState.NORMAL.toString() %>">
	<portlet:param name="jspPage" value="/html/payment/thanks.jsp"/>
</portlet:renderURL>

<script>
	window.onload = function(){
		if(window.opener){
			window.opener.window.location.href = "<%= thanksURL %>";
		} 
		else{
			if(top.dg.isOpen() == true){
				top.dg.closeFlow();
				return true;
			}
		}                              
	};                             
</script>