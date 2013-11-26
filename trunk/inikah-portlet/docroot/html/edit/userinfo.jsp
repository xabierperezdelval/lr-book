<%@ include file="/html/edit/init.jsp" %>

<aui:form>
	<aui:input type="hidden" name="userId" value="<%= user.getUserId() %>"/>
	<aui:input name="userName" label="your-full-name"/>
	<aui:input type="checkbox" lable="i-am-a-female" name="female"/>
	<aui:button onClick="javascript:saveUserInfo();" />
</aui:form>

<aui:script>
	function saveUserInfo() {
		var fm = document.<portlet:namespace/>fm;
		var userId = fm.<portlet:namespace/>userId.value;
		var userName = fm.<portlet:namespace/>userName.value;
		var female = fm.<portlet:namespace/>female.value;
		
		Liferay.Service(
  			'/inikah-portlet.profile/update-user-info',
  			{
    			userId: userId,
    			userName: userName,
    			female: female
  			},
  			function(obj) {
  				// close the popup
  			}
		);
	}
</aui:script>