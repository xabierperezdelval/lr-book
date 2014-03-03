package com.fingence.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;

import com.fingence.slayer.model.CountryExt;
import com.fingence.slayer.service.CountryExtLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
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

		double value = 0;
		
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
	public static List<String> getCurrencies(){
		
		List<String> currencyList = new ArrayList<String>();
		
		List<CountryExt> countryExtLists = null;
		try {
			countryExtLists = CountryExtLocalServiceUtil.getCountryExts(0, CountryExtLocalServiceUtil.getCountryExtsCount());
			for(CountryExt countryExt : countryExtLists){
				String currency = countryExt.getCurrency();
				if(!currencyList.contains(currency)){
					currencyList.add(countryExt.getCurrency());
				}	
			}
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return currencyList;	
	}
}