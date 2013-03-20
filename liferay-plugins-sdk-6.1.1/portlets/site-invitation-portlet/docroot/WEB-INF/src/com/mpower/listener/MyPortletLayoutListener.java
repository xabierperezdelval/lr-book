package com.mpower.listener;

import java.io.IOException;
import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.portlet.PortletLayoutListener;
import com.liferay.portal.kernel.portlet.PortletLayoutListenerException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.util.ContentUtil;
import com.mpower.util.InvitationConstants;

public class MyPortletLayoutListener implements PortletLayoutListener {

	public void onAddToLayout(String portletId, long plid)
			throws PortletLayoutListenerException {
	
		Layout layout = null;
		try {
			layout = LayoutLocalServiceUtil.fetchLayout(plid);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(layout)) return;
		
		PortletPreferences portletPreferences = null;
		try {
			portletPreferences = PortletPreferencesFactoryUtil.getLayoutPortletSetup(layout, portletId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(portletPreferences)) return;
		
		String messageBody = ContentUtil.get(InvitationConstants.EMAIL_TEMPLATE_PATH);
		try {
			portletPreferences.setValue(InvitationConstants.EMAIL_BODY_TEXT, 
				portletPreferences.getValue(InvitationConstants.EMAIL_BODY_TEXT, messageBody));
		} catch (ReadOnlyException e) {
			e.printStackTrace();
		}
		
		try {
			portletPreferences.store();
		} catch (ValidatorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void onMoveInLayout(String arg0, long arg1)
			throws PortletLayoutListenerException {
	}

	public void onRemoveFromLayout(String arg0, long arg1)
			throws PortletLayoutListenerException {
	}
}