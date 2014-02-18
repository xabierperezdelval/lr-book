<%@ include file="/html/portfolio/init.jsp"%>

<%@page import="com.fingence.slayer.model.impl.AssetImpl"%>
<%@page import="com.fingence.slayer.model.Asset"%>
<%@page import="com.fingence.slayer.service.PortfolioItemLocalServiceUtil"%>

<%@page import="com.fingence.slayer.model.impl.PortfolioItemImpl"%>
<%@page import="com.fingence.slayer.model.PortfolioItem"%>

<%
	long portfolioItemId = ParamUtil.getLong(request, "portfolioItemId");

	Asset asset = new AssetImpl();
	PortfolioItem portfolioItem = new PortfolioItemImpl();
	if (portfolioItemId > 0l) {
		portfolioItem = PortfolioItemLocalServiceUtil.fetchPortfolioItem(portfolioItemId);
		asset = AssetLocalServiceUtil.fetchAsset(portfolioItem.getAssetId());
	}
	
	String readonly = (portfolioItemId > 0l)? "readonly" : StringPool.BLANK;
%>

<aui:form action="#">
	<aui:input type="hidden" name="portfolioItemId" value="<%= portfolioItemId %>"/>
	<aui:row>
		<aui:column>
			<aui:input name="isinId" value="<%= asset.getId_isin() %>" />
		</aui:column>
		<aui:column>
			<aui:input name="ticker" value="<%= asset.getSecurity_ticker() %>" />
		</aui:column>
	</aui:row>
	
	<aui:row>
		<aui:column>
			<aui:input name="purchasePrice" value="<%= portfolioItem.getPurchasePrice() %>" />
		</aui:column>
		<aui:column>
			<aui:input name="purchaseDate" value="<%= portfolioItem.getPurchaseDate() %>" />
		</aui:column>
	</aui:row>
	
	<aui:row>
		<aui:column>
			<aui:input name="purchaseQty" value="<%= portfolioItem.getPurchaseQty() %>" />
		</aui:column>
		<aui:column>
			&nbsp;
		</aui:column>
	</aui:row>
	<aui:button onclick='javascript:saveItem();' value="save" cssClass="btn-primary"/>
</aui:form>

<portlet:actionURL name="updatePortfolioItem" var="updateItemURL" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>" />

<aui:script>
    function saveItem() {
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