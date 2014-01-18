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
			<c:choose>
				<c:when test="<%= relationship <= IConstants.RELATION_MOTHER %>">
					<aui:input type="checkbox" name="passedAway"/>
				</c:when>
				<c:otherwise>
					<c:if test="<%= relationship != IConstants.RELATION_BRO_IN_LAW %>">
						<aui:fieldset>
							<aui:column cssClass="display-down">
								<aui:input type="checkbox" name="unMarried"/>
							</aui:column>
							<aui:column cssClass="quarter-width">
								<aui:input name="age"/> 
							</aui:column>							
						</aui:fieldset>
					</c:if>
				</c:otherwise>
			</c:choose>
		</aui:column>
	</aui:row>
	
	<aui:row>
		<aui:column>
			<aui:input name="mobile" prefix="+91"/>
		</aui:column>
		<aui:column>
			<aui:input name="emailAddress">
				<aui:validator name="email"/>
			</aui:input>
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
	
	<c:if test="<%= relationship == IConstants.RELATION_BROTHER %>">
		<aui:input type="checkbox" name="younger" label="he-is-younger"/>
	</c:if>
	
	<c:if test="<%= relationship == IConstants.RELATION_SISTER %>">
		<aui:input type="checkbox" name="younger" label="she-is-younger"/>
	</c:if>	
	
	<aui:button onClick="javascript:saveRelative();" value="save" />
</aui:form>

<script type="text/javascript">
	function saveRelative() {
			
		var frm = document.<portlet:namespace/>fm;
		var name = frm.<portlet:namespace/>name.value;
		var unMarried = false;
		var passedAway = false;
		var phone = frm.<portlet:namespace/>mobile.value;
		var emailAddress = frm.<portlet:namespace/>emailAddress.value;
		var comments = frm.<portlet:namespace/>comments.value;
		var profession = frm.<portlet:namespace/>profession.value;
		var relationship = '<%= relationship %>';
		var age = 0;
		var younger = false;
		
		<%
			if (relationship == IConstants.RELATION_BROTHER || relationship == IConstants.RELATION_SISTER) {
				%>younger = frm.<portlet:namespace/>younger.value;<%
			}
		
			if (relationship > IConstants.RELATION_MOTHER && relationship != IConstants.RELATION_BRO_IN_LAW) {
				%>
					age = frm.<portlet:namespace/>age.value;
					unMarried = frm.<portlet:namespace/>unMarried.value;
				<%
			}
			
			if (relationship <= IConstants.RELATION_MOTHER) {
				%>passedAway = frm.<portlet:namespace/>passedAway.value;<%
			}
		%>
				
		Liferay.Service(
			'/inikah-portlet.relative/add-relative',
			{
			    userId: '<%= user.getUserId() %>',
			    profileId: '<%= profile.getProfileId() %>',
			    name: name,
			    unMarried: unMarried,
			    passedAway: passedAway,
			    phone: phone,
			    emailAddress: emailAddress,
			    profession: profession,
			    comments: comments,
			    owner: true,
			    relationship: relationship,
			    younger: younger,
			    age: age
			},
			function(data) {
				Liferay.Util.getWindow('<portlet:namespace/>relativeInfoPopup').destroy();
				Liferay.Util.getOpener().<portlet:namespace />reloadPortlet();
			}
		); 
	}
</script>