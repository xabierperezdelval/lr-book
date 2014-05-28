<%@ include file="/html/report/init.jsp"%>

<%@page import="com.fingence.slayer.service.PortfolioItemLocalServiceUtil"%>

<%
	long _portfolioItemId = ParamUtil.getLong(request, "portfolioItemId");
	PortfolioItem portfolioItem = null;
	if (_portfolioItemId > 0l) {
		portfolioItem = PortfolioItemLocalServiceUtil.fetchPortfolioItem(_portfolioItemId);
	}
%>

<portlet:actionURL var="discussionURL" name="discussOnPortfolio" />

<liferay-ui:discussion classPK="<%= portfolioItem.getItemId() %>"
	userId="<%= userId %>"
	className="<%= PortfolioItem.class.getName() %>"
	formAction="<%= discussionURL %>" 
	ratingsEnabled="<%= true %>"
/>

<aui:button type="button" onClick="javascript:closePopup();" value="close" cssClass="btn-primary"/>

<aui:script>
	function closePopup() {
		Liferay.Util.getWindow('<portlet:namespace/>itemDiscussionPopup').destroy();
	}
</aui:script>