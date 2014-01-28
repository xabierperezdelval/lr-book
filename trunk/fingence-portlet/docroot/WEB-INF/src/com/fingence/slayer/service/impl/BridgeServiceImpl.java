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

import java.util.ArrayList;
import java.util.List;

import com.fingence.IConstants;
import com.fingence.slayer.service.base.BridgeServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.CompanyThreadLocal;

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
			parentOrg = organizationLocalService.fetchOrganization(companyId, "Banks");
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNotNull(parentOrg)) {
			try {
				institutions = organizationLocalService.getOrganizations(companyId, parentOrg.getOrganizationId());
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		
		return institutions;
	}
	
	private boolean isUserHavingRole(long userId, String parentOrg, String roleName) {
		
		boolean havingRole = false;
		
		try {
			List<Organization> organizations = organizationLocalService.getUserOrganizations(userId);
			
			for (Organization organization:organizations) {

				try {
					List<Organization> parents = organizationLocalService.getParentOrganizations(organization.getOrganizationId());
					for (Organization parent: parents) {
						if (!parent.getName().equalsIgnoreCase(parentOrg)) continue;
					}					
				} catch (PortalException e) {
					e.printStackTrace();
				}
				
				try {
					havingRole = userGroupRoleLocalService.hasUserGroupRole(
							userId, organization.getGroupId(),
							roleName);
				} catch (PortalException e) {
					e.printStackTrace();
				}
				
				if (havingRole){
					break;
				}
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return havingRole;
	}
	
	public int getUserType(long userId) {
		int userType = IConstants.USER_TYPE_INVESTOR;
		
		if (isUserHavingRole(userId, "Firms", RoleConstants.ORGANIZATION_ADMINISTRATOR)) {
			userType = IConstants.USER_TYPE_WEALTH_ADVISOR;
		} else if (isUserHavingRole(userId, "Firms", IConstants.ROLE_RELATIONSHIP_MANAGER)) {
			userType = IConstants.USER_TYPE_REL_MANAGER;
		} else if (isUserHavingRole(userId, "Banks", RoleConstants.ORGANIZATION_ADMINISTRATOR)) {
			userType = IConstants.USER_TYPE_BANK_ADMIN;
		}
		
		return userType;
	}
	
	public List<User> getInvestors(long userId) {
		
		int userType = getUserType(userId);
		
		List<User> users = new ArrayList<User>();
		
		switch (userType) {
		case IConstants.USER_TYPE_INVESTOR:
			try {
				User user = userLocalService.fetchUser(userId);
				users.add(user);
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		
		return users;
		
	}
}