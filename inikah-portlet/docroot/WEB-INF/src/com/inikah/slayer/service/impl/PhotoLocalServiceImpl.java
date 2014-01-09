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
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.cloudinary.Cloudinary;
import com.inikah.slayer.model.Photo;
import com.inikah.slayer.model.Profile;
import com.inikah.slayer.service.base.PhotoLocalServiceBaseImpl;
import com.inikah.util.AppConfig;
import com.inikah.util.CloudinaryUtil;
import com.inikah.util.ConfigConstants;
import com.inikah.util.IConstants;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.image.ImageBag;
import com.liferay.portal.kernel.image.ImageToolUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Image;
import com.liferay.portlet.documentlibrary.store.DLStoreUtil;
import com.liferay.util.portlet.PortletProps;

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
		
	public Photo upload(long imageId, long profileId, File file, String description) {
		
		Photo photo = null;
		
		if (imageId == 0l) {
			try {
				imageId = counterLocalService.increment(Image.class.getName());
				photo = createPhoto(imageId);
				photo = addPhoto(photo);
			} catch (SystemException e) {
				e.printStackTrace();
			}
		} else {
			try {
				photo = fetchPhoto(imageId);
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		
		try {
			imageLocalService.updateImage(imageId, file);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
				
		photo.setUploadDate(new java.util.Date());
		photo.setProfileId(profileId);
		photo.setDescription(description);
		photo.setImageType(IConstants.IMG_TYPE_PHOTO);
		
		try {
			photo = updatePhoto(photo);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		//createThumbnail(imageId);
		Message message = new Message();
		message.put("messageName", "createThumbnail");
		message.put("imageId", String.valueOf(imageId));
		MessageBusUtil.sendMessage("inikah/destination", message);
		
		return photo;
	}

	public long createThumbnail(long imageId) {
		
		Image image = null;
		try {
			image = imageLocalService.fetchImage(imageId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(image)) return 0l;
		
		long thumbnailId = imageId;
		
		// check the original width of the image. 
		float thumbnailWidth = GetterUtil.getFloat(PortletProps.get("profile.photo.thumbnail.width"));
		if (image.getWidth() > (int)thumbnailWidth) {
			try {
				thumbnailId = counterLocalService.increment(Image.class.getName());
			} catch (SystemException e) {
				e.printStackTrace();
			}
			
			ImageBag imageBag = null;
			try {
				imageBag = ImageToolUtil.read(image.getTextObj());
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (Validator.isNull(imageBag)) return 0l;
			
			RenderedImage renderedImage = imageBag.getRenderedImage();
			
			float reduceBy = (thumbnailWidth / (float) image.getWidth());
						
			long height = Math.round(image.getHeight() * reduceBy);
			long width = Math.round(image.getWidth() * reduceBy);
			renderedImage = ImageToolUtil.scale(renderedImage, (int) height, (int) width);
			
			byte[] bytes = null;
			try {
				bytes = ImageToolUtil.getBytes(renderedImage, image.getType());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (Validator.isNotNull(bytes)) {
				try {
					imageLocalService.updateImage(thumbnailId, bytes);
				} catch (PortalException e) {
					e.printStackTrace();
				} catch (SystemException e) {
					e.printStackTrace();
				}
			}			
		} 
		
		Photo photo = null;
		try {
			photo = fetchPhoto(imageId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		photo.setThumbnailId(thumbnailId);
		try {
			updatePhoto(photo);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return thumbnailId;
	}
	
	public long createPortrait(long imageId) {
		
		Photo photo = null;
		try {
			photo = fetchPhoto(imageId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(photo)) return 0l;
		
		long profileId = photo.getProfileId();
		String publicId = String.valueOf(imageId);
		
		String fileName = StringPool.BLANK;
		try {
			Image image = imageLocalService.fetchImage(imageId);
			fileName = imageId + StringPool.PERIOD + image.getType();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		File file = null;
		try {
			file = DLStoreUtil.getFile(0l, 0l, fileName);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		try {
			CloudinaryUtil.getService().uploader().upload(file, Cloudinary.asMap("public_id", publicId));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		long thumbnailId = savePortrait(imageId, profileId);
		
		try {
			CloudinaryUtil.getService().uploader().destroy(publicId, Cloudinary.asMap("public_id", publicId));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return thumbnailId;
	}
	
	private long savePortrait(long imageId, long profileId) {
		
		Image image = null;
		try {
			image = imageLocalService.fetchImage(imageId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		String publicId = String.valueOf(imageId) + StringPool.PERIOD + image.getType();
		
		URL url = null;
		try {
			url = new URL("http://res.cloudinary.com/" 
					+ AppConfig.get(ConfigConstants.CLDY_CLOUD_NAME) 
					+ "/image/upload/w_80,h_100,c_thumb,g_face/" 
					+ publicId);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		File file = FileUtils.getFile(publicId);
		
		try {
			FileUtils.copyURLToFile(url, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		long portraitId = 0l;
		try {
			portraitId = counterLocalService.increment(Image.class.getName());
		} catch (SystemException e) {
			e.printStackTrace();
		}
				
		try {
			imageLocalService.updateImage(portraitId, file);
			
			Profile profile = profileLocalService.fetchProfile(profileId);
			profile.setPortraitId(portraitId);
			profileLocalService.updateProfile(profile);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		file.delete();
		
		return portraitId;
	}
	
	
	public List<Photo> getPhotos(long profileId) {
		
		List<Photo> photos = null;
		
		try {
			photos = photoPersistence.findByProfileId_ImageType(profileId, IConstants.IMG_TYPE_PHOTO);
		} catch (SystemException e) {
			e.printStackTrace();
		}		
		
		return photos;
	}
	
	@Override
	@Indexable(type = IndexableType.DELETE)
	public Photo deletePhoto(long imageId) throws PortalException,
			SystemException {
		
		imageLocalService.deleteImage(imageId);
		
		Photo photo = fetchPhoto(imageId);
		if (Validator.isNotNull(photo) && photo.getThumbnailId() > 0l) {
			imageLocalService.deleteImage(photo.getThumbnailId());
		}
		
		return super.deletePhoto(imageId);
	}
	
	public void approve(long imageId) {
		try {
			Photo photo = fetchPhoto(imageId);
			photo.setApproved(true);
			updatePhoto(photo);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
}