package com.fingence.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.LayoutLocalServiceUtil;

public class PageUtil {
	public static long getPageLayoutId(long groupId, String pageName, Locale locale) {
		
		long plid = 0l;
		try {
			List<Layout> layouts = LayoutLocalServiceUtil.getLayouts(groupId, false);
			
			for (Layout layout: layouts) {
				if (layout.getName(locale).equalsIgnoreCase(pageName)) {
					plid = layout.getPlid();
					break;
				}
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return plid;
	}
	
	public static String getFormattedDate(Date date) {
		
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		if (Validator.isNull(date)) {
			date = new Date();
		}
		return dateFormat.format(date);
	}
}