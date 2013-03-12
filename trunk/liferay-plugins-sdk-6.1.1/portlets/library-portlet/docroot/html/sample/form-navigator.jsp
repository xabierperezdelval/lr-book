<%@include file="/html/library/init.jsp" %>

<%
	String[] categoryNames = {"Category1", "Category2"};
	String[] Category1 = {"item1", "item2"};
	String[] Category2 = {"item3", "item4"};
	String[][] categorySections = {Category1, Category2};
%>

<aui:form>
	<aui:field-wrapper label="NaviForm Sample">
		<liferay-ui:form-navigator 
			categoryNames="<%= categoryNames %>"
			categorySections="<%= categorySections %>" 
			showButtons="<%=true %>"
			jspPath="/html/sample/sections/" />
	</aui:field-wrapper>	
</aui:form>
