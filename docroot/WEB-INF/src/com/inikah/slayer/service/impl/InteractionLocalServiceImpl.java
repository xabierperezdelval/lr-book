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
import java.util.Date;
import java.util.List;

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
	
	/**
	 * 
	 */
	public Interaction setOperation(long sourceId, long targetId, int operation, long parentId) {
		
		Interaction interaction = null;
		try {
			interaction = interactionPersistence.findBySourceId_TargetId_Operation(sourceId, targetId, operation);
		} catch (NoSuchInteractionException e) {
			long interactionId = 0l;
			try {
				interactionId = counterLocalService.increment("interaction-id");
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
			
			interaction = createInteraction(interactionId);
			interaction.setSourceId(sourceId);
			interaction.setTargetId(targetId);
			interaction.setOperation(operation);
			interaction.setPerformedOn(new Date());
			interaction.setParentId(parentId);
			
			try {
				interactionLocalService.addInteraction(interaction);
			} catch (SystemException e1) {
				e1.printStackTrace();
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return interaction;
	}
	
	/**
	 * 
	 * @param sourceId
	 * @param targetId
	 * @return
	 */
	public List<Interaction> getInteractionsInitiatedByMe(long sourceId, long targetId) {
		
		List<Interaction> interactions = new ArrayList<Interaction>();
		
		try {
			interactions = interactionPersistence.findBySourceId_TargetId(sourceId, targetId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return interactions;
	}
	
	/**
	 * 
	 * @param sourceId
	 * @param targetId
	 * @return
	 */
	public List<Interaction> getInteractionsInitiatedByOthers(long sourceId, long targetId) {
		
		List<Interaction> interactions = new ArrayList<Interaction>();
		
		try {
			interactions = interactionPersistence.findBySourceId_TargetId(targetId, sourceId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return interactions;
	}
	
	/**
	 * 
	 * @param profileId
	 * @return
	 */
	public List<Interaction> getInteractionsInitiatedByMe(long sourceId) {
		
		List<Interaction> interactions = new ArrayList<Interaction>();
		
		try {
			interactions = interactionPersistence.findBySourceId(sourceId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return interactions;
	}
	
	/**
	 * 
	 * @param profileId
	 * @return
	 */
	public List<Interaction> getInteractionsInitiatedByOthers(long targetId) {
		
		List<Interaction> interactions = new ArrayList<Interaction>();
		
		try {
			interactions = interactionPersistence.findByTargetId(targetId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return interactions;
	}
	
	public List<Interaction> getInteractionsInitiatedByMe(long sourceId, int operation) {
		
		List<Interaction> interactions = new ArrayList<Interaction>();
		
		try {
			interactions = interactionPersistence.findBySourceId_Operation(sourceId, operation);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return interactions;
	}
	
	public List<Interaction> getInteractionsInitiatedByOthers(long targetId, int operation) {
		
		List<Interaction> interactions = new ArrayList<Interaction>();
		
		try {
			interactions = interactionPersistence.findByTargetId_Operation(targetId, operation);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return interactions;
	}	
	

	public List<Long> getTargetIds(long sourceId, int operation) {
		
		List<Long> interactionIds = new ArrayList<Long>();
		
		List<Interaction> interactions = getInteractionsInitiatedByMe(sourceId, operation);
		
		for (Interaction interaction: interactions) {
			interactionIds.add(interaction.getInteractionId());
		}
		
		return interactionIds;
	}
}