/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.slayer.model.impl;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;

import com.slayer.model.Profile;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing Profile in entity cache.
 *
 * @author Ahmed Hasan
 * @see Profile
 * @generated
 */
public class ProfileCacheModel implements CacheModel<Profile>, Externalizable {
	@Override
	public String toString() {
		StringBundler sb = new StringBundler(25);

		sb.append("{profileId=");
		sb.append(profileId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", userName=");
		sb.append(userName);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", field1=");
		sb.append(field1);
		sb.append(", field2=");
		sb.append(field2);
		sb.append(", field3=");
		sb.append(field3);
		sb.append(", field4=");
		sb.append(field4);
		sb.append(", field5=");
		sb.append(field5);
		sb.append(", field6=");
		sb.append(field6);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public Profile toEntityModel() {
		ProfileImpl profileImpl = new ProfileImpl();

		profileImpl.setProfileId(profileId);
		profileImpl.setCompanyId(companyId);
		profileImpl.setUserId(userId);

		if (userName == null) {
			profileImpl.setUserName(StringPool.BLANK);
		}
		else {
			profileImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			profileImpl.setCreateDate(null);
		}
		else {
			profileImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			profileImpl.setModifiedDate(null);
		}
		else {
			profileImpl.setModifiedDate(new Date(modifiedDate));
		}

		if (field1 == null) {
			profileImpl.setField1(StringPool.BLANK);
		}
		else {
			profileImpl.setField1(field1);
		}

		if (field2 == null) {
			profileImpl.setField2(StringPool.BLANK);
		}
		else {
			profileImpl.setField2(field2);
		}

		if (field3 == null) {
			profileImpl.setField3(StringPool.BLANK);
		}
		else {
			profileImpl.setField3(field3);
		}

		if (field4 == null) {
			profileImpl.setField4(StringPool.BLANK);
		}
		else {
			profileImpl.setField4(field4);
		}

		if (field5 == null) {
			profileImpl.setField5(StringPool.BLANK);
		}
		else {
			profileImpl.setField5(field5);
		}

		if (field6 == null) {
			profileImpl.setField6(StringPool.BLANK);
		}
		else {
			profileImpl.setField6(field6);
		}

		profileImpl.resetOriginalValues();

		return profileImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		profileId = objectInput.readLong();
		companyId = objectInput.readLong();
		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		field1 = objectInput.readUTF();
		field2 = objectInput.readUTF();
		field3 = objectInput.readUTF();
		field4 = objectInput.readUTF();
		field5 = objectInput.readUTF();
		field6 = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput)
		throws IOException {
		objectOutput.writeLong(profileId);
		objectOutput.writeLong(companyId);
		objectOutput.writeLong(userId);

		if (userName == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(userName);
		}

		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		if (field1 == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(field1);
		}

		if (field2 == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(field2);
		}

		if (field3 == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(field3);
		}

		if (field4 == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(field4);
		}

		if (field5 == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(field5);
		}

		if (field6 == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(field6);
		}
	}

	public long profileId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public String field1;
	public String field2;
	public String field3;
	public String field4;
	public String field5;
	public String field6;
}