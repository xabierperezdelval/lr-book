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

import com.inikah.slayer.model.Relative;
import com.inikah.slayer.service.BridgeLocalServiceUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Phone;

/**
 * The extended model implementation for the Relative service. Represents a row in the &quot;inikah_Relative&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * Helper methods and all application logic should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.inikah.slayer.model.Relative} interface.
 * </p>
 *
 * @author Ahmed Hasan
 */
public class RelativeImpl extends RelativeBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. All methods that expect a relative model instance should use the {@link com.inikah.slayer.model.Relative} interface instead.
	 */
	public RelativeImpl() {
	}
	
	public String getNumber() {
		
		String number = StringPool.BLANK;
		
		Phone phone = BridgeLocalServiceUtil.getPhone(getRelativeId(), Relative.class.getName(), true);
		
		if (Validator.isNotNull(phone)) {
			number = phone.getNumber();
		}
		
		return number;
	}
	
	public String getIdd(String defaultValue) {
		String idd = StringPool.PLUS + defaultValue;
		
		Phone phone = BridgeLocalServiceUtil.getPhone(getRelativeId(), Relative.class.getName(), true);
		
		if (Validator.isNotNull(phone)) {
			idd = StringPool.PLUS + phone.getExtension();
		}
		
		return idd;
	}
}