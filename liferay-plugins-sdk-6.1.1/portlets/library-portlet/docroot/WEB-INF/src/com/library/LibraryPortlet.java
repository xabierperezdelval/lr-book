package com.library;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.ProcessEvent;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Address;
import com.liferay.portal.model.Contact;
import com.liferay.portal.model.ListType;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.AddressLocalServiceUtil;
import com.liferay.portal.service.ListTypeServiceUtil;
import com.liferay.portal.service.ResourceLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.permission.PortletPermissionUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.PortletURLFactoryUtil;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.slayer.model.LMSBook;
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
		
		long bookId = ParamUtil.getLong(actionRequest, "bookId");
		
		ServiceContext serviceContext = null;
		try {
			serviceContext = ServiceContextFactory.getInstance(actionRequest);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		LMSBook lmsBook = (bookId > 0l)?
				LMSBookLocalServiceUtil.modifyBook(bookId, bookTitle, author) :
					LMSBookLocalServiceUtil.insertBook(bookTitle, author, serviceContext);
		saveAddress(actionRequest, lmsBook);
		
		// redirect after insert
		ThemeDisplay themeDisplay = 
				(ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		
		PortletConfig portletConfig = 
				(PortletConfig) actionRequest.getAttribute("javax.portlet.config");
		
		String portletName = portletConfig.getPortletName();
		
		PortletURL successPageURL = PortletURLFactoryUtil.create(
				actionRequest,
				portletName + "_WAR_" + portletName + "portlet", 
				themeDisplay.getPlid(), 
				PortletRequest.RENDER_PHASE);
	
		successPageURL.setParameter("jspPage", 
			(bookId > 0l)? LibraryConstants.PAGE_LIST : LibraryConstants.PAGE_SUCCESS);
		actionResponse.sendRedirect(successPageURL.toString());
	}

	private void saveAddress(PortletRequest request, LMSBook lmsBook) {
		boolean saveAddress = ParamUtil.getBoolean(
				request, "saveAddress", false);
		
		if (!saveAddress) return;
		
		long addressId = ParamUtil.getLong(request, "addressId", 0l);
		long countryId = ParamUtil.getLong(request, "countryId");
		String city = ParamUtil.getString(request, "city"); 
		String street1 = ParamUtil.getString(request, "street1"); 
		String street2 = ParamUtil.getString(request, "street2"); 
		String zip = ParamUtil.getString(request, "zip");
		
		long userId = PortalUtil.getUserId(request);
		long companyId = PortalUtil.getCompanyId(request);
		
		saveAddress(addressId, countryId, city, street1,
				street2, zip, userId, companyId, lmsBook.getBookId(), LMSBook.class.getName());	
	}

	private void saveAddress(long addressId, long countryId, 
			String city, String street1, String street2, 
			String zip, long userId, long companyId, 
			long parentId, String parentClassName) {
		
		// 1. creating the address object (fresh or old)
		Address address = getAddress(addressId); 
		
		// 2. setting the UI fields
		address.setCountryId(countryId); 
		address.setCity(city); 
		address.setStreet1(street1); 
		address.setStreet2(street2); 
		address.setZip(zip);
		
		// 3. set audit fields
		address.setUserId(userId);
		address.setCompanyId(companyId);
		
		// 4. Set address type and if primary.
		address.setTypeId(getTypeId("address", "personal"));
		address.setPrimary(true); 		
	
		// 5. most importantly set the parent details.
		address.setClassPK(parentId);
		address.setClassName(parentClassName);
		
		// 6. finally update the object (save address)
		try {
			AddressLocalServiceUtil.updateAddress(address);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	private int getTypeId(String entity, String type) {
	
		int typeId = 0;		
		List<ListType> listTypes = null;
		
		try {
			listTypes = ListTypeServiceUtil.getListTypes(
				Contact.class.getName() + "." + entity);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		for (ListType listType: listTypes) {
			if (listType.getName().equalsIgnoreCase(type)) {
				typeId = listType.getListTypeId();
				break;
			}
		}
		
		return typeId;
	}

	private Address getAddress(long addressId) {
		Address address = null;
		if (addressId > 0l) {
			try {
				address = AddressLocalServiceUtil.getAddress(addressId);
				address.setModifiedDate(new Date());
			} catch (PortalException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}
		} else {
			try {
				addressId = CounterLocalServiceUtil.increment(); 
			} catch (SystemException e) {
		       e.printStackTrace();
			}
			address = AddressLocalServiceUtil.createAddress(addressId);
			address.setCreateDate(new Date());
		}
		return address;
	}

	public void deleteBook(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
	
		long bookId = ParamUtil.getLong(actionRequest, "bookId");
		
		if (bookId > 0l) { // valid bookId
			try {
				LMSBookLocalServiceUtil.deleteLMSBook(bookId);
			} catch (PortalException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}
			
			// delete the resource
			LMSBook lmsBook = null;
			try {
				lmsBook = LMSBookLocalServiceUtil.fetchLMSBook(bookId);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			try {
				ResourceLocalServiceUtil.deleteResource(
					lmsBook, ResourceConstants.SCOPE_INDIVIDUAL);
			} catch (PortalException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}
			
			// delete the asset
			try {
				AssetEntryLocalServiceUtil.deleteEntry(
					LMSBook.class.getName(), lmsBook.getPrimaryKey());
			} catch (PortalException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}	
		}

		// gracefully redirecting to the default list view
		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");
		actionResponse.sendRedirect(redirectURL);
	}
	
	public void render(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		
		setSortParams(request);
		checkBeforeServe(request);
		super.render(request, response);
	}

	private void checkBeforeServe(RenderRequest request) 
			throws PortletException {
		
		String jspPage = ParamUtil.getString(request, "jspPage");
		
		if (jspPage.equalsIgnoreCase(LibraryConstants.PAGE_UPDATE)) {
			ThemeDisplay themeDisplay = (ThemeDisplay)
				request.getAttribute(WebKeys.THEME_DISPLAY);
			
			StringBuilder portletName = new StringBuilder()
				.append(getPortletName())
				.append("_WAR_")
				.append(getPortletName())
				.append("portlet");
			
			try {
				PortletPermissionUtil.check(
					themeDisplay.getPermissionChecker(), 
					portletName.toString(), ActionKeys.ADD_ENTRY);
			} catch (PortalException | SystemException e) {
				throw new PortletException(e.getMessage());
			}
		}		
	}

	private void setSortParams(RenderRequest request) {
		String jspPage = ParamUtil.getString(request, "jspPage");
		
		if (jspPage.equalsIgnoreCase(LibraryConstants.PAGE_LIST)) {
			String orderByCol = ParamUtil.getString(
								request, "orderByCol", "bookTitle");
			request.setAttribute("orderByCol", orderByCol);
			
			String orderByType = ParamUtil.getString(
								request, "orderByType", "asc");
			request.setAttribute("orderByType", orderByType);
		}
	}
	
	public void searchBooks(ActionRequest actionRequest,
			ActionResponse actionResponse) 
					throws IOException, PortletException {
		
		String searchTerm = 
				ParamUtil.getString(actionRequest, "searchTerm");
		
		if (Validator.isNotNull(searchTerm)) {
			try {
				List<LMSBook> lmsBooks = 
					LMSBookLocalServiceUtil.searchBooks(searchTerm);
	
				actionRequest.setAttribute("SEARCH_RESULT", 
						lmsBooks);
				actionResponse.setRenderParameter("jspPage",
			             LibraryConstants.PAGE_LIST);
	
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void deleteBooks(ActionRequest actionRequest,
			ActionResponse actionResponse) 
					throws IOException, PortletException {
	
		String bookIdsForDelete = 
				ParamUtil.getString(actionRequest, "bookIdsForDelete");
	
		// convert this into JSON format. 
		bookIdsForDelete = "[" + bookIdsForDelete + "]";
		
		// The presence of ":" in the string 
		// creates problem while parsing.
		// replace all occurance of ":" with 
		// some other unique string, eg. "-"
		bookIdsForDelete = bookIdsForDelete.replaceAll(":", "-");
		
		// parse and get a JSON array of objects
		JSONArray jsonArray = null;
		try {
			jsonArray = JSONFactoryUtil.createJSONArray(bookIdsForDelete);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// process the jsonArray
		if (Validator.isNotNull(jsonArray)) {
			for (int i=0; i<jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				
				long bookId = jsonObject.getLong("bookId");
				try {
					LMSBookLocalServiceUtil.deleteLMSBook(bookId);
				} catch (PortalException e) {
					e.printStackTrace();
				} catch (SystemException e) {
					e.printStackTrace();
				}
			}			
		}
		
		// redirect to the list page again. 
		actionResponse.setRenderParameter("jspPage", LibraryConstants.PAGE_LIST);
	}
	
	public void setPreferences(ActionRequest actionRequest,
			ActionResponse actionResponse) 
				throws IOException, PortletException {
	
		String maxBooksLimit = 
				ParamUtil.getString(actionRequest, "maxBooksLimit");
		
		PortletPreferences preferences = 
				actionRequest.getPreferences();		
		preferences.setValue("maxBooksLimit", maxBooksLimit);
		preferences.store();
		
		actionResponse.setPortletMode(PortletMode.VIEW);
	}
	
	@ProcessEvent(qname = "{http://liferay.com}lmsBook")
	public void quickAdd(EventRequest request, EventResponse response)
			throws PortletException, IOException {
		
		Event event = request.getEvent();
		
		LMSBook lmsBook = (LMSBook) event.getValue();
		
		ServiceContext serviceContext = null;
		try {
			serviceContext = ServiceContextFactory.getInstance(request);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		LMSBookLocalServiceUtil.insertBook(
				lmsBook.getBookTitle(), 
				lmsBook.getAuthor(), 
				serviceContext);
		
		response.setRenderParameter("jspPage", LibraryConstants.PAGE_LIST);
	}
	
	public void UploadFiles(ActionRequest actionRequest,
			ActionResponse actionResponse) 
				throws IOException, PortletException {
	
		UploadPortletRequest uploadRequest = 
			PortalUtil.getUploadPortletRequest(actionRequest);
		
		File coverImage = uploadRequest.getFile("coverImage");
		
		if (coverImage.getTotalSpace() > 0) {
			long bookId = ParamUtil.getLong(uploadRequest, "bookId");
			try {
				ServiceContext serviceContext = 
					ServiceContextFactory
						.getInstance(actionRequest);
				
				serviceContext.setAttribute("COVER_IMAGE", coverImage);
				LMSBookLocalServiceUtil
						.attachFiles(bookId, serviceContext);
			} catch (PortalException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		
		// redirecting to original list page
		actionResponse.sendRedirect(
			ParamUtil.getString(uploadRequest, "redirectURL"));
	}
}