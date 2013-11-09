package com.inikah.jobs;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;

import com.inikah.slayer.model.Currency;
import com.inikah.slayer.service.CurrencyLocalServiceUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.util.StringPool;

public class UpdateXRates extends BaseMessageListener {

	@Override
	protected void doReceive(Message arg) throws Exception {
		
		System.out.println("going to update the Exchange Rates...xxx");
		
		String appId = "d87f7d237e0b4a58bf5e85c40b777cb0";
		String url = "http://openexchangerates.org/api/latest.json?app_id="+ appId;
	
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url);
		
		client.executeMethod(method);
		
		InputStream inputStream = method.getResponseBodyAsStream();
		
		String json = IOUtils.toString(inputStream);
		
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(json);
		JSONObject rates = jsonObject.getJSONObject("rates");
		
		int count = CurrencyLocalServiceUtil.getCurrenciesCount();
		List<Currency> currencies = CurrencyLocalServiceUtil.getCurrencies(0, count);
								
		for (Currency currency: currencies) {
			
			double toDollars = 1.0d;
			String currencyCode = currency.getCurrencyCode();
			double xrate = rates.getDouble(currencyCode);
			
			if (Double.isNaN(xrate)) {
				currency.setCurrencyCode("USD");
				currency.setCurrencyName("US Dollar");
				currency.setSubUnit("Cents");
				currency.setCurrencySymbol(StringPool.DOLLAR);
			} else {
				toDollars = 1/xrate;
			}
			
			currency.setToDollars(toDollars);
			
			CurrencyLocalServiceUtil.updateCurrency(currency);
		}
	}
}