package com.fingence;

import com.fingence.slayer.service.PortfolioLocalServiceUtil;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;

public class FingenceListener extends BaseMessageListener{

	@Override
	protected void doReceive(Message message) throws Exception {
		String messageName = (String)message.get("MESSAGE_NAME");
		
		if (messageName.equalsIgnoreCase("setConvertionRate")) {
			PortfolioLocalServiceUtil.applyConversion(message.getLong("portfolioId"));
		}
	}
}