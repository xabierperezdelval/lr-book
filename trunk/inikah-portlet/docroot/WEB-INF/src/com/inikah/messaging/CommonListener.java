package com.inikah.messaging;

import com.inikah.slayer.service.PhotoLocalServiceUtil;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;

public class CommonListener extends BaseMessageListener {

	@Override
	protected void doReceive(Message message) throws Exception {

		String messageName = (String)message.get("messageName");
		
		if (messageName.equalsIgnoreCase("createThumbnail")) {
			long imageId = message.getLong("imageId");			
			PhotoLocalServiceUtil.createThumbnail(imageId);
		}
	}
}
