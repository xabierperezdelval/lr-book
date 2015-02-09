<%@include file="/html/profile/init.jsp" %>

<%@page import="java.util.Calendar"%>

<%
	String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
%>

<liferay-ui:header title="<%= profile.getCode() %>" />

<portlet:actionURL var="saveProfileURL" name="saveProfile"/>

<aui:form action="<%= saveProfileURL %>">
	<aui:input name="step" type="hidden" value="1"/>
	<aui:input name="profileId" type="hidden" bean="<%= profile %>"/>

	<!-- Basic Info -->
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
					<liferay-ui:message key="born-on"/> <span class="label-required">(<liferay-ui:message key="required"/>)</span>
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
	
	<aui:fieldset label="location-info">
		<aui:column>
			<aui:select name="residingCountry" required="true" showEmptyOption="true">
				<aui:option value="1"/>
			</aui:select> 
		</aui:column>
		<aui:column>
			<aui:select name="residingRegion" required="true" showEmptyOption="true">
				<aui:option value="1"/>
			</aui:select> 
		</aui:column>
		<aui:column>
			<aui:select name="residingCity" required="true" showEmptyOption="true">
				<aui:option value="1"/>
			</aui:select> 
		</aui:column>				
	</aui:fieldset>

	<aui:select name="sameLocation" label="same-location" showEmptyOption="true" required="true" inlineLabel="true">
		<aui:option value="true" label="yes"/>
		<aui:option value="false" label="no"/>
	</aui:select>
	
	<aui:button type="submit" />
</aui:form>

<%--

<liferay-ui:panel-container accordion="true">
	<liferay-ui:panel title="basic-info" extended="true" collapsible="true" >
		<aui:fieldset>
			<aui:row>
				<aui:column>
					<aui:select name="maritalStatus" required="true" showEmptyOption="true">
						<%= MyListUtil.getMaritalStatusOptions(locale, profile) %>
					</aui:select>					
				</aui:column>
				
				<aui:column>
					<aui:select name="createdFor" required="true" showEmptyOption="true">
						<%= MyListUtil.getCreatedFor(locale, profile) %>
					</aui:select>					
				</aui:column>
			</aui:row>
			
			<aui:row>
				<aui:column>
					<%@ include file="/html/edit/step1-bornon.jspf" %>					
				</aui:column>
				
				<aui:column>
					<aui:select name="complexion" required="true" showEmptyOption="true">
						<%= MyListUtil.getComplexionsList(locale, profile) %>
					</aui:select>						
				</aui:column>
			</aui:row>
			
			<aui:row>
				<aui:column>
					<aui:select name="height" required="true" showEmptyOption="true">
						<%= MyListUtil.getHeightList(locale, profile.getHeight()) %>
					</aui:select>				
				</aui:column>
				
				<aui:column>
					<aui:select name="weight" showEmptyOption="true">
						<%= MyListUtil.getWeightList(locale, profile.getWeight()) %>
					</aui:select>						
				</aui:column>
			</aui:row>
		</aui:fieldset>
	</liferay-ui:panel>
	
	<liferay-ui:panel title="location-info" extended="true" collapsible="true">

		<c:if test="<%= LocationLocalServiceUtil.userHasLocation(user.getUserId())  %>">
			<div>
				You are currently accessing the site from: <b><%= LocationLocalServiceUtil.getUserLocation(user.getUserId()) %></b>.
				<br/> Please confirm OR change <b>BIRTH</b> and <b>RESIDING</b> locations.		
			</div>		
		</c:if>
		
		<aui:fieldset>
			<aui:row>
				<aui:column>
					<aui:select name="countryOfBirth" required="true" showEmptyOption="true"
							onChange="javascript:cascade1(this, 'stateOfBirth', 'cityOfBirth');">
						<%= MyListUtil.getCountries(profile.getCountryOfBirth()) %>
					</aui:select>					
				</aui:column>
				<aui:column>
					<aui:select name="residingCountry" required="true" showEmptyOption="true" 
							onChange="javascript:cascade1(this, 'residingState', 'residingCity');">
						<%= MyListUtil.getCountries(profile.getResidingCountry()) %>
					</aui:select>				
				</aui:column>				
			</aui:row>
			
			<aui:row>
				<aui:column>
					<aui:select name="stateOfBirth" required="true" showEmptyOption="true"
							onChange="javascript:cascade2(this, 'cityOfBirth');">
						<%= MyListUtil.getLocations(profile.getCountryOfBirth(), profile.getStateOfBirth(), IConstants.LOC_TYPE_REGION) %>
					</aui:select>					
				</aui:column>
				<aui:column>
					<aui:select name="residingState" required="true" showEmptyOption="true"
							onChange="javascript:cascade2(this, 'residingCity');">
						<%= MyListUtil.getLocations(profile.getResidingCountry(), profile.getResidingState(), IConstants.LOC_TYPE_REGION) %>
					</aui:select>				
				</aui:column>				
			</aui:row>
			
			<aui:row>
				<aui:column>
					<aui:select name="cityOfBirth" required="true" showEmptyOption="true"
							onChange="javascript:cascade3(this, 'newCityOfBirth');">
						<%= MyListUtil.getLocations(profile.getStateOfBirth(), profile.getCityOfBirth(), IConstants.LOC_TYPE_CITY) %>
					</aui:select>				
				</aui:column>
				<aui:column>
					<aui:select name="residingCity" required="true" showEmptyOption="true"
							onChange="javascript:cascade3(this, 'newResidingCity');">
						<%= MyListUtil.getLocations(profile.getResidingState(), profile.getResidingCity(), IConstants.LOC_TYPE_CITY) %>
					</aui:select>			
				</aui:column>				
			</aui:row>		
			
			<aui:row>
				<aui:column>
					<div id="<portlet:namespace/>newCityOfBirthDiv" hidden="true">
						<aui:input name="newcityOfBirth" />
					</div>			
				</aui:column>
				<aui:column>
					<div id="<portlet:namespace/>newResidingCityDiv" hidden="true">
						<aui:input name="newresidingCity" />
					</div>		
				</aui:column>				
			</aui:row>
		</aui:fieldset>
	</liferay-ui:panel>	
</liferay-ui:panel-container>

<script type="text/javascript">
	function cascade1(obj, fld1, fld2) {
		var index = obj.selectedIndex;
		var child = document.getElementById("<portlet:namespace/>" + fld1);
		var grandChild = document.getElementById("<portlet:namespace/>" + fld2);
		
		if (index > 0) {
			var value = obj.options[index].value;
			Liferay.Service(
	  			'/inikah-portlet.location/get-regions',
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
		
		if (index > 0) {
			var value = obj.options[index].value;
			Liferay.Service(
	  			'/inikah-portlet.location/get-cities',
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
</script> 

--%>