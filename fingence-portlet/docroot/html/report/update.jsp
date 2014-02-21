
<%@ include file="/html/report/init.jsp"%>

<portlet:actionURL name="savePortfolio" var="savePortfolioURL"
	windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>" />

<%
	long portfolioId = ParamUtil.getLong(request, "portfolioId");
	String portfolioUploadURL = StringPool.BLANK;
	Portfolio portfolio = new PortfolioImpl();
	if (portfolioId > 0l) {
		portfolioUploadURL = "#";
		portfolio = PortfolioLocalServiceUtil.fetchPortfolio(portfolioId);
	}else{
		ResourceURL resourceURL = renderResponse.createResourceURL();
		portfolioUploadURL = resourceURL.toString();
	}
%>

<aui:form action="<%= portfolioUploadURL %>" enctype="multipart/form-data" id="fm">
	<aui:input type="hidden" name="portfolioId" value="<%= portfolio.getPortfolioId() %>"/>
	<aui:row>
		<aui:column>
			<aui:input name="portfolioName" required="true" autoFocus="true" value="<%= portfolio.getPortfolioName() %>"/>
		</aui:column>
		
		<aui:column cssClass="display-down">
			<aui:input type="checkbox" name="trial" label="this-is-a-trial" value="<%= portfolio.isTrial() %>"/>
		</aui:column>		
	</aui:row>

	<aui:row>
		<aui:column>
			<aui:select name="investorId" label="investor" required="true" showEmptyOption="true">
				<%
					List<User> users = BridgeServiceUtil.getUsersByTargetType(userId, IConstants.USER_TYPE_INVESTOR);
					for (User _user: users) {
						if(_user.getStatus()==WorkflowConstants.STATUS_APPROVED){
						%><aui:option value="<%= _user.getUserId() %>" label="<%= _user.getFullName() %>" selected="<%= (_user.getUserId() == portfolio.getInvestorId()) %>" /><% 
						}
					}	
				%>
			</aui:select>			
		</aui:column>
		
		<aui:column>
			<aui:select name="wealthAdvisorId" label="wealth-advisor" required="true" showEmptyOption="true">
				<%
					List<User> users = BridgeServiceUtil.getUsersByTargetType(userId, IConstants.USER_TYPE_WEALTH_ADVISOR);
					for (User _user: users) {
						%><aui:option value="<%= _user.getUserId() %>" label="<%= _user.getFullName() %>" selected="<%= (_user.getUserId() == portfolio.getWealthAdvisorId()) %>" /><% 
					}
				%>
			</aui:select>			
		</aui:column>		
	</aui:row>
	
	<aui:row>
		<aui:column>
			<aui:select name="relationshipManagerId" label="relationship-manager" required="true" showEmptyOption="true">
				<%
					List<User> users = BridgeServiceUtil.getUsersByTargetType(userId, IConstants.USER_TYPE_REL_MANAGER);
					for (User _user: users) {
						if(_user.getStatus()==WorkflowConstants.STATUS_APPROVED){
						%><aui:option value="<%= _user.getUserId() %>" label="<%= _user.getFullName() %>" selected="<%= (_user.getUserId() == portfolio.getRelationshipManagerId()) %>" /><% 
						}
					}	
				%>
			</aui:select>			
		</aui:column>
		
		<aui:column>
			<aui:select name="institutionId" label="institution" required="true" showEmptyOption="true">
				<%
					List<Organization> institutions = BridgeServiceUtil.getInstitutions();
					for (Organization institution: institutions) {
						%><aui:option value="<%= institution.getOrganizationId() %>" label="<%= institution.getName() %>" selected="<%= (institution.getOrganizationId() == portfolio.getInstitutionId()) %>" /><% 
					}
				%>
			</aui:select>
		</aui:column>		
	</aui:row>
	<c:choose>
		<c:when test="<%= (portfolioId == 0l) %>">
			<aui:input type="file" name="excelFile" label="portfolio-assets"/>
			<aui:button onClick="javascript:uploadExcelURL();" value="save" cssClass="primary-btn"/>
		</c:when>
		<c:otherwise>
			<aui:button type="button" onClick="javascript:updateInfo();" value="save" cssClass="primary-btn"/>
		</c:otherwise>
	</c:choose>
	
</aui:form>

<aui:script>
	function updateInfo() {
		AUI().io.request('<%= savePortfolioURL %>',{
			sync: true,
			method: 'POST',
			form: { id: '<portlet:namespace/>fm' },
			on: {
				success: function() {
					Liferay.Util.getWindow('<portlet:namespace/>editPortfolioPopup').destroy();
                	Liferay.Util.getOpener().<portlet:namespace/>reloadPortlet();
    			}
  			}
 		});
	}
	
	
	function uploadExcelURL() {
    	<%-- alert("<%=portfolioUploadURL%>"); --%>
        var options =
           {               
               url:'<%=portfolioUploadURL%>',
               success:function(data) {
            	   Liferay.Util.getWindow('<portlet:namespace/>editPortfolioPopup').destroy();
                   Liferay.Util.getOpener().<portlet:namespace/>reloadPortlet();
               },
               error:function(){
               		alert('error');
               }
           };
        	
           $("#<portlet:namespace/>fm").ajaxSubmit(options);
           return false; 
           
    }
	
</aui:script >