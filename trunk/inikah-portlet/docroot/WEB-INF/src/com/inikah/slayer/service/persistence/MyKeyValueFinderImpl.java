package com.inikah.slayer.service.persistence;

import java.util.List;

import com.inikah.slayer.model.MyKeyValue;
import com.inikah.slayer.model.impl.MyKeyValueImpl;
import com.inikah.util.IConstants;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

public class MyKeyValueFinderImpl extends BasePersistenceImpl<MyKeyValue> 
		implements MyKeyValueFinder {
	
	static String QUERY = MyKeyValueFinderImpl.class.getName() + ".findMotherTongue";

	public List<MyKeyValue> findMotherTongue(boolean bride) { 
		return findResults(bride, "motherTongue", 0l, null);
	}
	
	public List<MyKeyValue> findResidingCountries(boolean bride) { 
		return findResults(bride, "residingCountry", 0l, null);
	}
	
	public List<MyKeyValue> findResidingRegions(boolean bride, long countryId) { 
		return findResults(bride, "residingState", countryId, "residingCountry");
	}	
	
	public List<MyKeyValue> findResidingCities(boolean bride, long regionId) { 
		return findResults(bride, "residingCity", regionId, "residingState");
	}	
	
	private List<MyKeyValue> findResults(boolean bride, String column, long parentId, String parentColumn) {
		Session session = openSession();
		String sql = CustomSQLUtil.get(QUERY);
		
		sql = StringUtil.replace(sql, "[$COLUMN_NAME$]", column);
		
		String nextClause = StringPool.BLANK;
		if (Validator.isNotNull(parentColumn) && parentId > 0l) {
			nextClause = " and " + parentColumn + StringPool.EQUAL + StringPool.QUESTION;
		} 
		sql = StringUtil.replace(sql, "[$NEXT_CLAUSE$]", nextClause);
				
		SQLQuery query = session.createSQLQuery(sql);
		
		query.addEntity("MyKeyValue", MyKeyValueImpl.class);
		QueryPos qPos = QueryPos.getInstance(query);
		qPos.add(bride);
		qPos.add(IConstants.PROFILE_STATUS_ACTIVE);
		
		if (parentId > 0l) {
			qPos.add(parentId);
		}
		
		@SuppressWarnings("unchecked")
		List<MyKeyValue> result = query.list();

		closeSession(session);
				
		return result;
	}
}