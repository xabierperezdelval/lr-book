package com.library.social;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.social.model.BaseSocialActivityInterpreter;
import com.liferay.portlet.social.model.SocialActivity;
import com.liferay.portlet.social.model.SocialActivityFeedEntry;
import com.slayer.model.LMSBook;
import com.slayer.service.LMSBookLocalServiceUtil;

public class LibraryActivityInterpreter 
			extends BaseSocialActivityInterpreter {

	public String[] getClassNames() {
		return new String[]{LMSBook.class.getName()};
	}

	protected SocialActivityFeedEntry doInterpret(
			SocialActivity activity, ThemeDisplay themeDisplay) 
				throws Exception {
		
		long bookId = activity.getClassPK();
		
		LMSBook lmsBook =
			LMSBookLocalServiceUtil.fetchLMSBook(bookId);
		
		String link = getLink(themeDisplay, bookId);
		String title = getTitle(activity, 
			lmsBook.getBookTitle(), link, themeDisplay);
		String body = StringPool.BLANK;
		
		return new SocialActivityFeedEntry(link , title, body);
	}
	
	private String getLink(ThemeDisplay themeDisplay, long bookId) {
		
		StringBuilder sb = new StringBuilder()
			.append(themeDisplay.getPathFriendlyURLPublic())
			.append("/guest/my-library/-/library/detail/")
			.append(bookId);
		
		return sb.toString();
	}
	
	private String getTitle(SocialActivity activity,
			String content, String link, ThemeDisplay themeDisplay) {
		String userName = getUserName(
			activity.getUserId(),themeDisplay);
		
		String text = wrapLink(link, content);
		
		String groupName = getGroupName(
				activity.getGroupId(), themeDisplay);
		
		String pattern = 
			"{0} has commented on the book \"{1}\" in the group, {2}";
		
	    return themeDisplay.translate(
	        pattern, new Object[] {userName, text, groupName});
	}	
}