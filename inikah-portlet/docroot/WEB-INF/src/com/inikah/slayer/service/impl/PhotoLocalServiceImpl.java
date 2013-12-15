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

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import com.cloudinary.Cloudinary;
import com.inikah.slayer.service.base.PhotoLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.image.ImageBag;
import com.liferay.portal.kernel.image.ImageToolUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.CompanyConstants;
import com.liferay.portal.model.Image;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.service.ImageLocalServiceUtil;
import com.liferay.portlet.documentlibrary.store.DLStoreUtil;
import com.inikah.slayer.model.Photo;

/**
 * The implementation of the photo local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.inikah.slayer.service.PhotoLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.inikah.slayer.service.base.PhotoLocalServiceBaseImpl
 * @see com.inikah.slayer.service.PhotoLocalServiceUtil
 */
public class PhotoLocalServiceImpl extends PhotoLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.inikah.slayer.service.PhotoLocalServiceUtil} to access the photo local service.
	 */
	
	Cloudinary cloudinary;
	
	public PhotoLocalServiceImpl() {
		cloudinary = new Cloudinary("cloudinary://684163674418368:p0TvTj4jm0cR6NJGa-N-IRkaTnw@inikah-com");
	}
	
	public Photo upload(long imageId, long profileId, File file, String description) {
		
		Photo photo = null;
		
		long companyId = CompanyThreadLocal.getCompanyId();
		long repositoryId = CompanyConstants.SYSTEM;
		
		if (imageId == 0) {
			try {
				imageId = counterLocalService.increment(Image.class.getName());
				photo = createPhoto(imageId);
				addPhoto(photo);
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		
		try {
			String filePath = getPath(profileId, imageId, companyId, repositoryId, "I");
			DLStoreUtil.addFile(companyId, repositoryId, filePath, true, file);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		try {
			imageLocalService.updateImage(imageId, file);
		} catch (PortalException e1) {
			e1.printStackTrace();
		} catch (SystemException e1) {
			e1.printStackTrace();
		}
		
		photo.setUploadDate(new java.util.Date());
		photo.setProfileId(profileId);
		photo.setDescription(description);
		photo.setImageType(1);
		
		try {
			updatePhoto(photo);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return photo;
	}

	private String getPath(long profileId, long imageId, long companyId, long repositoryId, String prefix) {
		
		String folderName = "P" + String.format("%07d", profileId);
		
		boolean folderExists = false;
		
		try {
			folderExists = DLStoreUtil.hasDirectory(
				companyId, repositoryId, folderName);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (!folderExists) {
			try {
				DLStoreUtil.addDirectory(
					companyId, repositoryId, folderName);
			} catch (PortalException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		
		return folderName + StringPool.SLASH + prefix + String.format("%07d", imageId);
	}

	public byte[] getThumbnail(long imageId) {
		byte[] bytes = null;
		ImageBag imageBag = null;
		
		Image image = null;
		try {
			image = imageLocalService.fetchImage(imageId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(image)) return null; 
		
		try {
			imageBag = ImageToolUtil.read(image.getTextObj());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (Validator.isNull(imageBag)) return null;
		
		RenderedImage renderedImage = imageBag.getRenderedImage();
		
		float reduceBy = 0.20f;
		
		long height = Math.round(image.getHeight() * reduceBy);
		long width = Math.round(image.getWidth() * reduceBy);
		renderedImage = ImageToolUtil.scale(renderedImage, (int) height, (int) width);
		try {
			bytes = ImageToolUtil.getBytes(renderedImage, image.getType());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytes;
	}
	
	public long createThumbnail(long imageId) {
		
		Photo photo = null;
		try {
			photo = fetchPhoto(imageId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(photo)) return 0l;
		
		long profileId = photo.getProfileId();
		long companyId = CompanyThreadLocal.getCompanyId();
		long repositoryId = CompanyConstants.SYSTEM;
		
		String filePath = getPath(profileId, imageId, companyId, repositoryId, "I");
		
		File file = null;
		try {
			file = DLStoreUtil.getFile(companyId, repositoryId, filePath);
		} catch (PortalException e1) {
			e1.printStackTrace();
		} catch (SystemException e1) {
			e1.printStackTrace();
		}
		
		String publicId = "T" + String.format("%07d", imageId);
		try {
			cloudinary.uploader().upload(file, Cloudinary.asMap("public_id", publicId));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		long thumbnailId = storeThumbnail(imageId, profileId, companyId, repositoryId);
		
		try {
			cloudinary.uploader().destroy(publicId, Cloudinary.asMap("public_id", publicId));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return thumbnailId;
	}
	
	private long storeThumbnail(long imageId, long profileId, long companyId, long repositoryId) {
		
		Image image = null;
		try {
			image = imageLocalService.fetchImage(imageId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		String publicId = "T" + String.format("%07d", imageId) + StringPool.PERIOD + image.getType();
		
		URL url = null;
		try {
			url = new URL("http://res.cloudinary.com/inikah-com/image/upload/w_80,h_100,c_thumb,g_face/" + publicId);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		File file = FileUtils.getFile(publicId);
		
		try {
			FileUtils.copyURLToFile(url, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		long thumbnailId = 0l;
		try {
			thumbnailId = counterLocalService.increment(Image.class.getName());
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		String filePath = getPath(profileId, thumbnailId, companyId, repositoryId, "T");
		
		try {
			DLStoreUtil.addFile(companyId, repositoryId, filePath, file);
			ImageLocalServiceUtil.updateImage(thumbnailId, file);
			
			Photo photo = createPhoto(thumbnailId);
			photo.setImageType(2);
			photo.setApproved(true);
			photo.setUploadDate(new java.util.Date());
			photo.setProfileId(profileId);
			
			addPhoto(photo);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		file.delete();
		
		return thumbnailId;
	}	
}