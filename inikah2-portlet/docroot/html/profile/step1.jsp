<%@page import="com.slayer.model.Location"%>
<%@page import="com.slayer.service.LocationLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.util.TextFormatter"%>
<%@include file="/html/profile/init.jsp" %>

<%@page import="java.util.Calendar"%>
<%@page import="com.liferay.portal.model.Country"%>
<%@page import="com.liferay.portal.service.CountryServiceUtil"%>

<%
	String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
%>

<lui:header title="<%= profile.getCode() %>" />

<portlet:actionURL var="saveProfileURL" name="saveProfile"/>

<aui:form action="<%= saveProfileURL %>">
	<aui:input name="step" type="hidden" value="1"/>
	<aui:input name="profileId" type="hidden" bean="<%= profile %>"/>

	<!-- Basic Info -->
	<aui:fieldset label="basic-info">
		<aui:row>
			<aui:column>
				<aui:input name="profileName" required="true" autoFocus="true" bean="<%= profile %>"/>
			</aui:column>
			
			<aui:column>
				<aui:select name="createdFor" required="true" showEmptyOption="true">
				
					<c:if test="<%= ProfileLocalServiceUtil.showSelfOption(user.getUserId()) %>">
						<aui:option value="1" label="created-for-self" selected="<%= profile.getCreatedFor() == 1 %>"/>
					</c:if>
					
					<c:choose>
						<c:when test="<%= !profile.isBride() %>">
							<aui:option value="2" label="created-for-son" selected="<%= profile.getCreatedFor() == 2 %>"/>
							<aui:option value="3" label="created-for-brother" selected="<%= profile.getCreatedFor() == 3 %>"/>
							<aui:option value="4" label="created-for-grand-son" selected="<%= profile.getCreatedFor() == 4 %>"/>
							<aui:option value="5" label="created-for-uncle" selected="<%= profile.getCreatedFor() == 5 %>"/>
							<aui:option value="6" label="created-for-nephew" selected="<%= profile.getCreatedFor() == 6 %>"/>
						</c:when>
						<c:otherwise>
							<aui:option value="7" label="created-for-daughter" selected="<%= profile.getCreatedFor() == 7 %>"/>
							<aui:option value="8" label="created-for-sister" selected="<%= profile.getCreatedFor() == 8 %>"/>
							<aui:option value="9" label="created-for-grand-daughter" selected="<%= profile.getCreatedFor() == 9 %>"/>
							<aui:option value="10" label="created-for-aunt" selected="<%= profile.getCreatedFor() == 10 %>"/>
							<aui:option value="11" label="created-for-niece" selected="<%= profile.getCreatedFor() == 11 %>"/>						
						</c:otherwise>
					</c:choose>
					
					<aui:option value="12" label="created-for-cousin" selected="<%= profile.getCreatedFor() == 12 %>"/>
					<aui:option value="13" label="created-for-friend" selected="<%= profile.getCreatedFor() == 13 %>"/>
					<aui:option value="14" label="created-for-neighbor" selected="<%= profile.getCreatedFor() == 14 %>"/>
					<aui:option value="15" label="created-for-customer" selected="<%= profile.getCreatedFor() == 15 %>"/>				
				</aui:select>
			</aui:column>
			<aui:column>
				<aui:select name="maritalStatus" required="true" showEmptyOption="true">
					<aui:option value="1" label="marital-status-single" selected="<%= profile.getMaritalStatus() == 1 %>"/>
					<aui:option value="2" label="marital-status-divorced" selected="<%= profile.getMaritalStatus() == 2 %>"/>
					<aui:option value="3" label="marital-status-widow" selected="<%= profile.getMaritalStatus() == 3 %>"/>
					<c:if test="<%= !profile.isBride() %>">
						<aui:option value="4" label="marital-status-married" selected="<%= profile.getMaritalStatus() == 4 %>"/>
					</c:if>
				</aui:select> 
			</aui:column>			
		</aui:row>
		
		<aui:row>
			<aui:column>
				<div class="control-group">
					<label class="control-label" for="bornOn">
						<lui:message key="born-on"/> <span class="label-required">(<lui:message key="required"/>)</span>
					</label>		
						
					<aui:column cssClass="month-selection">
						<select name="<portlet:namespace/>bornMonth">
							<option value="-1">Month...</option>
							<%
								for (int i=0; i<months.length; i++) {
									%><option value="<%= i %>"><%= months[i] + StringPool.OPEN_PARENTHESIS + String.format("%02d", i+1) + StringPool.CLOSE_PARENTHESIS %></option><%
								}
							%>
						</select>
					</aui:column>
			
					<aui:column cssClass="year-selection">
						<select name="<portlet:namespace/>bornYear">
							<option value="-1">Year...</option>
							<%
								int curYear = Calendar.getInstance().get(Calendar.YEAR);
								int start = curYear - 71;
								int end = curYear - 10;
								
								for (int i=end; i>start; i--) {
									%><option value="<%= i %>"><%= i + StringPool.NBSP + StringPool.OPEN_PARENTHESIS + StringPool.TILDE + (curYear - i) + " Yrs" + StringPool.CLOSE_PARENTHESIS %></option><%									
								}
							%>
						</select>
					</aui:column>
				</div>
			</aui:column>	
			<aui:column>
				<aui:select name="height" required="true" showEmptyOption="true">
					<%
						for (int i=140; i<=190; i++) {
							String label = i + " Cms " + StringPool.SLASH + StringPool.NBSP + MyListUtil.getHeight(i);
							%><aui:option value="<%= i %>" label="<%= label %>" selected="<%= profile.getHeight() == i %>"/><%
						}
					%>
				</aui:select>		
			</aui:column>
			<aui:column>
				<aui:select name="weight" showEmptyOption="true" bean="<%= profile %>">
					<%
						for (int i=30; i<=120; i++) {
							String label = String.format("%03d", i) + " Kgs " + StringPool.SLASH + StringPool.NBSP + MyListUtil.getWeight(i);
							%><aui:option value="<%= i %>" label="<%= label %>" selected="<%= profile.getWeight() == i %>"/><%
						}
					%>
				</aui:select>		
			</aui:column>
		</aui:row>	
	</aui:fieldset>

	
	<aui:fieldset label="location-info">
		<aui:column columnWidth="50">
			<aui:select name="residingCountry" required="true" showEmptyOption="true"
					onChange="javascript:cascade1(this, 'residingState', 'residingCity');">
				<%
					List<Country> countries = CountryServiceUtil.getCountries(false);
					for (Country country: countries) {
						long countryId = country.getCountryId();
						%>
							<aui:option value="<%= countryId %>" 
								label="<%= TextFormatter.format(country.getName(), TextFormatter.J) %>" 
								selected="<%= (countryId == profile.getResidingCountry()) %>" />
						<%
					}
				%>
			</aui:select>
			
			<aui:select name="residingState" required="true" showEmptyOption="true" 
					onChange="javascript:cascade2(this, 'residingCity');">
				<%
					List<Location> regions = LocationLocalServiceUtil.getRegions(profile.getResidingCountry());
					for (Location region: regions) {
						long regionId = region.getLocationId();
						%>
							<aui:option value="<%= regionId %>" 
								label="<%= region.getName() %>" 
								selected="<%= (regionId == profile.getResidingState()) %>" />
						<%
					}
				%>
			</aui:select>
			
			<aui:select name="residingCity" required="true" showEmptyOption="true" 
					onChange="javascript:cascade3(this, 'newResidingCity');">
				<%
					List<Location> cities = LocationLocalServiceUtil.getCities(profile.getResidingState());
					for (Location city: cities) {
						long cityId = city.getLocationId();
						%>
							<aui:option value="<%= cityId %>" 
								label="<%= city.getName() %>" 
								selected="<%= (cityId == profile.getResidingCity()) %>" />
						<%
					}
				%>
			</aui:select> 
			
			<div id="<portlet:namespace/>newResidingCityDiv" hidden="true">
				<aui:input name="newResidingCity" />
			</div>				
	 
		</aui:column>
		<aui:column>
			another column...
		</aui:column>
	</aui:fieldset>

	<aui:button type="submit" />
