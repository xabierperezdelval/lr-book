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

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portal.util.PortalUtil;

import com.mpower.slayer.service.InvitationCardLocalServiceUtil;

import java.io.Serializable;

import java.lang.reflect.Proxy;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Brian Wing Shun Chan
 */
public class InvitationCardClp extends BaseModelImpl<InvitationCard>
	implements InvitationCard {
	public InvitationCardClp() {
	}

	public Class<?> getModelClass() {
		return InvitationCard.class;
	}

	public String getModelClassName() {
		return InvitationCard.class.getName();
	}

	public long getPrimaryKey() {
		return _invitationId;
	}

	public void setPrimaryKey(long primaryKey) {
		setInvitationId(primaryKey);
	}

	public Serializable getPrimaryKeyObj() {
		return new Long(_invitationId);
	}

	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
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

	@Override
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

	public long getInvitationId() {
		return _invitationId;
	}

	public void setInvitationId(long invitationId) {
		_invitationId = invitationId;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	public long getGroupId() {
		return _groupId;
	}

	public void setGroupId(long groupId) {
		_groupId = groupId;
	}

	public long getUserId() {
		return _userId;
	}

	public void setUserId(long userId) {
		_userId = userId;
	}

	public String getUserUuid() throws SystemException {
		return PortalUtil.getUserValue(getUserId(), "uuid", _userUuid);
	}

	public void setUserUuid(String userUuid) {
		_userUuid = userUuid;
	}

	public String getUserName() {
		return _userName;
	}

	public void setUserName(String userName) {
		_userName = userName;
	}

	public Date getCreateDate() {
		return _createDate;
	}

	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = modifiedDate;
	}

	public String getInviteeEmail() {
		return _inviteeEmail;
	}

	public void setInviteeEmail(String inviteeEmail) {
		_inviteeEmail = inviteeEmail;
	}

	public int getStatus() {
		return _status;
	}

	public void setStatus(int status) {
		_status = status;
	}

	public long getInviteeNewUserId() {
		return _inviteeNewUserId;
	}

	public void setInviteeNewUserId(long inviteeNewUserId) {
		_inviteeNewUserId = inviteeNewUserId;
	}

	public String getInviteeNewUserUuid() throws SystemException {
		return PortalUtil.getUserValue(getInviteeNewUserId(), "uuid",
			_inviteeNewUserUuid);
	}

	public void setInviteeNewUserUuid(String inviteeNewUserUuid) {
		_inviteeNewUserUuid = inviteeNewUserUuid;
	}

	public BaseModel<?> getInvitationCardRemoteModel() {
		return _invitationCardRemoteModel;
	}

	public void setInvitationCardRemoteModel(
		BaseModel<?> invitationCardRemoteModel) {
		_invitationCardRemoteModel = invitationCardRemoteModel;
	}

	public void persist() throws SystemException {
		if (this.isNew()) {
			InvitationCardLocalServiceUtil.addInvitationCard(this);
		}
		else {
			InvitationCardLocalServiceUtil.updateInvitationCard(this);
		}
	}

	@Override
	public InvitationCard toEscapedModel() {
		return (InvitationCard)Proxy.newProxyInstance(InvitationCard.class.getClassLoader(),
			new Class[] { InvitationCard.class },
			new AutoEscapeBeanHandler(this));
	}

	@Override
	public Object clone() {
		InvitationCardClp clone = new InvitationCardClp();

		clone.setInvitationId(getInvitationId());
		clone.setCompanyId(getCompanyId());
		clone.setGroupId(getGroupId());
		clone.setUserId(getUserId());
		clone.setUserName(getUserName());
		clone.setCreateDate(getCreateDate());
		clone.setModifiedDate(getModifiedDate());
		clone.setInviteeEmail(getInviteeEmail());
		clone.setStatus(getStatus());
		clone.setInviteeNewUserId(getInviteeNewUserId());

		return clone;
	}

	public int compareTo(InvitationCard invitationCard) {
		long primaryKey = invitationCard.getPrimaryKey();

		if (getPrimaryKey() < primaryKey) {
			return -1;
		}
		else if (getPrimaryKey() > primaryKey) {
			return 1;
		}
		else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		InvitationCardClp invitationCard = null;

		try {
			invitationCard = (InvitationCardClp)obj;
		}
		catch (ClassCastException cce) {
			return false;
		}

		long primaryKey = invitationCard.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(21);

		sb.append("{invitationId=");
		sb.append(getInvitationId());
		sb.append(", companyId=");
		sb.append(getCompanyId());
		sb.append(", groupId=");
		sb.append(getGroupId());
		sb.append(", userId=");
		sb.append(getUserId());
		sb.append(", userName=");
		sb.append(getUserName());
		sb.append(", createDate=");
		sb.append(getCreateDate());
		sb.append(", modifiedDate=");
		sb.append(getModifiedDate());
		sb.append(", inviteeEmail=");
		sb.append(getInviteeEmail());
		sb.append(", status=");
		sb.append(getStatus());
		sb.append(", inviteeNewUserId=");
		sb.append(getInviteeNewUserId());
		sb.append("}");

		return sb.toString();
	}

	public String toXmlString() {
		StringBundler sb = new StringBundler(34);

		sb.append("<model><model-name>");
		sb.append("com.mpower.slayer.model.InvitationCard");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>invitationId</column-name><column-value><![CDATA[");
		sb.append(getInvitationId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>companyId</column-name><column-value><![CDATA[");
		sb.append(getCompanyId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>groupId</column-name><column-value><![CDATA[");
		sb.append(getGroupId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userId</column-name><column-value><![CDATA[");
		sb.append(getUserId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userName</column-name><column-value><![CDATA[");
		sb.append(getUserName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>createDate</column-name><column-value><![CDATA[");
		sb.append(getCreateDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>modifiedDate</column-name><column-value><![CDATA[");
		sb.append(getModifiedDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>inviteeEmail</column-name><column-value><![CDATA[");
		sb.append(getInviteeEmail());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>status</column-name><column-value><![CDATA[");
		sb.append(getStatus());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>inviteeNewUserId</column-name><column-value><![CDATA[");
		sb.append(getInviteeNewUserId());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private long _invitationId;
	private long _companyId;
	private long _groupId;
	private long _userId;
	private String _userUuid;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private String _inviteeEmail;
	private int _status;
	private long _inviteeNewUserId;
	private String _inviteeNewUserUuid;
	private BaseModel<?> _invitationCardRemoteModel;
}