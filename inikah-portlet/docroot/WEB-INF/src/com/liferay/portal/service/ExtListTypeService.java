package com.liferay.portal.service;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.ListTypeService;

public class ExtListTypeService extends ListTypeServiceWrapper {
	/* (non-Java-doc)
	 * @see com.liferay.portal.service.ListTypeServiceWrapper#ListTypeServiceWrapper(ListTypeService listTypeService)
	 */
	public ExtListTypeService(ListTypeService listTypeService) {
		super(listTypeService);
	}
	
	@Override
	public void validate(int listTypeId, String type) throws PortalException,
			SystemException {
		// super.validate(listTypeId, type);
	}
}