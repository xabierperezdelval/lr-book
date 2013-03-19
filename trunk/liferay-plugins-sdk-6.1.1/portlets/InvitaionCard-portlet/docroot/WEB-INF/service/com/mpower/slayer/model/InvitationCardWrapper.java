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

package com.mpower.slayer.model;

import com.liferay.portal.model.ModelWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link InvitationCard}.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       InvitationCard
 * @generated
 */
public class InvitationCardWrapper implements InvitationCard,
	ModelWrapper<InvitationCard> {
	public InvitationCardWrapper(InvitationCard invitationCard) {
		_invitationCard = invitationCard;
	}

	public Class<?> getModelClass() {
		return InvitationCard.class;
	}

	public String getModelClassName() {
		return InvitationCard.class.getName();
	}

	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("invitationId", getInvitationId());
		attributes.put("companyId", getCompanyId());
		attributes.put("groupId", getGroupId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("inviteeEmail", getInviteeEmail());
		attributes.put("status", getStatus());
		attributes.put("inviteeNewUserId", getInviteeNewUserId());

		return attributes;
	}

	public void setModelAttributes(Map<String, Object> attributes) {
		Long invitationId = (Long)attributes.get("invitationId");

		if (invitationId != null) {
			setInvitationId(invitationId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		String inviteeEmail = (String)attributes.get("inviteeEmail");

		if (inviteeEmail != null) {
			setInviteeEmail(inviteeEmail);
		}

		Integer status = (Integer)attributes.get("status");

		if (status != null) {
			setStatus(status);
		}

		Long inviteeNewUserId = (Long)attributes.get("inviteeNewUserId");

		if (inviteeNewUserId != null) {
			setInviteeNewUserId(inviteeNewUserId);
		}
	}

	/**
	* Returns the primary key of this invitation card.
	*
	* @return the primary key of this invitation card
	*/
	public long getPrimaryKey() {
		return _invitationCard.getPrimaryKey();
	}

	/**
	* Sets the primary key of this invitation card.
	*
	* @param primaryKey the primary key of this invitation card
	*/
	public void setPrimaryKey(long primaryKey) {
		_invitationCard.setPrimaryKey(primaryKey);
	}

	/**
	* Returns the invitation ID of this invitation card.
	*
	* @return the invitation ID of this invitation card
	*/
	public long getInvitationId() {
		return _invitationCard.getInvitationId();
	}

	/**
	* Sets the invitation ID of this invitation card.
	*
	* @param invitationId the invitation ID of this invitation card
	*/
	public void setInvitationId(long invitationId) {
		_invitationCard.setInvitationId(invitationId);
	}

	/**
	* Returns the company ID of this invitation card.
	*
	* @return the company ID of this invitation card
	*/
	public long getCompanyId() {
		return _invitationCard.getCompanyId();
	}

	/**
	* Sets the company ID of this invitation card.
	*
	* @param companyId the company ID of this invitation card
	*/
	public void setCompanyId(long companyId) {
		_invitationCard.setCompanyId(companyId);
	}

	/**
	* Returns the group ID of this invitation card.
	*
	* @return the group ID of this invitation card
	*/
	public long getGroupId() {
		return _invitationCard.getGroupId();
	}

	/**
	* Sets the group ID of this invitation card.
	*
	* @param groupId the group ID of this invitation card
	*/
	public void setGroupId(long groupId) {
		_invitationCard.setGroupId(groupId);
	}

	/**
	* Returns the user ID of this invitation card.
	*
	* @return the user ID of this invitation card
	*/
	public long getUserId() {
		return _invitationCard.getUserId();
	}

	/**
	* Sets the user ID of this invitation card.
	*
	* @param userId the user ID of this invitation card
	*/
	public void setUserId(long userId) {
		_invitationCard.setUserId(userId);
	}

	/**
	* Returns the user uuid of this invitation card.
	*
	* @return the user uuid of this invitation card
	* @throws SystemException if a system exception occurred
	*/
	public java.lang.String getUserUuid()
		throws com.liferay.portal.kernel.exception.SystemException {
		return _invitationCard.getUserUuid();
	}

	/**
	* Sets the user uuid of this invitation card.
	*
	* @param userUuid the user uuid of this invitation card
	*/
	public void setUserUuid(java.lang.String userUuid) {
		_invitationCard.setUserUuid(userUuid);
	}

	/**
	* Returns the user name of this invitation card.
	*
	* @return the user name of this invitation card
	*/
	public java.lang.String getUserName() {
		return _invitationCard.getUserName();
	}

	/**
	* Sets the user name of this invitation card.
	*
	* @param userName the user name of this invitation card
	*/
	public void setUserName(java.lang.String userName) {
		_invitationCard.setUserName(userName);
	}

	/**
	* Returns the create date of this invitation card.
	*
	* @return the create date of this invitation card
	*/
	public java.util.Date getCreateDate() {
		return _invitationCard.getCreateDate();
	}

	/**
	* Sets the create date of this invitation card.
	*
	* @param createDate the create date of this invitation card
	*/
	public void setCreateDate(java.util.Date createDate) {
		_invitationCard.setCreateDate(createDate);
	}

	/**
	* Returns the modified date of this invitation card.
	*
	* @return the modified date of this invitation card
	*/
	public java.util.Date getModifiedDate() {
		return _invitationCard.getModifiedDate();
	}

	/**
	* Sets the modified date of this invitation card.
	*
	* @param modifiedDate the modified date of this invitation card
	*/
	public void setModifiedDate(java.util.Date modifiedDate) {
		_invitationCard.setModifiedDate(modifiedDate);
	}

	/**
	* Returns the invitee email of this invitation card.
	*
	* @return the invitee email of this invitation card
	*/
	public java.lang.String getInviteeEmail() {
		return _invitationCard.getInviteeEmail();
	}

	/**
	* Sets the invitee email of this invitation card.
	*
	* @param inviteeEmail the invitee email of this invitation card
	*/
	public void setInviteeEmail(java.lang.String inviteeEmail) {
		_invitationCard.setInviteeEmail(inviteeEmail);
	}

	/**
	* Returns the status of this invitation card.
	*
	* @return the status of this invitation card
	*/
	public int getStatus() {
		return _invitationCard.getStatus();
	}

	/**
	* Sets the status of this invitation card.
	*
	* @param status the status of this invitation card
	*/
	public void setStatus(int status) {
		_invitationCard.setStatus(status);
	}

	/**
	* Returns the invitee new user ID of this invitation card.
	*
	* @return the invitee new user ID of this invitation card
	*/
	public long getInviteeNewUserId() {
		return _invitationCard.getInviteeNewUserId();
	}

	/**
	* Sets the invitee new user ID of this invitation card.
	*
	* @param inviteeNewUserId the invitee new user ID of this invitation card
	*/
	public void setInviteeNewUserId(long inviteeNewUserId) {
		_invitationCard.setInviteeNewUserId(inviteeNewUserId);
	}

	/**
	* Returns the invitee new user uuid of this invitation card.
	*
	* @return the invitee new user uuid of this invitation card
	* @throws SystemException if a system exception occurred
	*/
	public java.lang.String getInviteeNewUserUuid()
		throws com.liferay.portal.kernel.exception.SystemException {
		return _invitationCard.getInviteeNewUserUuid();
	}

	/**
	* Sets the invitee new user uuid of this invitation card.
	*
	* @param inviteeNewUserUuid the invitee new user uuid of this invitation card
	*/
	public void setInviteeNewUserUuid(java.lang.String inviteeNewUserUuid) {
		_invitationCard.setInviteeNewUserUuid(inviteeNewUserUuid);
	}

	public boolean isNew() {
		return _invitationCard.isNew();
	}

	public void setNew(boolean n) {
		_invitationCard.setNew(n);
	}

	public boolean isCachedModel() {
		return _invitationCard.isCachedModel();
	}

	public void setCachedModel(boolean cachedModel) {
		_invitationCard.setCachedModel(cachedModel);
	}

	public boolean isEscapedModel() {
		return _invitationCard.isEscapedModel();
	}

	public java.io.Serializable getPrimaryKeyObj() {
		return _invitationCard.getPrimaryKeyObj();
	}

	public void setPrimaryKeyObj(java.io.Serializable primaryKeyObj) {
		_invitationCard.setPrimaryKeyObj(primaryKeyObj);
	}

	public com.liferay.portlet.expando.model.ExpandoBridge getExpandoBridge() {
		return _invitationCard.getExpandoBridge();
	}

	public void setExpandoBridgeAttributes(
		com.liferay.portal.service.ServiceContext serviceContext) {
		_invitationCard.setExpandoBridgeAttributes(serviceContext);
	}

	@Override
	public java.lang.Object clone() {
		return new InvitationCardWrapper((InvitationCard)_invitationCard.clone());
	}

	public int compareTo(com.mpower.slayer.model.InvitationCard invitationCard) {
		return _invitationCard.compareTo(invitationCard);
	}

	@Override
	public int hashCode() {
		return _invitationCard.hashCode();
	}

	public com.liferay.portal.model.CacheModel<com.mpower.slayer.model.InvitationCard> toCacheModel() {
		return _invitationCard.toCacheModel();
	}

	public com.mpower.slayer.model.InvitationCard toEscapedModel() {
		return new InvitationCardWrapper(_invitationCard.toEscapedModel());
	}

	@Override
	public java.lang.String toString() {
		return _invitationCard.toString();
	}

	public java.lang.String toXmlString() {
		return _invitationCard.toXmlString();
	}

	public void persist()
		throws com.liferay.portal.kernel.exception.SystemException {
		_invitationCard.persist();
	}

	/**
	 * @deprecated Renamed to {@link #getWrappedModel}
	 */
	public InvitationCard getWrappedInvitationCard() {
		return _invitationCard;
	}

	public InvitationCard getWrappedModel() {
		return _invitationCard;
	}

	public void resetOriginalValues() {
		_invitationCard.resetOriginalValues();
	}

	private InvitationCard _invitationCard;
}