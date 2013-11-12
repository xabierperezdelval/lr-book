package com.inikah.slayer.service.persistence;

import java.util.List;

import com.inikah.slayer.model.Invitation;
import com.inikah.slayer.model.impl.InvitationImpl;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;


@SuppressWarnings("rawtypes")
public class InvitationFinderImpl extends  BasePersistenceImpl implements InvitationFinder{
	public static String GET_INVITATION = 
			InvitationFinderImpl.class.getName()+ ".findTodaysInvitation";
	public List <Invitation> findTodaysInvitation(long userId,String date) throws SystemException
	{ Session session = openSession();
	String sql = CustomSQLUtil.get(GET_INVITATION);
	SQLQuery query = session.createSQLQuery(sql);
	query.addEntity("Invitation", InvitationImpl.class);
	
	QueryPos qPos = QueryPos.getInstance(query);
	qPos.add (userId);
	qPos.add(date);
	return (List<Invitation>)query.list();
		
	}
}
