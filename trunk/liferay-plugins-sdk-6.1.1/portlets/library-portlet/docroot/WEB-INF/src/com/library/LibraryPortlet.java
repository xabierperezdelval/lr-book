package com.library;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class LibraryPortlet
 */
public class LibraryPortlet extends MVCPortlet {
	
	public void updateBook(ActionRequest actionRequest,
			ActionResponse actionResponse) 
					throws IOException, PortletException {
	
		String bookTitle = ParamUtil.getString(actionRequest, "bookTitle");
		String author = ParamUtil.getString(actionRequest, "author");
		
		System.out.println("Your inputs ==> " + bookTitle + ", " + author);

	}
}
