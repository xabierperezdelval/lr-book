<%@ include file="/html/library/init.jsp" %>

<h1>New book inserted!!</h1>

<br/><a href="<portlet:renderURL/>">&laquo; Go Back</a> |

<portlet:renderURL var="addBookURL">
	<portlet:param name="jspPage" value="<%= LibraryConstants.PAGE_UPDATE %>"/>
</portlet:renderURL>
<aui:a href="<%= addBookURL.toString() %>">Add Another Book &raquo;</aui:a>

<aui:a href=""></aui:a>