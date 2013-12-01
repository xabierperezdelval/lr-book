package com.inikah.slayer.service.persistence;

import java.util.List;

import com.inikah.slayer.model.MyKeyValue;
import com.inikah.slayer.model.impl.MyKeyValueImpl;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

public class MyKeyValueFinderImpl extends BasePersistenceImpl<MyKeyValue> 
		implements MyKeyValueFinder {
	
	static String QUERY = MyKeyValueFinderImpl.class.getName() + ".findMotherTongue";

	public List<MyKeyValue> findMotherTongue(boolean bride) { 
		Session session = openSession();
		String sql = CustomSQLUtil.get(QUERY); 
		SQLQuery query = session.createSQLQuery(sql);
		
		query.addEntity("MyKeyValue", MyKeyValueImpl.class);
		QueryPos qPos = QueryPos.getInstance(query);
		qPos.add(bride);
		
		@SuppressWarnings("unchecked")
		List<MyKeyValue> result = query.list();

		closeSession(session);
				
		return result;
	}
}