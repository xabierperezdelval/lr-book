package com.inikah.hook.startup;

import java.io.File;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.util.ContentUtil;


public class CurrencyConfig extends SimpleAction {
	public void run(String[] arg0) throws ActionException {
		//InputStream inputStream = CurrencyConfig.class.getClassLoader().getResourceAsStream("data/currency.csv");
		
		//System.out.println(inputStream);
		
		File file = new File("");
		
		System.out.println(file.getName());
		System.out.println(file.getAbsolutePath());
		
		String currencies = ContentUtil.get("data/currency.csv");
		
		System.out.println(currencies.toString());
		
	}
}
