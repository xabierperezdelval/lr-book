<%@ include file="/html/report/init.jsp"%>

<%@page import="com.fingence.slayer.service.PortfolioServiceUtil"%>

<%

	int portfolioCount = PortfolioServiceUtil.getPortoliosCount(userId);

	if (portfolioCount == 0 && (userType == IConstants.USER_TYPE_INVESTOR || userType == IConstants.USER_TYPE_WEALTH_ADVISOR) && !layoutName.equalsIgnoreCase(IConstants.ADD_USER)) {
		layoutName = IConstants.ADD_PORTFOLIO;
	}

	long portfolioId = GetterUtil.getLong(portletSession.getAttribute(
			"PORTFOLIO_ID", PortletSession.APPLICATION_SCOPE),
			PortfolioServiceUtil.getDefault(userId));
	
	// make CSV if more portfolio's are added for the purpose of report. 
	String portfolioIds = String.valueOf(portfolioId);
	Enumeration<String> enm = portletSession.getAttributeNames();
	while (enm.hasMoreElements()) {
		String attrName = enm.nextElement();
		if (attrName.startsWith("PORTFOLIO_ADDED_")) {
			portfolioIds += StringPool.COMMA + attrName.replaceAll("PORTFOLIO_ADDED_", StringPool.BLANK);
		}
	}	
	
	int allocationBy = GetterUtil.getInteger(portletSession.getAttribute(
			"ALLOCATION_BY", PortletSession.APPLICATION_SCOPE),
			IConstants.BREAKUP_BY_RISK_COUNTRY);
	
	boolean reportsPage = !layoutName.equalsIgnoreCase(IConstants.PAGE_REPORTS_HOME) && !layoutName.equalsIgnoreCase(IConstants.ADD_PORTFOLIO)  && !layoutName.equalsIgnoreCase(IConstants.ADD_USER);
	boolean showAllocationSwitch = layoutName.equalsIgnoreCase(IConstants.PAGE_ASSET_REPORT) && reportsPage;
	boolean performanceReport = layoutName.equalsIgnoreCase(IConstants.PAGE_PERFORMANCE) && reportsPage;
	boolean fixedIncomeReport = layoutName.equalsIgnoreCase(IConstants.PAGE_FIXED_INCOME) && reportsPage;
	
	int assetsToShow = 0;
	if (performanceReport) {
		assetsToShow = GetterUtil.getInteger(PrefsUtil.getUserPreference(userId, plid, portletDisplay.getRootPortletId(), "assetsToShow"), 7);
	}
%>

