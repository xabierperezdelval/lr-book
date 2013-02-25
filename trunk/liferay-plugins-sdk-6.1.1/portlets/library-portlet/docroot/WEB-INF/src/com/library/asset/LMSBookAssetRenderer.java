package com.library.asset;

import java.util.Locale;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portlet.asset.model.BaseAssetRenderer;
import com.slayer.model.LMSBook;

public class LMSBookAssetRenderer extends BaseAssetRenderer {

	public long getClassPK() {
		return _lmsBook.getPrimaryKey();
	}
	
	public long getGroupId() {
		return _lmsBook.getGroupId();
	}
	
	public String getSummary(Locale locale) {
		return _lmsBook.getBookTitle();
	}
	
	public String getTitle(Locale locale) {
		return _lmsBook.getBookTitle();
	}
	
	public long getUserId() {
		return _lmsBook.getUserId();
	}
	
	public String getUuid() {
		return _lmsBook.getUuid();
	}	
	
	public String getUserName() {
		
		String userName = StringPool.BLANK;
		
		try {
			User user = UserLocalServiceUtil.fetchUser(getUserId());
			userName = user.getFullName();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return userName;
	}

	public String render(RenderRequest request, 
		RenderResponse response, String template)
			throws Exception {
		
		request.setAttribute("ASSET_ENTRY", _lmsBook);
		String page = "/html/library/asset/abstract.jsp";
		
		if (template.equals(TEMPLATE_FULL_CONTENT)) {
			page = "/html/library/asset/full_content.jsp";
		}
		
		return page;
	}
	
	private LMSBook _lmsBook;
	
	public LMSBookAssetRenderer(LMSBook lmsBook) { 
		_lmsBook = lmsBook;
	}
	
	public boolean isConvertible() {
		return true;
	}
	
	public boolean isLocalizable() {
		return true;
	}
	
	public boolean isPrintable() {
		return true;
	}
	
	public String getDiscussionPath() {
		return "edit_entry_discussion";
	}
}