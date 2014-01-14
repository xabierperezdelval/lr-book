<%@ include file="/html/edit/init.jsp" %>

<%
List<Phone>phones = PhoneLocalServiceUtil.getPhones(profile.getCompanyId(), Profile.class.getName(), profile.getProfileId());
long phoneId = 0l;
for(Phone phone:phones)
{	String phoneNumber = phone.getExtension() + StringPool.DASH + phone.getNumber();
	if(phoneNumber.equalsIgnoreCase(profile.getPhone(true)))
	{
		phoneId = phone.getPhoneId();
		break;
	}
}
%>
<div id="<portlet:namespace/>verificationDiv">
	<b><%=StringPool.PLUS + profile.getPhone(true)%></b>

	<aui:input name="verification" value='' label="Verification Code"
		required="true">
		<aui:validator name="digits"></aui:validator>
		<aui:validator name="maxLength">4</aui:validator>
	</aui:input>
	<label id="verifiaction_failed" style="color: maroon;"></label>
	<aui:input name='' type="button" value="Verify" onClick="verify()" />
</div>

<div hidden="true" id="<portlet:namespace/>secondaryPhoneDiv">

	You have successfully verified your primary number:
	<%=StringPool.PLUS + profile.getPhone(true)%>
	<aui:input name="secondaryPhone"
		prefix="<%=StringPool.PLUS + profile.getPhoneIdd(true)%>"
		required="true">
		<aui:validator name="digits"></aui:validator>
	</aui:input>
	<aui:input name='' type="button" value="ADD & CLOSE"
		onClick="addPhone()" />

</div>

<script>
function verify()
{
	var addPhonediv = document.getElementById("<portlet:namespace/>secondaryPhoneDiv");
	var fld = document.getElementById("<portlet:namespace/>verification");
	var verDiv = document.getElementById("<portlet:namespace/>verificationDiv");
	Liferay.Service(
			  '/inikah-portlet.bridge/verify-phone',
			  {
			    phoneId:<%= phoneId %> ,
			    verificationCode: fld.value
			  },
			  function(obj) {
				  if(obj.toString()=='true')
				  {
					alert('Validation is successful!!');
					verDiv.style.display = 'none';
					addPhonediv.style.display = 'block';
					
				  }
				  else
					  {
					  alert('Validation Failed!! Try Again!!');
					  AUI().one('#verifiaction_failed').html('Validation failed');
					  fld.focus();
					  fld.value='';
					  }
			  }
			); 
}

 function addPhone()
{	
	 var addPhoneFld = document.getElementById("<portlet:namespace/>secondaryPhone");
	 Liferay.Service(
			  '/inikah-portlet.bridge/add-phone',
			  {
			    userId: '<%= user.getUserId() %>',
			    className: '<%= Profile.class.getName() %>',
			    classPK: '<%= profile.getProfileId() %>',
			    number: addPhoneFld.value ,
			    extension: '<%=profile.getPhoneIdd(true) %>',
			    primary: false
			  },
			  function(obj) {
				  	Liferay.Util.getWindow('<portlet:namespace/>verificationPopup').destroy();			  }
			);
}
</script>