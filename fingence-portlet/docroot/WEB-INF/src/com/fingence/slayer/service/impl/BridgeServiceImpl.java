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
import java.util.Locale;

import com.fingence.IConstants;
import com.fingence.slayer.service.base.BridgeServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Contact;
import com.liferay.portal.model.ListType;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.service.ListTypeServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextThreadLocal;
import com.liferay.portal.service.UserGroupRoleLocalServiceUtil;

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
				
				boolean performRoleCheck = false;
				
				try {
					List<Organization> parents = organizationLocalService.getParentOrganizations(organization.getOrganizationId());
					for (Organization parent: parents) {
						if (parent.getName().equalsIgnoreCase(parentOrg)) {
							performRoleCheck = true;
							break;
						} 
					}					
				} catch (PortalException e) {
					e.printStackTrace();
				}
				
				if (performRoleCheck) {
					try {
						havingRole = userGroupRoleLocalService.hasUserGroupRole(
								userId, organization.getGroupId(),
								roleName);
						
						if (havingRole) {
							break;
						}
					} catch (PortalException e) {
						e.printStackTrace();
					}					
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
	
	public List<User> getUsersByTargetType(long loggedInUserId, int targetUserType) {
		
		List<User> users = new ArrayList<User>();
		
		if (getUserType(loggedInUserId) == targetUserType) {
			try {
				User user = userLocalService.fetchUser(loggedInUserId);
				user.setLastName(user.getLastName() + "(My Self)");
				users.add(user);
			} catch (SystemException e) {
				e.printStackTrace();
			}
		} else {
			try {
				List<Organization> organizations = organizationLocalService.getUserOrganizations(loggedInUserId);
				
				for (Organization organization: organizations) {
					List<User> organizationUsers = userLocalService.getOrganizationUsers(organization.getOrganizationId());
					
					for (User user: organizationUsers) {
						
						boolean wealthAdvisor = false;
						try {
							wealthAdvisor = userGroupRoleLocalService
									.hasUserGroupRole(
											user.getUserId(),
											organization.getGroupId(),
											RoleConstants.ORGANIZATION_ADMINISTRATOR);
						} catch (PortalException e) {
							e.printStackTrace();
						}
						
						boolean relationshipManager = false;
						try {
							relationshipManager = userGroupRoleLocalService
									.hasUserGroupRole(
											user.getUserId(),
											organization.getGroupId(),
											IConstants.ROLE_RELATIONSHIP_MANAGER);
						} catch (PortalException e) {
							e.printStackTrace();
						}
						
						if (!wealthAdvisor && !relationshipManager && (targetUserType == IConstants.USER_TYPE_INVESTOR)) {
							users.add(user);
						} else if (relationshipManager && targetUserType == IConstants.USER_TYPE_REL_MANAGER) {
							users.add(user);
						} else if (wealthAdvisor && targetUserType == IConstants.USER_TYPE_WEALTH_ADVISOR) {
							users.add(user);
						}				
					}
				}
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		
		return users;
	}
	
	public Organization getCurrentOrganization(long userId) {
        
        int userType = getUserType(userId);
        
        String roleName = RoleConstants.ORGANIZATION_ADMINISTRATOR;
        
        if (userType == IConstants.USER_TYPE_REL_MANAGER) {
            roleName = IConstants.ROLE_RELATIONSHIP_MANAGER;
        }
        
        Organization organization = null;
        
        try {
            List<Organization> organizations = organizationLocalService.getUserOrganizations(userId);
            
            for (Organization _organization: organizations) {
                if (isUserHavingRole(userId, "Firms", roleName)) {
                    organization = _organization;
                    break;
                }
            }
        } catch (SystemException e) {
            e.printStackTrace();
        }
        
        return organization;
    }
	
	public void assignRole(long loggedInUser, long createdUserId, int userType) {
		
		Organization currentOrg = getCurrentOrganization(loggedInUser);
		
		String roleName = RoleConstants.ORGANIZATION_USER;
        if (userType == IConstants.USER_TYPE_REL_MANAGER) {
            roleName = IConstants.ROLE_RELATIONSHIP_MANAGER;
        }
        
		assignOrganizationRole(createdUserId, currentOrg, roleName);
	}
	
	private void assignOrganizationRole(long userId, Organization currentOrg, String roleName) {
		
		long companyId = CompanyThreadLocal.getCompanyId();
		
		long roleId = 0l;
		try {
			Role role = RoleLocalServiceUtil.fetchRole(companyId, roleName);
			if (Validator.isNull(role)) return;
			roleId = role.getRoleId();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		try {
			organizationLocalService.addUserOrganization(userId, currentOrg.getOrganizationId());
		} catch (SystemException e) {
			e.printStackTrace();
		}

		try {
			UserGroupRoleLocalServiceUtil.addUserGroupRoles(userId, currentOrg.getGroupId(), new long[] { roleId });
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
	
	public void addWealthAdvisorFirm(String firmName, long wealthAdvisorId) {
		
		long companyId = CompanyThreadLocal.getCompanyId();
		
		long creatorUserId = 0l;
		try {
			creatorUserId = userLocalService.getDefaultUserId(companyId);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		Organization parentOrg = null;
		try {
			parentOrg = organizationLocalService.fetchOrganization(companyId, "Firms");
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNotNull(parentOrg)) {
			Organization newFirm = null;
			try {
				newFirm = organizationLocalService.addOrganization(creatorUserId, parentOrg.getOrganizationId(), firmName, false);
			} catch (PortalException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}
			
			if (Validator.isNotNull(newFirm)) {
				assignOrganizationRole(wealthAdvisorId, newFirm, RoleConstants.ORGANIZATION_ADMINISTRATOR);	
			}
		}
	}
	
	public User addUser(long creatorUserId, String firstName, String lastName,
			String emailAddress, boolean male, long countryId, String jobTitle) {
		
		long companyId = CompanyThreadLocal.getCompanyId();
		ServiceContext serviceContext = ServiceContextThreadLocal.getServiceContext();

		User user = null;
		try {
			user = userLocalService.addUser(creatorUserId, companyId, true,
					null, null, true, null, emailAddress,
					0, null, Locale.US, firstName, StringPool.BLANK, lastName,
					0, 0, male, 1, 1,
					1, jobTitle, null, null, null,
					null, true, serviceContext);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}

		// insert a record into the "Address" table for this user
		try {
			addressLocalService.addAddress(user.getUserId(),
					User.class.getName(), user.getUserId(), "Not Specified",
					StringPool.BLANK, StringPool.BLANK, "Not Specified",
					"Not Specified", 0l, countryId,
					getType(Contact.class.getName(), "address"), false, true,
					serviceContext);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
	private int getType(String className, String type) {
		
		int _type = 0;
		
		try {
			List<ListType> listTypes = ListTypeServiceUtil.getListTypes(className + StringPool.PERIOD + type);
			
			for (ListType listType: listTypes) {
				if (listType.getName().equalsIgnoreCase("business")) {
					_type = listType.getListTypeId();
					break;
				}
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return _type;
	}
	
	public String getFirmName(long userId) {
		
		String firmName = StringPool.BLANK;
		Organization organization = getCurrentOrganization(userId);
		
		if (Validator.isNotNull(organization)) {
			firmName = organization.getName();
		}
		
		return firmName;
	}
	
	public String getOrganizationName(long organizationId) {
		String orgName = StringPool.BLANK;
		
		try {
			Organization organization = organizationLocalService.fetchOrganization(organizationId);
			
			if (Validator.isNotNull(organization)) {
				orgName = organization.getName();
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return orgName;
	}
	
	public String getUserName(long userId) {
		
		String userName = StringPool.BLANK;
		
		try {
			User user = userLocalService.fetchUser(userId);
			
			if (Validator.isNotNull(user)) {
				userName = user.getFullName();
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return userName;
		
	}
}