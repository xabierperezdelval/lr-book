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

import com.inikah.slayer.service.BridgeServiceUtil;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.rmi.RemoteException;

/**
 * Provides the SOAP utility for the
 * {@link com.inikah.slayer.service.BridgeServiceUtil} service utility. The
 * static methods of this class calls the same methods of the service utility.
 * However, the signatures are different because it is difficult for SOAP to
 * support certain types.
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
 * @see BridgeServiceHttp
 * @see com.inikah.slayer.service.BridgeServiceUtil
 * @generated
 */
public class BridgeServiceSoap {
	public static com.liferay.portal.model.Country getCountry(
		java.lang.String isoCode) throws RemoteException {
		try {
			com.liferay.portal.model.Country returnValue = BridgeServiceUtil.getCountry(isoCode);

			return returnValue;
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static com.liferay.portal.model.Country[] getCountries()
		throws RemoteException {
		try {
			java.util.List<com.liferay.portal.model.Country> returnValue = BridgeServiceUtil.getCountries();

			return returnValue.toArray(new com.liferay.portal.model.Country[returnValue.size()]);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static void createListItem(java.lang.String type,
		java.lang.String value) throws RemoteException {
		try {
			BridgeServiceUtil.createListItem(type, value);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static int getListTypeId(java.lang.String listName,
		java.lang.String suffix) throws RemoteException {
		try {
			int returnValue = BridgeServiceUtil.getListTypeId(listName, suffix);

			return returnValue;
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static com.liferay.portal.model.ListType[] getList(
		java.lang.String listName) throws RemoteException {
		try {
			java.util.List<com.liferay.portal.model.ListType> returnValue = BridgeServiceUtil.getList(listName);

			return returnValue.toArray(new com.liferay.portal.model.ListType[returnValue.size()]);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static void sendVerificationCode(long phoneId)
		throws RemoteException {
		try {
			BridgeServiceUtil.sendVerificationCode(phoneId);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static boolean verifyPhone(long phoneId,
		java.lang.String verificationCode) throws RemoteException {
		try {
			boolean returnValue = BridgeServiceUtil.verifyPhone(phoneId,
					verificationCode);

			return returnValue;
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static void addPhone(long userId, java.lang.String className,
		long classPK, java.lang.String number, java.lang.String extension,
		boolean primary) throws RemoteException {
		try {
			BridgeServiceUtil.addPhone(userId, className, classPK, number,
				extension, primary);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static java.lang.String getIdd(long countryId)
		throws RemoteException {
		try {
			java.lang.String returnValue = BridgeServiceUtil.getIdd(countryId);

			return returnValue;
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	private static Log _log = LogFactoryUtil.getLog(BridgeServiceSoap.class);
}