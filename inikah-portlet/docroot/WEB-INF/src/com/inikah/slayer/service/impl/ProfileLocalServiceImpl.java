/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.inikah.slayer.service.impl;

import java.util.Date;
import java.util.List;

import com.inikah.slayer.model.Profile;
import com.inikah.slayer.service.BridgeServiceUtil;
import com.inikah.slayer.service.base.ProfileLocalServiceBaseImpl;
import com.inikah.util.IConstants;
import com.inikah.util.ProfileCodeUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.service.ServiceContext;

/**
 * The implementation of the profile local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.slayer.service.ProfileLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.slayer.service.base.ProfileLocalServiceBaseImpl
 * @see com.slayer.service.ProfileLocalServiceUtil
 */
public class ProfileLocalServiceImpl extends ProfileLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.slayer.service.ProfileLocalServiceUtil} to access the profile local service.
	 */
	
	/**
	 * 
	 */
	public Profile quickAdd(boolean bride, String emailAddress, 
			String month, String year, int maritalStatus, 
			int createdFor, int motherTongue, String profileName, 
			ServiceContext serviceContext) {
						
		long profileId = 0l;
		try {
			profileId = counterLocalService.increment();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		Profile profile = createProfile(profileId);
				
		// setting values from serviceContext
		profile.setCompanyId(serviceContext.getCompanyId());
		profile.setGroupId(serviceContext.getScopeGroupId());
		profile.setUserId(serviceContext.getUserId());
		profile.setCreateDate(new Date());
		profile.setOwnerLastLogin(new Date());
				
		// basic info
		profile.setBride(bride);
		profile.setProfileName(profileName);
		profile.setCreatedFor(createdFor);
		profile.setProfileCode(ProfileCodeUtil.getProfileCode(bride));
		profile.setMaritalStatus(maritalStatus);
		profile.setEmailAddress(emailAddress);
				
		// profile coordinates
		profile.setBornOn(Integer.valueOf(year + month));
				
		try {
			profile = addProfile(profile);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		// setting the match criteria
		matchCriteriaLocalService.init(profile);
		
		return profile;
	}
	
	/**
	 * 
	 */
	public Profile updateProfile(long profileId, String phone1, String phone2, 
			long residingCountry, long residingState, long residingCity, 
			String reMarriageReason, boolean hasChildren, int sons, int daughters, 
			int height, int weight, int complexion, int motherTongue, 
			int education, String educationDetail, int religiousEducation, String religiousEducationDetail,
			int profession, String professionDetail, String organization, int currency, int monthlyIncome,
			long countryOfOrigin, long stateOfOrigin, long cityOfOrigin, int community, int ethnicity, 
			String description, String expectation, String hobbies, boolean physicallyChallenged, 
			String physicallyChallengedDetails, boolean performedHaj, 
			boolean revertedToIslam, int muslimSince, String status,
			ServiceContext serviceContext) {
				
		Profile profile = null;
		try {
			profile = fetchProfile(profileId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(profile)) return profile ;
		
		// setting values from serviceContext
		profile.setModifiedDate(new Date());
				
		// contact information	
		profile.setPhone1(phone1);
		profile.setPhone2(phone2);
		profile.setResidingCountry(residingCountry);
		profile.setResidingState(residingState);
		profile.setResidingCity(residingCity);
		
		// fields related to marital status
		profile.setReMarriageReasons(reMarriageReason);
		profile.setHasChildren(hasChildren);
		profile.setSons(sons);
		profile.setDaughters(daughters);
		
		// additional attributes
		profile.setHeight(height);
		profile.setWeight(weight);
		profile.setComplexion(complexion);
		profile.setMotherTongue(motherTongue);
		
		// Education / Occupation
		profile.setEducation(education);
		profile.setEducationDetail(educationDetail);
		profile.setReligiousEducation(religiousEducation);
		profile.setReligiousEducationDetail(religiousEducationDetail);
		profile.setProfession(profession);
		profile.setProfessionDetail(professionDetail);
		profile.setOrganization(organization);
		profile.setCurrency(currency);
		profile.setMonthlyIncome(monthlyIncome);
		
		// Family Details
		//profile.setCountryOfOrigin(countryOfOrigin);
		//profile.setStateOfOrigin(stateOfOrigin);
		//profile.setCityOfOrigin(cityOfOrigin);
		profile.setCommunity(community);
		profile.setEthnicity(ethnicity);
		
		// other details
		profile.setDescription(description);
		profile.setExpectation(expectation);
		profile.setHobbies(hobbies);
		profile.setPhysicallyChallenged(physicallyChallenged);
		profile.setPhysicallyChallengedDetails(physicallyChallengedDetails);
		profile.setPerformedHaj(performedHaj);
		profile.setRevertedToIslam(revertedToIslam);
		profile.setMuslimSince(muslimSince);
		
		// workflow status
		//profile.setStatus(status);
						
		try {
			profile = addProfile(profile);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return profile;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.inikah.slayer.service.ProfileLocalService#updateStatus(long, java.lang.String)
	 */
	public void updateStatus(long profileId, String status) {
		
		Profile profile = null;
		try {
			profile = fetchProfile(profileId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(profile)) return;
		
		//profile.setStatus(status);
		try {
			updateProfile(profile);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	public void setOwnerLastLogin(long userId) {
		try {
			List<Profile> profiles = profilePersistence.findByUserId(userId);
			
			for (Profile profile: profiles) {
				profile.setOwnerLastLogin(new Date());
				updateProfile(profile);
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param emailAddress
	 * @return
	 */
	private Profile getProfileByEmail(String emailAddress) {
		
		long companyId = CompanyThreadLocal.getCompanyId();
		
		long userId = 0l;
		try {
			userId = userLocalService.getDefaultUserId(companyId);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
				
		Profile profile = null;
		try {
			profile = profilePersistence.fetchByUserId_EmailAddress(userId, emailAddress);
		} catch (SystemException e) {
			e.printStackTrace();
		}

		return profile;
	}
	
	/**
	 * 
	 * @param user
	 * @param serviceContext
	 */
	public void attachProfileToUser(User user) {

		Profile profile = getProfileByEmail(user.getEmailAddress());
		
		if (Validator.isNull(profile)) return; 
		
		profile.setUserId(user.getUserId());
		profile.setUserName(user.getFirstName());
		
		BridgeServiceUtil.setDefaultLocation(user, profile);
		
		try {
			updateProfile(profile);
		} catch (SystemException e) {
			e.printStackTrace();
		}		
	}
	
	public boolean isOwner(long userId, long profileId) {
		
		boolean flag = false;
		try {
			Profile profile = profilePersistence.fetchByUserId_ProfileId(userId, profileId);
			
			flag = (Validator.isNotNull(profile));
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public boolean hasSelfProfile(long userId) {
		
		boolean flag = false;
		
		int createdFor = BridgeServiceUtil.getListTypeId(IConstants.LIST_CREATED_FOR, "self");
		
		try {
			Profile profile = profilePersistence.fetchByUserId_CreatedFor(userId, createdFor);
			
			flag = (Validator.isNotNull(profile));
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return flag;
	}
}