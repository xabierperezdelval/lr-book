package com.inikah.util;

import java.util.Calendar;

import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.StringPool;

public class AgeUtil {
	
	public static boolean MIN = true;
	public static boolean MAX = true;
	
	/**
	 * 
	 * @return
	 */
	public static double getComputeAge(int bornOn) {
		
		double age = 0.0d;
		
		if (bornOn == 0) return age;
		
		Calendar now = Calendar.getInstance();
		int nowYear = now.get(Calendar.YEAR);
		int nowMonth = now.get(Calendar.MONTH); 
		
		int bornYear = Integer.valueOf(String.valueOf(bornOn).substring(0, 4));
		int bornMonth = Integer.valueOf(String.valueOf(bornOn).substring(4));
		
		int decimalPart = nowMonth - bornMonth;
		int wholePart = nowYear - bornYear;
		
		if (decimalPart < 0) {
			decimalPart += 12;
			--wholePart;
		}
		
		return Double.valueOf(wholePart + StringPool.PERIOD + decimalPart);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getDisplayAge(int bornOn) {
		if (getComputeAge(bornOn) == 0.0d) return StringPool.BLANK;
		
		String[] parts = String.valueOf(getComputeAge(bornOn)).split("\\.");
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(parts[0]).append(parts[0]);
		sb.append(StringPool.NBSP).append("Years");
		
		int months = Integer.valueOf(parts[1]);
		
		if (months > 0) {
			sb.append(StringPool.COMMA).append(StringPool.NBSP);
			sb.append(parts[1]).append(StringPool.NBSP);
			sb.append("Month");
			
			if (months > 1) {
				sb.append(CharPool.LOWER_CASE_S);
			}
		}
		
		return sb.toString();
	}
	
	public static int getBornOnFigure(int figure, boolean min) {
		Calendar now = Calendar.getInstance();
		
		int year = now.get(Calendar.YEAR);
		
		StringBuilder sb = new StringBuilder();
		sb.append(year - figure);
		sb.append(min? "00":"11");

		return Integer.valueOf(sb.toString());
	}
}