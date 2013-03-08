package com.library.search;

import java.util.List;
import java.util.Locale;

import javax.portlet.PortletURL;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.search.BaseIndexer;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchEngineUtil;
import com.liferay.portal.kernel.search.Summary;
import com.slayer.model.LMSBook;
import com.slayer.service.LMSBookLocalServiceUtil;

public class LibraryIndexer extends BaseIndexer {

	public static String[] CLASS_NAMES = new String[] {LMSBook.class.getName()};
	
	public String[] getClassNames() {
		return CLASS_NAMES;
	}

	public String getPortletId() {
		System.out.println("inside getPortletId()");
		return "library_WAR_libraryportlet";
	}

	protected void doDelete(Object arg0) throws Exception {
		
	}
	
	protected Document doGetDocument(Object obj) throws Exception {
	
		LMSBook lmsBook = (LMSBook) obj;
		Document document = 
			getBaseModelDocument(getPortletId(), lmsBook);
		
		document.addKeyword(Field.TITLE, lmsBook.getBookTitle());
		document.addKeyword("author", lmsBook.getAuthor());
		document.addText(Field.TITLE, lmsBook.getBookTitle());
		
		return document;
	}

	protected Summary doGetSummary(Document document, Locale local, String snippet,
			PortletURL portletUrl) throws Exception {
		System.out.println("inside doGetSummary");
		return null;
	}
	
	protected void doReindex(Object obj) throws Exception {
		LMSBook lmsBook = (LMSBook) obj;
		
		Document document = getDocument(lmsBook);
	
		SearchEngineUtil.updateDocument(getSearchEngineId(), 
			lmsBook.getCompanyId(), document);
	}

	protected void doReindex(String[] args) throws Exception {
		long companyId = Long.valueOf(args[0]);
		
		DynamicQuery dynamicQuery = 
			DynamicQueryFactoryUtil.forClass(LMSBook.class);
		dynamicQuery.add(
			RestrictionsFactoryUtil.eq("companyId", companyId));
		
		List<LMSBook> books = 
			LMSBookLocalServiceUtil.dynamicQuery(dynamicQuery);
		
		for (LMSBook book: books) {
			if (book.getGroupId() > 0l) reindex(book);
		}
	}

	protected void doReindex(String className, long classPK) throws Exception {
		
	}

	protected String getPortletId(SearchContext searchContext) {
		System.out.println("inside getPortletId(SearchContext searchContext)");
		return null;
	}	
}