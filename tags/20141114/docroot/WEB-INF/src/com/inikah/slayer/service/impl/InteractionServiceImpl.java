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

import com.inikah.slayer.model.Interaction;
import com.inikah.slayer.service.base.InteractionServiceBaseImpl;
import com.inikah.util.IConstants;

/**
 * The implementation of the interaction remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.inikah.slayer.service.InteractionService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.inikah.slayer.service.base.InteractionServiceBaseImpl
 * @see com.inikah.slayer.service.InteractionServiceUtil
 */
public class InteractionServiceImpl extends InteractionServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.inikah.slayer.service.InteractionServiceUtil} to access the interaction remote service.
	 */
	
	public void setViewed(long sourceId, long targetId) {
		interactionLocalService.setOperation(sourceId, targetId, IConstants.INT_ACTION_VIEWED, 0l);
	}
	
	public void setBlocked(long sourceId, long targetId) {
		Interaction parent = interactionLocalService.setOperation(sourceId, targetId, IConstants.INT_ACTION_BLOCKED, 0l);
		interactionLocalService.setOperation(targetId, sourceId, IConstants.INT_ACTION_BLOCKED, parent.getInteractionId());
	}
}