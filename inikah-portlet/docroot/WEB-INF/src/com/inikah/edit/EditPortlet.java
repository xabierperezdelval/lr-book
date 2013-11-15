package com.inikah.edit;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import com.inikah.slayer.model.Profile;
import com.inikah.slayer.service.ProfileLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class ManagePortlet
 */
public class EditPortlet extends MVCPortlet {
 
	public void saveProfile(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {

		long profileId = ParamUtil.getLong(actionRequest, "profileId", 0l);
		int currentStep = ParamUtil.getInteger(actionRequest, "currentStep", 0);
		
		Profile profile = null;
		try {
			profile = ProfileLocalServiceUtil.fetchProfile(profileId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		long userId = PortalUtil.getUserId(actionRequest);
		if (Validator.isNotNull(profile) 
				&& ProfileLocalServiceUtil.isOwner(userId, profileId)) {
			
			switch (currentStep) {
				case 1: saveStep1(actionRequest, profile);
						break;
					
				case 2: saveStep2(actionRequest, profile);
						break;
						
				case 3: saveStep3(actionRequest, profile);
						break;
			}
			
			try {
				ProfileLocalServiceUtil.updateProfile(profile);
			} catch (SystemException e) {
				e.printStackTrace();
			}			
		}
		
		if (currentStep == 3) {
			actionResponse.sendRedirect("/pay?id=" + String.valueOf(profileId));
		} else {
			actionResponse.sendRedirect("/edit?id=" + String.valueOf(profileId));
		}
	}
	
	private void saveStep1(ActionRequest actionRequest, Profile profile) {

		int maritalStatus = ParamUtil.getInteger(actionRequest, "maritalStatus");
		int createdFor = ParamUtil.getInteger(actionRequest, "createdFor");
		
		int bornMonth = ParamUtil.getInteger(actionRequest, "bornMonth");
		int bornYear = ParamUtil.getInteger(actionRequest, "bornYear");
		
		if (bornMonth >= 0 && bornYear > 0) {
			profile.setBornOn(Integer.valueOf(bornYear + String.format("%02d", bornMonth)));
		}
		
		profile.setMaritalStatus(maritalStatus);
		profile.setCreatedFor(createdFor);
	}	

	private void saveStep2(ActionRequest actionRequest, Profile profile) {
		// TODO Auto-generated method stub
		
	}
	
	private void saveStep3(ActionRequest actionRequest, Profile profile) {
	
		
		
	}
}