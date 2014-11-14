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

import java.util.List;

import com.inikah.slayer.model.MyLanguage;
import com.inikah.slayer.service.base.MyLanguageLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.SystemException;

/**
 * The implementation of the my language local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.inikah.slayer.service.MyLanguageLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.inikah.slayer.service.base.MyLanguageLocalServiceBaseImpl
 * @see com.inikah.slayer.service.MyLanguageLocalServiceUtil
 */
public class MyLanguageLocalServiceImpl extends MyLanguageLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.inikah.slayer.service.MyLanguageLocalServiceUtil} to access the my language local service.
	 */
	
	public List<MyLanguage> getLanguagesSpoken(long countryId) {
		
		List<MyLanguage> languages = null;
		
		try {
			languages = myLanguagePersistence.findByCountryId(countryId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return languages;
	}
}