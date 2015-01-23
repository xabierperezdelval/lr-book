/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.slayer.model.impl;

import java.util.Calendar;
/**
 * The extended model implementation for the Profile service. Represents a row in the &quot;inikah_Profile&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * Helper methods and all application logic should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.slayer.model.Profile} interface.
 * </p>
 *
 * @author Ahmed Hasan
 */
public class ProfileImpl extends ProfileBaseImpl {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. All methods that expect a profile model instance should use the {@link com.slayer.model.Profile} interface instead.
	 */
	public ProfileImpl() {
	}
	
    static char[] letters = {
    	'1', '2', '3', '4', '5', '6', '7', '8',
    	'9', 'A', 'B', 'C', 'D', 'E', 'F', 'G',  
    	'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 
    	'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'		
    };
    
	public String getCode() {
	
		StringBuilder sb = new StringBuilder();
		
		// gender part
		sb.append(isBride()?"B":"G");
		
		// year part
		int eraBegin = 2015;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(getCreateDate());

		int currentYear = cal.get(Calendar.YEAR);
		
		int offset = currentYear - eraBegin;
		
		char yearPart = (char) (offset + 65);
		if (offset > 25) {
			// handle differently
		}
		sb.append(yearPart);
		
		// week part
		sb.append(String.format("%02d", cal.get(Calendar.WEEK_OF_YEAR)));
		
		int size = letters.length;
		int value = (int) (getProfileId() % (size * size));
		sb.append((char)(letters[value / size]));
		sb.append((char)(letters[value % size]));
		
		return sb.toString();
	}	
}