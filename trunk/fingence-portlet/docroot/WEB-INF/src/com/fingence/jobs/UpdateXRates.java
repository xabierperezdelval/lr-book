package com.fingence.jobs;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;

import com.fingence.slayer.model.Currency;
import com.fingence.slayer.service.CurrencyLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;

public class UpdateXRates extends BaseMessageListener {
	
	@Override
	protected void doReceive(Message arg) throws Exception {
		
		update();
	}

	private void update() {
						
		String appId = "d87f7d237e0b4a58bf5e85c40b777cb0";
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
			
			String currencyCode = currency.getCurrencyCode();
			double xrate = rates.getDouble(currencyCode);
			
			if (Double.isNaN(xrate)) continue;
			
			currency.setConversion(1/xrate);
			
			try {
				CurrencyLocalServiceUtil.updateCurrency(currency);
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
	}
}