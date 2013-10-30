package com.inikah.util;

import java.util.Calendar;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;


public class ProfileCodeUtil {
	
    static char[] letters = {
        	'1', '2', '3', '4', '5', '6', '7', '8',
        	'9', 'A', 'B', 'C', 'D', 'E', 'F', 'G',  
        	'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 
        	'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'		
    };

	public static String getCode(boolean bride) {

		Calendar cal = Calendar.getInstance();
		
		StringBuilder sb = new StringBuilder();
		
		// gender part
		sb.append(bride?"B":"G");
		
		// year part
		int eraBegin = 2013;
		int currentYear = cal.get(Calendar.YEAR);
		int offset = currentYear - eraBegin;
		
		char yearPart = (char) (offset + 65);
		if (offset > 25) {
			// handle differently
		}
		sb.append(yearPart);
		
		// week part
		sb.append(String.format("%02d", cal.get(Calendar.WEEK_OF_YEAR)));
		
		// increment part
		
		long counter = 0l;
		try {
			counter = CounterLocalServiceUtil.increment();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		int size = letters.length;
		int value = (int) (counter % (size * size));
		sb.append((char)(letters[value / size]));
		sb.append((char)(letters[value % size]));
		
		return sb.toString();
	}
}