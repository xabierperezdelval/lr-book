package com.fingence;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Contact;
import com.liferay.portal.model.ListType;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.AddressLocalServiceUtil;
import com.liferay.portal.service.ListTypeServiceUtil;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class RegisterPortlet
 */
public class RegisterPortlet extends MVCPortlet {
 
	public void register(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
			
		long creatorUserId = PortalUtil.getUserId(actionRequest);
		long companyId = PortalUtil.getCompanyId(actionRequest);
		
		String emailAddress = ParamUtil.getString(actionRequest, "emailAddress");
		String firstName = ParamUtil.getString(actionRequest, "firstName");
		String lastName = ParamUtil.getString(actionRequest, "lastName");
		boolean male = ParamUtil.getBoolean(actionRequest, "male");
		String jobTitle = ParamUtil.getString(actionRequest, "jobTitle");
		
		ServiceContext serviceContext = null;
		try {
			serviceContext = ServiceContextFactory.getInstance(actionRequest);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}

		User user = null;
		try {
			user = UserLocalServiceUtil.addUser(creatorUserId, companyId, true,
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
		
		if (Validator.isNull(user)) return;
						
		// insert a record into the "Address" table for this user
		long countryId = ParamUtil.getLong(actionRequest, "countryId");
		try {
			AddressLocalServiceUtil.addAddress(user.getUserId(),
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

		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		
		if (!themeDisplay.isSignedIn()) {
			
			boolean someProblem = false;
			try {
				Organization parentOrg = OrganizationLocalServiceUtil.fetchOrganization(companyId, "Firms");
				
				if (Validator.isNotNull(parentOrg)) {
					
					String firmName = ParamUtil.getString(actionRequest, "firmName");
					try {
						Organization firm = OrganizationLocalServiceUtil.addOrganization(user.getUserId(), parentOrg.getOrganizationId(), firmName, false);
						OrganizationLocalServiceUtil.addUserOrganization(user.getUserId(), firm.getOrganizationId());
						assignOrganizationRole(user.getUserId(), firm.getOrganizationId(), companyId, RoleConstants.ORGANIZATION_ADMINISTRATOR);
						
						actionResponse.sendRedirect(themeDisplay.getURLSignIn());
					} catch (PortalException e) {
						e.printStackTrace();
						someProblem = true;
					}
				} else {
					someProblem = true;
				}
			} catch (SystemException e) {
				e.printStackTrace();
				someProblem = true;
			}
			
			if (someProblem) {
				
			}
		} else {
			
		}
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
	
	private void assignOrganizationRole(long userId, long organizationId, long companyId, String roleName) {
		
		long roleId = 0l;
		try {
			Role role = RoleLocalServiceUtil.fetchRole(companyId, roleName);
			if (Validator.isNull(role)) return;
			roleId = role.getRoleId();
		} catch (SystemException e) {
			e.printStackTrace();
		}

		try {
			UserGroupRoleLocalServiceUtil.addUserGroupRoles(userId, organizationId, new long[] { roleId });
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
}