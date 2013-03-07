package com.library.listener;

import java.util.List;

import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.model.Subscription;
import com.liferay.portal.model.User;
import com.liferay.portal.service.SubscriptionLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.slayer.model.LMSBook;

public class NewBookListener extends BaseMessageListener {

	protected void doReceive(Message message) throws Exception {
		
		System.out.println("inside doReceive method....");
		
		LMSBook lmsBook = (LMSBook) message.getPayload();
		
		System.out.println(lmsBook.toXmlString());
		
		long companyId = lmsBook.getCompanyId();
		long classPK = lmsBook.getGroupId();
		
		List<Subscription> librarySubscriptions = 
			SubscriptionLocalServiceUtil.getSubscriptions(
				companyId, LMSBook.class.getName(), classPK);
		
		for (Subscription subscription: librarySubscriptions) {
			User user = UserLocalServiceUtil.fetchUser(
				subscription.getUserId());
			
			// send notification to the user.
		}
	}
}
