package com.inikah.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.inikah.slayer.model.Profile;
import com.inikah.slayer.service.BridgeServiceUtil;
import com.inikah.slayer.service.ProfileLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.TextFormatter;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Country;
import com.liferay.portal.model.ListType;
import com.liferay.portal.service.CountryServiceUtil;
import com.liferay.portal.service.ListTypeServiceUtil;

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
	
	public static int getListTypeId(String listName, String suffix) {
		return BridgeServiceUtil.getListTypeId(listName, suffix);
	}
	
	public static String getMaritalStatusOptions(Locale locale, Profile profile) {
		return getOptions(locale, profile, IConstants.LIST_MARITAL_STATUS, profile.getMaritalStatus());
	}

	private static String getOptions(Locale locale, Profile profile, String suffix, long currentValue) {
		
		StringBuilder sb = new StringBuilder();
		try {
			List<ListType> listTypes = ListTypeServiceUtil.getListTypes(Profile.class.getName() + 
					StringPool.PERIOD + suffix);
			
			for (ListType listType: listTypes) {
				
				String name = listType.getName();
					
				// only if the list is for "maritalStatus"
				if (suffix.equalsIgnoreCase(IConstants.LIST_MARITAL_STATUS) 
						&& profile.isBride() && name.endsWith("married")) continue;
				
				// check for createdFor
				if (suffix.equalsIgnoreCase(IConstants.LIST_CREATED_FOR) 
						&& ProfileLocalServiceUtil.hasSelfProfile(profile.getUserId()) 
						&& name.endsWith("self")) continue;

				long listTypeId = listType.getListTypeId();
				
				sb.append("<option value=");
				sb.append(StringPool.QUOTE);
				sb.append(listTypeId);
				sb.append(StringPool.QUOTE);
				if (listTypeId == currentValue) {
					sb.append(StringPool.SPACE);
					sb.append("selected");
				}
				sb.append(">");
				
				String value = LanguageUtil.get(locale, listType.getName());
				if (suffix.equalsIgnoreCase(IConstants.LIST_CREATED_FOR)) {
					String[] parts = value.split(StringPool.COLON);
					if (parts.length == 2) {
						value = profile.isBride()? parts[1]:parts[0];
					}
				} 
				
				sb.append(value);
				sb.append("</option>");
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	public static String getCreatedFor(Locale locale, Profile profile) {
		return getOptions(locale, profile, IConstants.LIST_CREATED_FOR, profile.getCreatedFor());
	}
	
	public static String getMonths(Locale locale, int bornOn) {
		StringBuilder sb = new StringBuilder();
		sb.append("<option>Month</option>");
		
		String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
		for (int i=0; i<12; i++) {
			sb.append("<option value=");
			sb.append(StringPool.QUOTE);
			sb.append(String.format("%02d",i));
			sb.append(StringPool.QUOTE);
			
			if (bornOn > 0 && (Integer.valueOf(String.valueOf(bornOn).substring(4)) == i)) {
				 sb.append(StringPool.SPACE);
				 sb.append("selected");
			}
			
			sb.append(">");
			sb.append(months[i]);
			sb.append(StringPool.SPACE);
			sb.append(StringPool.OPEN_PARENTHESIS);
			sb.append(String.format("%02d",i+1));
			sb.append(StringPool.CLOSE_PARENTHESIS);
			sb.append("</option>");
		}
		return sb.toString();
	}
	
	public static String getYears(Locale locale, int bornOn) {
		StringBuilder sb = new StringBuilder();
		sb.append("<option>Year</option>");

		int curYear = Calendar.getInstance().get(Calendar.YEAR);
		int start = curYear - 70;
		int end = curYear - 12;
		for (int i=end; i>start; i--) {
			sb.append("<option value=");
			sb.append(StringPool.QUOTE);
			sb.append(i);
			sb.append(StringPool.QUOTE);
			
			if (bornOn > 0 && (Integer.valueOf(String.valueOf(bornOn).substring(0,4)) == i)) {
				 sb.append(StringPool.SPACE);
				 sb.append("selected");
			}
			
			sb.append(">");
			sb.append(i);
			sb.append("</option>");
		}	
		
		return sb.toString();
	}
	
	public static String getAgeList(Locale locale, int age, boolean bride) {
		StringBuilder sb = new StringBuilder();
		
		int minLimit = bride? 20 : 10;
		int maxLimit = bride? 70 : 60;
		
		for (int i=minLimit; i<=maxLimit; i++) {
			sb.append("<option value=");
			sb.append(StringPool.QUOTE);
			sb.append(i);
			sb.append(StringPool.QUOTE);
			
			if (i == age) {
				sb.append(StringPool.SPACE);
				sb.append("selected");
			}
			
			sb.append(">");
			sb.append(i);
			sb.append("</option>");
		}
		
		return sb.toString();
	}
	
	public static String getHeightList(Locale locale, int currValue) {
		StringBuilder sb = new StringBuilder();
		
		for (int i=140; i<=190; i++) {
			sb.append("<option value=");
			sb.append(StringPool.QUOTE);
			sb.append(i);
			sb.append(StringPool.QUOTE);
			
			if (i == currValue) {
				sb.append(StringPool.SPACE);
				sb.append("selected");
			}
			
			sb.append(">");
			sb.append(getHeightText(i, false));
			sb.append("</option>");
		}
		
		return sb.toString();
	}
	
	public static String getWeightList(Locale locale, int currValue) {
		StringBuilder sb = new StringBuilder();
		
		for (int i=30; i<=120; i++) {
			sb.append("<option value=");
			sb.append(StringPool.QUOTE);
			sb.append(i);
			sb.append(StringPool.QUOTE);
			
			if (i == currValue) {
				sb.append(StringPool.SPACE);
				sb.append("selected");
			}
			
			sb.append(">");
			sb.append(getWeightText(i, false));
			sb.append("</option>");
		}
		
		return sb.toString();				
	}
	
	public static String getChecked(String csv, String listTypeId) {
		String checked = StringPool.BLANK;
		
		if (Validator.isNotNull(csv)) {
			String[] items = csv.split(StringPool.COMMA);
			
			for (int i=0; i<items.length; i++) {
				if (items[i].equalsIgnoreCase(listTypeId)) {
					checked = "checked";
					break;
				}
			}
		}
		
		return checked;
	}
	
	public static List<KeyValuePair> getMaritalStatus(boolean bride) {
		
		List<KeyValuePair> kvPairs = new ArrayList<KeyValuePair>();
		String type = Profile.class.getName() + StringPool.PERIOD + IConstants.LIST_MARITAL_STATUS;
		
		try {
			List<ListType> mStatusItems = ListTypeServiceUtil.getListTypes(type);
			
			for (ListType listType: mStatusItems) { 
				if (!bride && listType.getName().endsWith("married")) continue;
				kvPairs.add(new KeyValuePair(String.valueOf(listType.getListTypeId()), listType.getName()));
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return kvPairs;
	}
	
	public static List<Integer> getIntegers(String csv) {
		
		List<Integer> items = new ArrayList<Integer>();
		if (Validator.isNotNull(csv)) {
			String[] parts = csv.split(StringPool.COMMA);
			for (int i=0; i<parts.length; i++) {
				items.add(Integer.parseInt(parts[i]));
			}
		}		
		return items;
	}
	
	 public static String getComplexionsList(Locale locale, Profile profile) {
         return getOptions(locale, profile, IConstants.LIST_COMPLEXION, profile.getComplexion());
	 }
	 
	public static String getCountriesList(long currValue) {
		StringBuilder sb = new StringBuilder();
		
		try {
			List<Country> countries = CountryServiceUtil.getCountries(false);
			
			for (Country country: countries) {
				long countryId = country.getCountryId();
				sb.append("<option value=");
				sb.append(StringPool.QUOTE);
				sb.append(countryId);
				sb.append(StringPool.QUOTE);
				
				if (countryId == currValue) {
					sb.append(StringPool.SPACE);
					sb.append("selected");
				}
				
				sb.append(">");
				sb.append(TextFormatter.formatName(country.getName()));
				sb.append("</option>");
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return sb.toString();				
	}	 
}