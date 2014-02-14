<%@page import="com.fingence.slayer.service.PortfolioItemLocalServiceUtil"%>
<%@ include file="/html/portfolio/init.jsp"%>

<%@page import="com.fingence.slayer.model.impl.PortfolioItemImpl"%>
<%@page import="com.fingence.slayer.model.PortfolioItem"%>

<%
	long itemId = ParamUtil.getLong(request, "itemId");

	PortfolioItem portfolioItem = new PortfolioItemImpl();
	if (itemId > 0l) {
		portfolioItem = PortfolioItemLocalServiceUtil.fetchPortfolioItem(itemId);
	}
%>

<aui:form>
	<aui:row>
		<aui:column>
			<aui:input name="isinId" />
		</aui:column>
		<aui:column>
			<aui:input name="ticker" />
		</aui:column>
	</aui:row>
	
	<aui:row>
		<aui:column>
			<aui:input name="purchasePrice" />
		</aui:column>
		<aui:column>
			<aui:input name="purchaseDate" />
		</aui:column>
	</aui:row>
	
	<aui:row>
		<aui:column>
			<aui:input name="purchaseQty" />
		</aui:column>
		<aui:column>
			<aui:button onclick='javascript:saveItem();' value="save" cssClass="btn-primary"/>
		</aui:column>
	</aui:row>
</aui:form>

<aui:script>
    function saveItem(){
        var frm = document.<portlet:namespace/>fm;
        var purchasePrice = frm.<portlet:namespace/>purchasePrice.value;
        var purchaseQuantity = frm.<portlet:namespace/>purchaseQuantity.value;
        
        Liferay.Service(
            '/fingence-portlet.portfolioitem/update-item',
            {
            portfolioItemId : '<%= portfolioItemId %>',
            purchasePrice : purchasePrice,
            purchaseQuantity : purchaseQuantity
           
          },
            function(data) {
                Liferay.Util.getWindow('<portlet:namespace/>editPortfolioItemPopup').destroy();


                Liferay.Util.getOpener().<portlet:namespace />reloadPortlet();
            }
        );          
    }
</aui:script>