package com.fingence.hook;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fingence.IConstants;
import com.fingence.slayer.service.BridgeServiceUtil;
import com.fingence.util.SecurityUtil;
import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.struts.LastPath;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.util.PortalUtil;

public class LandingPageAction extends Action {
	/* (non-Java-doc)
	 * @see com.liferay.portal.kernel.events.SimpleAction#SimpleAction()
	 */
	public LandingPageAction() {
		super();
	}

	@Override
	public void run(HttpServletRequest request, HttpServletResponse response)
			throws ActionException {

		// line inserted by mPower Labs
		String path = getLandingPath(request);

		if (Validator.isNotNull(path)) {
			LastPath lastPath = new LastPath(
				StringPool.BLANK, path, new HashMap<String, String[]>());

			HttpSession session = request.getSession();

			session.setAttribute(WebKeys.LAST_PATH, lastPath);
		}
	}

	private String getLandingPath(HttpServletRequest request) {
		
		String path = StringPool.BLANK;
		
		long userId = PortalUtil.getUserId(request);

		int userType = BridgeServiceUtil.getUserType(userId);
				
		switch (userType) {
			case IConstants.USER_TYPE_INVESTOR:
				path = "/investor?investorId=" + SecurityUtil.getEncrypted(userId);
				break;
			
			case IConstants.USER_TYPE_REL_MANAGER:
				path = "/manager";
				break;
				
			case IConstants.USER_TYPE_BANK_ADMIN:
				path = "/bank";
				break;
				
			case IConstants.USER_TYPE_WEALTH_ADVISOR:
				path = "/advisor";
				break;				
		} 
		
		// if path is still null
		if (Validator.isNull(path)) {
			long companyId = PortalUtil.getCompanyId(request);
			try {
				path = PrefsPropsUtil.getString(
					companyId, PropsKeys.DEFAULT_LANDING_PAGE_PATH);
			} catch (SystemException e) {
				e.printStackTrace();
			}			
		}		

		return path;
	}
}