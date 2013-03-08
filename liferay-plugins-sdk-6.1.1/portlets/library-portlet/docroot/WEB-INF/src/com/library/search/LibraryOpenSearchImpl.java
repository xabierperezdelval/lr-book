package com.library.search;

import com.liferay.portal.kernel.search.HitsOpenSearchImpl;

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
}