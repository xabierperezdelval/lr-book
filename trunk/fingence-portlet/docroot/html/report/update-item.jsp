<%@ include file="/html/report/init.jsp"%>

<portlet:actionURL name="updatePortfolioItem" var="updateItemURL" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>" />

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

	<aui:button onclick='javascript:saveItem();' value="save" cssClass="btn-primary"/>	
</aui:form>

<aui:script>
	$(function() {
		var maxDate = new Date();
		var minDate = new Date(maxDate.getFullYear()-2, 0, 0);
		
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
		var datepicker = document.getElementById('<portlet:namespace/>purchaseDate').value;
		var purchaseQty = document.getElementById('<portlet:namespace/>purchaseQty').value;
		
		if(!(isinId == "" || ticker == "" || purchasePrice =="" || datepicker == "" || purchaseQty == "")) {		
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