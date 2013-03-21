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
		ranks = SiteInvitationLocalServiceUtil.findRankByCount();
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



	public static int caluclatePoints(long inviterId,RenderRequest request) throws SystemException {
		PortletPreferences preferences = request.getPreferences();
		String acceptancePoints = preferences.getValue("accept", "");
		System.out.println(acceptancePoints);
		int emailAcceptancePoints = Integer.parseInt(acceptancePoints);
		System.out.println(emailAcceptancePoints);
		System.out.println("acceptedddd"+emailAcceptancePoints);
		String sentPoints = preferences.getValue("sent", "");
		int emailSentPoints = Integer.parseInt(sentPoints);
		System.out.println("emailSentPoints"+emailSentPoints);
		// points Calculation
		List<SiteInvitation> pointCaluclationsInvited = SiteInvitationLocalServiceUtil.getUserInvitations(inviterId, InvitationConstants.STATUS_INVITED);
					
		
		int pointSize = pointCaluclationsInvited.size();
		int invitationsPoints = emailSentPoints * pointSize;
		
		List<SiteInvitation> pointCaluclationAccepted = SiteInvitationLocalServiceUtil.getUserInvitations(inviterId, InvitationConstants.STATUS_INVITED);
		int acceptedSize = pointCaluclationAccepted.size();
		int invitationsAccept = emailAcceptancePoints * acceptedSize;
		int pointCaluclation = invitationsAccept + invitationsPoints;
		
		return pointCaluclation;

	}
	
	
	public static List<SiteInvitation> chartDispalay(long inviterId,int status)
			throws SystemException {
		
		List<SiteInvitation> siteInvitation = null;
		try {
			siteInvitation = SiteInvitationLocalServiceUtil.getUserInvitations(inviterId, status);
		} catch (Exception e) {

		}
		return siteInvitation;

	}
	*/

}
