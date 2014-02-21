<%@ include file="/html/report/init.jsp"%>

<div id="assetAllocationContainer"></div>

<%
	long portfolioId = ParamUtil.getLong(renderRequest, "portfolioId");
%>

<aui:script>
	AUI().ready(function(A) {
		Liferay.Service(
  			'/fingence-portlet.myresult/get-my-results',
  			{
    			portfolioId: '<%= portfolioId %>'
  			},
  			function(data) {
  			
  				showAllocationDetails(data, '#assetAllocationContainer');
  			}
		);
	});
	
	function showAllocationDetails(data, divId){
		
		
		YUI().use(
			'aui-datatable',
		  	function(Y) {
		    	var columns = [
		    		{key: 'name', label: 'Portfolio'},
		    		{key: 'security_ticker', label: 'Security Ticker'},
		    		{
			 			key: 'purchasedMarketValue', 
			 			label: 'Purchased Value',
			 			formatter: function(obj) {
			 				obj.value = accounting.formatMoney(obj.value);
			 			}
			 						 			
			 		},
			 		{
			 			key: 'currentMarketValue', 
			 			label: 'Current Value',
			 			formatter: function(obj) {
			 				obj.value = accounting.formatMoney(obj.value);
			 			}
			 		},
			 		{
			 			key: 'gain_loss', 
			 			label: 'Gain/Loss',
			 			formatter: function(obj) {
			 				obj.value = obj.value.toFixed(2);
			 			}
			 					 			
			 		},
			 		{
			 			key: 'gain_loss_percent', 
			 			label: 'Gain/Loss %',
			 			formatter: function(obj) {
			 				obj.value = obj.value.toFixed(2);
			 			}
			 		}
		   	 	];
		   
		   		new Y.DataTable.Base({
					columnset: columns,
			    	recordset: data
				}).render(divId);
		  	}
		);
	}

</aui:script>