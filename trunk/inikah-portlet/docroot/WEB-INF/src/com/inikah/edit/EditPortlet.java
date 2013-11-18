package com.inikah.edit;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;

import com.inikah.slayer.model.Profile;
import com.inikah.slayer.service.ProfileLocalServiceUtil;
import com.inikah.util.IConstants;
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

		Profile profile = (Profile) actionRequest.getPortletSession().getAttribute("SEL_PROFILE", PortletSession.APPLICATION_SCOPE);
		
		long userId = PortalUtil.getUserId(actionRequest);
		
		if (Validator.isNotNull(profile) && profile.isOwner(userId)) {
			
			switch (profile.getStatus()) {
				case IConstants.PROFILE_STATUS_CREATED: 
					saveStep1(actionRequest, profile);
					break;
					
				case IConstants.PROFILE_STATUS_STEP1_DONE: 
					saveStep2(actionRequest, profile);
					break;
						
				case IConstants.PROFILE_STATUS_STEP2_DONE: 
					saveStep3(actionRequest, profile);
					break;
						
				case IConstants.PROFILE_STATUS_STEP3_DONE: 
					saveStep4(actionRequest, profile);
					break;					
			}
			
			try {
				ProfileLocalServiceUtil.updateProfile(profile);
			} catch (SystemException e) {
				e.printStackTrace();
			}			
		}
	}
	
	private void saveStep1(ActionRequest actionRequest, Profile profile) {
		
		System.out.println("saving step1...");

		int maritalStatus = ParamUtil.getInteger(actionRequest, "maritalStatus");
		int createdFor = ParamUtil.getInteger(actionRequest, "createdFor");
		
		int bornMonth = ParamUtil.getInteger(actionRequest, "bornMonth");
		int bornYear = ParamUtil.getInteger(actionRequest, "bornYear");
		
		if (bornMonth >= 0 && bornYear > 0) {
			profile.setBornOn(Integer.valueOf(bornYear + String.format("%02d", bornMonth)));
		}
		
		profile.setMaritalStatus(maritalStatus);
		profile.setCreatedFor(createdFor);
		
		if (!profile.isEditMode() && profile.getStatus() == IConstants.PROFILE_STATUS_CREATED) {
			System.out.println("setting the new status....");
			profile.setStatus(IConstants.PROFILE_STATUS_STEP1_DONE);
		}
	}	

	private void saveStep2(ActionRequest actionRequest, Profile profile) {
		// TODO Auto-generated method stub
		
		if (!profile.isEditMode() && profile.getStatus() == IConstants.PROFILE_STATUS_STEP1_DONE) {
			profile.setStatus(IConstants.PROFILE_STATUS_STEP2_DONE);
		}
	}
	
	private void saveStep3(ActionRequest actionRequest, Profile profile) {
	
		if (!profile.isEditMode() && profile.getStatus() == IConstants.PROFILE_STATUS_STEP2_DONE) {
			profile.setStatus(IConstants.PROFILE_STATUS_STEP3_DONE);
		}		
	}
	
	private void saveStep4(ActionRequest actionRequest, Profile profile) {
		// TODO Auto-generated method stub
		
		if (!profile.isEditMode() && profile.getStatus() == IConstants.PROFILE_STATUS_STEP3_DONE) {
			profile.setStatus(IConstants.PROFILE_STATUS_STEP4_DONE);
		}
	}
}