<%@ include file="/html/edit/init.jsp" %>

<%
	int relationship = ParamUtil.getInteger(request, "relationship");
	long relativeId = ParamUtil.getLong(request, "relativeId", 0l);
%>

<aui:form>
	<aui:row>
		<aui:column>
			<aui:input name="name" required="true" />
		</aui:column>
		<aui:column>
			
		</aui:column>
	</aui:row>
	
	<aui:row>
		<aui:column>
			<aui:input name="mobile" prefix="+91"/>
		</aui:column>
		<aui:column>
			<aui:input name="emailAddress" />
		</aui:column>
	</aui:row>	
	
	<aui:row>
		<aui:column>
			<aui:input name="profession" />
		</aui:column>
		<aui:column>
			<aui:input name="comments" />
		</aui:column>
	</aui:row>
	
	<aui:button onClick="javascript:saveRelative();" value="save" />
</aui:form>

<script type="text/javascript">
	function saveRelative() {
		
		var frm = document.<portlet:namespace/>fm;
		var name = frm.<portlet:namespace/>name.value;
		var married = true;
		var passedAway = false;
		var phone = frm.<portlet:namespace/>mobile.value;
		var emailAddress = frm.<portlet:namespace/>emailAddress.value;
		var comments = frm.<portlet:namespace/>comments.value;
		var profession = frm.<portlet:namespace/>profession.value;
		var relationship = '<%= relationship %>';
		var age = 20;
				
		Liferay.Service(
			'/inikah-portlet.relative/add-relative',
			{
			    userId: '<%= user.getUserId() %>',
			    profileId: '<%= profile.getProfileId() %>',
			    name: name,
			    married: married,
			    passedAway: passedAway,
			    phone: phone,
			    emailAddress: emailAddress,
			    profession: profession,
			    comments: comments,
			    owner: true,
			    relationship: relationship,
			    age: age
			},
			function(data) {
				Liferay.Util.getWindow('<portlet:namespace/>relativeInfoPopup').destroy();
				Liferay.Util.getOpener().<portlet:namespace />reloadPortlet();
			}
		); 
	}
</script>