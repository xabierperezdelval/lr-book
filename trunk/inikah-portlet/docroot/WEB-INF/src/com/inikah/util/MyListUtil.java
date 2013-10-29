package com.inikah.util;

import java.text.DecimalFormat;

import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.StringPool;

public class MyListUtil {
	
	public static String getHeightText(int heightCms, boolean htmlSpace) {
		
		String delimiter = htmlSpace? StringPool.NBSP : StringPool.SPACE;
		
		StringBuilder sb = new StringBuilder();
		sb.append(heightCms);
		sb.append(delimiter);
		sb.append("Cms");
		sb.append(delimiter);
		sb.append(StringPool.SLASH);
		sb.append(delimiter);		
		
		double conversion = 0.39370078740157477d;
		
		double totalInches = heightCms * conversion;
		
		int foot = (int) Math.floor(totalInches / 12); 
		
		sb.append(String.format("%02d", foot));
		sb.append(delimiter);
		sb.append("Ft");
		sb.append(delimiter);
		
		double inches = totalInches % 12;
		
		DecimalFormat decimalFormat = new DecimalFormat("00.00");
		sb.append(decimalFormat.format(inches));
		
		sb.append(delimiter);
		sb.append("Inch");
		
		return sb.toString();
	}
	
	public static String getWeightText(int weightKgs, boolean htmlSpace) {
		
		String delimiter = htmlSpace? StringPool.NBSP : StringPool.SPACE;
		
		StringBuilder sb = new StringBuilder();
		sb.append(weightKgs);
		sb.append(delimiter);
		sb.append("Kgs");
		sb.append(delimiter);
		sb.append(StringPool.SLASH);
		sb.append(delimiter);			
		
		double conversion = 2.20462d;
		
		double pounds = weightKgs * conversion;
		
		sb.append(CharPool.TILDE);
		DecimalFormat decimalFormat = new DecimalFormat("000");
		sb.append(decimalFormat.format(pounds));
		
		sb.append(delimiter);
		sb.append("Lbs");
		
		return sb.toString();
	}
}