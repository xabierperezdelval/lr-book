package com.library.taglib;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.liferay.portal.kernel.servlet.PipingServletResponse;
import com.liferay.taglib.util.IncludeTag;

public class SubscribeTag extends IncludeTag {
	final String _PAGE = "/html/library/taglib/subscription.jsp";
	
	protected void setAttributes(HttpServletRequest request) {
		request.setAttribute("library:className", className);
		request.setAttribute("library:classPK", classPK);
	}
	
	public int doEndTag() throws JspException {
		try {
			include(_PAGE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.doEndTag();
	}	
	
	protected void include(String page) throws Exception {
		ServletContext servletContext = 
			pageContext.getServletContext();
		RequestDispatcher requestDispatcher 
			= servletContext.getRequestDispatcher(page);
		
		requestDispatcher.include(getServletRequest(), 
			new PipingServletResponse(
				pageContext, isTrimNewLines()));
	}	
	
	public void setClassName(String className) {
		this.className = className;
	}

	public void setClassPK(long classPK) {
		this.classPK = classPK;
	}

	private String className;
	
	private long classPK;
}
