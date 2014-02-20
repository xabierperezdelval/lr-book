<%@ include file="/html/report/init.jsp"%>

<div id="slicePortfolio"></div>

<%
	long portfolioId = ParamUtil.getLong(renderRequest, "portfolioId");
	String allocationBy = ParamUtil.getString(renderRequest, "allocationBy");
	String allocationParam = ParamUtil.getString(renderRequest, "allocationParam");
	String slicingParameter = ParamUtil.getString(renderRequest, "sliceBy");
%>

<aui:script>
	AUI().ready(function(A) {
		Liferay.Service(
  			'/fingence-portlet.myresult/get-my-results',
  			{
    			portfolioId: '<%= portfolioId %>'
  			},
  			function(data) {
  			
  				showPortfolioItems(data, '<%= slicingParameter %>', '<%= allocationParam %>', '#slicePortfolio');
  			}
		);
	});
	
	function showPortfolioItems(data, sliceBy, allocationParam, divId){
		var jsonArray = [];
		_(_.groupBy(data, allocationParam)).map(function(itemList, key) {
			
			if(key == sliceBy){
				_.each(itemList, function(item){
					var jsonObj = {};
					jsonObj.name = item.name;
					jsonObj.ticker = item.security_ticker;
					jsonObj.purchasedValue = item.purchasedMarketValue;
					jsonObj.currentValue = item.currentMarketValue;
					jsonObj.gainLoss = item.gain_loss;
					jsonObj.gainLossPerc = item.gain_loss_percent;
					jsonArray.push(jsonObj);
				});
			}
		});	
		
		YUI().use(
			'aui-datatable',
		  	function(Y) {
		    	var columns = [
		    		{key: 'name', label: 'Portfolio'},
		    		{key: 'ticker', label: 'Security Ticker'},
		    		{
			 			key: 'purchasedValue', 
			 			label: 'Purchased Value',
			 			formatter: function(obj) {
			 				obj.value = accounting.formatMoney(obj.value);
			 			}			 			
			 		},
			 		{
			 			key: 'currentValue', 
			 			label: 'Current Value',
			 			formatter: function(obj) {
			 				obj.value = accounting.formatMoney(obj.value);
			 			}			 			
			 		},
			 		{
			 			key: 'gainLoss', 
			 			label: 'Gain/Loss',
			 			formatter: function(obj) {
			 				obj.value = obj.value.toFixed(2);
			 			}			 			
			 		},
			 		{
			 			key: 'gainLossPerc', 
			 			label: 'Gain/Loss %',
			 			formatter: function(obj) {
			 				obj.value = obj.value.toFixed(2) + '%';
			 			}			 			
			 		}
		   	 	];
		   
		   		new Y.DataTable.Base({
					columnset: columns,
			    	recordset: jsonArray
				}).render(divId);
		  	}
		);
	}

</aui:script>