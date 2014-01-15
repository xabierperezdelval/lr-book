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
	
	<aui:button type="submit" onClick="javascript:saveRelative();" value="save" />
</aui:form>

<script>
	function saveRelative() {
		
		alert('inside function...');
		var fm = document.<portlet:namespace/>fm;
		var userId = '<%= user.getUserId() %>';
		var profileId = '<%= profile.getProfileId() %>';
		var name = fm.<portlet:namespace/>name.value;
		var married = true;
		var passedAway = false;
		var phone = fm.<portlet:namespace/>mobile.value;
		var emailAddress = fm.<portlet:namespace/>emailAddress.value;
		var comments = fm.<portlet:namespace/>comments.value;
		var profession = fm.<portlet:namespace/>profession.value;
		var relationship = '<%= relationship %>';
		var age = 20;
		
		Liferay.Service(
			'/inikah-portlet.relative/add-relative',
		  	{
		    	userId: userId,
		    	profileId: profileId,
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
		  	function(obj) {
		  		alert("success" + obj);
		  	}
		); 
		
		Liferay.Util.getWindow('<portlet:namespace/>relativeInfoPopup').destroy();
	}
</script>