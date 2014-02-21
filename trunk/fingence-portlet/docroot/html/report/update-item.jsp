<%@ include file="/html/report/init.jsp"%>
<portlet:actionURL name="updatePortfolioItem" var="updateItemURL" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>" />
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
		if(Validator.isNotNull(pDate))
			purchaseDate = dateFormat.format(pDate);
	}
%>

<aui:form>
	<aui:row>
		<aui:column>
			<aui:input name="isinId" value="<%=asset.getId_isin() %>" required="true"/>
			<aui:input name="itemId" type="hidden" value="<%= itemId %>"/>
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
			<aui:button onclick='javascript:saveItem();' value="save" cssClass="btn-primary"/>
		</aui:column>
	</aui:row>
</aui:form>

<aui:script>
$(function() {
	$( "#<portlet:namespace />datepicker" ).datepicker({
		changeMonth: true,
		changeYear: true
	});
});

    function saveItem(){
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
</aui:script>