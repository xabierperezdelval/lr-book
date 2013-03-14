package com.library.search;

import javax.portlet.PortletURL;

import com.library.LibraryConstants;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.HitsOpenSearchImpl;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.theme.ThemeDisplay;

public class LibraryOpenSearchImpl extends HitsOpenSearchImpl {

	public String getPortletId() {
		return "library_WAR_libraryportlet";
	}

	public String getSearchPath() {
		return "/c/library/open_search";
	}

	public String getTitle(String keywords) {
		return "Library Open Search for " + keywords;
	}
	
	protected String getURL(ThemeDisplay themeDisplay, long groupId,
			Document result, PortletURL portletURL) throws Exception {
		
		portletURL.setParameter("jspPage", LibraryConstants.PAGE_DETAILS);
		long bookId = GetterUtil.getLong(
				result.get(Field.ENTRY_CLASS_PK));
		portletURL.setParameter("bookId", String.valueOf(bookId));
	
		return super.getURL(themeDisplay, groupId, result, portletURL);
	}
}