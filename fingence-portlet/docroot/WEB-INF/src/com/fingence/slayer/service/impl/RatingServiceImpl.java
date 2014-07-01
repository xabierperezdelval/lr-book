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

package com.fingence.slayer.service.impl;

import java.util.List;

import com.fingence.IConstants;
import com.fingence.slayer.model.Rating;
import com.fingence.slayer.service.base.RatingServiceBaseImpl;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

/**
 * The implementation of the rating remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.fingence.slayer.service.RatingService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.fingence.slayer.service.base.RatingServiceBaseImpl
 * @see com.fingence.slayer.service.RatingServiceUtil
 */
public class RatingServiceImpl extends RatingServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.fingence.slayer.service.RatingServiceUtil} to access the rating remote service.
	 */
	
	public String getRatings(String description) {
				
		List<Rating> ratings = null;
		try {
			ratings = ratingPersistence.findByDescription(description);			
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(ratings) || ratings.size() == 0) return IConstants.UN_SPECIFIED;
		
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		
		for (Rating rating: ratings) {
			String sandp = rating.getSandp();
			String moodys = rating.getMoodys();
			
			if (Validator.isNotNull(moodys)) {
				if (Validator.isNull(sb1.toString())) {
					sb1.append("Moody's:");
				} else {
					sb1.append(StringPool.SLASH);
				}
				sb1.append(moodys);
			}
			
			if (Validator.isNotNull(sandp)) {
				if (Validator.isNull(sb2.toString())) {
					sb2.append("S&P:");
				} else {
					sb2.append(StringPool.SLASH);
				}
				sb2.append(sandp);
			}			
		}
		
		return (Validator.isNotNull(sb1.toString())? sb1.toString() : StringPool.BLANK) 
			 + (Validator.isNotNull(sb1.toString())? StringPool.SEMICOLON + StringPool.SPACE : StringPool.BLANK)	
			 + (Validator.isNotNull(sb2.toString())? sb2.toString() : StringPool.BLANK);
	}
}