package com.inikah.hook.startup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.inikah.slayer.model.MyLanguage;
import com.inikah.slayer.service.MyLanguageLocalServiceUtil;
import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;


public class LanguageConfig extends SimpleAction {
	public void run(String[] arg0) throws ActionException {
		
		// check for the presence of control record in the table. 
		try {
			MyLanguage cntrlRecord = MyLanguageLocalServiceUtil.fetchMyLanguage(1000);
			if (Validator.isNotNull(cntrlRecord)) {
				_log.debug("found control record, existing....");
				return;
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		InputStream inputStream = LanguageConfig.class.getClassLoader().getResourceAsStream("data/languages.csv");
				
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		
		try {
			String line = StringPool.BLANK;
			while (Validator.isNotNull(line = br.readLine())) {
				String parts[] = line.split(StringPool.COMMA);
								
				long countryId = Long.parseLong(parts[0]);
				
				long languageId = 0l;
				try {
					languageId = CounterLocalServiceUtil.increment(MyLanguage.class.getName());
				} catch (SystemException e1) {
					e1.printStackTrace();
				}
				
				MyLanguage myLanguage = MyLanguageLocalServiceUtil.createMyLanguage(languageId);
				myLanguage.setCountryId(countryId);
				myLanguage.setLanguage(parts[2]);
				
				try {
					MyLanguageLocalServiceUtil.addMyLanguage(myLanguage);
				} catch (SystemException e1) {
					e1.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// insert the control record. 
		MyLanguage myLanguage = MyLanguageLocalServiceUtil.createMyLanguage(1000l);
		myLanguage.setLanguage("Control");
		
		try {
			MyLanguageLocalServiceUtil.addMyLanguage(myLanguage);
		} catch (SystemException e1) {
			e1.printStackTrace();
		}
	}
	
	private static Log _log = LogFactoryUtil.getLog(
			LanguageConfig.class);
}