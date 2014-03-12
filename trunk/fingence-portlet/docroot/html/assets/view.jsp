<%@ include file="/html/init.jsp"%>

<portlet:actionURL var="uploadAssetsURL" name="uploadAssets"/>

<aui:form action="<%= uploadAssetsURL %>" enctype="multipart/form-data">
	<aui:input name="assetsMaster" type="file" required="true">
		<aui:validator name="acceptFiles">'xls,xlsx'</aui:validator>
	</aui:input>
	
	<aui:button type="submit" value="upload" />
</aui:form>