<c:if test="<%= reportsPage %>">
	<aui:row>
		<aui:column columnWidth="30">
			<h4>Base Currency: <%= PortfolioServiceUtil.getBaseCurrency(portfolioId) %></h4>
		</aui:column>
		<aui:column>
			<c:choose>
				<c:when test="<%= portfolioCount == 1 %>">
					<h4><%= PortfolioServiceUtil.getPortfolioName(portfolioId) %></h4>
				</c:when>		
				<c:otherwise>
					<aui:select name="portfolioList" onChange="javascript:changePortfolio(this.value);" />
				</c:otherwise>
			</c:choose>
		</aui:column>
		
		<c:if test="<%= portfolioCount > 1 %>">
			<aui:column>
				<a id="mergeLink" href="javascript:void();">Merge Portfolio &raquo;</a>
				<ul class="dropdown-menu merge" id="version-dropdown">
					<%
						List<Portfolio> _portfolios = PortfolioLocalServiceUtil.getPortfolios(userId);
						for (Portfolio _portfolio: _portfolios) {
							if (portfolioId != _portfolio.getPortfolioId()) {
								long otherPortfolioId = _portfolio.getPortfolioId();
								String checkboxName = "addToReport_" + otherPortfolioId;
								boolean checked = Validator.isNotNull(portletSession.getAttribute("PORTFOLIO_ADDED_"+otherPortfolioId));
								%><li><aui:input type="checkbox" checked="<%= checked %>" value="<%= otherPortfolioId %>" name="<%= checkboxName %>" label="<%= _portfolio.getPortfolioName() %>" onChange="javascript:appendPortfolio(this);"/><%
							}	
						}
					%>
				</ul>
			</aui:column>
		</c:if>
				
		<c:if test="<%= showAllocationSwitch %>">
			<aui:column>
				<aui:select name="allocationBy" onChange="javascript:switchAllocationBy(this.value);">
					<aui:option value="<%=IConstants.BREAKUP_BY_RISK_COUNTRY%>"
						label="<%=IConstants.LBL_BREAKUP_BY_RISK_COUNTRY%>"
						selected="<%=(allocationBy == IConstants.BREAKUP_BY_RISK_COUNTRY)%>" />
					<aui:option value="<%=IConstants.BREAKUP_BY_CURRENCY%>"
						label="<%=IConstants.LBL_BREAKUP_BY_CURRENCY%>"
						selected="<%=(allocationBy == IConstants.BREAKUP_BY_CURRENCY)%>" />
					<aui:option value="<%=IConstants.BREAKUP_BY_SECURITY_CLASS%>"
						label="<%=IConstants.LBL_BREAKUP_BY_SECURITY_CLASS%>"
						selected="<%=(allocationBy == IConstants.BREAKUP_BY_SECURITY_CLASS)%>" />
					<aui:option value="<%=IConstants.BREAKUP_BY_INDUSTRY_SECTOR%>"
						label="<%=IConstants.LBL_BREAKUP_BY_INDUSTRY_SECTOR %>"
						selected="<%=(allocationBy == IConstants.BREAKUP_BY_INDUSTRY_SECTOR)%>" />
				</aui:select>
			</aui:column>
		</c:if>
		
		<c:if test="<%= performanceReport %>">
			<aui:column>
				<aui:select name="assetsToShow" onChange="javascript:setAssetsToShow(this.value);">
					<aui:option value="3" label="Three" selected="<%= (assetsToShow == 3) %>"/>
					<aui:option value="5" label="Five" selected="<%= (assetsToShow == 5) %>"/>
					<aui:option value="7" label="Seven" selected="<%= (assetsToShow == 7) %>"/>
					<aui:option value="10" label="Ten" selected="<%= (assetsToShow == 10) %>"/>
					<aui:option value="15" label="Fifteen" selected="<%= (assetsToShow == 15) %>"/>
				</aui:select>
			</aui:column>
		</c:if>
		
		<c:if test="<%= fixedIncomeReport %>">
			<%
				int fixedIncomeReportType = GetterUtil.getInteger(portletSession.getAttribute("FIXED_INCOME_REPORT_TYPE"), IConstants.FIXED_INCOME_TYPE_BONDS_MATURITY);
			%>
			<aui:column>
				<aui:select name="fixedIncomeReportType" onChange="javascript:changeFixedIncomeReport(this.value);">
					<aui:option value="<%= IConstants.FIXED_INCOME_TYPE_BONDS_MATURITY %>" label="fixed-income-bonds-maturity" selected="<%= (IConstants.FIXED_INCOME_TYPE_BONDS_MATURITY == fixedIncomeReportType) %>"/>
					<aui:option value="<%= IConstants.FIXED_INCOME_TYPE_BONDS_QUALITY %>" label="fixed-income-bonds-quality" selected="<%= (IConstants.FIXED_INCOME_TYPE_BONDS_QUALITY == fixedIncomeReportType) %>"/>
					<aui:option value="<%= IConstants.FIXED_INCOME_TYPE_CASH_FLOW %>" label="fixed-income-cash-flow" selected="<%= (IConstants.FIXED_INCOME_TYPE_CASH_FLOW == fixedIncomeReportType) %>"/>
					<aui:option value="<%= IConstants.FIXED_INCOME_TYPE_COLLATERAL %>" label="fixed-income-collateral" selected="<%= (IConstants.FIXED_INCOME_TYPE_COLLATERAL == fixedIncomeReportType) %>"/>
				</aui:select>
			</aui:column>
		</c:if>		
	</aui:row>
</c:if>

