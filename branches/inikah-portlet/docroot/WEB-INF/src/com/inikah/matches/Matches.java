package com.inikah.matches;

import java.io.IOException;
import java.util.List;

import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;

import com.inikah.slayer.model.MatchCriteria;
import com.inikah.slayer.model.Profile;
import com.inikah.slayer.service.MatchCriteriaLocalServiceUtil;
import com.inikah.slayer.service.ProfileLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class Matches
 */
public class Matches extends MVCPortlet {
 
	@javax.portlet.ProcessEvent(qname = "{http://liferay.com}filter")

	 public void quickAdd(EventRequest request,EventResponse response)
	throws PortletException, IOException {           
	        
	        
	        
	        long profileId = ParamUtil.getLong(request, "profileId");
	        
			PortletSession portletSession = request.getPortletSession();
			Profile profile = null;
			if (Validator.isNull(portletSession.getAttribute("PROFILE"))) {
				
				try {
					profile = ProfileLocalServiceUtil.fetchProfile(profileId);
				} catch (SystemException e) {
					e.printStackTrace();
				}
				if (Validator.isNotNull(profile)) {
					portletSession.setAttribute("PROFILE", profile);
				}
			}
			
	        System.out.println(profileId);
	        
	        Event event = request.getEvent();
	        
	        //MatchCriteria matchCriteria = (MatchCriteria) event.getValue();
	        
	        MatchCriteria matchCriteria = (MatchCriteria) event.getValue();
	        
	        System.out.println(matchCriteria);
	        
	        MatchCriteriaLocalServiceUtil.insert(profileId, matchCriteria);                  
                
	        System.out.println("done");	        
	        	        
	        List<Profile> matchResults = MatchCriteriaLocalServiceUtil.getMatches(profileId);
	        
	        System.out.println(matchResults);
	        

	 }

}
