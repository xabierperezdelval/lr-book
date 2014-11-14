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
import com.liferay.util.portlet.PortletProps;

public class MyKeyValueFinderImpl extends BasePersistenceImpl<MyKeyValue> 
		implements MyKeyValueFinder {
	
	static String QUERY = MyKeyValueFinderImpl.class.getName() + ".findResults";
		
	public List<MyKeyValue> findResults(boolean bride, String column, long parentId, String parentColumn) {
		Session session = openSession();
		String sql = CustomSQLUtil.get(QUERY);
		
		String[] tokens = PortletProps.get("breakup_finder_" + column).split(StringPool.COMMA);
		
		String[] tags = {"[$COLUMN_NAME$]", "[$JOIN_TABLE$]", "[$TABLE_PRIM_KEY$]"};
		String[] vals = {column, tokens[0], tokens[1]};
		
		sql = StringUtil.replace(sql, tags, vals);
		
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