<c:choose>
	<c:when test="<%= layoutName.equalsIgnoreCase(IConstants.PAGE_REPORTS_HOME) %>">
		<%@ include file="/html/report/reports-home.jspf"%>
	</c:when>
	
	<c:when test="<%= layoutName.equalsIgnoreCase(IConstants.PAGE_ASSET_REPORT) %>">
		<%@ include file="/html/report/asset-report.jspf"%>
	</c:when>
	
	<c:when test="<%= layoutName.equalsIgnoreCase(IConstants.PAGE_FIXED_INCOME) %>">
		<%@ include file="/html/report/fixed-income-report.jspf"%>
	</c:when>
	
	<c:when test="<%= layoutName.equalsIgnoreCase(IConstants.PAGE_RISK_REPORT) %>">
		<%@ include file="/html/report/risk-report.jspf"%>
	</c:when>
	
	<c:when test="<%= layoutName.equalsIgnoreCase(IConstants.PAGE_PERFORMANCE) %>">
		<%@ include file="/html/report/performance-report.jspf"%>
	</c:when>
	
	<c:when test="<%= layoutName.equalsIgnoreCase(IConstants.PAGE_VIOLATIONS) %>">
		<%@ include file="/html/report/violations-report.jspf"%>
	</c:when>	
	
	<c:when test="<%= layoutName.equalsIgnoreCase(IConstants.ADD_PORTFOLIO) %>">
		<%@ include file="/html/report/update.jspf"%>
	</c:when>
	
	<c:when test="<%= layoutName.equalsIgnoreCase(IConstants.ADD_USER) %>">
		<%@ include file="/html/register/register.jspf"%>
	</c:when>
</c:choose>

