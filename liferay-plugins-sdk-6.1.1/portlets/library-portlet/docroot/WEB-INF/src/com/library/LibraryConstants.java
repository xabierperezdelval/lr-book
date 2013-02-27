package com.library;

public interface LibraryConstants {

	static final String ACTION_UPDATE_BOOK = "updateBook";
	
	static final String ACTION_DELETE_BOOK = "deleteBook";
	
	static final String ACTION_DELETE_BOOKS = "deleteBooks";
	
	static final String ACTION_SEARCH_BOOKS = "searchBooks";
	
	static final String PAGE_UPDATE = "/html/library/update.jsp";
	
	static final String PAGE_SUCCESS = "/html/library/success.jsp";
	
	static final String PAGE_LIST = "/html/library/list.jsp";
	
	static final String PAGE_ACTIONS = "/html/library/actions.jsp";
	
	static final String PAGE_DETAILS = "/html/library/detail.jsp";
	
	static final String PAGE_UPLOAD = "/html/library/upload.jsp";
	
	static final String KEY_SEARCH = "search";
	
	static final String PROP_BOOK_TYPES = "book.types";
	
	static final String PROP_DEFAULTING_REMINDER_EMAIL_SUBJECT = 
			"defaulting.reminder.email.subject";
	
	static final String PROP_BOOK_COVER_IMAGE_MAX_SIZE = 
			"book.cover.image.max.size";
}