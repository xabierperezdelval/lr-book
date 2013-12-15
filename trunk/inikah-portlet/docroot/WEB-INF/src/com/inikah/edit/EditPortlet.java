package com.inikah.edit;

import java.io.File;
import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletResponse;

import com.inikah.slayer.model.Profile;
import com.inikah.slayer.service.PhotoLocalServiceUtil;
import com.inikah.slayer.service.LocationLocalServiceUtil;
import com.inikah.slayer.service.ProfileLocalServiceUtil;
import com.inikah.util.IConstants;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.PwdGenerator;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.inikah.slayer.service.PhotoLocalServiceUtil;

/**
 * Portlet implementation class ManagePortlet
 */
public class EditPortlet extends MVCPortlet {
	
	public void start(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		
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
		
		long userId = PortalUtil.getUserId(actionRequest);
		int bornMonth = ParamUtil.getInteger(actionRequest, "bornMonth");
		int bornYear = ParamUtil.getInteger(actionRequest, "bornYear");
		
		if (bornMonth >= 0 && bornYear > 0) {
			profile.setBornOn(Integer.valueOf(bornYear + String.format("%02d", bornMonth)));
		}
		
		profile.setMaritalStatus(ParamUtil.getInteger(actionRequest, "maritalStatus"));
		profile.setCreatedFor(ParamUtil.getInteger(actionRequest, "createdFor"));
		profile.setComplexion(ParamUtil.getInteger(actionRequest, "complexion"));
		profile.setHeight(ParamUtil.getInteger(actionRequest, "height"));
		profile.setWeight(ParamUtil.getInteger(actionRequest, "weight"));
		
		String mobileNumber = ParamUtil.getString(actionRequest, "mobileNumber");
		profile.setMobileNumber(mobileNumber);
		profile.setVerificationCode(sendVerificationCode(mobileNumber));
		profile.setAllowNonSingleProposals(!profile.isSingle());
		
		// setting locations
		setLocation(actionRequest, profile, userId, "BIRTH");	
		setLocation(actionRequest, profile, userId, "LIVING");	
		
		if (!profile.isEditMode() && profile.getStatus() == IConstants.PROFILE_STATUS_CREATED) {
			profile.setStatus(IConstants.PROFILE_STATUS_STEP1_DONE);
		}
	}

	private void setLocation(ActionRequest actionRequest, Profile profile,
			long userId, String type) {
		
		String countryFld = "countryOfBirth";
		String stateFld = "stateOfBirth";
		String cityFld = "cityOfBirth";
		
		if (type.equalsIgnoreCase("LIVING")) {
			countryFld = "residingCountry";
			stateFld = "residingState";
			cityFld = "residingCity";			
		}
		
		long countryId = ParamUtil.getLong(actionRequest, countryFld);
		long regionId = ParamUtil.getLong(actionRequest, stateFld);
		long cityId = ParamUtil.getLong(actionRequest, cityFld, -1l);
		
		if (cityId == -1l) {
			String cityText = ParamUtil.getString(actionRequest, "new" + cityFld);
			cityId = LocationLocalServiceUtil.insertCity(regionId, cityText, userId);
		}		
		
		if (type.equalsIgnoreCase("BIRTH")) {
			profile.setCountryOfBirth(countryId);
			profile.setStateOfBirth(regionId);
			profile.setCityOfBirth(cityId);
		} else {
			profile.setResidingCountry(countryId);
			profile.setResidingState(regionId);
			profile.setResidingCity(cityId);
		}
	}

	private void saveStep2(ActionRequest actionRequest, Profile profile) {
		
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
	
	public void uploadImage(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {

		long profileId = ParamUtil.getLong(actionRequest, "profileId", 100l);
		UploadPortletRequest uploadPortletRequest = PortalUtil.getUploadPortletRequest(actionRequest);
		
		for (int i=1; i<=4; i++) {
			File profilePhoto = uploadPortletRequest.getFile("profilePhoto_" + i);
			
			if (profilePhoto.length() > 0) {
				long imageId = ParamUtil.getLong(uploadPortletRequest, "imageId_"+i, 0l);
				String description = ParamUtil.getString(uploadPortletRequest, "description_"+i);
				PhotoLocalServiceUtil.upload(imageId, profileId, profilePhoto, description);
			}
		}
	}
	
	public void serveResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws IOException,
			PortletException {
		
		String cmd = ParamUtil.getString(resourceRequest, Constants.CMD);
		
		if (cmd.equalsIgnoreCase("servePhoto")) {
			long imageId = ParamUtil.getLong(resourceRequest, "imageId");
			byte[] bytes = PhotoLocalServiceUtil.getThumbnail(imageId);
			HttpServletResponse response = PortalUtil
					.getHttpServletResponse(resourceResponse);
			ServletResponseUtil.write(response, bytes);
		}
	}
	
	public void makeThumbnail(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		
		long imageId = ParamUtil.getLong(actionRequest, "imageId");

		long thumbnailId = PhotoLocalServiceUtil.createThumbnail(imageId);
	}	
}