package com.inikah.util;

import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.StringPool;

public class URLUtil {

	public static String editURL(long profileId) {
		return StringPool.BLANK + Constants.EDIT + StringPool.QUESTION
				+ "profileId" + StringPool.EQUAL + profileId;
	}
}