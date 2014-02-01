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

package com.inikah.slayer.service.http;

import com.inikah.slayer.service.EarningServiceUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.rmi.RemoteException;

/**
 * Provides the SOAP utility for the
 * {@link com.inikah.slayer.service.EarningServiceUtil} service utility. The
 * static methods of this class calls the same methods of the service utility.
 * However, the signatures are different because it is difficult for SOAP to
 * support certain types.
 *
 * <p>
 * ServiceBuilder follows certain rules in translating the methods. For example,
 * if the method in the service utility returns a {@link java.util.List}, that
 * is translated to an array of {@link com.inikah.slayer.model.EarningSoap}.
 * If the method in the service utility returns a
 * {@link com.inikah.slayer.model.Earning}, that is translated to a
 * {@link com.inikah.slayer.model.EarningSoap}. Methods that SOAP cannot
 * safely wire are skipped.
 * </p>
 *
 * <p>
 * The benefits of using the SOAP utility is that it is cross platform
 * compatible. SOAP allows different languages like Java, .NET, C++, PHP, and
 * even Perl, to call the generated services. One drawback of SOAP is that it is
 * slow because it needs to serialize all calls into a text format (XML).
 * </p>
 *
 * <p>
 * You can see a list of services at http://localhost:8080/api/axis. Set the
 * property <b>axis.servlet.hosts.allowed</b> in portal.properties to configure
 * security.
 * </p>
 *
 * <p>
 * The SOAP utility is only generated for remote services.
 * </p>
 *
 * @author Ahmed Hasan
 * @see EarningServiceHttp
 * @see com.inikah.slayer.model.EarningSoap
 * @see com.inikah.slayer.service.EarningServiceUtil
 * @generated
 */
public class EarningServiceSoap {
	public static double getEarnings(long userId) throws RemoteException {
		try {
			double returnValue = EarningServiceUtil.getEarnings(userId);

			return returnValue;
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static java.lang.String getEarningsText(long userId)
		throws RemoteException {
		try {
			java.lang.String returnValue = EarningServiceUtil.getEarningsText(userId);

			return returnValue;
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static com.inikah.slayer.model.EarningSoap debit(long userId,
		double amount, java.lang.String details) throws RemoteException {
		try {
			com.inikah.slayer.model.Earning returnValue = EarningServiceUtil.debit(userId,
					amount, details);

			return com.inikah.slayer.model.EarningSoap.toSoapModel(returnValue);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static com.inikah.slayer.model.EarningSoap credit(long userId,
		double amount, java.lang.String details) throws RemoteException {
		try {
			com.inikah.slayer.model.Earning returnValue = EarningServiceUtil.credit(userId,
					amount, details);

			return com.inikah.slayer.model.EarningSoap.toSoapModel(returnValue);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	private static Log _log = LogFactoryUtil.getLog(EarningServiceSoap.class);
}