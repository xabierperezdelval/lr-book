package com.hook.login;

import com.liferay.portal.kernel.events.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;
import com.slayer.service.LocationLocalServiceUtil;
import com.slayer.service.ProfileLocalServiceUtil;

public class PostLoginAction extends Action {
	/* (non-Java-doc)
	 * @see com.liferay.portal.kernel.events.Action#Action()
	 */
	public PostLoginAction() {
		super();
	}

	/* (non-Java-doc)
	 * @see com.liferay.portal.kernel.events.Action#run(HttpServletRequest arg0, HttpServletResponse arg1)
	 */
	public void run(HttpServletRequest request, HttpServletResponse response) throws ActionException {

		User user = null;
		try {
			user = PortalUtil.getUser(request);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(user)) return;
		
		// Set Max Mind Coordinates
		LocationLocalServiceUtil.setCoordinates(user);
		
		// check for any invitations for this user
		//InvitationLocalServiceUtil.linkInvitation(user);
		
		// Set the ownerLastLogin for all the profiles owned by this user
		ProfileLocalServiceUtil.setOwnerLastLogin(user.getUserId());
	}
}