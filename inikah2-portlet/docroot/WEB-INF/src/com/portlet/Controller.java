package com.portlet;

import java.io.IOException;
import java.util.Date;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.slayer.model.Profile;
import com.slayer.service.ProfileLocalServiceUtil;

public class Controller extends MVCPortlet {
	
	public void saveProfile(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {

		int step = ParamUtil.getInteger(actionRequest, "step");
		
		Profile profile = null;
		long profileId = ParamUtil.getLong(actionRequest, "profileId");
		try {
			profile = ProfileLocalServiceUtil.fetchProfile(profileId);
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
		//profile.setField1(ParamUtil.getString(actionRequest, "field1"));
		//profile.setField2(ParamUtil.getString(actionRequest, "field2"));
	}
	
	private void saveStep2(ActionRequest actionRequest, Profile profile) {
		
		_log.debug("inside step2...");
		//profile.setField3(ParamUtil.getString(actionRequest, "field3"));
		//profile.setField4(ParamUtil.getString(actionRequest, "field4"));
	}
	
	private void saveStep3(ActionRequest actionRequest, Profile profile) {
		
		_log.debug("inside step3...");
		//profile.setField5(ParamUtil.getString(actionRequest, "field5"));
		//profile.setField6(ParamUtil.getString(actionRequest, "field6"));
	}
	
	private final Log _log = LogFactoryUtil.getLog(Controller.class);
}