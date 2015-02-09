package com.slayer.service.persistence;

import java.util.List;

import com.slayer.model.Location;
import com.slayer.model.impl.LocationImpl;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

public class LocationFinderImpl extends BasePersistenceImpl<Location> implements LocationFinder {

	static String QUERY_1 = LocationFinderImpl.class.getName() + ".locateCity";
	static String QUERY_2 = LocationFinderImpl.class.getName() + ".locateCityIP";
	static String QUERY_3 = LocationFinderImpl.class.getName() + ".locateRegion";
	
	public Location getCity(String country, String region, String city) {
		Location _city = null;
		
		Session session = openSession();
		String sql = CustomSQLUtil.get(QUERY_1);
		
		SQLQuery query = session.createSQLQuery(sql);
		
		query.addEntity("Location", LocationImpl.class);
		QueryPos qPos = QueryPos.getInstance(query);
		qPos.add(country);
		qPos.add(region);
		qPos.add(city);
		
		@SuppressWarnings("unchecked")
		List<Location> result = query.list();
		
		if (Validator.isNotNull(result) && !result.isEmpty()) {
			_city = result.get(0);
		}

		closeSession(session);
		
		return _city;
	}
	
	public Location getCity(long countryId, long regionId, long cityId) {
		Location _city = null;
		
		Session session = openSession();
		String sql = CustomSQLUtil.get(QUERY_2);
		
		SQLQuery query = session.createSQLQuery(sql);
		
		query.addEntity("Location", LocationImpl.class);
		QueryPos qPos = QueryPos.getInstance(query);
		qPos.add(countryId);
		qPos.add(regionId);
		qPos.add(cityId);
		
		@SuppressWarnings("unchecked")
		List<Location> result = query.list();
		
		if (Validator.isNotNull(result) && !result.isEmpty()) {
			_city = result.get(0);
		}

		closeSession(session);
		
		return _city;
	}	
	
	public Location getRegion(String country, String region) {
		Location _region = null;
		
		Session session = openSession();
		String sql = CustomSQLUtil.get(QUERY_3);
		
		SQLQuery query = session.createSQLQuery(sql);
		
		query.addEntity("Location", LocationImpl.class);
		QueryPos qPos = QueryPos.getInstance(query);
		qPos.add(country);
		qPos.add(region);		
		
		@SuppressWarnings("unchecked")
		List<Location> result = query.list();
		
		if (Validator.isNotNull(result) && !result.isEmpty()) {
			_region = result.get(0);
		}		

		closeSession(session);
		
		return _region;
	}
}