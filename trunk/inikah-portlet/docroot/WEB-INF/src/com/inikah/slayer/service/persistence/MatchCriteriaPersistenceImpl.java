/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.inikah.slayer.service.persistence;

import com.inikah.slayer.NoSuchMatchCriteriaException;
import com.inikah.slayer.model.MatchCriteria;
import com.inikah.slayer.model.impl.MatchCriteriaImpl;
import com.inikah.slayer.model.impl.MatchCriteriaModelImpl;

import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnmodifiableList;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The persistence implementation for the match criteria service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Ahmed Hasan
 * @see MatchCriteriaPersistence
 * @see MatchCriteriaUtil
 * @generated
 */
public class MatchCriteriaPersistenceImpl extends BasePersistenceImpl<MatchCriteria>
	implements MatchCriteriaPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link MatchCriteriaUtil} to access the match criteria persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = MatchCriteriaImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(MatchCriteriaModelImpl.ENTITY_CACHE_ENABLED,
			MatchCriteriaModelImpl.FINDER_CACHE_ENABLED,
			MatchCriteriaImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(MatchCriteriaModelImpl.ENTITY_CACHE_ENABLED,
			MatchCriteriaModelImpl.FINDER_CACHE_ENABLED,
			MatchCriteriaImpl.class, FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(MatchCriteriaModelImpl.ENTITY_CACHE_ENABLED,
			MatchCriteriaModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);

	public MatchCriteriaPersistenceImpl() {
		setModelClass(MatchCriteria.class);
	}

	/**
	 * Caches the match criteria in the entity cache if it is enabled.
	 *
	 * @param matchCriteria the match criteria
	 */
	@Override
	public void cacheResult(MatchCriteria matchCriteria) {
		EntityCacheUtil.putResult(MatchCriteriaModelImpl.ENTITY_CACHE_ENABLED,
			MatchCriteriaImpl.class, matchCriteria.getPrimaryKey(),
			matchCriteria);

		matchCriteria.resetOriginalValues();
	}

	/**
	 * Caches the match criterias in the entity cache if it is enabled.
	 *
	 * @param matchCriterias the match criterias
	 */
	@Override
	public void cacheResult(List<MatchCriteria> matchCriterias) {
		for (MatchCriteria matchCriteria : matchCriterias) {
			if (EntityCacheUtil.getResult(
						MatchCriteriaModelImpl.ENTITY_CACHE_ENABLED,
						MatchCriteriaImpl.class, matchCriteria.getPrimaryKey()) == null) {
				cacheResult(matchCriteria);
			}
			else {
				matchCriteria.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all match criterias.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(MatchCriteriaImpl.class.getName());
		}

		EntityCacheUtil.clearCache(MatchCriteriaImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the match criteria.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(MatchCriteria matchCriteria) {
		EntityCacheUtil.removeResult(MatchCriteriaModelImpl.ENTITY_CACHE_ENABLED,
			MatchCriteriaImpl.class, matchCriteria.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<MatchCriteria> matchCriterias) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (MatchCriteria matchCriteria : matchCriterias) {
			EntityCacheUtil.removeResult(MatchCriteriaModelImpl.ENTITY_CACHE_ENABLED,
				MatchCriteriaImpl.class, matchCriteria.getPrimaryKey());
		}
	}

	/**
	 * Creates a new match criteria with the primary key. Does not add the match criteria to the database.
	 *
	 * @param profileId the primary key for the new match criteria
	 * @return the new match criteria
	 */
	@Override
	public MatchCriteria create(long profileId) {
		MatchCriteria matchCriteria = new MatchCriteriaImpl();

		matchCriteria.setNew(true);
		matchCriteria.setPrimaryKey(profileId);

		return matchCriteria;
	}

	/**
	 * Removes the match criteria with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param profileId the primary key of the match criteria
	 * @return the match criteria that was removed
	 * @throws com.inikah.slayer.NoSuchMatchCriteriaException if a match criteria with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public MatchCriteria remove(long profileId)
		throws NoSuchMatchCriteriaException, SystemException {
		return remove((Serializable)profileId);
	}

	/**
	 * Removes the match criteria with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the match criteria
	 * @return the match criteria that was removed
	 * @throws com.inikah.slayer.NoSuchMatchCriteriaException if a match criteria with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public MatchCriteria remove(Serializable primaryKey)
		throws NoSuchMatchCriteriaException, SystemException {
		Session session = null;

		try {
			session = openSession();

			MatchCriteria matchCriteria = (MatchCriteria)session.get(MatchCriteriaImpl.class,
					primaryKey);

			if (matchCriteria == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchMatchCriteriaException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(matchCriteria);
		}
		catch (NoSuchMatchCriteriaException nsee) {
			throw nsee;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected MatchCriteria removeImpl(MatchCriteria matchCriteria)
		throws SystemException {
		matchCriteria = toUnwrappedModel(matchCriteria);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(matchCriteria)) {
				matchCriteria = (MatchCriteria)session.get(MatchCriteriaImpl.class,
						matchCriteria.getPrimaryKeyObj());
			}

			if (matchCriteria != null) {
				session.delete(matchCriteria);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (matchCriteria != null) {
			clearCache(matchCriteria);
		}

		return matchCriteria;
	}

	@Override
	public MatchCriteria updateImpl(
		com.inikah.slayer.model.MatchCriteria matchCriteria)
		throws SystemException {
		matchCriteria = toUnwrappedModel(matchCriteria);

		boolean isNew = matchCriteria.isNew();

		Session session = null;

		try {
			session = openSession();

			if (matchCriteria.isNew()) {
				session.save(matchCriteria);

				matchCriteria.setNew(false);
			}
			else {
				session.merge(matchCriteria);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		EntityCacheUtil.putResult(MatchCriteriaModelImpl.ENTITY_CACHE_ENABLED,
			MatchCriteriaImpl.class, matchCriteria.getPrimaryKey(),
			matchCriteria);

		return matchCriteria;
	}

	protected MatchCriteria toUnwrappedModel(MatchCriteria matchCriteria) {
		if (matchCriteria instanceof MatchCriteriaImpl) {
			return matchCriteria;
		}

		MatchCriteriaImpl matchCriteriaImpl = new MatchCriteriaImpl();

		matchCriteriaImpl.setNew(matchCriteria.isNew());
		matchCriteriaImpl.setPrimaryKey(matchCriteria.getPrimaryKey());

		matchCriteriaImpl.setProfileId(matchCriteria.getProfileId());
		matchCriteriaImpl.setMinAge(matchCriteria.getMinAge());
		matchCriteriaImpl.setMaxAge(matchCriteria.getMaxAge());
		matchCriteriaImpl.setMinHeight(matchCriteria.getMinHeight());
		matchCriteriaImpl.setMaxHeight(matchCriteria.getMaxHeight());
		matchCriteriaImpl.setMaritalStatus(matchCriteria.getMaritalStatus());
		matchCriteriaImpl.setWithNoChildren(matchCriteria.isWithNoChildren());
		matchCriteriaImpl.setMotherTongue(matchCriteria.getMotherTongue());
		matchCriteriaImpl.setMotherTongueExcluded(matchCriteria.isMotherTongueExcluded());
		matchCriteriaImpl.setCommunity(matchCriteria.getCommunity());
		matchCriteriaImpl.setCommunityExcluded(matchCriteria.isCommunityExcluded());
		matchCriteriaImpl.setEducation(matchCriteria.getEducation());
		matchCriteriaImpl.setEducationExcluded(matchCriteria.isEducationExcluded());
		matchCriteriaImpl.setReligiousEducation(matchCriteria.getReligiousEducation());
		matchCriteriaImpl.setReligiousEducationExcluded(matchCriteria.isReligiousEducationExcluded());
		matchCriteriaImpl.setProfession(matchCriteria.getProfession());
		matchCriteriaImpl.setProfessionExcluded(matchCriteria.isProfessionExcluded());
		matchCriteriaImpl.setResidingCountry(matchCriteria.getResidingCountry());
		matchCriteriaImpl.setResidingState(matchCriteria.getResidingState());
		matchCriteriaImpl.setResidingCity(matchCriteria.getResidingCity());
		matchCriteriaImpl.setLocationOfResidenceExcluded(matchCriteria.isLocationOfResidenceExcluded());
		matchCriteriaImpl.setCountryOfBirth(matchCriteria.getCountryOfBirth());
		matchCriteriaImpl.setStateOfBirth(matchCriteria.getStateOfBirth());
		matchCriteriaImpl.setCityOfBirth(matchCriteria.getCityOfBirth());
		matchCriteriaImpl.setLocationOfBirthExcluded(matchCriteria.isLocationOfBirthExcluded());
		matchCriteriaImpl.setRevertedToIslam(matchCriteria.isRevertedToIslam());
		matchCriteriaImpl.setPhyChallenged(matchCriteria.isPhyChallenged());
		matchCriteriaImpl.setHasNoMother(matchCriteria.isHasNoMother());
		matchCriteriaImpl.setHasNoFather(matchCriteria.isHasNoFather());
		matchCriteriaImpl.setMinSons(matchCriteria.getMinSons());
		matchCriteriaImpl.setMaxSons(matchCriteria.getMaxSons());
		matchCriteriaImpl.setMinDauthers(matchCriteria.getMinDauthers());
		matchCriteriaImpl.setMaxDauthers(matchCriteria.getMaxDauthers());
		matchCriteriaImpl.setCreateDate(matchCriteria.getCreateDate());
		matchCriteriaImpl.setModifiedDate(matchCriteria.getModifiedDate());
		matchCriteriaImpl.setTimeTaken(matchCriteria.getTimeTaken());

		return matchCriteriaImpl;
	}

	/**
	 * Returns the match criteria with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the match criteria
	 * @return the match criteria
	 * @throws com.inikah.slayer.NoSuchMatchCriteriaException if a match criteria with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public MatchCriteria findByPrimaryKey(Serializable primaryKey)
		throws NoSuchMatchCriteriaException, SystemException {
		MatchCriteria matchCriteria = fetchByPrimaryKey(primaryKey);

		if (matchCriteria == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchMatchCriteriaException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return matchCriteria;
	}

	/**
	 * Returns the match criteria with the primary key or throws a {@link com.inikah.slayer.NoSuchMatchCriteriaException} if it could not be found.
	 *
	 * @param profileId the primary key of the match criteria
	 * @return the match criteria
	 * @throws com.inikah.slayer.NoSuchMatchCriteriaException if a match criteria with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public MatchCriteria findByPrimaryKey(long profileId)
		throws NoSuchMatchCriteriaException, SystemException {
		return findByPrimaryKey((Serializable)profileId);
	}

	/**
	 * Returns the match criteria with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the match criteria
	 * @return the match criteria, or <code>null</code> if a match criteria with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public MatchCriteria fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		MatchCriteria matchCriteria = (MatchCriteria)EntityCacheUtil.getResult(MatchCriteriaModelImpl.ENTITY_CACHE_ENABLED,
				MatchCriteriaImpl.class, primaryKey);

		if (matchCriteria == _nullMatchCriteria) {
			return null;
		}

		if (matchCriteria == null) {
			Session session = null;

			try {
				session = openSession();

				matchCriteria = (MatchCriteria)session.get(MatchCriteriaImpl.class,
						primaryKey);

				if (matchCriteria != null) {
					cacheResult(matchCriteria);
				}
				else {
					EntityCacheUtil.putResult(MatchCriteriaModelImpl.ENTITY_CACHE_ENABLED,
						MatchCriteriaImpl.class, primaryKey, _nullMatchCriteria);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(MatchCriteriaModelImpl.ENTITY_CACHE_ENABLED,
					MatchCriteriaImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return matchCriteria;
	}

	/**
	 * Returns the match criteria with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param profileId the primary key of the match criteria
	 * @return the match criteria, or <code>null</code> if a match criteria with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public MatchCriteria fetchByPrimaryKey(long profileId)
		throws SystemException {
		return fetchByPrimaryKey((Serializable)profileId);
	}

	/**
	 * Returns all the match criterias.
	 *
	 * @return the match criterias
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<MatchCriteria> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the match criterias.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.MatchCriteriaModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of match criterias
	 * @param end the upper bound of the range of match criterias (not inclusive)
	 * @return the range of match criterias
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<MatchCriteria> findAll(int start, int end)
		throws SystemException {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the match criterias.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.MatchCriteriaModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of match criterias
	 * @param end the upper bound of the range of match criterias (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of match criterias
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<MatchCriteria> findAll(int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL;
			finderArgs = FINDER_ARGS_EMPTY;
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_ALL;
			finderArgs = new Object[] { start, end, orderByComparator };
		}

		List<MatchCriteria> list = (List<MatchCriteria>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_MATCHCRITERIA);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_MATCHCRITERIA;

				if (pagination) {
					sql = sql.concat(MatchCriteriaModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<MatchCriteria>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<MatchCriteria>(list);
				}
				else {
					list = (List<MatchCriteria>)QueryUtil.list(q, getDialect(),
							start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the match criterias from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeAll() throws SystemException {
		for (MatchCriteria matchCriteria : findAll()) {
			remove(matchCriteria);
		}
	}

	/**
	 * Returns the number of match criterias.
	 *
	 * @return the number of match criterias
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countAll() throws SystemException {
		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_MATCHCRITERIA);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Initializes the match criteria persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.util.service.ServiceProps.get(
						"value.object.listener.com.inikah.slayer.model.MatchCriteria")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<MatchCriteria>> listenersList = new ArrayList<ModelListener<MatchCriteria>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<MatchCriteria>)InstanceFactory.newInstance(
							getClassLoader(), listenerClassName));
				}

				listeners = listenersList.toArray(new ModelListener[listenersList.size()]);
			}
			catch (Exception e) {
				_log.error(e);
			}
		}
	}

	public void destroy() {
		EntityCacheUtil.removeCache(MatchCriteriaImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_MATCHCRITERIA = "SELECT matchCriteria FROM MatchCriteria matchCriteria";
	private static final String _SQL_COUNT_MATCHCRITERIA = "SELECT COUNT(matchCriteria) FROM MatchCriteria matchCriteria";
	private static final String _ORDER_BY_ENTITY_ALIAS = "matchCriteria.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No MatchCriteria exists with the primary key ";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
				PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
	private static Log _log = LogFactoryUtil.getLog(MatchCriteriaPersistenceImpl.class);
	private static MatchCriteria _nullMatchCriteria = new MatchCriteriaImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<MatchCriteria> toCacheModel() {
				return _nullMatchCriteriaCacheModel;
			}
		};

	private static CacheModel<MatchCriteria> _nullMatchCriteriaCacheModel = new CacheModel<MatchCriteria>() {
			@Override
			public MatchCriteria toEntityModel() {
				return _nullMatchCriteria;
			}
		};
}