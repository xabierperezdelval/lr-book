package com.fingence.slayer.service.persistence;

import java.util.List;

import com.fingence.slayer.model.MyResult;
import com.fingence.slayer.model.impl.MyResultImpl;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

public class MyResultFinderImpl extends BasePersistenceImpl<MyResult> implements MyResultFinder {

	static String QUERY = MyResultFinderImpl.class.getName() + ".findResults";
	
	public List<MyResult> findResults(long porfolioId) {
		Session session = openSession();
		String sql = CustomSQLUtil.get(QUERY);
		
		SQLQuery query = session.createSQLQuery(sql);
		
		query.addEntity("MyResult", MyResultImpl.class);
		QueryPos qPos = QueryPos.getInstance(query);
		qPos.add(porfolioId);
		
		@SuppressWarnings("unchecked")
		List<MyResult> results = query.list();

		closeSession(session);
				
		return results;
	}	
}