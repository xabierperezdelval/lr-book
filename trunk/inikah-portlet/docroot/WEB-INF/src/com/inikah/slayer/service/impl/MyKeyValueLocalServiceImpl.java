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

import java.util.ArrayList;
import java.util.List;

import com.inikah.slayer.model.MyKeyValue;
import com.inikah.slayer.service.base.MyKeyValueLocalServiceBaseImpl;
import com.liferay.portal.kernel.util.KeyValuePair;

/**
 * The implementation of the my key value local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.inikah.slayer.service.MyKeyValueLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.inikah.slayer.service.base.MyKeyValueLocalServiceBaseImpl
 * @see com.inikah.slayer.service.MyKeyValueLocalServiceUtil
 */
public class MyKeyValueLocalServiceImpl extends MyKeyValueLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.inikah.slayer.service.MyKeyValueLocalServiceUtil} to access the my key value local service.
	 */
	
	public List<KeyValuePair> getLanguagesSpokenForFilter(boolean bride) {
		List<MyKeyValue> items = myKeyValueFinder.findMotherTongue(bride);
		
		List<KeyValuePair> kvPairs = new ArrayList<KeyValuePair>();
		for (MyKeyValue myKeyValue: items) {
			kvPairs.add(new KeyValuePair(String.valueOf(myKeyValue.getMyKey()), myKeyValue.getMyValue()));
		}
		
		return kvPairs;
	}
}