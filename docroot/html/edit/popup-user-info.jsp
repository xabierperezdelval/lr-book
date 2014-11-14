<%@ include file="/html/edit/init.jsp" %>

<aui:form>
	<aui:input type="hidden" name="userId" value="<%= user.getUserId() %>"/>
	<aui:input name="userName" label="your-full-name" required="<%= true %>"/>
	<aui:input type="checkbox" lable="i-am-a-female" name="female" />
	<aui:input name="occupation" label="your-occupation" required="<%= true %>"/>
	<aui:input type="textarea" name="additionalInfo" label="additional-info" row="5" column="30" required="<%= true %>"/>
	<span id="<portlet:namespace/>additionalInfo_Counter"></span> character(s) remaining.
	<aui:input type="button" value="save" onClick="javascript:saveUserInfo();" name="dummy"/>
</aui:form>

<aui:script>
	function saveUserInfo() {
		var fm = document.<portlet:namespace/>fm;
		var userId = fm.<portlet:namespace/>userId.value;
		var userName = fm.<portlet:namespace/>userName.value;
		var female = fm.<portlet:namespace/>female.value;
		var occupation = fm.<portlet:namespace/>occupation.value;
		var additionalInfo = fm.<portlet:namespace/>additionalInfo.value;
		
		Liferay.Service(
  			'/inikah-portlet.profile/update-user-info',
  			{
    			userId: userId,
    			userName: userName,
    			female: female,
    			occupation: occupation,
    			additionalInfo: additionalInfo
  			},
  			function(obj) {
  				Liferay.Util.getWindow('<portlet:namespace/>userInfoPopup').destroy();
  			}
		);
	}
	
	YUI().use(
  		'aui-char-counter',
  		function(Y) {
    		new Y.CharCounter({
        		input: '#<portlet:namespace/>additionalInfo',
        		counter: '#<portlet:namespace/>additionalInfo_Counter',
        		maxLength: 150
 			});
		}
	);
</aui:script>