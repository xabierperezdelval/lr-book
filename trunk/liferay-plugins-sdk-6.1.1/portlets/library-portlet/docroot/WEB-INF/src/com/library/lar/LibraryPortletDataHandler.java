package com.library.lar;

import java.util.List;

import javax.portlet.PortletPreferences;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.BasePortletDataHandler;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.PortletDataHandlerBoolean;
import com.liferay.portal.kernel.lar.PortletDataHandlerControl;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.service.ServiceContext;
import com.slayer.model.LMSBook;
import com.slayer.service.LMSBookLocalServiceUtil;

public class LibraryPortletDataHandler extends BasePortletDataHandler {
	
	public boolean isAlwaysExportable() {
		return true;
	}
	
	public boolean isPublishToLiveByDefault() {
		return true;
	}
	
	public PortletDataHandlerControl[] getExportControls() {
		PortletDataHandlerBoolean _books = 
			new PortletDataHandlerBoolean(_NAMESPACE, "books", true, true);
		return new PortletDataHandlerControl[] { _books };
	}
	
	private static final String _NAMESPACE = "lms";
	
	protected String doExportData(PortletDataContext portletDataContext,
			String portletId, PortletPreferences portletPreferences)
			throws Exception {
		
		long companyId = portletDataContext.getCompanyId();
		long groupId = portletDataContext.getScopeGroupId();
		List<LMSBook> books = 
			LMSBookLocalServiceUtil.getLibraryBooks(companyId, groupId);
		
		if (Validator.isNull(books) || books.isEmpty()) 
			return StringPool.BLANK;
		
		Document document = SAXReaderUtil.createDocument();
		Element rootElement = document.addElement("library-data");
		rootElement.addAttribute("group-id", Long.toString(groupId));
		for (LMSBook lmsBook : books) {
			exportBook(portletDataContext, rootElement, lmsBook);
		}
		
		return document.formattedString();
	}

	private void exportBook(PortletDataContext portletDataContext,
			Element rootElement, LMSBook lmsBook) {
	
		// Setting unique XPath for the node
		StringBuilder path = new StringBuilder(); 		
		path.append(portletDataContext.getPortletPath(_NAMESPACE)); 
		path.append("/books/");
		path.append(lmsBook.getBookId());
		path.append(".xml");
		
		Element element = rootElement.addElement("book");
		try {
			portletDataContext.addClassedModel(
					element, path.toString(), lmsBook, _NAMESPACE);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
	
	protected PortletPreferences doImportData(
			PortletDataContext portletDataContext, String portletId,
			PortletPreferences portletPreferences, String data)
			throws Exception {
	
		Document document = SAXReaderUtil.read(data);
		
		if (Validator.isNull(document)) return null;
		
		Element rootElement = document.getRootElement();
		
		for (Element bookElement : rootElement.elements("book")) {
			String path = bookElement.attributeValue("path");
			
			if (!portletDataContext.isPathNotProcessed(path)) continue;
			
			LMSBook lmsBook = 
				(LMSBook)portletDataContext.getZipEntryAsObject(path);
			importBook(portletDataContext, bookElement, lmsBook);
		}
		
		return null;
	}

	private void importBook(PortletDataContext portletDataContext,
			Element bookElement, LMSBook lmsBook) {
		
		// Getting the serviceContext object
		ServiceContext serviceContext = 
			portletDataContext.createServiceContext(
				bookElement, lmsBook, _NAMESPACE);
		
		String bookTitle = lmsBook.getBookTitle();
		String author = lmsBook.getAuthor();
		LMSBook importedBook = 
			LMSBookLocalServiceUtil.insertBook(
				bookTitle, author, serviceContext);
		
		try {
			portletDataContext.importClassedModel(
				lmsBook, importedBook, _NAMESPACE);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
	
	public PortletDataHandlerControl[] getImportControls() {
		return getExportControls();
	}
}