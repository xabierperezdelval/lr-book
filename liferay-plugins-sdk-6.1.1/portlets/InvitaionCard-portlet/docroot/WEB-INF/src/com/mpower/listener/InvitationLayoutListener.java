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

public class InvitationLayoutListener implements PortletLayoutListener {

	public void onAddToLayout(String portletId, long plid)
			throws PortletLayoutListenerException {
		
		String messageBody = ContentUtil.get("/com/mpower/listener/invitation.tmpl");
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
		
		try {
			portletPreferences.setValue("newMessageBody",messageBody );
			portletPreferences.setValue("accept", "5");
			portletPreferences.setValue("sent", "2");
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