package com.util;

import java.security.Key;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Company;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.util.Encryptor;
import com.liferay.util.EncryptorException;

public class LMSUtil {
	public static String encrypt(String data, long companyId) {
		return transform(data, companyId, true);
	}
	
	public static String decrypt(String data, long companyId) {
		return transform(data, companyId, false);
	}
	
	private static String transform(String data, 
			long companyId, boolean encrypt) {
		
		String transformedData = null;
		
		Company company = null;
		try {
			company = 
				CompanyLocalServiceUtil.fetchCompany(companyId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (company != null) {
			Key keyObj = company.getKeyObj();
			try {
				transformedData = encrypt?
						Encryptor.encrypt(keyObj, data):
						Encryptor.decrypt(keyObj, data);
			} catch (EncryptorException e) {
				e.printStackTrace();
			}
		}
		
		return transformedData;
	}
}