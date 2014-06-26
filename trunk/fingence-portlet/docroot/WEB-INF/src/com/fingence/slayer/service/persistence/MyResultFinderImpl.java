package com.fingence.slayer.service.persistence;

import java.util.List;

import com.fingence.slayer.model.MyResult;
import com.fingence.slayer.model.impl.MyResultImpl;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

public class MyResultFinderImpl extends BasePersistenceImpl<MyResult> implements MyResultFinder {

	static String QUERY = MyResultFinderImpl.class.getName() + ".findResults";	
	
	public List<MyResult> findResults(String portfolioIds) {
		Session session = openSession();
		
		String[] tokens = {"[$PORTFOLIO_IDS$]", "[$FING_BOND_COLUMNS$]", "[$FING_BOND_TABLE$]", "[$FING_BOND_WHERE_CLAUSE$]"};
		String[] replacements = {portfolioIds, StringPool.BLANK, StringPool.BLANK, StringPool.BLANK};
				
		String sql = StringUtil.replace(CustomSQLUtil.get(QUERY), tokens, replacements);
		
		SQLQuery query = session.createSQLQuery(sql);
				
		query.addEntity("MyResult", MyResultImpl.class);
		
		@SuppressWarnings("unchecked")
		List<MyResult> results = query.list();
		
		closeSession(session);
		
		// new query
		return results;
	}	
}