package com.hook.startup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Company;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.LockLocalServiceUtil;
import com.slayer.model.MyLanguage;
import com.slayer.service.MyLanguageLocalServiceUtil;


public class LanguageConfig extends SimpleAction {
	public void run(String[] args) throws ActionException {
		 
		String className = LanguageConfig.class.getName();
		long companyId = Long.valueOf(args[0]);
		
		Company company = null;
		try {
			company = CompanyLocalServiceUtil.fetchCompany(companyId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		try {
			boolean locked = LockLocalServiceUtil.isLocked(className, company.getKey());
			if (locked) {
				System.out.println("already locked...returning....");
				return;
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}

		System.out.println("going to insert the records...");
		
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
		
		// lock the process
		try {
			LockLocalServiceUtil.lock(className, company.getKey(), "Portal Admin");
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
}