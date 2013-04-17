package com.mpower.slayer.service.persistence;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

public class SiteInvitationFinderImpl extends BasePersistenceImpl implements SiteInvitationFinder{
	static String FIND_CountByUserId = SiteInvitationFinderImpl.class.getName()+ ".breakupByUserId";
	
	public int getUserRank(long userId) {
		
		int rank = 1;
		Session session = openSession();
		String sql = CustomSQLUtil.get(FIND_CountByUserId);
		SQLQuery query = session.createSQLQuery(sql);
		
		List<Object[]> results = (List<Object[]>)query.list();
		
		if (Validator.isNotNull(results) && !results.isEmpty()) {
			int index = 0;
			Map<Integer, List<String>> rankMap = new LinkedHashMap<Integer, List<String>>();
			for (Object[] row: results) {
				String inviterId = row[0].toString();
				Integer count = Integer.valueOf(row[1].toString());
				
				List<String> userIds = rankMap.get(count);
				
				if (Validator.isNull(userIds) || userIds.isEmpty()) {
					userIds = new ArrayList<String>();
					index++;
				}
				
				userIds.add(inviterId);
				rankMap.put(count, userIds);
				
				if (inviterId.equals(String.valueOf(userId))) {
					rank = index;
					break;
				} 
			}
		}
		
		return rank;
	}
}