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

import com.fingence.slayer.service.base.MyResultServiceBaseImpl;

/**
 * The implementation of the my result remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.fingence.slayer.service.MyResultService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.fingence.slayer.service.base.MyResultServiceBaseImpl
 * @see com.fingence.slayer.service.MyResultServiceUtil
 */
public class MyResultServiceImpl extends MyResultServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.fingence.slayer.service.MyResultServiceUtil} to access the my result remote service.
	 */
	
	public double getNetWorth(long portfolioId) {
		double networth = 500.00d;
		
		//List<Result> results = myResultFinder.getResults(portfolioId);
		
		//for (Result result: results) {
		//	networth += result.getCurrentMarketValue();
		//}
		
		return networth;
	}	
}