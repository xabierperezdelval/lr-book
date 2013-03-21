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

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.BooleanQueryFactoryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.ParseException;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchEngineUtil;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowHandlerRegistryUtil;
import com.liferay.portal.model.CompanyConstants;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.documentlibrary.store.DLStoreUtil;
import com.slayer.model.LMSBook;
import com.slayer.model.LMSBorrowing;
import com.slayer.model.impl.LMSBookImpl;
import com.slayer.service.base.LMSBookLocalServiceBaseImpl;
import com.slayer.service.persistence.LMSBookFinderUtil;
import com.slayer.service.persistence.LMSBorrowingUtil;
import com.util.LMSUtil;

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
	
	private static final Log _log = 
			LogFactoryUtil.getLog(LMSBookLocalServiceImpl.class);
	
	public LMSBook insertBook(String bookTitle, String author, ServiceContext serviceContext) {
		// 1. Instantiate an empty object of type LMSBookImpl
		LMSBook lmsBook = new LMSBookImpl();
		
		// set Additional Audit fields
		lmsBook.setCompanyId(serviceContext.getCompanyId());
		lmsBook.setUserId(serviceContext.getUserId());		
		
		// 2. Set the fields for this object
		lmsBook.setBookTitle(bookTitle);
		lmsBook.setAuthor(author);
		lmsBook.setCreateDate(new Date());
		lmsBook.setUuid(serviceContext.getUuid());
		lmsBook.setGroupId(serviceContext.getScopeGroupId());
		
		// workflow status
		lmsBook.setStatus(WorkflowConstants.STATUS_PENDING);
		lmsBook.setStatusByUserId(serviceContext.getUserId());
		lmsBook.setStatusDate(new java.util.Date());
		
		// 4. Call the Service Layer API to persist the object
		try {
			lmsBook = lmsBookPersistence.update(lmsBook, false);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		try {
			WorkflowHandlerRegistryUtil.startWorkflowInstance(
				lmsBook.getCompanyId(), 	// companyId
				lmsBook.getGroupId(), 		// groupId
				lmsBook.getUserId(), 		// userId
				LMSBook.class.getName(), 	// className
				lmsBook.getPrimaryKey(), 	// classPK
				lmsBook, 					// model
				serviceContext);			// serviceContext
		} catch (PortalException e1) {
			e1.printStackTrace();
		} catch (SystemException e1) {
			e1.printStackTrace();
		}
		
		_log.debug("Book just got added - debug"); 
		_log.info("Book just got added - info"); 
		_log.warn("Book just got added - warn"); 
		_log.error("Book just got added - error"); 
		_log.fatal("Book just got added - fatal");
		
		try {
			resourceLocalService.addModelResources(lmsBook, serviceContext);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		try {
			assetEntryLocalService.updateEntry(
				serviceContext.getUserId(), 			// userId
				serviceContext.getScopeGroupId(), 		// groupId
				LMSBook.class.getName(), 				// className
				lmsBook.getPrimaryKey(), 				// classPK
				serviceContext.getAssetCategoryIds(), 	// category Ids
				serviceContext.getAssetTagNames());		// tag Names
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		lmsBook.setExpandoBridgeAttributes(serviceContext);
		
		// indexer 
		Indexer indexer = 
			IndexerRegistryUtil.getIndexer(LMSBook.class);
		try {
			indexer.reindex(lmsBook);
		} catch (SearchException e) {
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
	
	public List<LMSBook> searchBooks(String bookTitle, long companyId, long groupId) 
			throws SystemException { 
		
		PermissionChecker permissionChecker = 
			PermissionThreadLocal.getPermissionChecker();
		
		return null;
	}
	
	
	public List<LMSBook> searchIndex(
			String keyword, long companyId, long groupId) 
				throws SystemException { 
		
		Hits hits = getHits(keyword, companyId, groupId);
		
		// 4. return null if no results
		if (Validator.isNull(hits) || hits.getLength() == 0) 
			return null;
		
		// 5. Convert results into a List of LMSBook objects
		List<LMSBook> books = new ArrayList<LMSBook>();
		for (Document document : hits.getDocs()) {
			long bookId = GetterUtil.getLong(
					document.get(Field.ENTRY_CLASS_PK));
			
			LMSBook book = fetchLMSBook(bookId);
			
			books.add(book);
		}
		
		return books;
	}

	public Hits getHits(String keyword, long companyId, long groupId) {
		// 1. Preparing a Search Context
		SearchContext searchContext = new SearchContext();
		searchContext.setCompanyId(companyId);
		
		String[] CLASS_NAMES = { LMSBook.class.getName() };
		searchContext.setEntryClassNames(CLASS_NAMES);
		
		long[] groupIds = {groupId};
		searchContext.setGroupIds(groupIds);
		
		// 2. Preparing a Query to search
		BooleanQuery searchQuery = 
			BooleanQueryFactoryUtil.create(searchContext);
		String[] terms = {Field.TITLE, "author"};
		
		try {
			searchQuery.addTerms(terms, keyword);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		// 3. Firing the query to get hits
		Hits hits = null;
		try {
			hits = SearchEngineUtil.search(
					searchContext, searchQuery);
		} catch (SearchException e) {
			e.printStackTrace();
		}
		return hits;
	}
 	
	public List<LMSBorrowing> getBorrowings(long bookId) 
			throws SystemException {
		//return lmsBookPersistence.getLMSBorrowings(bookId);
		
		return LMSBorrowingUtil.findByBookId(bookId);
	}
	
	public List<LMSBook> getLibraryBooks(long companyId, long groupId)
			throws SystemException {
		return lmsBookPersistence
			.filterFindByCompanyId_GroupId(companyId, groupId);
	}
	
	public void attachFiles(long bookId, ServiceContext serviceContext) {
		// uploading the image
		File coverImage = (File) 
			serviceContext.getAttribute("COVER_IMAGE");
		
		if (Validator.isNotNull(coverImage)) {
			try {
				imageLocalService.updateImage(
					bookId, coverImage);
			} catch (PortalException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		
		File sampleChapter = (File)
			serviceContext.getAttribute("SAMPLE_CHAPTER");
		
		if (Validator.isNotNull(sampleChapter)) {
			LMSBook lmsBook = null;
			try {
				lmsBook = fetchLMSBook(bookId);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			
			if (Validator.isNotNull(lmsBook)) {
				
				// update the original object
				String fileName = (String) 
					serviceContext.getAttribute("FILE_NAME");
				
				lmsBook.setSampleChapter(fileName);
				
				try {
					updateLMSBook(lmsBook);
				} catch (SystemException e) {
					e.printStackTrace();
				}
				
				// create a folder to store this file
				long companyId = serviceContext.getCompanyId();
				String folderName = "Sample_Chapters";
				LMSUtil.createFolder(folderName, companyId);
				
				// Saving the file now
				String filePath = folderName + StringPool.SLASH + fileName;
				try {
					DLStoreUtil.addFile(companyId, 
						CompanyConstants.SYSTEM, filePath, sampleChapter);
				} catch (PortalException e) {
					e.printStackTrace();
				} catch (SystemException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public List<LMSBook> advancedSearch(long companyId, long groupId, 
			String bookTitle, String author, boolean andSearch) {
		
		Hits hits = null;
		try {
			hits = getHits(
				companyId, groupId, bookTitle, author, andSearch);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (hits == null || hits.getLength() == 0) return null;
		
		List<LMSBook> books = new ArrayList<LMSBook>();
		for (int i=0; i<hits.getLength(); i++) {
			Document doc = hits.doc(i);
			
			long bookId = GetterUtil.getLong(
					doc.get(Field.ENTRY_CLASS_PK));
			try {
				LMSBook book = fetchLMSBook(bookId);
				books.add(book);
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		
		return books;
	}
	
	protected Hits getHits(
		long companyId, long groupId, String bookTitle, 
		String author, boolean andSearch)
				throws SystemException {
	
		// 1. Preparing the Search Context
		SearchContext searchContext = new SearchContext();
		searchContext.setAndSearch(andSearch);
		searchContext.setCompanyId(companyId);
		long[] groupIds = {groupId};
		searchContext.setGroupIds(groupIds);
		
		BooleanQuery searchQuery = 
				BooleanQueryFactoryUtil.create(searchContext);
		
		appendSearchTerm(Field.TITLE, bookTitle, 
			searchContext.isAndSearch(), searchQuery);
		appendSearchTerm("author", author, 
			searchContext.isAndSearch(), searchQuery);
		
		
		// 2. Firing the query and getting the hits
		Hits hits = null;
		try {
			hits = SearchEngineUtil.search(
					searchContext, searchQuery);
		} catch (SearchException e) {
			e.printStackTrace();
		}
		
		return hits;
	}

	private void appendSearchTerm(String field, String value, 
		boolean isAndSearch, BooleanQuery searchQuery) {
		
		if (Validator.isNotNull(value)) {
			if (isAndSearch) {
				searchQuery.addRequiredTerm(field, value, true);
			} else {
				try {
					searchQuery.addTerm(field, value, true);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}
}