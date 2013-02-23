package com.library.security;

import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.service.permission.PortletPermissionUtil;
import com.liferay.portal.theme.ThemeDisplay;

public class LibraryPermissionUtil {
	
	public static final String portletId = "library_WAR_libraryportlet";
	
	public static boolean hasPermissionToAddBook(
			PermissionChecker permissionChecker) {
		
		boolean hasPermission = false;
		
		try {
			hasPermission = PortletPermissionUtil.contains(
					permissionChecker, 
					portletId, 
			                ActionKeys.ADD_ENTRY);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	
		return hasPermission;
	}
	
	public static boolean hasPermissionToAddBook(
			PortletRequest portletRequest) {
		
		ThemeDisplay themeDisplay = (ThemeDisplay) 
			portletRequest.getAttribute(WebKeys.THEME_DISPLAY);
		
		return hasPermissionToAddBook(themeDisplay.getPermissionChecker());
	}
}