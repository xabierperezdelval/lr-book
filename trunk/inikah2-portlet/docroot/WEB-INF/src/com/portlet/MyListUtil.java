package com.portlet;

import java.text.DecimalFormat;

import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.StringPool;

public class MyListUtil {
	
	public static String getHeight(int cms) {
		
		StringBuilder sb = new StringBuilder();
		
		double totalInches = cms * 0.39370078740157477d;
		
		int foot = (int) Math.floor(totalInches / 12); 
		
		sb.append(CharPool.TILDE);
		sb.append(String.format("%02d", foot));
		sb.append(StringPool.NBSP);
		sb.append("Ft");
		sb.append(StringPool.NBSP);
		
		double inches = totalInches % 12;
		
		DecimalFormat decimalFormat = new DecimalFormat("00");
		sb.append(decimalFormat.format(inches));
		sb.append(StringPool.NBSP);
		sb.append("Inch");
		
		return sb.toString();
	}
	
	public static String getWeight(int kgs) {
		
		
		StringBuilder sb = new StringBuilder();
		
		double pounds = kgs * 2.20462d;
		
		sb.append(CharPool.TILDE);
		DecimalFormat decimalFormat = new DecimalFormat("000");
		sb.append(decimalFormat.format(pounds));
		
		sb.append(StringPool.NBSP);
		sb.append("Lbs");
		
		return sb.toString();
	}	
}