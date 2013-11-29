package com.inikah.edit;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;

import com.inikah.slayer.model.Profile;
import com.inikah.slayer.service.ProfileLocalServiceUtil;
import com.inikah.util.IConstants;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.PwdGenerator;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class ManagePortlet
 */
public class EditPortlet extends MVCPortlet {
	
	public void start(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {

		System.out.println("inside start method.....");
		
		boolean bride = ParamUtil.getBoolean(actionRequest, "bride", false);
		String profileName = ParamUtil.getString(actionRequest, "profileName", "Dummy");
		boolean createdForSelf = ParamUtil.getBoolean(actionRequest, "createdForSelf", false);

		User user = null;
		try {
			user = PortalUtil.getUser(actionRequest);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		ServiceContext serviceContext = null;
		try {
			serviceContext = ServiceContextFactory.getInstance(actionRequest);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		Profile profile = ProfileLocalServiceUtil.init(bride, user.getEmailAddress(), profileName, createdForSelf, serviceContext);
		
		PortletSession portletSession = actionRequest.getPortletSession();
		portletSession.setAttribute("SEL_PROFILE", profile, PortletSession.APPLICATION_SCOPE);
	}
 
	public void saveProfile(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {

		Profile profile = (Profile) actionRequest.getPortletSession().getAttribute("SEL_PROFILE", PortletSession.APPLICATION_SCOPE);
		
		User user = null;
		try {
			user = PortalUtil.getUser(actionRequest);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
				
		if (Validator.isNotNull(profile) && profile.isOwner(user.getUserId())) {
			
			switch (profile.getStatus()) {
				case IConstants.PROFILE_STATUS_CREATED: 
					saveStep1(actionRequest, profile, user);
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
		actionResponse.setRenderParameter("tabs1", ParamUtil.getString(actionRequest, "tabs1"));
	}
	
	private void saveStep1(ActionRequest actionRequest, Profile profile, User user) {
		
		int bornMonth = ParamUtil.getInteger(actionRequest, "bornMonth");
		int bornYear = ParamUtil.getInteger(actionRequest, "bornYear");
		
		if (bornMonth >= 0 && bornYear > 0) {
			profile.setBornOn(Integer.valueOf(bornYear + String.format("%02d", bornMonth)));
		}
		
		profile.setMaritalStatus(ParamUtil.getInteger(actionRequest, "maritalStatus"));
		profile.setCreatedFor(ParamUtil.getInteger(actionRequest, "createdFor"));
		profile.setHeight(ParamUtil.getInteger(actionRequest, "height"));
		profile.setWeight(ParamUtil.getInteger(actionRequest, "weight"));
		
		String mobileNumber = ParamUtil.getString(actionRequest, "mobileNumber");
		profile.setMobileNumber(mobileNumber);
		profile.setVerificationCode(sendVerificationCode(mobileNumber));
		profile.setAllowNonSingleProposals(!profile.isSingle());
		
		if (!profile.isEditMode() && profile.getStatus() == IConstants.PROFILE_STATUS_CREATED) {
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
	
	private String sendVerificationCode(String mobileNumber) {
		// TODO Auto-generated method stub
		
		String verificationCode = PwdGenerator.getPinNumber();
		
		//SMSUtil.sendVerificationCode("91" + mobileNumber, verificationCode);
		
		return verificationCode;
	}
}