<aui:script>

	function formatCustom(value, _type) {
		var _value = (_type == 'amount')? 
			accounting.formatMoney(Math.abs(value)) : accounting.toFixed(Math.abs(value), 2) + '%';
		
		if (value < 0) {
			_value = _value.fontcolor('red');
		}
		
		return _value;
	}
	
	function formatCustom1(value, _type) {
		var _value = (_type == 'amount')? 
			accounting.formatMoney(Math.abs(value)) : accounting.toFixed(Math.abs(value), 2) + '%';
		
		if (value < 0) {
			_value = '-'.fontcolor('red') + _value.fontcolor('red');
		} else {
			_value = _value.fontcolor('green');
		}
		
		return _value;
	}	
	
	<c:if test="<%= (portfolioCount > 1) && reportsPage %>">
		function changePortfolio(value) {
			var ajaxURL = Liferay.PortletURL.createResourceURL();
			ajaxURL.setPortletId('report_WAR_fingenceportlet');
			ajaxURL.setParameter('<%= Constants.CMD %>', '<%= IConstants.CMD_SET_PORTFOLIO_ID %>');
			ajaxURL.setParameter('portfolioId', value);
			ajaxURL.setWindowState('exclusive');
			
			AUI().io.request('<%= themeDisplay.getURLPortal() %>' + ajaxURL, {
				sync: true,
				on: {
					success: function() {
						Liferay.Portlet.refresh('#p_p_id<portlet:namespace/>');
					}
				}
			});	
		}
		
		function appendPortfolio(checkbox) {
			var ajaxURL = Liferay.PortletURL.createResourceURL();
			ajaxURL.setPortletId('report_WAR_fingenceportlet');
			ajaxURL.setParameter('<%= Constants.CMD %>', '<%= IConstants.CMD_ADD_PORTFOLIO_ID %>');
			ajaxURL.setParameter('portfolioId', checkbox.value);
			ajaxURL.setParameter('checked', checkbox.checked);
			ajaxURL.setWindowState('exclusive');
			
			AUI().io.request('<%= themeDisplay.getURLPortal() %>' + ajaxURL, {
				sync: true,
				on: {
					success: function() {
						Liferay.Portlet.refresh('#p_p_id<portlet:namespace/>');
					}
				}
			});	
		}		

		AUI().ready(function(A) {
			var list = document.getElementById('<portlet:namespace/>portfolioList');
			if (list != null){
				Liferay.Service(
		  			'/fingence-portlet.portfolio/get-portfolios',
		  			{
		    			userId: '<%= userId %>'
		  			},
		  			function(data) {
		    			for (var i=0; i<(data.length); i++) {
		    				var obj = data[i];
		    				list.options[i] = new Option(obj.portfolioName, obj.portfolioId);
		    				list.options[i].selected = (obj.portfolioId == '<%= portfolioId %>');
		    			}
		  			}
				);
			}
			
			var mergeLink = A.one('#mergeLink');
			var portfolios = A.one('#version-dropdown');
			mergeLink.on('click', function(e) {
				portfolios.toggleClass('show');
				e.preventDefault();
			});			
		});
	</c:if>
	
	<c:if test="<%= showAllocationSwitch %>">
		function switchAllocationBy(value) {
			var ajaxURL = Liferay.PortletURL.createResourceURL();
			ajaxURL.setPortletId('report_WAR_fingenceportlet');
			ajaxURL.setParameter('<%= Constants.CMD %>', '<%= IConstants.CMD_SET_ALLOCATION_BY %>');
			ajaxURL.setParameter('allocationBy', value);
			ajaxURL.setWindowState('exclusive');
			
			AUI().io.request('<%= themeDisplay.getURLPortal() %>' + ajaxURL, {
				sync: true,
				on: {
					success: function() {
						Liferay.Portlet.refresh('#p_p_id<portlet:namespace/>');
					}
				}
			});
		}
	</c:if>
	
	<c:if test="<%= performanceReport %>">
		function setAssetsToShow(value) {
		
			var ajaxURL = Liferay.PortletURL.createResourceURL();
			ajaxURL.setPortletId('report_WAR_fingenceportlet');
			ajaxURL.setParameter('<%= Constants.CMD %>', '<%= IConstants.CMD_SET_ASSETS_TO_SHOW %>');
			ajaxURL.setParameter('assetsToShow', value);
			ajaxURL.setWindowState('exclusive');
			
			AUI().io.request('<%= themeDisplay.getURLPortal() %>' + ajaxURL, {
				sync: true,
				on: {
					success: function() {
						Liferay.Portlet.refresh('#p_p_id<portlet:namespace/>');
					}
				}
			});		
		}
	</c:if>
	
	<c:if test="<%= fixedIncomeReport %>">
		function changeFixedIncomeReport(value) {
			var ajaxURL = Liferay.PortletURL.createResourceURL();
			ajaxURL.setPortletId('report_WAR_fingenceportlet');
			ajaxURL.setParameter('<%= Constants.CMD %>', '<%= IConstants.CMD_CHANGE_FIXED_INCOME_RPT %>');
			ajaxURL.setParameter('reportType', value);
			ajaxURL.setWindowState('exclusive');
			
			AUI().io.request('<%= themeDisplay.getURLPortal() %>' + ajaxURL, {
				sync: true,
				on: {
					success: function() {
						Liferay.Portlet.refresh('#p_p_id<portlet:namespace/>');
					}
				}
			});			
		}
	</c:if>
	
	function updateItem(portfolioItemId, portfolioId) {
		
		var ajaxURL = Liferay.PortletURL.createRenderURL();
		ajaxURL.setPortletId('report_WAR_fingenceportlet');
		ajaxURL.setParameter('jspPage', '/html/report/update-item.jsp');
		ajaxURL.setParameter('portfolioItemId', portfolioItemId);
		ajaxURL.setParameter('portfolioId', portfolioId);
		ajaxURL.setWindowState('pop_up');
	
	    AUI().use('aui-dialog', function(A) {
			Liferay.Util.openWindow({
	        	dialog: {
	            	centered: true,
	                modal: true,
	                width: 600,
	                height: 400,
	                destroyOnHide: true,
	                resizable: false           
	           	},
	            id: '<portlet:namespace/>editPortfolioItemPopup',
	            title: 'Add/Update Asset',
	           	uri: ajaxURL
	       	});
			
	       	Liferay.provide(
	        	window, '<portlet:namespace/>reloadPortlet', function() {
	            	Liferay.Portlet.refresh('#p_p_id<portlet:namespace />');
	            }
	        );
	    });
	}
	
    function deleteItem(portfolioItemId) {
        if (confirm('Are you sure to delete this item from portfolio?')) {
            Liferay.Service(
                '/fingence-portlet.portfolioitem/delete-item',
                {
                    portfolioItemId: portfolioItemId
                },
                function(obj) {
                    Liferay.Portlet.refresh('#p_p_id<portlet:namespace/>');
                }
            );
        }
    } 
    
	function discussions(portfolioItemId) {
		
		var ajaxURL = Liferay.PortletURL.createRenderURL();
		ajaxURL.setPortletId('report_WAR_fingenceportlet');
		ajaxURL.setParameter('jspPage', '/html/report/discussion-portfolio-item.jsp');
		ajaxURL.setParameter('portfolioItemId', portfolioItemId);
		ajaxURL.setWindowState('pop_up');
	
	    AUI().use('aui-dialog', function(A) {
			Liferay.Util.openWindow({
	        	dialog: {
	            	centered: true,
	                modal: true,
	                width: 800,
	                height: 600,
	                destroyOnHide: true,
	                resizable: false           
	           	},
	            id: '<portlet:namespace/>itemDiscussionPopup',
	            title: 'Discussions',
	           	uri: ajaxURL
	       	});
	    });
	}
	
	function getTotalNetWorth(data){
		var value = 0;
		_.each(data, function(itemValue){
			value += itemValue.currentMarketValue;
		});
	  	return value;
	}	
	
	function displayItemsGrid(results, divId, networth) {
	
		var divContent = AUI().one(divId);
		if (divContent) {
			divContent.setContent('');
		}
			
		YUI().use('aui-datatable', function(Y) {
			var columns = [
				{key: 'name', label: 'Security Name', sortable: true},
				{key: 'security_ticker', label: 'TICKER', sortable: true},
				{
	               	key: 'purchasedMarketValue',
	               	sortable: true, 
	               	label: 'Purchased Value',
	               	formatter: function(obj) {
						obj.value = formatCustom(obj.value, 'amount');
					},
					allowHTML: true,
					sortable: true
				},
	            {
	            	key: 'currentMarketValue', 
	               	label: 'Market Value',
	                formatter: function(obj) {
						obj.value = formatCustom(obj.value, 'amount');
					},
					allowHTML: true,
					sortable: true
				},
	            {
	           		key: 'purchaseQty',
	             	label: 'Quantity',
	             	sortable: true,
	             	formatter: function(obj) {
						obj.value = accounting.toFixed(obj.value, 2);
					}
	           	},
	            {
	            	key: 'fx_gain_loss',
	                label: 'FX Gain/Loss',
	                formatter: function(obj) {
						obj.value = formatCustom1(obj.value, 'amount');
					},
					allowHTML: true,
					sortable: true
	           	},	           	
	            {
	            	key: 'gain_loss',
	                label: 'Gain/Loss',
	                formatter: function(obj) {
						obj.value = formatCustom1(obj.value, 'amount');
					},
					allowHTML: true,
					sortable: true
	           	},
	            {
	               	key: 'gain_loss_percent',
	               	label: 'Gain/Loss%',
	               	formatter: function(obj) {
	             		obj.value = formatCustom1(obj.value, 'percent');
					},
					allowHTML: true,
					sortable: true
	            },
	            {
	               	key: 'weight',
	               	label: 'Weight',
	               	formatter: function(obj) {
	             		obj.value = formatCustom(obj.data.currentMarketValue/networth*100, 'percent');
					},
					allowHTML: true,
					sortable: true
	            },		                                             	
	            {
	                 key: 'itemId',
	                 label: 'Actions',
	                 formatter: function(obj) {
	                  	obj.value = 
	                  		'<a href="javascript:void(0);" title="Update Asset" onclick="javascript:updateItem(' + obj.data.itemId + ',' + obj.data.portfolioId + ');"><img src="<%= themeDisplay.getPathThemeImages() + IConstants.THEME_ICON_EDIT %>"/></a>&nbsp;' +
	                  		'<a href="javascript:void(0);" title="Discussion" onclick="javascript:discussions(' + obj.data.itemId + ');"><img src="<%= themeDisplay.getPathThemeImages() + IConstants.THEME_ICON_DISCUSSION %>"/></a>' +
	                 		'<a href="javascript:void(0);" title="Delete Asset" onclick="javascript:deleteItem(' + obj.data.itemId + ');"><img src="<%= themeDisplay.getPathThemeImages() + IConstants.THEME_ICON_DELETE %>"/></a>';
	     			},
	                allowHTML: true
	            }
			];
			
			new Y.DataTable({
				columnset: columns,
			    recordset: results
			}).render(divId);
		});
	}
</aui:script>