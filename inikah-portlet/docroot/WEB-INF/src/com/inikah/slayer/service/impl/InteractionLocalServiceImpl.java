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

import java.util.Date;

import com.inikah.slayer.NoSuchInteractionException;
import com.inikah.slayer.model.Interaction;
import com.inikah.slayer.service.base.InteractionLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.SystemException;

/**
 * The implementation of the interaction local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.inikah.slayer.service.InteractionLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.inikah.slayer.service.base.InteractionLocalServiceBaseImpl
 * @see com.inikah.slayer.service.InteractionLocalServiceUtil
 */
public class InteractionLocalServiceImpl extends InteractionLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.inikah.slayer.service.InteractionLocalServiceUtil} to access the interaction local service.
	 */
	
	public void setOperation(long sourceId, long targetId, int operation, boolean duplicate) {
		
		System.out.println("setViewed....");
		
		try {
			interactionPersistence.findBySourceId_TargetId_Operation(sourceId, targetId, operation);
		} catch (NoSuchInteractionException e) {
			long interactionId = 0l;
			try {
				interactionId = counterLocalService.increment("interaction-id");
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
			
			Interaction interaction = createInteraction(interactionId);
			interaction.setSourceId(sourceId);
			interaction.setTargetId(targetId);
			interaction.setOperation(operation);
			interaction.setPerformedOn(new Date());
			interaction.setDuplicate(duplicate);
			
			try {
				interactionLocalService.addInteraction(interaction);
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}	
}