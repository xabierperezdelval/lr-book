package com.inikah.hook.startup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;


public class CurrencyConfig extends SimpleAction {
	public void run(String[] arg0) throws ActionException {
		InputStream inputStream = CurrencyConfig.class.getClassLoader().getResourceAsStream("data/currency.csv");
				
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		
		try {
			String line = StringPool.BLANK;
			while (Validator.isNotNull(line = br.readLine())) {
				System.out.println("line ==> " + line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
