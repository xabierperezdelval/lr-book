<%@include file="/html/library/init.jsp"%>

<% 
	String bookId = ParamUtil.getString(request, "bookId");
	String redirectURL = ParamUtil.getString(request, "redirectURL"); 
%>

<portlet:actionURL var="uploadFilesURL" name="UploadFiles" />

<liferay-ui:error key="image-size-exceeded" 
			message="error-image-size-exceeded"/>

<aui:form action="<%= uploadFilesURL %>" enctype="multipart/form-data">
	<aui:input type="hidden" name="bookId" value="<%= bookId %>"/>
	<aui:input type="hidden" name="redirectURL" 
				value="<%= redirectURL %>"/>
	<aui:input name="coverImage" type="file">
		<aui:validator name="acceptFiles">
			'jpg, png'
		</aui:validator>
	</aui:input>
	
	<aui:input name="sampleChapter" type="file" />
	
	<aui:button type="submit"/>
</aui:form>