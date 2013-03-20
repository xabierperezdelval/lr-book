package com.mpower.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvitationUtil {
	
	public static List<String> extractEmails(String text) {
		final Pattern linePattern = Pattern.compile("([\\w+|\\.?]+)\\w+@([\\w+|\\.?]+)\\.(\\w{2,8}\\w?)");
		List<String> emails = new ArrayList<String>();
		Matcher pattern = linePattern.matcher(text); // Line matcher
		
		while (pattern.find()) {
			emails.add(pattern.group());
		}
		return emails;
	}
	
	/*
	public static void  getRank(HttpServletRequest request) throws SystemException {
			
		List<UserRank> ranks=null;
		final String append=",";
		ranks = InvitationCardLocalServiceUtil.findByCount();
		Map<Integer , String> rankMap = new LinkedHashMap<Integer, String>();
		for(UserRank userRank: ranks){
			long userId=userRank.getUserId();
			int count=userRank.getCountofAcceptedInvitations();
			String val = rankMap.get(count);
			
			if(val==null){
				rankMap.put(count, String.valueOf(userId));
			}else{
				rankMap.put(count, val + append + String.valueOf(userId));
			}
		
		}
		request.setAttribute("mapObject", rankMap);
	}

	
	public static List<InvitationCard> getInvitationByStatus(int status)
			throws SystemException {
		
		List<InvitationCard> invitationCard = null;
		try {
			invitationCard = InvitationCardLocalServiceUtil.findbystatus(status);
		} catch (Exception e) {

		}
		return invitationCard;

	}

	public static int caluclatePoints(ThemeDisplay themedisplay,
			RenderRequest request) throws SystemException {

		ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		PortletPreferences preferences = request.getPreferences();
	
		String acceptancePoints = preferences.getValue("accept", "");
		int emailAcceptancePoints = Integer.parseInt(acceptancePoints);
		String sentPoints = preferences.getValue("sent", "");
		int emailSentPoints = Integer.parseInt(sentPoints);

		// points Calculation
		List<InvitationCard> pointCaluclationsInvited = InvitationCardLocalServiceUtil
					.findByUserIdSatus(themeDisplay.getUserId(),InvitationConstants.STATUS_INVITED);
		
		int pointSize = pointCaluclationsInvited.size();
		int invitationsPoints = emailSentPoints * pointSize;
		
		List<InvitationCard> pointCaluclationAccepted = InvitationCardLocalServiceUtil
				.findByUserIdSatus(themeDisplay.getUserId(),InvitationConstants.STATUS_ACCEPTED);
		
		int acceptedSize = pointCaluclationAccepted.size();
		int invitationsAccept = emailAcceptancePoints * acceptedSize;
		int pointCaluclation = invitationsAccept + invitationsPoints;
		
		return pointCaluclation;

	}
	*/
}
