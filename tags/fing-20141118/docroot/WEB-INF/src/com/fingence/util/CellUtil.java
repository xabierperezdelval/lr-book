package com.fingence.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.TextFormatter;
import com.liferay.portal.kernel.util.Validator;

public class CellUtil {

	public static long getLong(Cell cell) {
		
		long value = 0l;
		
		if (Validator.isNull(cell)) return value; 
		
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
		
		String cellValue = StringPool.BLANK;
		
		try {
			cellValue = cell.toString();
		} catch (NullPointerException npe) {
			
		}
		
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
		
		if (Validator.isNull(cell)) return value;
		
		String cellValue = cell.toString();
		
		if (Validator.isNotNull(cellValue) && !cellValue.trim().equalsIgnoreCase(StringPool.DASH)) {
			value = cellValue;
		}
		
		return value;
	}
	
	public static String getStringCaps(Cell cell) {
		String original = getString(cell);
		
		return TextFormatter.format(original, TextFormatter.A);
	}
	
	public static Date getDate(Cell cell) {
		Date value = null;
		
		if (Validator.isNull(cell)) return value;
		
		String cellValue = cell.toString();
				
		if (Validator.isNotNull(cellValue)) {
			try {
				value = (new SimpleDateFormat("dd/MM/yyyy")).parse(cellValue);
			} catch (ParseException e) {
				// try with a different format
				try {
					value = (new SimpleDateFormat("dd-MMM-yy")).parse(cellValue);
				} catch (ParseException e1) {
					// try with a different format
					try {
						value = (new SimpleDateFormat("dd/MM/yy")).parse(cellValue);
					} catch (ParseException e2) {
						//e2.printStackTrace();
					}
				}
			}
		}		
		
		return value;
	}
	
	public static int getInteger(Cell cell) {

		int value = 0;
		
		if (Validator.isNull(cell)) return value;

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