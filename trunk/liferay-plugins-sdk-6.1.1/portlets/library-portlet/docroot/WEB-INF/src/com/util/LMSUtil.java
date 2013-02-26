package com.util;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.security.Key;
import java.util.List;
import java.util.Locale;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.image.ImageBag;
import com.liferay.portal.kernel.image.ImageToolUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Image;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.util.Encryptor;
import com.liferay.util.EncryptorException;

public class LMSUtil {
	public static String encrypt(String data, long companyId) {
		return transform(data, companyId, true);
	}
	
	public static String decrypt(String data, long companyId) {
		return transform(data, companyId, false);
	}
	
	private static String transform(String data, 
			long companyId, boolean encrypt) {
		
		String transformedData = null;
		
		Company company = null;
		try {
			company = 
				CompanyLocalServiceUtil.fetchCompany(companyId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (company != null) {
			Key keyObj = company.getKeyObj();
			try {
				transformedData = encrypt?
						Encryptor.encrypt(keyObj, data):
						Encryptor.decrypt(keyObj, data);
			} catch (EncryptorException e) {
				e.printStackTrace();
			}
		}
		
		return transformedData;
	}
	
	public static long getPlidByName(long groupId, Locale locale, 
			String name, boolean privateLayout) {
		long plid = 0l;
		
		List<Layout> layouts = null;
		try {
			layouts = LayoutLocalServiceUtil.getLayouts(
						groupId, privateLayout, "portlet");
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNotNull(layouts)) {
			for (Layout layout: layouts) {
				if (layout.getName(locale).equalsIgnoreCase(name)) {
					plid = layout.getPlid();
					break;
				}
			}
		}
				
		return plid;
	}
	
	public static long getPlidByFriendlyURL(long groupId, 
			String friendlyURL, boolean privateLayout) {
		long plid = 0l;
		
		try {
			Layout layout = 
				LayoutLocalServiceUtil.getFriendlyURLLayout(
					groupId, privateLayout, friendlyURL);
			
			if (Validator.isNotNull(layout)) {
				plid = layout.getPlid();
			}
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return plid;
	}
	
	public static byte[] getScaledImage(Image image, int percent) {
		
		byte[] bytes = null;
		ImageBag imageBag = null;
		try {
			imageBag = ImageToolUtil.read(image.getTextObj());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(imageBag)) return null;
		
		RenderedImage renderedImage = imageBag.getRenderedImage();
		
		long height = Math.round(image.getHeight() * percent/100); 
		long width = Math.round(image.getWidth() * percent/100);
		
		renderedImage = ImageToolUtil.scale( renderedImage,
				(int) height, (int) width);
		
		try {
			bytes = ImageToolUtil.getBytes(renderedImage, 
					image.getType());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bytes;
	}
}