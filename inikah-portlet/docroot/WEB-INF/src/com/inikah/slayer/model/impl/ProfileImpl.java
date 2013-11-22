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

package com.inikah.slayer.model.impl;

import java.util.Calendar;

import com.inikah.slayer.service.BridgeServiceUtil;
import com.inikah.slayer.service.ProfileLocalServiceUtil;
import com.inikah.util.IConstants;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Address;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;


/**
 * The extended model implementation for the Profile service. Represents a row in the &quot;inikah_Profile&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * Helper methods and all application logic should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.inikah.slayer.model.Profile} interface.
 * </p>
 *
 * @author Ahmed Hasan
 */
public class ProfileImpl extends ProfileBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. All methods that expect a profile model instance should use the {@link com.inikah.slayer.model.Profile} interface instead.
	 */
	public ProfileImpl() {
	}
	
	public boolean isCreatedForSelf() {
		int createdForSelf = BridgeServiceUtil.getListTypeId(IConstants.LIST_CREATED_FOR, "self");
		
		return (createdForSelf == getCreatedFor());
	}
	
	public String getTitle() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(getProfileName());
		sb.append(StringPool.SPACE);
		sb.append(StringPool.OPEN_PARENTHESIS);
		sb.append(getProfileCode());
		sb.append(StringPool.CLOSE_PARENTHESIS);
		
		return sb.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public double getComputeAge() {
		
		double age = 0.0d;
		
		int bornOn = getBornOn();
		
		if (bornOn == 0) return age;
		
		Calendar now = Calendar.getInstance();
		int nowYear = now.get(Calendar.YEAR);
		int nowMonth = now.get(Calendar.MONTH); 
		
		int bornYear = Integer.valueOf(String.valueOf(bornOn).substring(0, 4));
		int bornMonth = Integer.valueOf(String.valueOf(bornOn).substring(4));
		
		int decimalPart = nowMonth - bornMonth;
		int wholePart = nowYear - bornYear;
		
		if (decimalPart < 0) {
			decimalPart += 12;
			--wholePart;
		}
		
		return Double.valueOf(wholePart + StringPool.PERIOD + decimalPart);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDisplayAge() {
		if (getComputeAge() == 0.0d) return StringPool.BLANK;
		
		String[] parts = String.valueOf(getComputeAge()).split("\\.");
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(parts[0]).append(parts[0]);
		sb.append(StringPool.NBSP).append("Years");
		
		int months = Integer.valueOf(parts[1]);
		
		if (months > 0) {
			sb.append(StringPool.COMMA).append(StringPool.NBSP);
			sb.append(parts[1]).append(StringPool.NBSP);
			sb.append("Month");
			
			if (months > 1) {
				sb.append(CharPool.LOWER_CASE_S);
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * 
	 * @param user
	 */
	public void setDefaultLocation(User user) {
		// set other attributes for the profile before updating it
		Address address = ProfileLocalServiceUtil.getMaxMindAddress(user);
		
		if (Validator.isNotNull(address)) {
			
			long city = Long.valueOf(address.getCity());
			
			setResidingCountry(address.getCountryId());
			setResidingState(address.getRegionId());
			setResidingCity(city);
			
			setCountryOfBirth(address.getCountryId());
			setStateOfBirth(address.getRegionId());
			setCityOfBirth(city);
		}
	}
	
	public String getPriceText(long planId) {
		
		return String.valueOf(getPrice(planId));
		
	}
	
	public double getPrice(long planId) {
		
		long userCountryId = getUserCountryId() ;
		
		
		return 10.0d;
	}
	
	public long getUserCountryId() {
		long countryId = 108l;
		try {
			User user = UserLocalServiceUtil.fetchUser(getUserId());
			
			Address address = ProfileLocalServiceUtil.getMaxMindAddress(user);
			
			if (Validator.isNotNull(address)) {
				countryId = address.getCountryId();
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return countryId;
	}
	
	public boolean isEditMode() {
		return getStatus() >= IConstants.PROFILE_STATUS_STEP4_DONE;
		//return false;
	}
	
	public boolean isOwner(long userId) {
		return (userId == getUserId());
	}
	
	public boolean isSingle() {
		return (getMaritalStatus() == BridgeServiceUtil.getListTypeId(IConstants.LIST_MARITAL_STATUS, "single"));
	}
	
	public boolean isMarried() {
		return (!isBride() && 
				(getMaritalStatus() == BridgeServiceUtil.getListTypeId(IConstants.LIST_MARITAL_STATUS, "married")));
	}
	
	public String getAllowedMaritalStatus() {
		
		StringBuilder sb = new StringBuilder();
		
		long singleStatus = BridgeServiceUtil.getListTypeId(IConstants.LIST_MARITAL_STATUS, "single");
		long marriedStatus = BridgeServiceUtil.getListTypeId(IConstants.LIST_MARITAL_STATUS, "married");
		long divocedStatus = BridgeServiceUtil.getListTypeId(IConstants.LIST_MARITAL_STATUS, "divorced");
		long widowStatus = BridgeServiceUtil.getListTypeId(IConstants.LIST_MARITAL_STATUS, "widow");
		
		if (isSingle() && !isAllowNonSingleProposals()) {
			sb.append(singleStatus);
		}
		
		if (isSingle() && isAllowNonSingleProposals()) {
			sb.append(singleStatus);
			sb.append(StringPool.COMMA);
			sb.append(divocedStatus);
			sb.append(StringPool.COMMA);
			sb.append(widowStatus);
		}
		
		if (!isSingle()) {
			if (isBride()) {
				sb.append(marriedStatus);
				sb.append(StringPool.COMMA);				
			}
			sb.append(divocedStatus);
			sb.append(StringPool.COMMA);
			sb.append(widowStatus);
		}		
		
		return sb.toString();
	}
}