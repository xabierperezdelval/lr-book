package com.inikah.edit;

import java.io.File;
import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;

import org.apache.commons.lang.StringUtils;

import com.inikah.slayer.model.Profile;
import com.inikah.slayer.service.BridgeLocalServiceUtil;
import com.inikah.slayer.service.LocationLocalServiceUtil;
import com.inikah.slayer.service.PhotoLocalServiceUtil;
import com.inikah.slayer.service.ProfileLocalServiceUtil;
import com.inikah.util.IConstants;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class ManagePortlet
 */
public class EditPortlet extends MVCPortlet {
	
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
					
				case IConstants.PROFILE_STATUS_STEP4_DONE: 
					saveStep5(actionRequest, profile);
					break;						
			}
			
			try {
				ProfileLocalServiceUtil.updateProfile(profile);
			} catch (SystemException e) {
				e.printStackTrace();
			}			
		}
				
		if (profile.getStatus() == IConstants.PROFILE_STATUS_STEP5_DONE) {
			actionResponse.sendRedirect(StringPool.SLASH + "pay");
		} else {
			actionResponse.setRenderParameter("tabs1", ParamUtil.getString(actionRequest, "tabs1"));
		}
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
		BridgeLocalServiceUtil.addPhone(userId, Profile.class.getName(), profile.getProfileId(), mobileNumber, "91", true);
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
		
		//------------------social-info----------------------------        
  		profile.setResidingArea(ParamUtil.getString(actionRequest, "residingArea"));
        profile.setNearbyMasjid(ParamUtil.getString(actionRequest, "nearbyMasjid"));
        profile.setEmailAddress(ParamUtil.getString(actionRequest, "emailAddress"));
        
		String canSpeakList = StringUtils.join(
				ParamUtil.getParameterValues(actionRequest, "canSpeak"),
				CharPool.COMMA);        
        profile.setCanSpeak(canSpeakList);

        String mobileNumber = ParamUtil.getString(actionRequest, "mobileNumber");
        String extension = ParamUtil.getString(actionRequest, "mobileIdd", profile.getPhoneIdd(true));
        profile.setMotherTongue(ParamUtil.getInteger(actionRequest, "motherTongue"));
      
        long userId = PortalUtil.getUserId(actionRequest);
		BridgeLocalServiceUtil.addPhone(userId, Profile.class.getName(),
				profile.getProfileId(), mobileNumber, extension, true);
		
        //------------------non-single-info----------------------------
		
		if (!profile.isSingle()) {
			profile.setSons(ParamUtil.getInteger(actionRequest, "sons"));
			profile.setDaughters(ParamUtil.getInteger(actionRequest, "daughters"));
			
			if (profile.isMarried()) {
				profile.setReMarriageReason(ParamUtil.getInteger(actionRequest, "remarriageReason"));
			}
		}

 		//------------------education-info----------------------------
		
		int education = ParamUtil.getInteger(actionRequest, "education");
		profile.setEducation(education);
		if (education == -1) {
			profile.setEducationOther(ParamUtil.getString(actionRequest, "educationOther"));
		}
		profile.setEducationDetail(ParamUtil.getString(actionRequest, "educationDetails"));
		profile.setEducationSchool(ParamUtil.getString(actionRequest, "schoolAttended"));
		
		
		//------------------Islamic education-info----------------------------

		int religiousEducation = ParamUtil.getInteger(actionRequest,
				"religiousEducation");
		profile.setReligiousEducation(religiousEducation);
		if (religiousEducation == -1) {
			profile.setReligiousEducationOther(ParamUtil.getString(
					actionRequest, "religiousEducationOther"));
		}
		profile.setReligiousEducationDetail(ParamUtil.getString(actionRequest,
				"religiousEducationDetails"));
		profile.setReligiousEducationSchool(ParamUtil.getString(actionRequest,
				"religiousSchoolAttended"));
		
		
		//------------------Occupation-info----------------------------
		
		int profession = ParamUtil.getInteger(actionRequest, "occupation");
		profile.setProfession(profession);
		
		if (profession == -1) {
			profile.setProfessionOther(ParamUtil.getString(actionRequest, "occupationOther"));
		}
		profile.setProfessionDetail(ParamUtil.getString(actionRequest, "occupationDetails"));
		profile.setOrganization(ParamUtil.getString(actionRequest, "whereWorking"));
		profile.setIncome(ParamUtil.getInteger(actionRequest, "income"));;
		profile.setIncomeFrequency(ParamUtil.getInteger(actionRequest, "incomeFrequency"));
		
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
				
    	profile.setDescription(ParamUtil.getString(actionRequest, "description"));
    	profile.setExpectation(ParamUtil.getString(actionRequest, "expectation"));
    	profile.setPayZakath(ParamUtil.getBoolean(actionRequest, "payZakath"));
    	
		String hobbiesCSV = StringUtils.join(
				ParamUtil.getParameterValues(actionRequest, "hobbies"),
				CharPool.COMMA);      		
        	
    	profile.setHobbies(hobbiesCSV);
    	profile.setPhysicallyChallenged(ParamUtil.getBoolean(actionRequest, "physicallyChallenged"));
    	profile.setPhysicallyChallengedDetails(ParamUtil.getString(actionRequest, "physicallyChallengedDetail"));
    	profile.setPerformedHaj(ParamUtil.getBoolean(actionRequest, "performedHaj"));
    	profile.setRevertedToIslam(ParamUtil.getBoolean(actionRequest, "revertedToIslam"));
    	profile.setMuslimSince(ParamUtil.getInteger(actionRequest, "muslimSince"));		
		
		if (!profile.isEditMode() && profile.getStatus() == IConstants.PROFILE_STATUS_STEP3_DONE) {
			profile.setStatus(IConstants.PROFILE_STATUS_STEP4_DONE);
		}
	}	
	
	public void saveStep5(ActionRequest actionRequest, Profile profile) {

		UploadPortletRequest uploadPortletRequest = PortalUtil.getUploadPortletRequest(actionRequest);
		
		for (int i=0; i<4; i++) {
			File profilePhoto = uploadPortletRequest.getFile("profilePhoto_" + i);
			
			if (Validator.isNotNull(profilePhoto) && profilePhoto.length() > 0) {
				long imageId = ParamUtil.getLong(uploadPortletRequest, "imageId_"+i, 0l);
				String description = ParamUtil.getString(uploadPortletRequest, "description_"+i);
				PhotoLocalServiceUtil.upload(imageId, profile.getProfileId(), profilePhoto, description);
			}
		}
		
		if (!profile.isEditMode() && profile.getStatus() == IConstants.PROFILE_STATUS_STEP4_DONE) {
			profile.setStatus(IConstants.PROFILE_STATUS_STEP5_DONE);
		}
	}
	
	public void makePortrait(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException {
		
		long imageId = ParamUtil.getLong(actionRequest, "imageId");
		
		PhotoLocalServiceUtil.createPortrait(imageId);
	}	
} 