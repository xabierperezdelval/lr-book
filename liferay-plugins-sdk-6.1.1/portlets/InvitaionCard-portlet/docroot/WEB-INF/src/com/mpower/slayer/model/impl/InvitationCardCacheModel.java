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

package com.mpower.slayer.model.impl;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;

import com.mpower.slayer.model.InvitationCard;

import java.io.Serializable;

import java.util.Date;

/**
 * The cache model class for representing InvitationCard in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @see InvitationCard
 * @generated
 */
public class InvitationCardCacheModel implements CacheModel<InvitationCard>,
	Serializable {
	@Override
	public String toString() {
		StringBundler sb = new StringBundler(21);

		sb.append("{invitationId=");
		sb.append(invitationId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", groupId=");
		sb.append(groupId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", userName=");
		sb.append(userName);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", inviteeEmail=");
		sb.append(inviteeEmail);
		sb.append(", status=");
		sb.append(status);
		sb.append(", inviteeNewUserId=");
		sb.append(inviteeNewUserId);
		sb.append("}");

		return sb.toString();
	}

	public InvitationCard toEntityModel() {
		InvitationCardImpl invitationCardImpl = new InvitationCardImpl();

		invitationCardImpl.setInvitationId(invitationId);
		invitationCardImpl.setCompanyId(companyId);
		invitationCardImpl.setGroupId(groupId);
		invitationCardImpl.setUserId(userId);

		if (userName == null) {
			invitationCardImpl.setUserName(StringPool.BLANK);
		}
		else {
			invitationCardImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			invitationCardImpl.setCreateDate(null);
		}
		else {
			invitationCardImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			invitationCardImpl.setModifiedDate(null);
		}
		else {
			invitationCardImpl.setModifiedDate(new Date(modifiedDate));
		}

		if (inviteeEmail == null) {
			invitationCardImpl.setInviteeEmail(StringPool.BLANK);
		}
		else {
			invitationCardImpl.setInviteeEmail(inviteeEmail);
		}

		invitationCardImpl.setStatus(status);
		invitationCardImpl.setInviteeNewUserId(inviteeNewUserId);

		invitationCardImpl.resetOriginalValues();

		return invitationCardImpl;
	}

	public long invitationId;
	public long companyId;
	public long groupId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public String inviteeEmail;
	public int status;
	public long inviteeNewUserId;
}