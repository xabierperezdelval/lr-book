<%@ include file="/html/report/init.jsp"%>

<portlet:actionURL name="updatePortfolioItem" var="updateItemURL" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>" />

<portlet:renderURL var="renderDasboardURL">
	<portlet:param name="jspPage" value="/html/report/details.jsp" />
</portlet:renderURL>

<%
	long itemId = ParamUtil.getLong(request, "portfolioItemId");
	long portfolioId = ParamUtil.getLong(request, "portfolioId");
	PortfolioItem portfolioItem = new PortfolioItemImpl();
	Asset asset = new AssetImpl();
	String purchaseDate = StringPool.BLANK;
	
	if (itemId > 0l) {
		portfolioItem = PortfolioItemLocalServiceUtil.fetchPortfolioItem(itemId);
		asset = AssetLocalServiceUtil.fetchAsset(portfolioItem.getAssetId());
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date pDate = portfolioItem.getPurchaseDate();
		if (Validator.isNotNull(pDate))
			purchaseDate = dateFormat.format(pDate);
	}
%>

<aui:form  inlineLabel="true" cssClass="popupForm">
	<aui:row>
		<aui:column>
			<aui:input name="isinId" value="<%= asset.getId_isin() %>" required="true" />
			<aui:input name="itemId" type="hidden" value="<%= itemId %>"/>
			<aui:input name="redirectURL" type="hidden" value="<%= renderDasboardURL %>"/>
			<aui:input name="portfolioId" type="hidden" value="<%= portfolioId %>"/>
		</aui:column>
		<aui:column>
			<aui:input name="ticker" value="<%=asset.getSecurity_ticker() %>" required="true"/>
		</aui:column>
	</aui:row>
	
	<aui:row>
		<aui:column>
			<aui:input name="purchasePrice" value="<%=portfolioItem.getPurchasePrice() %>" required="true"/>
		</aui:column>
		<aui:column>
			<div class="aui-datepicker aui-helper-clearfix" id="#<portlet:namespace />startDatePicker">
				<aui:input name="purchaseDate" id="datepicker" size="30" readonly="true"  value="<%=purchaseDate %>" required="true"/>
			</div>
			<!-- <input type="text" id="datepicker"> -->
		</aui:column>
	</aui:row>
	
	<aui:row>
		<aui:column>
			<aui:input name="purchaseQty" value="<%=portfolioItem.getPurchaseQty() %>" required="true"/>
		</aui:column>
		<aui:column>
			<aui:input name="purchasedFx" value="<%=portfolioItem.getPurchasedFx() %>" required="true"/>
		</aui:column>
	</aui:row>

	<aui:button onclick='javascript:saveItem();' value="save" cssClass="btn-primary"/>
	
</aui:form>

<aui:script>
	$(function() {
		$('#<portlet:namespace/>datepicker').datepicker({
			changeMonth: true,
			changeYear: true
		});
	});

    function saveItem(){
    	var isinId = document.getElementById('<portlet:namespace/>isinId').value;
		var ticker = document.getElementById('<portlet:namespace/>ticker').value;
		var purchasePrice = document.getElementById('<portlet:namespace/>purchasePrice').value;
		var datepicker = document.getElementById('<portlet:namespace/>datepicker').value;
		var purchaseQty = document.getElementById('<portlet:namespace/>purchaseQty').value;
		var purchasedFx = document.getElementById('<portlet:namespace/>purchasedFx').value;
		
		if(!(isinId == "" || ticker == "" || purchasePrice =="" || datepicker == "" || purchaseQty == "" || purchasedFx =="")){
		
			document.getElementById('<portlet:namespace/>purchasePrice').value = parseFloat(purchasePrice).toFixed(2);
			document.getElementById('<portlet:namespace/>purchaseQty').value = parseFloat(purchaseQty).toFixed(2);
			document.getElementById('<portlet:namespace/>purchasedFx').value = parseFloat(purchasedFx).toFixed(2);
		
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
</aui:script>