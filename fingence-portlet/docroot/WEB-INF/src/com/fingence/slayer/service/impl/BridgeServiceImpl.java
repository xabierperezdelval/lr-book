/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.fingence.slayer.service.impl;

import java.util.List;

import com.fingence.slayer.service.base.BridgeServiceBaseImpl;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Organization;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.service.OrganizationLocalServiceUtil;

/**
 * The implementation of the bridge remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.fingence.slayer.service.BridgeService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.fingence.slayer.service.base.BridgeServiceBaseImpl
 * @see com.fingence.slayer.service.BridgeServiceUtil
 */
public class BridgeServiceImpl extends BridgeServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.fingence.slayer.service.BridgeServiceUtil} to access the bridge remote service.
	 */
	
	public List<Organization> getInstitutions() {
		
		List<Organization> institutions = null;
		
		long companyId = CompanyThreadLocal.getCompanyId();
		
		Organization parentOrg = null;
		try {
			parentOrg = OrganizationLocalServiceUtil.fetchOrganization(companyId, "Banks");
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNotNull(parentOrg)) {
			try {
				institutions = OrganizationLocalServiceUtil.getOrganizations(companyId, parentOrg.getOrganizationId());
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		
		return institutions;
	}
}