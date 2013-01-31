/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.slayer.service.impl;

import java.util.Date;
import java.util.List;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;
import com.slayer.model.LMSBook;
import com.slayer.model.impl.LMSBookImpl;
import com.slayer.service.LMSBookLocalServiceUtil;
import com.slayer.service.base.LMSBookLocalServiceBaseImpl;
import com.slayer.service.persistence.LMSBookFinderUtil;

/**
 * The implementation of the l m s book local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.slayer.service.LMSBookLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.slayer.service.base.LMSBookLocalServiceBaseImpl
 * @see com.slayer.service.LMSBookLocalServiceUtil
 */
public class LMSBookLocalServiceImpl extends LMSBookLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.slayer.service.LMSBookLocalServiceUtil} to access the l m s book local service.
	 */
	
	public LMSBook insertBook(String bookTitle, String author) {
		// 1. Instantiate an empty object of type LMSBookImpl
		LMSBook lmsBook = new LMSBookImpl();
		
		// 2. Set the fields for this object
		lmsBook.setBookTitle(bookTitle);
		lmsBook.setAuthor(author);
		lmsBook.setCreateDate(new Date());
		
		// 4. Call the Service Layer API to persist the object
		try {
			lmsBook = LMSBookLocalServiceUtil.addLMSBook(lmsBook);
		} catch (SystemException e) {
			e.printStackTrace();
		}

		return lmsBook;
	}
	
	public LMSBook modifyBook(long bookId, String bookTitle, String author) {
		
		LMSBook lmsBook = null;
		try {
			lmsBook = fetchLMSBook(bookId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNotNull(lmsBook)) {
			lmsBook.setBookTitle(bookTitle);
			lmsBook.setAuthor(author);
			lmsBook.setModifiedDate(new Date());
			
			// update the book
			try {
				lmsBook = updateLMSBook(lmsBook);
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		
		return lmsBook;
	}
	
	public List<LMSBook> searchBooks(String bookTitle) 
			throws SystemException { 
		//return lmsBookPersistence.findByBookTitle(bookTitle);
		
		// dynamic query
		/*
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(LMSBook.class);
		
		Property bookTitleProperty = PropertyFactoryUtil.forName("bookTitle");
		dynamicQuery.add(bookTitleProperty.like("%" + bookTitle + "%"));
		
		return dynamicQuery(dynamicQuery);
		*/
		
		return LMSBookFinderUtil.findBooks("%" + bookTitle + "%");
	}
}