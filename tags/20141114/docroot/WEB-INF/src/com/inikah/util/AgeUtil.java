package com.inikah.util;

import java.util.Calendar;

public class AgeUtil {
	
	public static boolean MIN = true;
	public static boolean MAX = false;
	
	public static int getBornOnFigure(int figure, boolean min) {
		Calendar now = Calendar.getInstance();
		
		int year = now.get(Calendar.YEAR);
		
		StringBuilder sb = new StringBuilder();
		sb.append(year - figure);
		sb.append(min? "00":"11");

		return Integer.valueOf(sb.toString());
	}
}