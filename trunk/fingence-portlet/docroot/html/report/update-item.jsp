<%@ include file="/html/report/init.jsp"%>

<%@page import="com.fingence.slayer.service.AssetLocalServiceUtil"%>
<%@page import="com.fingence.slayer.service.PortfolioItemLocalServiceUtil"%>

<%
	long itemId = ParamUtil.getLong(request, "portfolioItemId");
	long portfolioId = ParamUtil.getLong(request, "portfolioId");
	PortfolioItem portfolioItem = new PortfolioItemImpl();
	Asset asset = new AssetImpl();
	
	if (itemId > 0l) {
		portfolioItem = PortfolioItemLocalServiceUtil.fetchPortfolioItem(itemId);
		asset = AssetLocalServiceUtil.fetchAsset(portfolioItem.getAssetId());
	}
%>

<aui:form>
	<aui:input name="itemId" type="hidden" value="<%= itemId %>"/>
	<aui:input name="portfolioId" type="hidden" value="<%= portfolioId %>"/>
			
	<aui:row>
		<aui:column>
			<aui:input name="isinId" value="<%= asset.getId_isin() %>" required="true" />
		</aui:column>
		<aui:column>
			<aui:input name="ticker" value="<%=asset.getSecurity_ticker() %>" required="true"/>
		</aui:column>
	</aui:row>
	
	<aui:row>
		<aui:column>
			<aui:input name="purchasePrice" value="<%= portfolioItem.getPurchasePrice() %>" required="true"/>
		</aui:column>
		<aui:column>
			<aui:input name="purchaseDate" required="true" value="<%= PageUtil.getFormattedDate(portfolioItem.getPurchaseDate()) %>" />	
		</aui:column>
	</aui:row>
	
	<aui:row>
		<aui:column>
			<aui:input name="purchaseQty" value="<%= portfolioItem.getPurchaseQty() %>" required="true"/>
		</aui:column>
		<aui:column>
			<c:if test="<%= itemId > 0 %>">
				<aui:input name="currency" value="<%= asset.getCurrency() %>" readonly="true" />
			</c:if>
			&nbsp;
		</aui:column>
	</aui:row>
	
	<aui:row>
		<aui:column>
			<c:choose>
				<c:when test="<%= itemId > 0l && !asset.getCurrency().equalsIgnoreCase(IConstants.CURRENCY_USD) %>">
					<aui:input name="purchasedFx" cssClass="width-85" value="<%= portfolioItem.getPurchasedFx() %>" prefix="<%= IConstants.CURRENCY_UNIT + StringPool.SPACE + asset.getCurrency() + StringPool.EQUAL %>" suffix="<%= IConstants.CURRENCY_USD %>" />
				</c:when>
				<c:otherwise>
					&nbsp;
				</c:otherwise>
			</c:choose>
		</aui:column>
	</aui:row>
	
	<aui:row>
		<aui:column><aui:button onclick='javascript:saveItem();' value="save" cssClass="btn-primary"/></aui:column>
		<aui:column><aui:button onclick='javascript:closePopup();' value="cancel" cssClass="btn-primary"/></aui:column>
	</aui:row>
		
</aui:form>

<portlet:actionURL name="updatePortfolioItem" var="updateItemURL" 
	windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>" />

<aui:script>
	$(function() {
		var maxDate = new Date();
		var minDate = new Date(maxDate.getFullYear()-10, 0, 0);
		
		$('#<portlet:namespace/>purchaseDate').datepicker({
			minDate: minDate, 
			maxDate: maxDate, 
			changeMonth: true, 
			changeYear: true
		});
	});

    function saveItem() {
    
    	var isinId = document.getElementById('<portlet:namespace/>isinId').value;
		var ticker = document.getElementById('<portlet:namespace/>ticker').value;
		var purchasePrice = document.getElementById('<portlet:namespace/>purchasePrice').value;
		var purchaseDate = document.getElementById('<portlet:namespace/>purchaseDate').value;
		var purchaseQty = document.getElementById('<portlet:namespace/>purchaseQty').value;
				
		if(!(isinId == "" || ticker == "" || purchasePrice =="" || purchaseDate == "" || purchaseQty == "")) {
			AUI().io.request('<%= updateItemURL %>',{
				sync: true,
				method: 'POST',
				form: { id: '<portlet:namespace/>fm' },
				on: {
					success: function() {
						Liferay.Util.getWindow('<portlet:namespace/>editPortfolioItemPopup').destroy();
	                	Liferay.Util.getOpener().<portlet:namespace/>reloadPortlet();
	    			}
	  			}
	 		}); 
 		}         
    }
    
    function closePopup() {
    	Liferay.Util.getWindow('<portlet:namespace/>editPortfolioItemPopup').destroy();
    }
 
	AUI().use('autocomplete-list','aui-base','aui-io-request','autocomplete-filters','autocomplete-highlighters',function (A) {
		var testData;
		var autocompleteList1 = new A.AutoCompleteList({
			allowBrowserAutocomplete: 'true',
			activateFirstItem: 'true',
			inputNode: '#<portlet:namespace />isinId',
			resultTextLocator:'id_isin',
			render: 'true',
			resultHighlighter: 'phraseMatch',
			resultFilters:['phraseMatch'],
			source:function(){
				var inputValue = A.one("#<portlet:namespace />isinId").get('value');
				var myAjaxRequest = A.io.request('/api/jsonws/fingence-portlet.asset/get-assets/pattern/'+inputValue+'/ticker/false',{
					dataType: 'json',
					method:'POST',
					data:{
						<portlet:namespace />isinId:inputValue,
					},
					autoLoad:false,
					sync:true,
					on: {
						success:function(){
							testData = this.get('responseData');
						}
					}
				});
				myAjaxRequest.start();
				return testData;
			},
		});
	
		autocompleteList1.on('select', function (e) {
			Liferay.Service(
				'/fingence-portlet.asset/get-assets',
				{
			    	pattern: e.itemNode.text(),
			    	ticker: false
			  	},
			  	function(obj) {
			  		document.getElementById('<portlet:namespace />ticker').value = obj[0].security_ticker; 
			  	}
			);
		});
	});
	 
	AUI().use('autocomplete-list','aui-base','aui-io-request','autocomplete-filters','autocomplete-highlighters',function (A) {
		var testData;
		var autocompleteList2 = new A.AutoCompleteList({
			allowBrowserAutocomplete: 'true',
			activateFirstItem: 'true',
			inputNode: '#<portlet:namespace />ticker',
			resultTextLocator:'security_ticker',
			render: 'true',
			resultHighlighter: 'phraseMatch',
			resultFilters:['phraseMatch'],
			source:function() {
				var inputValue = A.one("#<portlet:namespace />ticker").get('value');
				var myAjaxRequest = A.io.request('/api/jsonws/fingence-portlet.asset/get-assets/pattern/'+inputValue+'/ticker/true',{
					dataType: 'json',
					method:'POST',
					data:{
						<portlet:namespace />ticker:inputValue,
					},
					autoLoad:false,
					sync:true,
					on: {
						success:function(){
							testData = this.get('responseData');
						}
					}
				});
				myAjaxRequest.start();
				return testData;
			},
		});
		
		autocompleteList2.on('select', function (e) {
			Liferay.Service(
				'/fingence-portlet.asset/get-assets',
				{
			    	pattern: e.itemNode.text(),
			    	ticker: true
			  	},
			  	function(obj) {
					document.getElementById('<portlet:namespace />isinId').value = obj[0].id_isin; 
			  	}
			);
		});
	});   
</aui:script>