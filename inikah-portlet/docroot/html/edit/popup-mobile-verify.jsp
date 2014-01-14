<%@ include file="/html/edit/init.jsp" %>

<%@page import="com.liferay.portal.model.Phone"%>

<%
	String phoneIDD = profile.getPhoneIdd(true);
%>

<h2><%= StringPool.PLUS + phoneIDD + StringPool.DASH + profile.getPhone(true) %></h2>

<aui:input name="verificationCode" label="verification-code" required="true">
	<aui:validator name="digits"/>
	<aui:validator name="maxLength">4</aui:validator>
</aui:input>

<label id="verifiaction_status" style="color: maroon;"></label>
<aui:input name="" type="button" value="Verify" onClick="javascript:verifyMobile();" />

<div hidden="true" id="<portlet:namespace/>secondaryPhoneDiv">
	<aui:input name="secondaryPhone" prefix="<%= StringPool.PLUS + phoneIDD %>">
		<aui:validator name="digits"/>
	</aui:input>
	<aui:input name="" type="button" value="Add & Close" onClick="javascript:addPhone();" />
</div>

<script>
	function verifyMobile(){
		var anotherPhoneDiv = document.getElementById("<portlet:namespace/>secondaryPhoneDiv");
		var fld = document.getElementById("<portlet:namespace/>verificationCode");
		
		Liferay.Service(
			'/inikah-portlet.bridge/verify-phone',
			{
				phoneId:<%= profile.getPhoneId(true) %>,
			    verificationCode: fld.value
			},
			function(obj) {
				if(obj.toString() =='true'){
					AUI().one('#verifiaction_status').html('Your Mobile Got Verified!!');
					anotherPhoneDiv.style.display = 'block';
				} else {
					AUI().one('#verifiaction_status').html('Verification Failed. Try Again.');
					fld.focus();
					fld.value = '';
				}
			}
		); 
	}

 	function addPhone() {	
		var addPhoneFld = document.getElementById("<portlet:namespace/>secondaryPhone");
	 	Liferay.Service(
			'/inikah-portlet.bridge/add-phone',
			{
				userId: '<%= user.getUserId() %>',
			    className: '<%= Profile.class.getName() %>',
			    classPK: '<%= profile.getProfileId() %>',
			    number: addPhoneFld.value ,
			    extension: '<%= phoneIDD %>',
			    primary: false
			},
			function(obj) {
				Liferay.Util.getWindow('<portlet:namespace/>verificationPopup').destroy();			  
			}
		);
	}
</script>