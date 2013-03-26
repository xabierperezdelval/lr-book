package com.mpower.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.WindowState;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;

public class InvitationUtil {
	
	public static List<String> extractEmails(String text) {
		final Pattern linePattern = Pattern.compile("([\\w+|\\.?]+)\\w+@([\\w+|\\.?]+)\\.(\\w{2,8}\\w?)");
		List<String> emails = new ArrayList<String>();
		Matcher pattern = linePattern.matcher(text); // Line matcher
		
		while (pattern.find()) {
			emails.add(pattern.group());
		}
		
		// remove duplicates from the list. 
		Set<String> uniqueEmails = new HashSet<String>(emails);
		
		return new ArrayList<String>(uniqueEmails);
	}
	
	public static String getCreateAccountURL(ThemeDisplay themeDisplay) {
		Layout layout = null;
		try {
			layout = LayoutLocalServiceUtil.fetchFirstLayout(themeDisplay.getScopeGroupId(), false, 0l);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(themeDisplay.getURLPortal());
		sb.append(layout.getFriendlyURL());
		sb.append("?p_p_id=58");
		sb.append("&p_p_lifecycle=").append(PortletRequest.RENDER_PHASE);
		sb.append("&p_p_state=").append(WindowState.MAXIMIZED);
		sb.append("&p_p_mode=").append(PortletMode.VIEW);
		sb.append("&saveLastPath=0");
		sb.append("&_58_struts_action=");
		sb.append(HttpUtil.encodeURL("/login/create_account"));
		sb.append("&inviterId=").append(themeDisplay.getUserId());
		
		return sb.toString();
	}
}
