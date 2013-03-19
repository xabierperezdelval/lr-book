/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.mpower.slayer.service.persistence;

import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.kernel.util.ReferenceRegistry;

/**
 * @author Brian Wing Shun Chan
 */
public class InvitationCardFinderUtil {
	public static java.util.List<com.mpower.slayer.model.UserRank> findCountBy()
		throws com.liferay.portal.kernel.exception.SystemException {
		return getFinder().findCountBy();
	}

	public static InvitationCardFinder getFinder() {
		if (_finder == null) {
			_finder = (InvitationCardFinder)PortletBeanLocatorUtil.locate(com.mpower.slayer.service.ClpSerializer.getServletContextName(),
					InvitationCardFinder.class.getName());

			ReferenceRegistry.registerReference(InvitationCardFinderUtil.class,
				"_finder");
		}

		return _finder;
	}

	public void setFinder(InvitationCardFinder finder) {
		_finder = finder;

		ReferenceRegistry.registerReference(InvitationCardFinderUtil.class,
			"_finder");
	}

	private static InvitationCardFinder _finder;
}