<%@include file="/html/invitation/init.jsp"%>

<%
	PortletURL iteratorURL = renderResponse.createRenderURL();
	iteratorURL.setParameter("jspPage","/html/invitation/my_invitation.jsp");
	String str1 = "submitForm('" + iteratorURL.toString() + "')";
	
	User users = themeDisplay.getUser();
	long userId=users.getUserId();
	String invitationStatus = StringPool.BLANK;
	List<InvitationCard> invitationcard = new ArrayList();
	if (Validator.isNotNull(ParamUtil.getString(request,"invitationStatus"))) {
		invitationStatus = ParamUtil.getString(request,"invitationStatus");
		} else {
		invitationStatus = "acceped";
	}
	
	if (invitationStatus.equals("sent")) {
		invitationcard = InvitationCardLocalServiceUtil.findByUserIdSatus(userId, InvitationConstants.STATUS_INVITED);
	} else if (invitationStatus.equals("pending")) {
		invitationcard = InvitationCardLocalServiceUtil.findByUserIdSatus(userId, InvitationConstants.STATUS_PENDING);
	} 
	 else {
		 invitationcard = InvitationCardLocalServiceUtil.findByUserIdSatus(userId, InvitationConstants.STATUS_ACCEPTED);
	}
	
%>

<liferay-ui:error key="No record to display" message="this-is-error-message-1" />

<aui:form name="sampleForm" method="post">
	<aui:select name="invitationStatus" onChange="<%=str1%>"
		id="formStatus">
		<aui:option value="acceped" label="accepted" selected="true" />
		<aui:option value="sent" label="sent-email" />
		<aui:option value="pending" label="pending" />
	</aui:select>
</aui:form>

<liferay-ui:search-container delta="4" iteratorURL="<%=iteratorURL%>" emptyResultsMessage="empty-list">
	<liferay-ui:search-container-results
		total="<%=invitationcard.size()%>"
		results="<%=ListUtil.subList(invitationcard,
						searchContainer.getStart(), searchContainer.getEnd())%>" />

	<liferay-ui:search-container-row className="com.mpower.slayer.model.InvitationCard" modelVar="Invitationcards">
		<liferay-ui:search-container-column-text name="invitee-email" property="inviteeEmail" />
		<liferay-ui:search-container-column-text name="createdDate" property="createDate" >
			<fmt:formatDate value="<%= Invitationcards.getCreateDate() %>" pattern="dd/MMM/yyyy"/>
		</liferay-ui:search-container-column-text>
	</liferay-ui:search-container-row>
	
	<liferay-ui:search-iterator searchContainer="<%=searchContainer%>" />
</liferay-ui:search-container>

<script type="text/javascript">
	function submitForm(url){
		document.forms["<portlet:namespace/>sampleForm"].action = url;
		document.forms["<portlet:namespace/>sampleForm"].submit();
	}
</script>



 