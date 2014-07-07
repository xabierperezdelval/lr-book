<%@ include file="/html/init.jsp"%>

<portlet:actionURL var="uploadAssetsURL" name="uploadAssets"/>

<aui:form action="<%= uploadAssetsURL %>" enctype="multipart/form-data">
	<aui:input name="assetsMaster" type="file" required="true">
		<aui:validator name="acceptFiles">'xls,xlsx'</aui:validator>
	</aui:input>
	
	<aui:input name="loadAssetData" type="checkbox" label="load-asset-data"/>
	
	<aui:input name="loadEquityPrice" type="checkbox" label="load-equity-price"/>
	
	<aui:button type="submit" value="upload" />
</aui:form>