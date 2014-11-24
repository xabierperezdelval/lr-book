<%@include file="/html/investor/init.jsp"%>

<%
	int allocationBy = GetterUtil.getInteger(portletPreferences.getValue("allocationBy", "0"));
%>

<c:choose>
	<c:when test="<%= (allocationBy > 0) %>">
		<div id="gridContainer"></div>
	</c:when>

	<c:otherwise>
		Configure allocation type.  
	</c:otherwise>
</c:choose>

<aui:script>
	AUI().ready(function(A) {
		Liferay.Service(
  			'/fingence-portlet.portfolio/get-snapshot',
  			{
    			portfolioIds: '1,2',
    			allocationBy: <%= allocationBy %>
  			},
  			function(data) {
				renderGrid('#gridContainer', data);
  			}
		);
	});		
	
	function renderGrid(divId, data) {
        YUI().use('aui-datatable', function(Y) {
            var columns = [
                {
                	key: 'name', 
                    label: 'Name',
                    allowHTML: true,
                    sortable: true
                },
                {
                    key: 'gainLoss', 
                    label: '% Gain/Loss',
                    allowHTML: true,
                    sortable: true
                },
                {
                    key: 'weight',
                    label: '% Weight',
                    sortable: true,
                    allowHTML: true
                }
            ];
            
            new Y.DataTable({
                columnset: columns,
                recordset: data
            }).render(divId);
        });
    }	
</aui:script>