</aui:form>

<aui:script>
	function cascade1(obj, fld1, fld2) {
		
		var index = obj.selectedIndex;
		var child = document.getElementById("<portlet:namespace/>" + fld1);
		var grandChild = document.getElementById("<portlet:namespace/>" + fld2);
		
		if (fld2 == 'residingCity') {
			hideNewCity('newResidingCity');	
		}
		
		if (index > 0) {
			var value = obj.options[index].value;
			Liferay.Service(
	  			'<%= request.getContextPath() %>.location/get-regions',
	  			{
	    			countryId: value
	  			},
	  			function(data) {
	  				child.length = data.length + 1;
	  				child.selectedIndex = 0;
	  				child.options[0] = new Option("-- select --", "");
	  				for (var i=0; i<(data.length); i++) {
	  					var location = data[i];
	  					child.options[i+1] = new Option(location.name, location.locationId);
	  				}
	  				
	  				clear(grandChild);
	  			}
			);			
		} else {
			clear(child);
			clear(grandChild);
		}
	}
	
	function cascade2(obj, fld1) {
		var index = obj.selectedIndex;
		var child = document.getElementById("<portlet:namespace/>" + fld1);
		
		if (fld1 == 'residingCity') {
			hideNewCity('newResidingCity');	
		}
		
		if (index > 0) {
			var value = obj.options[index].value;
			Liferay.Service(
	  			'<%= request.getContextPath() %>.location/get-cities',
	  			{
	    			regionId: value
	  			},
	  			function(data) {
	  				child.length = data.length + 1;
	  				child.selectedIndex = 0;
	  				child.options[0] = new Option("-- select --", "");
	  				for (var i=0; i<(data.length); i++) {
	  					var location = data[i];
	  					child.options[i+1] = new Option(location.name, location.locationId);
	  				}
	  				child.options[data.length + 1] = new Option("-- New City --", "-1");
	  			}
			);			
		} else {
			clear(child);
		}
	}
	
	function cascade3(obj, fld) {

		var index = obj.selectedIndex;
		var value = obj.options[index].value;
		var div = document.getElementById("<portlet:namespace/>"+ fld +"Div");
		var fld = document.getElementById("<portlet:namespace/>" + fld);

		if (value == '-1') {
			div.style.display = 'block';
			fld.focus();
			fld.required = true;
		} else {
			div.style.display = 'none';
			fld.required = false;
		}
	}	
	
	function clear(fld) {
		fld.length = 1;
		fld.selectedIndex = 0;
		fld.options[0] = new Option("-- select --", "");
	}
	
	function hideNewCity(fld) {
		var div = document.getElementById("<portlet:namespace/>"+ fld +"Div");
		var fld = document.getElementById("<portlet:namespace/>" + fld);
		
		div.style.display = 'none';
		fld.required = false;
	}
</aui:script>