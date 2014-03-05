package com.fingence.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.util.Validator;

public class ConversionUtil {
	public static Map<String, Double> getFxRates() {
		
		Map<String, Double> fxRates = new HashMap<String, Double>();
		Connection conn = null;
		try {
			conn = DataAccess.getConnection();
			
			Statement stmt = conn.createStatement();
			
			String sql = "SELECT distinct currency_, conversion FROM fing_CountryExt where currency_ != 'USD'";
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
}