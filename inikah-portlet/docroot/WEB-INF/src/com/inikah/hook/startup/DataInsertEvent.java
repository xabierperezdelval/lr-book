package com.inikah.hook.startup;

import java.util.Enumeration;
import java.util.ResourceBundle;

import com.inikah.slayer.model.Profile;
import com.inikah.slayer.service.BridgeServiceUtil;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;

public class DataInsertEvent extends SimpleAction {
	/* (non-Java-doc)
	 * @see com.liferay.portal.kernel.events.SimpleAction#SimpleAction()
	 */
	public DataInsertEvent() {
		super();
	}

	/* (non-Java-doc)
	 * @see com.liferay.portal.kernel.events.SimpleAction#run(String[] arg0)
	 */
	public void run(String[] arg0) throws ActionException {
				
		ResourceBundle rb = ResourceBundle.getBundle("content.Language");
		
		Enumeration<String> keys = rb.getKeys();
		
		String prefix = Profile.class.getName() + StringPool.PERIOD;
		
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
						
			if (!key.startsWith(Profile.class.getName())) continue;
						
			String tmp = StringUtil.replace(
				key, prefix, StringPool.BLANK);
						
			String[] parts = tmp.split("\\.");
										
			String type = parts[0];
			String value = parts[1];
	
			BridgeServiceUtil.createListItem(type, value);
		}
	}
}