<%@ include file="/html/init.jsp" %>

<% 
	PortletURL createAccountURL = PortletURLFactoryUtil.create(request, "58", plid, PortletRequest.ACTION_PHASE);
	createAccountURL.setWindowState(WindowState.NORMAL);
	createAccountURL.setPortletMode(PortletMode.VIEW);
	createAccountURL.setParameter("saveLastPath", "0");
	createAccountURL.setParameter("struts_action", "/login/create_account");
	
	String PORTLET_NSPACE = "_58_";
%>

<aui:form portletNamespace="<%= PORTLET_NSPACE %>" action="<%= createAccountURL.toString() %>">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.ADD %>" />
	
	<!-- birth day -->
	<aui:input name="birthdayDay" type="hidden" value="1" />
	<aui:input name="firstName" type="hidden" value="Not Specified" />
	<aui:input name="customRegistration" type="hidden" value="<%= true %>"/>
		
	<aui:fieldset>
		<aui:column>
			<aui:input name="profileName" label="profile-name" helpMessage="help-msg-profile-name" required="<%= true %>" />
			
			<!-- 
			<aui:select name="bride" label="profile-gender" showEmptyOption="<%= true %>" required="<%= true %>" showRequiredLabel="<%= true %>"
					onChange="javascript:updateOtherFields(this);">
				<aui:option value="1" label="bride"/>
				<aui:option value="0" label="groom"/>
			</aui:select>
			 -->
						
			<aui:input name="emailAddress" required="<%= true %>">
				<aui:validator name="email"/>
			</aui:input>

		</aui:column>
		<aui:column>
		
			<div class="control-group">
				<label class="control-label" for="<%= PORTLET_NSPACE %>bride">
					<liferay-ui:message key="profile-type"/> <span class="label-required">(<liferay-ui:message key="required"/>)</span>
				</label>
				<aui:fieldset>
					<aui:column>
						<aui:input name="bride" type="radio" value="1" label="bride"/>
					</aui:column>
					<aui:column>
						<aui:input name="bride" type="radio" value="0" label="groom"/>
					</aui:column>
				</aui:fieldset>	
			</div>
			
			<!-- 
			<aui:select name="maritalStatus"
					listType="<%= Profile.class.getName() + StringPool.PERIOD + IConstants.LIST_MARITAL_STATUS %>" 
					showEmptyOption="<%= true %>" required="<%= true %>" showRequiredLabel="<%= true %>"/>
			-->
			 
			<div class="control-group">
				<label class="control-label" for="<%= PORTLET_NSPACE %>bornOn">
					<liferay-ui:message key="born-on"/> <span class="label-required">(<liferay-ui:message key="required"/>)</span>
				</label>		
					
				<aui:fieldset>
					<aui:column cssClass="month-selection">
						<select name="<%= PORTLET_NSPACE %>birthdayMonth">
							<option value="">Month</option>
							<%
								String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
								for (int i=0; i<12; i++) {
									%><option value="<%= String.format("%02d",i) %>"><%= months[i] %><%
								}
							%>
						</select>
					</aui:column>
			
					<aui:column cssClass="year-selection">
						<select name="<%= PORTLET_NSPACE %>birthdayYear">
							<option value="">Year</option>
							<%
								int curYear = Calendar.getInstance().get(Calendar.YEAR);
								int start = curYear - 70;
								int end = curYear - 12;
								for (int i=start; i<end; i++) {
									%><option value="<%= i %>"><%= i %><%
								}
							%>
						</select>
					</aui:column>				
				</aui:fieldset>			
			</div>

			<aui:select name="createdFor"
				listType="<%= Profile.class.getName() + StringPool.PERIOD + IConstants.LIST_CREATED_FOR %>" 
				showEmptyOption="<%= true %>" required="<%= true %>" showRequiredLabel="<%= true %>" />

		</aui:column>
	</aui:fieldset>
	
	<aui:button type="submit" value="Register" />
</aui:form>

<aui:script>

	AUI().ready(function(A) {
		var ele = document.getElementById('<%= PORTLET_NSPACE %>createdFor');
	   	ele.options[0].value = '';
	});
	
	Liferay.Util.focusFormField(
		document.<%= PORTLET_NSPACE %>fm.<%= PORTLET_NSPACE %>profileName);

	function updateOtherFields(obj) {
		var ele = document.getElementById('<%= PORTLET_NSPACE %>createdFor');
		
		if (obj.value == 0 || obj.value == 1) {
			ele.disabled = false;
		}
		
		for (var i=1; i < ele.length; i++) {
			var original = ele.options[i].text;
			//alert(original);
			//var parts = original.toString().split(":");
			//alerts(parts);
			//if (parts.length == 1) continue;
			//ele.options[i].text = parts[obj.value];
		}
	}
</aui:script>