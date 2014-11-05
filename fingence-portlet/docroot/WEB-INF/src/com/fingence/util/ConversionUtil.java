package com.fingence.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;

import com.fingence.slayer.model.Portfolio;
import com.fingence.slayer.service.CurrencyServiceUtil;
import com.fingence.slayer.service.PortfolioLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;

public class ConversionUtil {

	public static double getConversion(String currency, Date purchaseDate) {

		double conversion = 1.0d;

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		StringBuilder sb = new StringBuilder()
			.append("http://currencies.apps.grandtrunk.net/getrate/")
			.append(formatter.format(purchaseDate))
			.append(StringPool.SLASH)
			.append(currency)
			.append(StringPool.SLASH)
			.append("usd");
			
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(sb.toString());
		
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

		try {
			conversion = Double.parseDouble(IOUtils.toString(inputStream));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("not able to get the conversion for this date...");
			conversion = CurrencyServiceUtil.getConversion(currency);
		}

		return conversion;
	}
	
	public static String getCurrency(long portfolioId) {
		
		String currency = StringPool.BLANK;
		
		try {
			Portfolio portfolio = PortfolioLocalServiceUtil.fetchPortfolio(portfolioId);
			currency = portfolio.getBaseCurrency();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return currency;
	}
}