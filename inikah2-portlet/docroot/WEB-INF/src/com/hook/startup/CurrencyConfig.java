package com.hook.startup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Country;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.CountryServiceUtil;
import com.liferay.portal.service.LockLocalServiceUtil;
import com.slayer.model.Currency;
import com.slayer.service.CurrencyLocalServiceUtil;

public class CurrencyConfig extends SimpleAction {
	
	public void run(String[] args) throws ActionException {
		 
		String className = CurrencyConfig.class.getName();
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
		
		InputStream inputStream = CurrencyConfig.class.getClassLoader().getResourceAsStream("data/currency.csv");
				
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		
		try {
			String line = StringPool.BLANK;
			while (Validator.isNotNull(line = br.readLine())) {
				String parts[] = line.split(StringPool.COMMA);
								
				long countryId = 0l;
				try {
					Country country = CountryServiceUtil.fetchCountryByA2(parts[0]);
					if (Validator.isNull(country)) continue;
					countryId = country.getCountryId();
				} catch (SystemException e) {
					e.printStackTrace();
				}
				
				if (countryId == 0l) continue;
				
				Currency currency = null;
				try {
					currency = CurrencyLocalServiceUtil.fetchCurrency(countryId);
				} catch (SystemException e) {
					e.printStackTrace();
				}
				
				if (Validator.isNotNull(currency)) continue;
				
				currency = CurrencyLocalServiceUtil.createCurrency(countryId);
				currency.setCurrencyCode(parts[1]);
				currency.setCurrencySymbol(parts[2]);
				currency.setCurrencyName(parts[3]);
				currency.setMainUnit(parts[4]);
				currency.setSubUnit(parts[5]);
				currency.setToDollars(Double.valueOf(parts[6]));
				currency.setPppFactor(Double.valueOf(parts[7]));
				
				try {
					CurrencyLocalServiceUtil.addCurrency(currency);
				} catch (SystemException e) {
					e.printStackTrace();
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