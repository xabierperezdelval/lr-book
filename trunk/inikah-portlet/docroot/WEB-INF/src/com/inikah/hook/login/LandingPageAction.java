package com.inikah.hook.login;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.inikah.slayer.model.Profile;
import com.inikah.slayer.service.ProfileLocalServiceUtil;
import com.inikah.util.IConstants;
import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.struts.LastPath;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
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

		long companyId = PortalUtil.getCompanyId(request);
		
		String path = null;

		User user = null;
		try {
			user = PortalUtil.getUser(request);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNotNull(user)) {
			// check if user has profile that is incomplete
			List<Profile> profiles = ProfileLocalServiceUtil.getProfilesForUser(user.getUserId());
			
			long profileId = 0l;
			boolean hasNoProfiles = (Validator.isNull(profiles));
			
			for (Profile profile: profiles) {
				if (profile.getStatus() == IConstants.PROFILE_STATUS_CREATED) {
					profileId = profile.getProfileId();
					break;
				}
			}

			if (!hasNoProfiles && (profileId == 0l)) {
				path = "/mine";
			} else {
				path = "/edit?id=" + String.valueOf(profileId);
			}
		}
		
		// if path is still null
		if (Validator.isNull(path)) {
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