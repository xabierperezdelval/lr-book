package com.library.asset;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.servlet.http.HttpServletRequest;

import com.library.LibraryConstants;
import com.library.security.LibraryPermissionUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.PortletURLFactoryUtil;
import com.liferay.portlet.asset.model.AssetRenderer;
import com.liferay.portlet.asset.model.BaseAssetRendererFactory;
import com.slayer.model.LMSBook;
import com.slayer.service.LMSBookLocalServiceUtil;

public class LMSBookAssetRendererFactory extends BaseAssetRendererFactory {

	public AssetRenderer getAssetRenderer(long bookId, int type)
			throws PortalException, SystemException {
		
		LMSBook lmsBook = LMSBookLocalServiceUtil.fetchLMSBook(bookId);
		
		return new LMSBookAssetRenderer(lmsBook);
	}
	
	public String getClassName() {
		return LMSBook.class.getName();
	}
	
	public String getType() {
		return "book";
	}
	
	public boolean isLinkable() {
		return true;
	}
	
	public PortletURL getURLAdd(
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse)
		throws PortalException, SystemException {
	
		HttpServletRequest request =
			liferayPortletRequest.getHttpServletRequest();
	
		ThemeDisplay themeDisplay = (ThemeDisplay)
			request.getAttribute(WebKeys.THEME_DISPLAY);
		
		if (!LibraryPermissionUtil.hasPermissionToAddBook(
				themeDisplay.getPermissionChecker())) {
			return null;
		}
	
		PortletURL portletURL = PortletURLFactoryUtil.create(
			request, "library_WAR_libraryportlet", 
			getControlPanelPlid(themeDisplay),
			PortletRequest.RENDER_PHASE);
		portletURL.setParameter("jspPage", LibraryConstants.PAGE_UPDATE);
	
		return portletURL;
	}
}