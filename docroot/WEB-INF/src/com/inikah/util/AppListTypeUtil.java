package com.inikah.util;

import java.util.List;

import com.inikah.slayer.model.Profile;
import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.ListType;
import com.liferay.portal.service.ListTypeServiceUtil;
import com.liferay.portal.service.persistence.ListTypeUtil;

public class AppListTypeUtil {
	public static void createListItem(String type, String value) {
		
		boolean itemExists = false;
		
		String prefix = Profile.class.getName() + StringPool.PERIOD;
		
		String name = prefix + type + StringPool.PERIOD + value;
		
		try {			
			List<ListType> items = ListTypeServiceUtil.getListTypes(prefix + type);
			
			for (ListType item: items) {
				if (item.getName().equalsIgnoreCase(name)) {
					itemExists = true;
					break;
				}
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		// create item for the first time
		if (itemExists) return;
		
		long listTypeId = 0l;
		try {
			listTypeId = CounterLocalServiceUtil.increment("list-item-id");
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		ListType listType = ListTypeUtil.create((int)listTypeId);
		
		listType.setName(name);
		listType.setType(prefix + type);
		
		try {
			ListTypeUtil.update(listType);
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
}