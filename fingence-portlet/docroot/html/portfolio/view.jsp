<%@page import="com.liferay.portal.util.PortalUtil"%>
<%@ include file="/html/portfolio/init.jsp"%>

<%
	List<Portfolio> portfolios = PortfolioLocalServiceUtil.getPortfolios(userId);
	
	PortletURL detailsURL = renderResponse.createRenderURL();
	detailsURL.setParameter("jspPage", "/html/portfolio/details.jsp");
	detailsURL.setParameter("backURL", themeDisplay.getURLCurrent());
%>

<portlet:renderURL var="addPortfolioURL">
	<portlet:param name="jspPage" value="/html/portfolio/update.jsp"/>
</portlet:renderURL>

<c:if test="<%= (userType != IConstants.USER_TYPE_BANK_ADMIN) %>">
	<aui:button-row>
		<aui:button cssClass="btn-primary btn" href="<%= addPortfolioURL %>" value="add-portfolio" />
		<c:if test="<%= (userType == IConstants.USER_TYPE_WEALTH_ADVISOR) %>">
			<aui:button cssClass="btn-primary btn" href="<%= PortalUtil.getCreateAccountURL(request, themeDisplay) %>" value="add-user" />
		</c:if>
	</aui:button-row>
</c:if>

<liferay-ui:search-container delta="5" emptyResultsMessage="no-portfolios">
	<liferay-ui:search-container-results results="<%=ListUtil.subList(portfolios, searchContainer.getStart(),searchContainer.getEnd()) %>" total="<%= portfolios.size() %>"/>
	<liferay-ui:search-container-row className="com.fingence.slayer.model.Portfolio" modelVar="portfolio">
		<%
			long portfolioId = portfolio.getPortfolioId();
			detailsURL.setParameter("portfolioId", String.valueOf(portfolioId));
		%>
		<liferay-ui:search-container-column-text name="portfolioName" href="<%= detailsURL.toString() %>"/>
		<liferay-ui:search-container-column-text name="institution">
			<%= BridgeServiceUtil.getOrganizationName(portfolio.getInstitutionId()) %>
		</liferay-ui:search-container-column-text>
		<liferay-ui:search-container-column-text name="assets">
			<%= PortfolioLocalServiceUtil.getAssetsCount(portfolioId) %>
		</liferay-ui:search-container-column-text>
		<liferay-ui:search-container-column-text name="Last Updated">
			<fmt:formatDate value="<%= portfolio.getModifiedDate() %>" pattern="dd/MMM/yyyy"/>
		</liferay-ui:search-container-column-text>
       	<liferay-ui:search-container-column-text name="primary">
       		<c:choose>
       			<c:when test="<%= !portfolio.isPrimary() && (userType == IConstants.USER_TYPE_INVESTOR || userType == IConstants.USER_TYPE_WEALTH_ADVISOR) %>">
       				<a href="javascript:void(0);" onClick="javascript:makePrimary('<%= portfolioId %>');">Set</a>
       			</c:when>     			
       			<c:otherwise>
       				<b><%= portfolio.isPrimary() %></b>
       			</c:otherwise>
       		</c:choose>
       	</liferay-ui:search-container-column-text>
       	
       	<c:if test="<%= (userType == IConstants.USER_TYPE_WEALTH_ADVISOR) %>">
            <liferay-ui:search-container-column-text name="Edit">
            	<a href="javascript:void(0);" onClick="javascript:showPopupForEdit('<%= portfolioId %>');"><img src="<%= themeDisplay.getPathThemeImages() + IConstants.THEME_ICON_EDIT %>"/></a>
            </liferay-ui:search-container-column-text>
        </c:if>
	
	</liferay-ui:search-container-row>
	<liferay-ui:search-iterator searchContainer="<%= searchContainer %>"/>
</liferay-ui:search-container>

<aui:script>
	function makePrimary(portfolioId) {
		Liferay.Service(
  			'/fingence-portlet.portfolio/make-primary',
  			{
    			portfolioId: portfolioId
  			},
  			function(obj) {
    			Liferay.Portlet.refresh('#p_p_id<portlet:namespace/>');
  			}
		);
	}
	
	<c:if test="<%= (userType == IConstants.USER_TYPE_WEALTH_ADVISOR) %>">
		function showPopupForEdit(portfolioId) {
		
			var ajaxURL = Liferay.PortletURL.createRenderURL();
			ajaxURL.setPortletId('portfolio_WAR_fingenceportlet');
			ajaxURL.setParameter('jspPage', '/html/portfolio/update.jsp');
			ajaxURL.setParameter('portfolioId', portfolioId);
			ajaxURL.setWindowState('<%= LiferayWindowState.POP_UP.toString() %>');
					
			AUI().use('aui-dialog', 'aui-dialog-iframe', function(A) {
				Liferay.Util.openWindow({
                	dialog: {
                    	centered: true,
                    	modal: true,
                       	width: 600,
                    	height: 400                
                	},
                	id: '<portlet:namespace/>editPortfolioPopup',
                	title: 'Update Portfolio Info...',
                	uri: ajaxURL
            	});
            	
            	Liferay.provide(
                 	window, '<portlet:namespace/>reloadPortlet', function() {
                        Liferay.Portlet.refresh('#p_p_id<portlet:namespace />');
                    }
                );			
			}); 
		}
	</c:if>
</aui:script>