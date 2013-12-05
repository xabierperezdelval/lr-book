package com.inikah.util;

import java.io.UnsupportedEncodingException;

import javax.mail.internet.InternetAddress;

import com.inikah.slayer.model.Location;
import com.inikah.slayer.model.Profile;
import com.inikah.slayer.service.LocationLocalServiceUtil;
import com.liferay.mail.service.MailServiceUtil;
import com.liferay.portal.kernel.mail.MailMessage;

public class NotifyUtil {
	
	public static void newCityCreated(Location location) {
		
		MailMessage mailMessage = new MailMessage();
		mailMessage.setFrom(getFrom());
		mailMessage.setTo(getAdmin());
		mailMessage.setSubject("New Region Created: " 
				+ LocationLocalServiceUtil.getDisplayInfo(location.getLocationId()));
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