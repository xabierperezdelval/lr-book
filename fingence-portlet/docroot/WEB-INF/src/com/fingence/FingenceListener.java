package com.fingence;

import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.mail.internet.InternetAddress;
import javax.portlet.PortletPreferences;

import com.fingence.slayer.service.AssetLocalServiceUtil;
import com.fingence.slayer.service.PortfolioLocalServiceUtil;
import com.liferay.mail.service.MailServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.mail.MailMessage;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.service.PortalPreferencesLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortletKeys;

public class FingenceListener extends BaseMessageListener{

	@Override
	protected void doReceive(Message message) throws Exception {
		String messageName = (String)message.get("MESSAGE_NAME");
		ServiceContext serviceContext = (ServiceContext)message.get("SERVICE_CONTEXT");
		long userId = message.getLong("USER_ID");
		File excelFile = (File)message.get("EXCEL_FILE");
		
		System.out.println("inside message bus....");
		
		if (messageName.equalsIgnoreCase("setConvertionRate")) {
			PortfolioLocalServiceUtil.applyConversion(message.getLong("portfolioId"));
		} else if (messageName.equalsIgnoreCase("loadAssetData")) {
			AssetLocalServiceUtil.importFromExcel(userId, excelFile, serviceContext);
		} else if (messageName.equalsIgnoreCase("loadEquityPrice")) {
			AssetLocalServiceUtil.loadPricingData(userId, excelFile, serviceContext, IConstants.HISTORY_TYPE_EQUITY);
		} else if (messageName.equalsIgnoreCase("loadBondPrice")) {
			AssetLocalServiceUtil.loadPricingData(userId, excelFile, serviceContext, IConstants.HISTORY_TYPE_BOND);
		} else if (messageName.equalsIgnoreCase("loadBondCashflow")) {
			AssetLocalServiceUtil.loadPricingData(userId, excelFile, serviceContext, IConstants.HISTORY_TYPE_BOND_CASHFLOW);
		} else if (messageName.equalsIgnoreCase("loadDividends")) {
			AssetLocalServiceUtil.loadDividends(userId, excelFile, serviceContext);
		}
		
		System.out.println("going to send email...");
		MailMessage mailMessage = new MailMessage();
		
		InternetAddress from = getFromAddress(CompanyThreadLocal.getCompanyId());
		mailMessage.setFrom(from);
		mailMessage.setTo(from);
		mailMessage.setBody(messageName + " completed successfully");
		mailMessage.setSubject(messageName + " completed successfully");
		MailServiceUtil.sendEmail(mailMessage);
	}
	
	private InternetAddress getFromAddress(long companyId) {
		PortletPreferences portletPreferences = null;
		try {
			portletPreferences = 
				PortalPreferencesLocalServiceUtil.getPreferences(companyId, PortletKeys.PREFS_OWNER_TYPE_COMPANY);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		String key1 = PropsKeys.ADMIN_EMAIL_FROM_ADDRESS;
		String fromEmail = portletPreferences.getValue(key1, 
							PropsUtil.get(key1));
		
		String key2 = PropsKeys.ADMIN_EMAIL_FROM_NAME;
		String fromName = portletPreferences.getValue(key2, 
				PropsUtil.get(key2));
		
		InternetAddress fromAddress = new InternetAddress(); 
		fromAddress.setAddress(fromEmail); 
		try {
			fromAddress.setPersonal(fromName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return fromAddress;
	}
}