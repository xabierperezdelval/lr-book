<%@page import="com.fingence.util.PageUtil"%>
<%@page import="com.liferay.portal.util.PortalUtil"%>
<%@ include file="/html/portfolio/init.jsp"%>

<%
	List<Portfolio> portfolios = PortfolioLocalServiceUtil.getPortfolios(userId);
%>

<c:if test="<%= (userType != IConstants.USER_TYPE_BANK_ADMIN) %>">
	<aui:button-row>
		<aui:button cssClass="btn-primary btn" value="add-portfolio" onClick="javascript:showPopupForEdit(0);"/>
		<c:if test="<%= (userType == IConstants.USER_TYPE_WEALTH_ADVISOR) %>">
			<aui:button cssClass="btn-primary" value="add-user" onClick="javascript:showAddUserPopup();"/>
		</c:if>
	</aui:button-row>
</c:if>

<liferay-ui:search-container delta="5" emptyResultsMessage="no-portfolios">
	<liferay-ui:search-container-results results="<%=ListUtil.subList(portfolios, searchContainer.getStart(),searchContainer.getEnd()) %>" total="<%= portfolios.size() %>"/>
	<liferay-ui:search-container-row className="com.fingence.slayer.model.Portfolio" modelVar="portfolio">
		<%
			long portfolioId = portfolio.getPortfolioId();
		%>
		<liferay-ui:search-container-column-text name="portfolioName">
			<a href="javascript:void(0);" onClick="javascript:showDetails('<%= portfolioId %>');"><%= portfolio.getPortfolioName() %></a>
		</liferay-ui:search-container-column-text>
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
	
	function showDetails(portfolioId) {
		var ajaxURL = Liferay.PortletURL.createRenderURL();
		ajaxURL.setPortletId('portfolio_WAR_fingenceportlet');
		ajaxURL.setParameter('jspPage', '/html/portfolio/details.jsp');
		ajaxURL.setParameter('portfolioId', portfolioId);
		ajaxURL.setParameter("backURL", '<%= themeDisplay.getURLCurrent() %>');
		ajaxURL.setWindowState('<%= LiferayWindowState.EXCLUSIVE.toString() %>');
				
		AUI().one('#p_p_id<portlet:namespace/>').load('<%= themeDisplay.getURLPortal() %>' + ajaxURL);		
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
                		destroyOnHide: true,
                    	centered: true,
                    	modal: true,
                       	width: 600,
                    	height: 500,
                    	resizable: false               
                	},
                	id: '<portlet:namespace/>editPortfolioPopup',
                	title: 'Add/Update Portfolio...',
                	uri: ajaxURL
            	});
            	
            	Liferay.provide(
                 	window, '<portlet:namespace/>reloadPortlet', function() {
                        Liferay.Portlet.refresh('#p_p_id<portlet:namespace/>');
                    }
                );			
			}); 
		}
		
		function showAddUserPopup() {
            var ajaxURL = Liferay.PortletURL.createRenderURL();
            ajaxURL.setPortletId('register_WAR_fingenceportlet');
            ajaxURL.setPlid('<%= PageUtil.getPageLayoutId(themeDisplay.getScopeGroupId(), "register", locale) %>');
            ajaxURL.setParameter('jspPage', '/html/register/view.jsp');
            ajaxURL.setWindowState('<%= LiferayWindowState.POP_UP.toString() %>'); 
                    
            AUI().use('aui-dialog', 'aui-dialog-iframe', function(A) {
                Liferay.Util.openWindow({
                    dialog: {
                        destroyOnHide: true,
                        centered: true,
                        modal: true,
                        width: 600,
                        height: 550,
                        resizable: false       
                    },
                    id: '<portlet:namespace/>addUserPopup',
                    title: 'Add Investor / Manager...',
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