package com.fingence.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.TreeMap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.InputStream;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;

import com.fingence.slayer.model.Portfolio;
import com.fingence.slayer.service.PortfolioLocalServiceUtil;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.StringPool;

public class ConversionUtil {
	public static Map<String, Double> getFxRates() {
		
		Map<String, Double> fxRates = new HashMap<String, Double>();
		Connection conn = null;
		try {
			conn = DataAccess.getConnection();
			
			Statement stmt = conn.createStatement();
			
			String sql = "SELECT distinct currency_, conversion FROM fing_CountryExt";
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				String currency = rs.getString(1);
				double fxRate = rs.getDouble(2);
				
				fxRates.put(currency, fxRate);
			}
			
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			DataAccess.cleanUp(conn);
		}
		
		return fxRates;
	}
	
	public static double getCurrentFx(String currency, Map<String, Double> currentFxMap) {
		
		if (Validator.isNull(currency)) {
			currency = "USD";
		}

		if (currency.equalsIgnoreCase("USD")) {
			return 1.0d;
		} else {
			return currentFxMap.get(currency);
		}
	}
	
	public static Map<String, String> getCurrencies() {
		
		Map<String, String> currencies = new TreeMap<String, String>();
		Connection conn = null;
		try {
			conn = DataAccess.getConnection();
			
			Statement stmt = conn.createStatement();
			
			String sql = "SELECT distinct currency_, currencyDesc FROM fing_CountryExt";
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				String currency = rs.getString(1);
				String currencyDesc = rs.getString(2);
				
				currencies.put(currency, currencyDesc);
			}
			
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} finally {
			DataAccess.cleanUp(conn);
		}
		
		return currencies;
	}
	
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
			e.printStackTrace();
		}

		return conversion;
	}
	
	public static String getBaseCurrency(long portfolioId) {
		
		String baseCurrency = StringPool.BLANK;
		
		try {
			Portfolio portfolio = PortfolioLocalServiceUtil.fetchPortfolio(portfolioId);
			
			if (Validator.isNotNull(portfolio)) {
				baseCurrency = getCurrencies().get(portfolio.getBaseCurrency());
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return baseCurrency;
	}
}