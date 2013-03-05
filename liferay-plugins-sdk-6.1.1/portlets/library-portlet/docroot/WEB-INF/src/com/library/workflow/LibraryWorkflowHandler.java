package com.library.workflow;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.BaseWorkflowHandler;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.security.permission.ResourceActionsUtil;
import com.slayer.model.LMSBook;
import com.slayer.service.LMSBookLocalServiceUtil;

public class LibraryWorkflowHandler extends BaseWorkflowHandler {
	
	static final String CLASS_NAME = LMSBook.class.getName();

	public String getClassName() {
		return CLASS_NAME;
	}

	public String getType(Locale locale) {
		return ResourceActionsUtil.getModelResource(
			locale, CLASS_NAME);
	}
	
	public Object updateStatus(int status, Map<String, 
		Serializable> workflowContext)
			throws PortalException, SystemException {
		
		if (status > WorkflowConstants.STATUS_APPROVED) return null;
		
		long statusByUserId = GetterUtil.getLong(
			(String)workflowContext.get(
				WorkflowConstants.CONTEXT_USER_ID));
		
		long resourcePrimKey = GetterUtil.getLong(
			(String)workflowContext.get(
				WorkflowConstants.CONTEXT_ENTRY_CLASS_PK));
		
		LMSBook lmsBook = 
			LMSBookLocalServiceUtil.fetchLMSBook(resourcePrimKey);
		
		lmsBook.setStatus(WorkflowConstants.STATUS_APPROVED);
		lmsBook.setStatusByUserId(statusByUserId);
		lmsBook.setStatusDate(new java.util.Date());
		
		return LMSBookLocalServiceUtil.updateLMSBook(lmsBook);
	}
}