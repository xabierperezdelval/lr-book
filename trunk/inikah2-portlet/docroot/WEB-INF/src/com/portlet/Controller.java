package com.portlet;

import java.io.IOException;
import java.util.Date;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.TextFormatter;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Contact;
import com.liferay.portal.service.ContactLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.slayer.model.Profile;
import com.slayer.service.ProfileLocalServiceUtil;
import com.util.IConstants;

public class Controller extends MVCPortlet {
	
	public void startProfile(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		
		long  userId = PortalUtil.getUserId(actionRequest);
				
		if (!ProfileLocalServiceUtil.hasIncompleteProfiles(userId)) {
			Profile profile = ProfileLocalServiceUtil.init(userId, ParamUtil.getBoolean(actionRequest, "bride"));
			actionResponse.setRenderParameter("jspPage", "/html/profile/step1.jsp");
			actionResponse.setRenderParameter("profileId", String.valueOf(profile.getProfileId()));
		}
	}
	
	public void saveProfile(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {

		int step = ParamUtil.getInteger(actionRequest, "step");
		
		Profile profile = null;
		long profileId = ParamUtil.getLong(actionRequest, "profileId");
		try {
			profile = ProfileLocalServiceUtil.fetchProfile(profileId);
			
			// do a sanity check for security
			if (PortalUtil.getUserId(actionRequest) != profile.getUserId()) return;
			
			profile.setModifiedDate(new Date());
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		switch (step) {
			case 1 : 
				saveStep1(actionRequest, profile);
				break;
				
			case 2 : 
				saveStep2(actionRequest, profile);
				break;				
				
			case 3 : 
				saveStep3(actionRequest, profile);
				break;								
		}
		
		try {
			ProfileLocalServiceUtil.updateProfile(profile);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (step < 3) {
			actionResponse.setRenderParameter("jspPage", "/html/profile/step" + (++step) + ".jsp");
			actionResponse.setRenderParameter("profileId", String.valueOf(profileId));
		}
	}
	
	private void saveStep1(ActionRequest actionRequest, Profile profile) {
		
		_log.debug("inside step1...");
		
		int bornMonth = ParamUtil.getInteger(actionRequest, "bornMonth");
		int bornYear = ParamUtil.getInteger(actionRequest, "bornYear");
		
		if (bornMonth >= 0 && bornYear > 0) {
			profile.setBornOn(Integer.valueOf(bornYear + String.format("%02d", bornMonth)));
		}
		
		profile.setProfileName(TextFormatter.format(ParamUtil.getString(actionRequest, "profileName"), TextFormatter.J));
		profile.setMaritalStatus(ParamUtil.getInteger(actionRequest, "maritalStatus"));
		profile.setCreatedFor(ParamUtil.getInteger(actionRequest, "createdFor"));
		profile.setHeight(ParamUtil.getInteger(actionRequest, "height"));
		profile.setWeight(ParamUtil.getInteger(actionRequest, "weight"));
		
		// set the user gender to male if the profile is created for self
		if (profile.getCreatedFor() == IConstants.CREATED_FOR_SELF && profile.isBride()) {
			try {
				Contact contact = PortalUtil.getUser(actionRequest).getContact();
				contact.setMale(false);
				contact.setParentContactId(1);
				ContactLocalServiceUtil.updateContact(contact);
			} catch (PortalException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void saveStep2(ActionRequest actionRequest, Profile profile) {
		
		_log.debug("inside step2...");

		try {
			Contact contact = PortalUtil.getUser(actionRequest).getContact();
			
			if (contact.getParentContactId() == 0) {
				contact.setMale(ParamUtil.getBoolean(actionRequest, "male"));
				contact.setParentContactId(1); // user gender set
				ContactLocalServiceUtil.updateContact(contact);
			}
			
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
	
	private void saveStep3(ActionRequest actionRequest, Profile profile) {
		
		_log.debug("inside step3...");
		//profile.setField5(ParamUtil.getString(actionRequest, "field5"));
		//profile.setField6(ParamUtil.getString(actionRequest, "field6"));
	}
	
	public void deleteProfile(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {

		long profileId = ParamUtil.getLong(actionRequest, "profileId");
		
		System.out.println("delete Profile Id " + profileId);
		
		try {
			Profile profile = ProfileLocalServiceUtil.fetchProfile(profileId);
			
			if (Validator.isNotNull(profile) && (profile.getStatus() < IConstants.STATUS_SUBMITTED)) {
				
				System.out.println("got the profile...");
				try {
					ProfileLocalServiceUtil.deleteProfile(profileId);
				} catch (PortalException e) {
					e.printStackTrace();
				}
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
	
	private final Log _log = LogFactoryUtil.getLog(Controller.class);
}