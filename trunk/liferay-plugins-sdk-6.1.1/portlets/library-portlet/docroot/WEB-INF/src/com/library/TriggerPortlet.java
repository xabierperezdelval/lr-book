package com.library;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.slayer.model.LMSBook;
import com.slayer.model.impl.LMSBookImpl;

/**
 * Portlet implementation class TriggerPortlet
 */
public class TriggerPortlet extends MVCPortlet {

	public void quickAdd(ActionRequest request, ActionResponse response) 
			throws IOException, PortletException {
			
		// Retrive parameters	
		String bookTitle = ParamUtil.getString(request, "bookTitle");
		String author = ParamUtil.getString(request, "author");
		
		// Prepre the payload
		LMSBook lmsBook = new LMSBookImpl();
		lmsBook.setBookTitle(bookTitle);
		lmsBook.setAuthor(author);
		
		// Fire the event
		QName qName = new QName("http://liferay.com", "lmsBook", "x");
		response.setEvent(qName, lmsBook);
	}
	
	public void setGreeting(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		
		String dailyGreeting = ParamUtil.getString(actionRequest, "dailyGreeting");
		
		actionResponse.setRenderParameter("DAILY_GREETING", dailyGreeting);			
	}
}