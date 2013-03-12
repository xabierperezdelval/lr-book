<%@page import="com.liferay.portal.kernel.util.StringUtil"%>
<%@page import="com.liferay.portlet.journal.NoSuchArticleException"%>
<%@page import="com.liferay.portlet.journalcontent.util.JournalContent"%>
<%@page import="com.liferay.portlet.journal.model.JournalArticle"%>
<%@page import="com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil"%>

<%@include file="/html/library/init.jsp"%>

<%
	LMSBook lmsBook = (LMSBook)request.getAttribute("ASSET_ENTRY");
	String articleId = "LIBRARY_BOOK_FULL_CONTENT_VIEW";
	
	JournalArticle journalArticle = null;
	String[] tokens = {
		"[{BOOK_TITLE}]", "[{AUTHOR}]", "[{CREATE_DATE}]"	
	};
	try { 
		journalArticle = 
			JournalArticleLocalServiceUtil.getArticle(
				themeDisplay.getScopeGroupId(), articleId);
	} catch (NoSuchArticleException nsae) {
		StringBuilder sb = new StringBuilder()
			.append("<b>No Web Content found with name:</b>")
			.append(articleId)
			.append("<br/>Create this article with tags")
			.append("<ul>");
		
		for (int i=0; i<tokens.length; i++) {
			sb.append("<li>").append(tokens[i]).append("</li>");
		}
		sb.append("</ul>");
		out.write(sb.toString());
	}
	
	if (Validator.isNotNull(journalArticle)) {
		String content = JournalArticleLocalServiceUtil
			.getArticleContent(journalArticle, null, null, 
				themeDisplay.getLanguageId(), themeDisplay);
		
		String[] values = {
				lmsBook.getBookTitle(), lmsBook.getAuthor(), 
				lmsBook.getCreateDate().toString()};
		content = StringUtil.replace(content, tokens, values);
		out.write(content);
	}
%>