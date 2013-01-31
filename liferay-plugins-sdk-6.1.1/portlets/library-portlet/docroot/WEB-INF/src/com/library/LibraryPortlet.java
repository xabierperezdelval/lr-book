package com.library;

import java.io.IOException;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.PortletURLFactoryUtil;
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
		
		if (bookId > 0l) {
			LMSBookLocalServiceUtil.modifyBook(bookId, bookTitle, author);
		} else {
			LMSBookLocalServiceUtil.insertBook(bookTitle, author);
		}
		
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


	public void deleteBook(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
	
		long bookId = ParamUtil.getLong(actionRequest, "bookId");
		
		if (bookId > 0l) { // valid bookId
			try {
				LMSBookLocalServiceUtil.deleteLMSBook(bookId);
			} catch (PortalException | SystemException e) {
				e.printStackTrace();
			}
		}

		// gracefully redirecting to the default list view
		String redirectURL = ParamUtil.getString(actionRequest, "redirectURL");
		actionResponse.sendRedirect(redirectURL);
	}
	
	@Override
	public void render(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		
		setSortParams(request);
		super.render(request, response);
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
}