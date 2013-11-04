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

import com.inikah.slayer.model.Plan;
import com.inikah.slayer.service.base.PlanLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.SystemException;

/**
 * The implementation of the plan local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.inikah.slayer.service.PlanLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.inikah.slayer.service.base.PlanLocalServiceBaseImpl
 * @see com.inikah.slayer.service.PlanLocalServiceUtil
 */
public class PlanLocalServiceImpl extends PlanLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.inikah.slayer.service.PlanLocalServiceUtil} to access the plan local service.
	 */
	
	public List<Plan> getPlans(long companyId) {
		
		List<Plan> plans = null;
		try {
			plans = planPersistence.findByCompanyId(companyId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return plans;
	}
}