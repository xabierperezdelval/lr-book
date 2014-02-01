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

import com.inikah.slayer.NoSuchEarningException;
import com.inikah.slayer.model.Earning;
import com.inikah.slayer.model.impl.EarningImpl;
import com.inikah.slayer.model.impl.EarningModelImpl;

import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
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
import com.liferay.portal.kernel.util.StringPool;
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
 * The persistence implementation for the earning service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Ahmed Hasan
 * @see EarningPersistence
 * @see EarningUtil
 * @generated
 */
public class EarningPersistenceImpl extends BasePersistenceImpl<Earning>
	implements EarningPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link EarningUtil} to access the earning persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = EarningImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(EarningModelImpl.ENTITY_CACHE_ENABLED,
			EarningModelImpl.FINDER_CACHE_ENABLED, EarningImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(EarningModelImpl.ENTITY_CACHE_ENABLED,
			EarningModelImpl.FINDER_CACHE_ENABLED, EarningImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(EarningModelImpl.ENTITY_CACHE_ENABLED,
			EarningModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_USERID = new FinderPath(EarningModelImpl.ENTITY_CACHE_ENABLED,
			EarningModelImpl.FINDER_CACHE_ENABLED, EarningImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUserId",
			new String[] {
				Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID =
		new FinderPath(EarningModelImpl.ENTITY_CACHE_ENABLED,
			EarningModelImpl.FINDER_CACHE_ENABLED, EarningImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUserId",
			new String[] { Long.class.getName() },
			EarningModelImpl.USERID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_USERID = new FinderPath(EarningModelImpl.ENTITY_CACHE_ENABLED,
			EarningModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUserId",
			new String[] { Long.class.getName() });

	/**
	 * Returns all the earnings where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the matching earnings
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Earning> findByUserId(long userId) throws SystemException {
		return findByUserId(userId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the earnings where userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.EarningModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param start the lower bound of the range of earnings
	 * @param end the upper bound of the range of earnings (not inclusive)
	 * @return the range of matching earnings
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Earning> findByUserId(long userId, int start, int end)
		throws SystemException {
		return findByUserId(userId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the earnings where userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.EarningModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param start the lower bound of the range of earnings
	 * @param end the upper bound of the range of earnings (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching earnings
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Earning> findByUserId(long userId, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID;
			finderArgs = new Object[] { userId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_USERID;
			finderArgs = new Object[] { userId, start, end, orderByComparator };
		}

		List<Earning> list = (List<Earning>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (Earning earning : list) {
				if ((userId != earning.getUserId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(3 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_EARNING_WHERE);

			query.append(_FINDER_COLUMN_USERID_USERID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(EarningModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				if (!pagination) {
					list = (List<Earning>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Earning>(list);
				}
				else {
					list = (List<Earning>)QueryUtil.list(q, getDialect(),
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
	 * Returns the first earning in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching earning
	 * @throws com.inikah.slayer.NoSuchEarningException if a matching earning could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Earning findByUserId_First(long userId,
		OrderByComparator orderByComparator)
		throws NoSuchEarningException, SystemException {
		Earning earning = fetchByUserId_First(userId, orderByComparator);

		if (earning != null) {
			return earning;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEarningException(msg.toString());
	}

	/**
	 * Returns the first earning in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching earning, or <code>null</code> if a matching earning could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Earning fetchByUserId_First(long userId,
		OrderByComparator orderByComparator) throws SystemException {
		List<Earning> list = findByUserId(userId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last earning in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching earning
	 * @throws com.inikah.slayer.NoSuchEarningException if a matching earning could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Earning findByUserId_Last(long userId,
		OrderByComparator orderByComparator)
		throws NoSuchEarningException, SystemException {
		Earning earning = fetchByUserId_Last(userId, orderByComparator);

		if (earning != null) {
			return earning;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEarningException(msg.toString());
	}

	/**
	 * Returns the last earning in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching earning, or <code>null</code> if a matching earning could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Earning fetchByUserId_Last(long userId,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByUserId(userId);

		if (count == 0) {
			return null;
		}

		List<Earning> list = findByUserId(userId, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the earnings before and after the current earning in the ordered set where userId = &#63;.
	 *
	 * @param earningId the primary key of the current earning
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next earning
	 * @throws com.inikah.slayer.NoSuchEarningException if a earning with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Earning[] findByUserId_PrevAndNext(long earningId, long userId,
		OrderByComparator orderByComparator)
		throws NoSuchEarningException, SystemException {
		Earning earning = findByPrimaryKey(earningId);

		Session session = null;

		try {
			session = openSession();

			Earning[] array = new EarningImpl[3];

			array[0] = getByUserId_PrevAndNext(session, earning, userId,
					orderByComparator, true);

			array[1] = earning;

			array[2] = getByUserId_PrevAndNext(session, earning, userId,
					orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected Earning getByUserId_PrevAndNext(Session session, Earning earning,
		long userId, OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_EARNING_WHERE);

		query.append(_FINDER_COLUMN_USERID_USERID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(EarningModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(userId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(earning);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<Earning> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the earnings where userId = &#63; from the database.
	 *
	 * @param userId the user ID
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeByUserId(long userId) throws SystemException {
		for (Earning earning : findByUserId(userId, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, null)) {
			remove(earning);
		}
	}

	/**
	 * Returns the number of earnings where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the number of matching earnings
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByUserId(long userId) throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_USERID;

		Object[] finderArgs = new Object[] { userId };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_EARNING_WHERE);

			query.append(_FINDER_COLUMN_USERID_USERID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_USERID_USERID_2 = "earning.userId = ?";

	public EarningPersistenceImpl() {
		setModelClass(Earning.class);
	}

	/**
	 * Caches the earning in the entity cache if it is enabled.
	 *
	 * @param earning the earning
	 */
	@Override
	public void cacheResult(Earning earning) {
		EntityCacheUtil.putResult(EarningModelImpl.ENTITY_CACHE_ENABLED,
			EarningImpl.class, earning.getPrimaryKey(), earning);

		earning.resetOriginalValues();
	}

	/**
	 * Caches the earnings in the entity cache if it is enabled.
	 *
	 * @param earnings the earnings
	 */
	@Override
	public void cacheResult(List<Earning> earnings) {
		for (Earning earning : earnings) {
			if (EntityCacheUtil.getResult(
						EarningModelImpl.ENTITY_CACHE_ENABLED,
						EarningImpl.class, earning.getPrimaryKey()) == null) {
				cacheResult(earning);
			}
			else {
				earning.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all earnings.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(EarningImpl.class.getName());
		}

		EntityCacheUtil.clearCache(EarningImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the earning.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(Earning earning) {
		EntityCacheUtil.removeResult(EarningModelImpl.ENTITY_CACHE_ENABLED,
			EarningImpl.class, earning.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<Earning> earnings) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Earning earning : earnings) {
			EntityCacheUtil.removeResult(EarningModelImpl.ENTITY_CACHE_ENABLED,
				EarningImpl.class, earning.getPrimaryKey());
		}
	}

	/**
	 * Creates a new earning with the primary key. Does not add the earning to the database.
	 *
	 * @param earningId the primary key for the new earning
	 * @return the new earning
	 */
	@Override
	public Earning create(long earningId) {
		Earning earning = new EarningImpl();

		earning.setNew(true);
		earning.setPrimaryKey(earningId);

		return earning;
	}

	/**
	 * Removes the earning with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param earningId the primary key of the earning
	 * @return the earning that was removed
	 * @throws com.inikah.slayer.NoSuchEarningException if a earning with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Earning remove(long earningId)
		throws NoSuchEarningException, SystemException {
		return remove((Serializable)earningId);
	}

	/**
	 * Removes the earning with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the earning
	 * @return the earning that was removed
	 * @throws com.inikah.slayer.NoSuchEarningException if a earning with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Earning remove(Serializable primaryKey)
		throws NoSuchEarningException, SystemException {
		Session session = null;

		try {
			session = openSession();

			Earning earning = (Earning)session.get(EarningImpl.class, primaryKey);

			if (earning == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchEarningException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(earning);
		}
		catch (NoSuchEarningException nsee) {
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
	protected Earning removeImpl(Earning earning) throws SystemException {
		earning = toUnwrappedModel(earning);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(earning)) {
				earning = (Earning)session.get(EarningImpl.class,
						earning.getPrimaryKeyObj());
			}

			if (earning != null) {
				session.delete(earning);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (earning != null) {
			clearCache(earning);
		}

		return earning;
	}

	@Override
	public Earning updateImpl(com.inikah.slayer.model.Earning earning)
		throws SystemException {
		earning = toUnwrappedModel(earning);

		boolean isNew = earning.isNew();

		EarningModelImpl earningModelImpl = (EarningModelImpl)earning;

		Session session = null;

		try {
			session = openSession();

			if (earning.isNew()) {
				session.save(earning);

				earning.setNew(false);
			}
			else {
				session.merge(earning);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !EarningModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		else {
			if ((earningModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						earningModelImpl.getOriginalUserId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID,
					args);

				args = new Object[] { earningModelImpl.getUserId() };

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID,
					args);
			}
		}

		EntityCacheUtil.putResult(EarningModelImpl.ENTITY_CACHE_ENABLED,
			EarningImpl.class, earning.getPrimaryKey(), earning);

		return earning;
	}

	protected Earning toUnwrappedModel(Earning earning) {
		if (earning instanceof EarningImpl) {
			return earning;
		}

		EarningImpl earningImpl = new EarningImpl();

		earningImpl.setNew(earning.isNew());
		earningImpl.setPrimaryKey(earning.getPrimaryKey());

		earningImpl.setEarningId(earning.getEarningId());
		earningImpl.setUserId(earning.getUserId());
		earningImpl.setAmount(earning.getAmount());
		earningImpl.setCreateDate(earning.getCreateDate());
		earningImpl.setDebit(earning.isDebit());
		earningImpl.setDetails(earning.getDetails());

		return earningImpl;
	}

	/**
	 * Returns the earning with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the earning
	 * @return the earning
	 * @throws com.inikah.slayer.NoSuchEarningException if a earning with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Earning findByPrimaryKey(Serializable primaryKey)
		throws NoSuchEarningException, SystemException {
		Earning earning = fetchByPrimaryKey(primaryKey);

		if (earning == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchEarningException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return earning;
	}

	/**
	 * Returns the earning with the primary key or throws a {@link com.inikah.slayer.NoSuchEarningException} if it could not be found.
	 *
	 * @param earningId the primary key of the earning
	 * @return the earning
	 * @throws com.inikah.slayer.NoSuchEarningException if a earning with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Earning findByPrimaryKey(long earningId)
		throws NoSuchEarningException, SystemException {
		return findByPrimaryKey((Serializable)earningId);
	}

	/**
	 * Returns the earning with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the earning
	 * @return the earning, or <code>null</code> if a earning with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Earning fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		Earning earning = (Earning)EntityCacheUtil.getResult(EarningModelImpl.ENTITY_CACHE_ENABLED,
				EarningImpl.class, primaryKey);

		if (earning == _nullEarning) {
			return null;
		}

		if (earning == null) {
			Session session = null;

			try {
				session = openSession();

				earning = (Earning)session.get(EarningImpl.class, primaryKey);

				if (earning != null) {
					cacheResult(earning);
				}
				else {
					EntityCacheUtil.putResult(EarningModelImpl.ENTITY_CACHE_ENABLED,
						EarningImpl.class, primaryKey, _nullEarning);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(EarningModelImpl.ENTITY_CACHE_ENABLED,
					EarningImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return earning;
	}

	/**
	 * Returns the earning with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param earningId the primary key of the earning
	 * @return the earning, or <code>null</code> if a earning with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Earning fetchByPrimaryKey(long earningId) throws SystemException {
		return fetchByPrimaryKey((Serializable)earningId);
	}

	/**
	 * Returns all the earnings.
	 *
	 * @return the earnings
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Earning> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the earnings.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.EarningModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of earnings
	 * @param end the upper bound of the range of earnings (not inclusive)
	 * @return the range of earnings
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Earning> findAll(int start, int end) throws SystemException {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the earnings.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.EarningModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of earnings
	 * @param end the upper bound of the range of earnings (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of earnings
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Earning> findAll(int start, int end,
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

		List<Earning> list = (List<Earning>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_EARNING);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_EARNING;

				if (pagination) {
					sql = sql.concat(EarningModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<Earning>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Earning>(list);
				}
				else {
					list = (List<Earning>)QueryUtil.list(q, getDialect(),
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
	 * Removes all the earnings from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeAll() throws SystemException {
		for (Earning earning : findAll()) {
			remove(earning);
		}
	}

	/**
	 * Returns the number of earnings.
	 *
	 * @return the number of earnings
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

				Query q = session.createQuery(_SQL_COUNT_EARNING);

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
	 * Initializes the earning persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.util.service.ServiceProps.get(
						"value.object.listener.com.inikah.slayer.model.Earning")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<Earning>> listenersList = new ArrayList<ModelListener<Earning>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<Earning>)InstanceFactory.newInstance(
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
		EntityCacheUtil.removeCache(EarningImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_EARNING = "SELECT earning FROM Earning earning";
	private static final String _SQL_SELECT_EARNING_WHERE = "SELECT earning FROM Earning earning WHERE ";
	private static final String _SQL_COUNT_EARNING = "SELECT COUNT(earning) FROM Earning earning";
	private static final String _SQL_COUNT_EARNING_WHERE = "SELECT COUNT(earning) FROM Earning earning WHERE ";
	private static final String _ORDER_BY_ENTITY_ALIAS = "earning.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No Earning exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No Earning exists with the key {";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
				PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
	private static Log _log = LogFactoryUtil.getLog(EarningPersistenceImpl.class);
	private static Earning _nullEarning = new EarningImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<Earning> toCacheModel() {
				return _nullEarningCacheModel;
			}
		};

	private static CacheModel<Earning> _nullEarningCacheModel = new CacheModel<Earning>() {
			@Override
			public Earning toEntityModel() {
				return _nullEarning;
			}
		};
}