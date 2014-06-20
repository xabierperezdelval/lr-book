package com.fingence.slayer.service.persistence;

import java.util.List;

import com.fingence.slayer.model.MyResult;
import com.fingence.slayer.model.impl.MyResultImpl;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

public class MyResultFinderImpl extends BasePersistenceImpl<MyResult> implements MyResultFinder {

	static String QUERY = MyResultFinderImpl.class.getName() + ".findResults";	
	
	@Override
	public List<MyResult> findResults(String portfolioIds) {
		Session session = openSession();
				
		String sql = StringUtil.replace(CustomSQLUtil.get(QUERY), "[$PORTFOLIO_IDS$]", portfolioIds);
		
		SQLQuery query = session.createSQLQuery(sql);
				
		query.addEntity("MyResult", MyResultImpl.class);
		
		@SuppressWarnings("unchecked")
		List<MyResult> results = query.list();
		
		closeSession(session);
				
		return results;
	}	
}