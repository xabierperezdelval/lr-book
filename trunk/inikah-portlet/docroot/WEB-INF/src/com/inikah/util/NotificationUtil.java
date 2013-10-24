package com.inikah.util;

import com.inikah.slayer.model.MMCity;
import com.inikah.slayer.model.MMRegion;
import com.inikah.slayer.model.Profile;
import com.liferay.mail.service.MailServiceUtil;
import com.liferay.portal.kernel.mail.MailMessage;

public class NotificationUtil {
	
	public static void newRegionCreated(MMRegion mmRegion) {
		
		MailMessage mailMessage = new MailMessage();
		
		//mailMessage.setTo(to);
		//mailMessage.setFrom(from);
		
		MailServiceUtil.sendEmail(mailMessage);
	}
	
	public static void newCityCreated(MMCity mmCity) {
		
	}
	
	public static void newProfileCreated(Profile profile) {
		
	}
}