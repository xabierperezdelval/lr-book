<%@page import="com.inikah.slayer.service.RelativeLocalServiceUtil"%>
<%@page import="com.inikah.slayer.model.Relative"%>
<%@page import="com.inikah.slayer.model.impl.RelativeImpl"%>

<%@ include file="/html/edit/init.jsp" %>

<%
	Relative relative = new RelativeImpl();
	int relationship = ParamUtil.getInteger(request, "relationship");
	long relativeId = ParamUtil.getLong(request, "relativeId", 0l);
	
	if (relativeId > 0l) {
		relative = RelativeLocalServiceUtil.fetchRelative(relativeId);
		relationship = relative.getRelationship();
	}
%>

<aui:form>
	<aui:row>
		<aui:column>
			<aui:input name="name" required="true" value="<%= relative.getName() %>"/>
		</aui:column>
		<aui:column cssClass="display-down">
			<c:choose>
				<c:when test="<%= relationship <= IConstants.RELATION_MOTHER %>">
					<aui:input type="checkbox" name="passedAway" value="<%= relative.getPassedAway() %>" />
				</c:when>
				<c:otherwise>
					<c:if test="<%= relationship != IConstants.RELATION_BRO_IN_LAW %>">
						<aui:fieldset>
							<aui:column>
								<aui:input type="checkbox" name="unMarried" onClick="javascript:toggleDiv();"/>
							</aui:column>
							<aui:column cssClass="quarter-width">
								<div id="<portlet:namespace/>ageDiv" hidden="true">
									<aui:input name="age" value="<%= relative.getAge() %>">
										<aui:validator name="digits"/>
									</aui:input> 
								</div>
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

	function toggleDiv() {
		
		var frm = document.<portlet:namespace/>fm;
		var fld = frm.<portlet:namespace/>age;
		var unMarried = frm.<portlet:namespace/>unMarried.value;				
		var div = document.getElementById('<portlet:namespace/>ageDiv');
		
		if (eval(unMarried)) {
			div.style.display = 'block';
			fld.focus();
		} else {
			div.style.display = 'none';
			fld.value = '';
		}
	}

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