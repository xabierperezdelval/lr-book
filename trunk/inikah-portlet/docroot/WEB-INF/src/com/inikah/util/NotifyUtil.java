package com.inikah.util;

import java.io.UnsupportedEncodingException;

import javax.mail.internet.InternetAddress;

import com.inikah.slayer.model.MMCity;
import com.inikah.slayer.model.MMRegion;
import com.inikah.slayer.model.Profile;
import com.inikah.slayer.service.MMCityServiceUtil;
import com.inikah.slayer.service.MMRegionServiceUtil;
import com.liferay.mail.service.MailServiceUtil;
import com.liferay.portal.kernel.mail.MailMessage;

public class NotifyUtil {
	
	public static void newRegionCreated(MMRegion mmRegion) {
		
		MailMessage mailMessage = new MailMessage();
		mailMessage.setFrom(getFrom());
		mailMessage.setTo(getAdmin());
		mailMessage.setSubject("New Region Created: " 
				+ MMRegionServiceUtil.getDisplayInfo(mmRegion.getRegionId()));
		MailServiceUtil.sendEmail(mailMessage);
	}
	
	public static void newCityCreated(MMCity mmCity) {
		
		MailMessage mailMessage = new MailMessage();
		mailMessage.setFrom(getFrom());
		mailMessage.setTo(getAdmin());
		mailMessage.setSubject("New City: " 
				+ MMCityServiceUtil.getDisplayInfo(mmCity.getCityId()));		
		MailServiceUtil.sendEmail(mailMessage);
	}
	
	public static void newProfileCreated(Profile profile) {
		
	}
	
	private static InternetAddress getFrom() {
		InternetAddress addr = new InternetAddress();
		addr.setAddress("info@inikah.com");
		try {
			addr.setPersonal("inikah.com");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return addr;
	}
	
	private static InternetAddress getAdmin() {
		InternetAddress addr = new InternetAddress();
		addr.setAddress("hasan@mpowerglobal.com");
		try {
			addr.setPersonal("Ahmed Hasan");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return addr;
	}	
}