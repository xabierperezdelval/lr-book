<%@ include file="/html/report/init.jsp"%>

<%
	String layoutName = layout.getName(locale);
%>

<c:choose>

	<c:when test="<%= layoutName.equalsIgnoreCase(IConstants.PAGE_ASSET_REPORT) %>">
		<%@ include file="/html/report/asset-reports.jspf"%>
	</c:when>
	
	<c:when test="<%= layoutName.equalsIgnoreCase(IConstants.PAGE_SECURITY_HOLDINGS) %>">
		<%@ include file="/html/report/security-holdings.jspf"%>
	</c:when>
	
	<c:otherwise>
	
	</c:otherwise>
	
</c:choose>