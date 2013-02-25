package com.library.asset;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portlet.asset.model.AssetRenderer;
import com.liferay.portlet.asset.model.BaseAssetRendererFactory;
import com.slayer.model.LMSBook;
import com.slayer.service.LMSBookLocalServiceUtil;

public class LMSBookAssetRendererFactory extends BaseAssetRendererFactory {

	public AssetRenderer getAssetRenderer(long bookId, int type)
			throws PortalException, SystemException {
		
		LMSBook lmsBook = LMSBookLocalServiceUtil.fetchLMSBook(bookId);
		
		return new LMSBookAssetRenderer(lmsBook);
	}
	
	public String getClassName() {
		return LMSBook.class.getName();
	}
	
	public String getType() {
		return "book";
	}
	
	public boolean isLinkable() {
		return true;
	}

}