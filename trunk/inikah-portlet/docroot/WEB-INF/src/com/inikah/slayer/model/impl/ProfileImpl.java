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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.inikah.slayer.model.Currency;
import com.inikah.slayer.model.Location;
import com.inikah.slayer.model.MatchCriteria;
import com.inikah.slayer.model.MyLanguage;
import com.inikah.slayer.model.Photo;
import com.inikah.slayer.model.Profile;
import com.inikah.slayer.service.BridgeLocalServiceUtil;
import com.inikah.slayer.service.BridgeServiceUtil;
import com.inikah.slayer.service.CurrencyLocalServiceUtil;
import com.inikah.slayer.service.LocationLocalServiceUtil;
import com.inikah.slayer.service.MatchCriteriaLocalServiceUtil;
import com.inikah.slayer.service.MyLanguageLocalServiceUtil;
import com.inikah.slayer.service.PhotoLocalServiceUtil;
import com.inikah.slayer.service.ProfileLocalServiceUtil;
import com.inikah.util.FilterUtil;
import com.inikah.util.IConstants;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.KeyValuePairComparator;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Address;
import com.liferay.portal.model.Country;
import com.liferay.portal.model.Phone;
import com.liferay.portal.model.User;
import com.liferay.portal.service.CountryServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.webserver.WebServerServletTokenUtil;


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
		Address address = LocationLocalServiceUtil.getLocation(user);
		
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
		long userCountryId = getUserCountryId();
		return 10.0d;
	}
	
	public long getUserCountryId() {
		long countryId = 108l;
		try {
			User user = UserLocalServiceUtil.fetchUser(getUserId());
			
			Address address = LocationLocalServiceUtil.getLocation(user);
			
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
		long divorcedStatus = BridgeServiceUtil.getListTypeId(IConstants.LIST_MARITAL_STATUS, "divorced");
		long widowStatus = BridgeServiceUtil.getListTypeId(IConstants.LIST_MARITAL_STATUS, "widow");
		
		if (isSingle() && !isAllowNonSingleProposals()) {
			sb.append(singleStatus);
		}
		
		if (isSingle() && isAllowNonSingleProposals()) {
			sb.append(singleStatus);
			sb.append(StringPool.COMMA);
			sb.append(divorcedStatus);
			sb.append(StringPool.COMMA);
			sb.append(widowStatus);
		}
		
		if (!isSingle()) {
			if (isBride()) {
				sb.append(marriedStatus);
				sb.append(StringPool.COMMA);				
			}
			sb.append(divorcedStatus);
			sb.append(StringPool.COMMA);
			sb.append(widowStatus);
		}		
		
		return sb.toString();
	}
	
	public boolean isFirst() {
		List<Profile> profiles = ProfileLocalServiceUtil.getProfilesForUser(getUserId());
		
		return (Validator.isNotNull(profiles) && (profiles.size() == 1));
	}
	
	public Map<Long, String> getLanguagesSpoken() {
		Map<Long, String> items = new HashMap<Long, String>();
	
		List<Long> countryIds = new ArrayList<Long>();
		countryIds.add(getCountryOfBirth());
		
		if (!countryIds.contains(getResidingCountry())) {
			countryIds.add(getResidingCountry());
		}
		
		countryIds.add(999l);
		
		for (Long countryId: countryIds) {
			List<MyLanguage> myLanguages = MyLanguageLocalServiceUtil.getLanguagesSpoken(countryId);
			
			for (MyLanguage myLanguage: myLanguages) {
				long key = myLanguage.getLanguageId();
				String value = myLanguage.getLanguage();
				
				items.put(key, value);
			}
		}
		
		items.remove(1000l);
		return items;
	}
	
	public List<KeyValuePair> getLanguagesSpokenAsList() {
		List<KeyValuePair> items = new ArrayList<KeyValuePair>();
		
		Map<Long, String> languages = getLanguagesSpoken();
		Iterator<Long> itr = languages.keySet().iterator();
		
		while (itr.hasNext()) {
			long key = itr.next();
			String value = languages.get(key);
			
			items.add(new KeyValuePair(String.valueOf(key), value));
		}
		
		return ListUtil.sort(items, new KeyValuePairComparator(false, true));
	}
	
	public List<KeyValuePair> getLanguagesSpokenForFilter() {
		
		List<KeyValuePair> results = new ArrayList<KeyValuePair>();
		
		List<KeyValuePair> original = getLanguagesSpokenAsList();
		List<KeyValuePair> actuals = FilterUtil.getLanguagesSpoken(isBride());
		
		for (KeyValuePair kvPair1: actuals) {
			String key1 = kvPair1.getKey();
			for (KeyValuePair kvPair2: original) {
				String key2 = kvPair2.getKey();
				if (key2.equalsIgnoreCase(key1)) {
					StringBuilder sb = new StringBuilder();
					sb.append(kvPair2.getValue());
					sb.append(StringPool.SPACE);
					sb.append(StringPool.OPEN_PARENTHESIS);
					sb.append(kvPair1.getValue());
					sb.append(StringPool.CLOSE_PARENTHESIS);
					results.add(new KeyValuePair(key1, sb.toString()));
					break;
				}
			}
		}
		
		return ListUtil.sort(results, new KeyValuePairComparator(false, true));
	}
	
	public List<KeyValuePair> getLanguagesSpokenLeft() {
		List<KeyValuePair> itemsOnLeft = getLanguagesSpokenForFilter();
		return ListUtil.remove(itemsOnLeft, getLanguagesSpokenRight());
	}
	
	public List<KeyValuePair> getLanguagesSpokenRight() {
		
		List<KeyValuePair> itemsOnRight = new ArrayList<KeyValuePair>();
		
		MatchCriteria matchCriteria = getMatchCriteria();
		
		String motherTongueCSV = matchCriteria.getMotherTongue();
		
		if (Validator.isNotNull(motherTongueCSV)) {
			String[] parts = motherTongueCSV.split(StringPool.COMMA);
			
			for (int i=0; i<parts.length; i++) {
				
				String key = parts[i];
				
				MyLanguage myLanguage = null;
				try {
					myLanguage = MyLanguageLocalServiceUtil.fetchMyLanguage(Long.valueOf(key));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (SystemException e) {
					e.printStackTrace();
				}
				
				if (Validator.isNull(myLanguage)) continue;
				
				String value = myLanguage.getLanguage();
				itemsOnRight.add(new KeyValuePair(key, value));
			}
		}
		
		return itemsOnRight;
	}
	
	public MatchCriteria getMatchCriteria() {
		MatchCriteria matchCriteria = null;
		try {
			matchCriteria = MatchCriteriaLocalServiceUtil.fetchMatchCriteria(getProfileId());
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return matchCriteria;
	}
	
	public boolean hasChildren() {
		return (getSons() > 0 || getDaughters() > 0);
	}
	
	public List<String> getIDDCodes() {
		List<String> iddCodes = new ArrayList<String>();
		
		long residingCountryId = getResidingCountry();
		
		List<Long> countryIds = new ArrayList<Long>();
		countryIds.add(residingCountryId);
		
		if (!countryIds.contains(getUserCountryId())) {
			countryIds.add(getUserCountryId());
		}
		
		for (Long countryId: countryIds) {
			try {
				Country country = CountryServiceUtil.fetchCountry(countryId);
				
				if (Validator.isNull(country)) continue;
					
				iddCodes.add(country.getIdd());
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		
		return iddCodes;
	}
	
	public String getPhotoURL(ThemeDisplay themeDisplay, long imageId) {
		
		String photoURL = StringPool.BLANK;
				
		if (imageId > 0l) {
			StringBuilder sb = new StringBuilder()
				.append(themeDisplay.getPathImage())
				.append("/photo?img_id=")
				.append(imageId)
				.append("&t=")
				.append(WebServerServletTokenUtil.getToken(imageId));			
			
			photoURL = sb.toString();
		}
		
		return photoURL;
	}	
	
	public String getThumbnailURL(ThemeDisplay themeDisplay, long thumbnailId) {
		
		String thumbnailURL = 
				themeDisplay.getPathThemeImages() + "/inikah/" + 
				(isBride()? "bride.png" : "groom.png");
				
		if (thumbnailId > 0l) {
			StringBuilder sb = new StringBuilder()
				.append(themeDisplay.getPathImage())
				.append("/photo?img_id=")
				.append(thumbnailId)
				.append("&t=")
				.append(WebServerServletTokenUtil.getToken(thumbnailId));			
			
			thumbnailURL = sb.toString();
		}
		
		return thumbnailURL;
	}
	
	public String getPortraitURL(ThemeDisplay themeDisplay) {
		
		String portraitURL = 
				themeDisplay.getPathThemeImages() + "/inikah/" + 
				(isBride()? "bride.png" : "groom.png");
				
		if (getPortraitId() > 0l) {
			StringBuilder sb = new StringBuilder()
				.append(themeDisplay.getPathImage())
				.append("/photo?img_id=")
				.append(getPortraitId())
				.append("&t=")
				.append(WebServerServletTokenUtil.getToken(getPortraitId()));			
			
			portraitURL = sb.toString();
		}
		
		return portraitURL;
	}	
	
	public List<Photo> getPhotos() {
		
		List<Photo> photos = new ArrayList<Photo>();
		
		List<Photo> _photos = PhotoLocalServiceUtil.getPhotos(getProfileId());
		
		int cnt = 0;
		for (Photo photo: _photos) {
			photos.add(photo);
			cnt++;
			if (cnt == 4) break; 
		}
		
		if (cnt < 4) {
			for (int i=0;i<(4-cnt); i++) {
				photos.add(new PhotoImpl());
			}
		}
		
		return photos;
	}
	
	public List<Photo> getApprovedPhotos() {
		
		List<Photo> photos = new ArrayList<Photo>();
		
		List<Photo> _photos = getPhotos();
		
		for (Photo photo: _photos) {
			if (photo.isApproved()) {
				photos.add(photo);
			}
		}
		
		return photos;
	}	
	
	public String getCurrencyCode() {
		
		String currencyCode = StringPool.BLANK;
		try {
			Currency currency = CurrencyLocalServiceUtil.fetchCurrency(getResidingCountry());
			
			if (Validator.isNotNull(currency)) {
				currencyCode = currency.getCurrencyCode();
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return currencyCode;
	}
	
	public String getCityText() {
		
		String cityText = StringPool.BLANK;
		try {
			Location city = LocationLocalServiceUtil.fetchLocation(getResidingCity());
			
			if (Validator.isNotNull(city)) {
				cityText = city.getName();
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return cityText;
	}
	
	public String getPhone(boolean primary) {
		String phoneNumber = StringPool.BLANK;
		
		Phone phone = BridgeLocalServiceUtil.getPhone(getProfileId(), Profile.class.getName(), primary);
		
		if (Validator.isNotNull(phone)) {
			phoneNumber = phone.getNumber();
		}
		
		return phoneNumber;
	}
	
	public boolean isPhoneVerified() {		
		return BridgeLocalServiceUtil.isPhoneVerified(getProfileId(), Profile.class.getName(), true);
	}
	
	public String getPhoneIdd(boolean primary) {
		String phoneIDD = StringPool.BLANK;
		
		Phone phone = BridgeLocalServiceUtil.getPhone(getProfileId(), Profile.class.getName(), primary);
		
		if (Validator.isNotNull(phone)) {
			phoneIDD = phone.getExtension();
		} else {
			try {
				Country country = CountryServiceUtil.fetchCountry(getResidingCountry());
				phoneIDD = country.getIdd();
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		
		return phoneIDD;		
	}
}