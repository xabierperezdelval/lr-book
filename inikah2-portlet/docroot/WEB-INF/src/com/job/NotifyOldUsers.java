package com.job;

import java.util.List;

import com.util.IConstants;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;

public class NotifyOldUsers extends BaseMessageListener {
	
	@Override
	protected void doReceive(Message arg) throws Exception {
		
		update();
	}

	private void update() {
		
		if (!IConstants.CFG_NOTIFY_OLD_USERS) return;
		
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
				User.class, PortalClassLoaderUtil.getClassLoader());
		dynamicQuery.add(RestrictionsFactoryUtil.eq("jobTitle", "mail-not-sent"));
		
		try {
			@SuppressWarnings("unchecked")
			List<User> users = UserLocalServiceUtil.dynamicQuery(dynamicQuery);
			
			int cnt = 0;
			
			for (User user: users) {
				// notifyUser thru email and SMS
				
				user.setJobTitle(StringPool.BLANK);
				user.setLastName(StringPool.BLANK);
				user = UserLocalServiceUtil.updateUser(user);
				cnt++;
				
				if (cnt == 200) break;
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
}