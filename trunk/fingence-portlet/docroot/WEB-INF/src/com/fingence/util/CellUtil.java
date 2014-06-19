package com.fingence.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;

import com.fingence.slayer.model.Portfolio;
import com.fingence.slayer.service.PortfolioLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.TextFormatter;
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
		
		String cellValue = cell.toString();
				
		if (Validator.isNotNull(cellValue)) {
			try {
				value = (new SimpleDateFormat("dd/MM/yyyy")).parse(cellValue);
			} catch (ParseException e) {
				e.printStackTrace();
			}
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
	
	public static boolean disablePrimaryPortfolio(List<Portfolio> portfolios){
		Boolean disabledPrimary = false;
		
		for(Portfolio portfolio: portfolios){
			if(portfolio.isPrimary()){
				portfolio.setPrimary(false);
				try {
					PortfolioLocalServiceUtil.updatePortfolio(portfolio);
					disabledPrimary = true;
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
		return disabledPrimary;
		
	}
}