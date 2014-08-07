package com.fingence.util;

import java.util.Iterator;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.model.PortletPreferences;
import com.liferay.portal.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portal.util.PortletKeys;

public class PrefsUtil {
	
	public static String getPortletPreferencesXML(String name, String[] values) {

		StringBundler sb = new StringBundler();

		sb.append("<portlet-preferences>");

		if ((name != null) || (values != null)) {
			sb.append("<preference>");

			if (name != null) {
				sb.append("<name>");
				sb.append(name);
				sb.append("</name>");
			}

			if (values != null) {
				for (String value : values) {
					sb.append("<value>");
					sb.append(value);
					sb.append("</value>");
				}
			}

			sb.append("</preference>");
		}

		sb.append("</portlet-preferences>");

		return sb.toString();
	}
	
	public static String getUserPreference(long userId, long plid, String portletId, String preferenceName) {
		String _preference = StringPool.BLANK;
		
		PortletPreferences preferences = null;
		try {
			preferences = PortletPreferencesLocalServiceUtil.getPortletPreferences(userId, PortletKeys.PREFS_OWNER_TYPE_USER, plid, portletId);
		} catch (PortalException e) {
			// ignore
		} catch (SystemException e) {
			// ignore
		}
		
		if (Validator.isNotNull(preferences)) return _preference;
		
		Document document = null;
		try {
			document = SAXReaderUtil.read(preferences.getPreferences());
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		for (Iterator<Element> itr = document.getRootElement().elementIterator("preference"); itr.hasNext();) {
			Element preference = (Element) itr.next();
			
			if (preference.element("name").getText().equalsIgnoreCase(preferenceName)) {
				_preference = preference.element("value").getText();
			}
		}
		
		return _preference;
	}	
}