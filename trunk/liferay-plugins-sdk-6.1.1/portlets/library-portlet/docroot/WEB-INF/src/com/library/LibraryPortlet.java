package com.library;

import java.io.IOException;
import java.util.Date;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.slayer.model.LMSBook;
import com.slayer.model.impl.LMSBookImpl;
import com.slayer.service.LMSBookLocalServiceUtil;

/**
 * Portlet implementation class LibraryPortlet
 */
public class LibraryPortlet extends MVCPortlet {
	
	public void updateBook(ActionRequest actionRequest,
			ActionResponse actionResponse) 
					throws IOException, PortletException {
	
		String bookTitle = ParamUtil.getString(actionRequest, "bookTitle");
		String author = ParamUtil.getString(actionRequest, "author");
		
		insertBook(bookTitle, author);
	}

	private void insertBook(String bookTitle, String author) {
		// 1. Instantiate an empty object of type LMSBookImpl
		LMSBook lmsBook = new LMSBookImpl();
		
		// 2. Generate a unique primary key to be set
		/*
		long bookId = 0l;
		
		try {
			bookId = CounterLocalServiceUtil.increment();
		} catch (SystemException e) {
			e.printStackTrace();
		}*/
		
		// 3. Set the fields for this object
		//lmsBook.setBookId(bookId);
		lmsBook.setBookTitle(bookTitle);
		lmsBook.setAuthor(author);
		lmsBook.setCreateDate(new Date());
		
		// 4. Call the Service Layer API to persist the object
		try {
			lmsBook = LMSBookLocalServiceUtil.addLMSBook(lmsBook);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
}
