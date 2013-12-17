package com.inikah.jobs;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;

import com.inikah.slayer.model.Currency;
import com.inikah.slayer.service.ConfigServiceUtil;
import com.inikah.slayer.service.CurrencyLocalServiceUtil;
import com.inikah.util.ConfigConstants;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.util.StringPool;

public class UpdateXRates extends BaseMessageListener {
	
	@Override
	protected void doReceive(Message arg) throws Exception {
		
		update();
	}

	private void update() {
		
		if (!ConfigConstants.OPENXCHAGE_UPDATE) return;
				
		String appId = ConfigServiceUtil.get(ConfigConstants.OPENXCHAGE_API_ID);
		String url = "http://openexchangerates.org/api/latest.json?app_id="+ appId;
	
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url);
		
		try {
			client.executeMethod(method);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		InputStream inputStream = null;
		try {
			inputStream = method.getResponseBodyAsStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String json = null;
		try {
			json = IOUtils.toString(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JSONObject jsonObject = null;
		try {
			jsonObject = JSONFactoryUtil.createJSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject rates = jsonObject.getJSONObject("rates");
		
		int count = 0;
		try {
			count = CurrencyLocalServiceUtil.getCurrenciesCount();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		List<Currency> currencies = null;
		try {
			currencies = CurrencyLocalServiceUtil.getCurrencies(0, count);
		} catch (SystemException e) {
			e.printStackTrace();
		}
								
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
			
			try {
				CurrencyLocalServiceUtil.updateCurrency(currency);
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
	}
}