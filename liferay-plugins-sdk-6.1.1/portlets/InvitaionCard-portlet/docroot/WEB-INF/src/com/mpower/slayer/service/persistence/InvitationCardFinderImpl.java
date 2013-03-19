package com.mpower.slayer.service.persistence;

import java.util.ArrayList;
import java.util.List;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;
import com.mpower.slayer.model.UserRank;

public class InvitationCardFinderImpl extends BasePersistenceImpl implements InvitationCardFinder{
	
	static String FIND_CountByUserId = InvitationCardFinderImpl.class.getName()+ ".findCountByUserId";

	public List<UserRank> findCountBy() throws SystemException {
		List<UserRank> userRankList = new ArrayList<UserRank>();
		Session session = openSession();
		String sql = CustomSQLUtil.get(FIND_CountByUserId);
		SQLQuery query = session.createSQLQuery(sql);
		List<Object[]> list = (List<Object[]>) query.list();
		for (Object[] obj : list) {
			UserRank userRanks = new UserRank();
			userRanks.setUserId(Long.valueOf((obj[0].toString())));
			userRanks.setCountofAcceptedInvitations(Integer.parseInt((String.valueOf(obj[1]))));
			userRankList.add(userRanks);
		}
		return userRankList;
	}
}



