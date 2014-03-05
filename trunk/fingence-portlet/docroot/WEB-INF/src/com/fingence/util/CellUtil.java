package com.fingence.util;

import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

public class CellUtil {

	public static long getLong(Cell cell) {
		
		long value = 0l;
		
		String cellValue = cell.toString();
		
		if (Validator.isNotNull(cellValue) && !cellValue.trim().equalsIgnoreCase(StringPool.DASH)) {
			try {
				value = Long.parseLong(cellValue.trim());
			} catch (NumberFormatException nfe) {
				System.out.println("Unable to convert to Long ==> " + cellValue);
			}
		}
		
		return value;
	}

	public static double getDouble(Cell cell) {

		double value = 0.0d;
		
		String cellValue = cell.toString();
		
		if (Validator.isNotNull(cellValue) && !cellValue.trim().equalsIgnoreCase(StringPool.DASH)) {
			try {
				value = Double.parseDouble(cellValue.trim());
			} catch (NumberFormatException nfe) {
				System.out.println("Unable to convert to double ==> " + cellValue);
			}
		}
		
		return value;

	}
	
	public static String getString(Cell cell) {
		
		String value = StringPool.BLANK;
		
		String cellValue = cell.toString();
		
		if (Validator.isNotNull(cellValue) && !cellValue.trim().equalsIgnoreCase(StringPool.DASH)) {
			value = cellValue;
		}
		
		return value;
	}
	
	public static Date getDate(Cell cell) {
		Date value = null;
		
		String cellValue = cell.toString();
		
		if (Validator.isNotNull(cellValue)) {
			value = cell.getDateCellValue();
		}		
		
		return value;
	}
	
	public static int getInteger(Cell cell) {

		int value = 0;

		String cellValue = cell.toString();

		if (Validator.isNotNull(cellValue) && !cellValue.trim().equalsIgnoreCase(StringPool.DASH)) {
			try {
				value = Integer.parseInt(cellValue.trim());
			} catch (NumberFormatException nfe) {
				System.out.println("Unable to convert to int ==> " + cellValue);
			}
		}

		return value;
	}
}