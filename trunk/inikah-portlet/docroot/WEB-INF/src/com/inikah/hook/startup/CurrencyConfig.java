package com.inikah.hook.startup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.inikah.slayer.model.Currency;
import com.inikah.slayer.service.CurrencyLocalServiceUtil;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Country;
import com.liferay.portal.service.CountryServiceUtil;


public class CurrencyConfig extends SimpleAction {
	public void run(String[] arg0) throws ActionException {
		InputStream inputStream = CurrencyConfig.class.getClassLoader().getResourceAsStream("data/currency.csv");
				
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		
		try {
			String line = StringPool.BLANK;
			while (Validator.isNotNull(line = br.readLine())) {
				String parts[] = line.split(StringPool.COMMA);
								
				long countryId = 0l;
				try {
					Country country = CountryServiceUtil.fetchCountryByA2(parts[2]);
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
				currency.setCurrencyCode(parts[2]);
				currency.setCurrencyName(parts[0]);
				//currency.setCurrencySymbol(currencySymbol);
				//currency.setSubUnit(subUnit);
				//currency.setPppFactor(pppFactor);
				//currency.setToDollars(toDollars);
				
				try {
					CurrencyLocalServiceUtil.addCurrency(currency);
				} catch (SystemException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}