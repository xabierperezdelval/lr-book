package com.library.custom;

import 
com.liferay.portlet.expando.model.BaseCustomAttributesDisplay;
import com.slayer.model.LMSBook;

public class LMSBookCustomFieldsDisplay 
		extends BaseCustomAttributesDisplay {

	public String getClassName() {
		return LMSBook.class.getName();
	}
}