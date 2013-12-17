package com.inikah.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.liferay.portal.kernel.dao.jdbc.DataAccess;

public class AppConfig {
	
	public static String get(String key) {
		
		String value = null;
		
		Connection connection = null;
		try {
			connection = DataAccess.getConnection();
			
			String sql = "select value from inikah_Config where name = ?";
			
			PreparedStatement pst = connection.prepareStatement(sql);
			pst.setString(1, key);
			ResultSet rs = pst.executeQuery();
			
			while (rs.next()) {
				value = rs.getString("value");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataAccess.cleanUp(connection);
		}
		
		return value;
	}
}