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

import com.inikah.slayer.NoSuchPlanException;
import com.inikah.slayer.model.Plan;
import com.inikah.slayer.model.impl.PlanImpl;
import com.inikah.slayer.model.impl.PlanModelImpl;

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
 * The persistence implementation for the plan service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Ahmed Hasan
 * @see PlanPersistence
 * @see PlanUtil
 * @generated
 */
public class PlanPersistenceImpl extends BasePersistenceImpl<Plan>
	implements PlanPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link PlanUtil} to access the plan persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = PlanImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(PlanModelImpl.ENTITY_CACHE_ENABLED,
			PlanModelImpl.FINDER_CACHE_ENABLED, PlanImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(PlanModelImpl.ENTITY_CACHE_ENABLED,
			PlanModelImpl.FINDER_CACHE_ENABLED, PlanImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(PlanModelImpl.ENTITY_CACHE_ENABLED,
			PlanModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_COMPANYID =
		new FinderPath(PlanModelImpl.ENTITY_CACHE_ENABLED,
			PlanModelImpl.FINDER_CACHE_ENABLED, PlanImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByCompanyId",
			new String[] {
				Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_COMPANYID =
		new FinderPath(PlanModelImpl.ENTITY_CACHE_ENABLED,
			PlanModelImpl.FINDER_CACHE_ENABLED, PlanImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByCompanyId",
			new String[] { Long.class.getName() },
			PlanModelImpl.COMPANYID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_COMPANYID = new FinderPath(PlanModelImpl.ENTITY_CACHE_ENABLED,
			PlanModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByCompanyId",
			new String[] { Long.class.getName() });

	/**
	 * Returns all the plans where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the matching plans
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Plan> findByCompanyId(long companyId) throws SystemException {
		return findByCompanyId(companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the plans where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.PlanModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of plans
	 * @param end the upper bound of the range of plans (not inclusive)
	 * @return the range of matching plans
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Plan> findByCompanyId(long companyId, int start, int end)
		throws SystemException {
		return findByCompanyId(companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the plans where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.PlanModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of plans
	 * @param end the upper bound of the range of plans (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching plans
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Plan> findByCompanyId(long companyId, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_COMPANYID;
			finderArgs = new Object[] { companyId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_COMPANYID;
			finderArgs = new Object[] { companyId, start, end, orderByComparator };
		}

		List<Plan> list = (List<Plan>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (Plan plan : list) {
				if ((companyId != plan.getCompanyId())) {
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

			query.append(_SQL_SELECT_PLAN_WHERE);

			query.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(PlanModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

				if (!pagination) {
					list = (List<Plan>)QueryUtil.list(q, getDialect(), start,
							end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Plan>(list);
				}
				else {
					list = (List<Plan>)QueryUtil.list(q, getDialect(), start,
							end);
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
	 * Returns the first plan in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching plan
	 * @throws com.inikah.slayer.NoSuchPlanException if a matching plan could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Plan findByCompanyId_First(long companyId,
		OrderByComparator orderByComparator)
		throws NoSuchPlanException, SystemException {
		Plan plan = fetchByCompanyId_First(companyId, orderByComparator);

		if (plan != null) {
			return plan;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("companyId=");
		msg.append(companyId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchPlanException(msg.toString());
	}

	/**
	 * Returns the first plan in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching plan, or <code>null</code> if a matching plan could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Plan fetchByCompanyId_First(long companyId,
		OrderByComparator orderByComparator) throws SystemException {
		List<Plan> list = findByCompanyId(companyId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last plan in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching plan
	 * @throws com.inikah.slayer.NoSuchPlanException if a matching plan could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Plan findByCompanyId_Last(long companyId,
		OrderByComparator orderByComparator)
		throws NoSuchPlanException, SystemException {
		Plan plan = fetchByCompanyId_Last(companyId, orderByComparator);

		if (plan != null) {
			return plan;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("companyId=");
		msg.append(companyId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchPlanException(msg.toString());
	}

	/**
	 * Returns the last plan in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching plan, or <code>null</code> if a matching plan could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Plan fetchByCompanyId_Last(long companyId,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByCompanyId(companyId);

		if (count == 0) {
			return null;
		}

		List<Plan> list = findByCompanyId(companyId, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the plans before and after the current plan in the ordered set where companyId = &#63;.
	 *
	 * @param planId the primary key of the current plan
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next plan
	 * @throws com.inikah.slayer.NoSuchPlanException if a plan with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Plan[] findByCompanyId_PrevAndNext(long planId, long companyId,
		OrderByComparator orderByComparator)
		throws NoSuchPlanException, SystemException {
		Plan plan = findByPrimaryKey(planId);

		Session session = null;

		try {
			session = openSession();

			Plan[] array = new PlanImpl[3];

			array[0] = getByCompanyId_PrevAndNext(session, plan, companyId,
					orderByComparator, true);

			array[1] = plan;

			array[2] = getByCompanyId_PrevAndNext(session, plan, companyId,
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

	protected Plan getByCompanyId_PrevAndNext(Session session, Plan plan,
		long companyId, OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_PLAN_WHERE);

		query.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

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
			query.append(PlanModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(companyId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(plan);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<Plan> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the plans where companyId = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeByCompanyId(long companyId) throws SystemException {
		for (Plan plan : findByCompanyId(companyId, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, null)) {
			remove(plan);
		}
	}

	/**
	 * Returns the number of plans where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the number of matching plans
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByCompanyId(long companyId) throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_COMPANYID;

		Object[] finderArgs = new Object[] { companyId };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_PLAN_WHERE);

			query.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(companyId);

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

	private static final String _FINDER_COLUMN_COMPANYID_COMPANYID_2 = "plan.companyId = ? AND plan.active_ = 1";

	public PlanPersistenceImpl() {
		setModelClass(Plan.class);
	}

	/**
	 * Caches the plan in the entity cache if it is enabled.
	 *
	 * @param plan the plan
	 */
	@Override
	public void cacheResult(Plan plan) {
		EntityCacheUtil.putResult(PlanModelImpl.ENTITY_CACHE_ENABLED,
			PlanImpl.class, plan.getPrimaryKey(), plan);

		plan.resetOriginalValues();
	}

	/**
	 * Caches the plans in the entity cache if it is enabled.
	 *
	 * @param plans the plans
	 */
	@Override
	public void cacheResult(List<Plan> plans) {
		for (Plan plan : plans) {
			if (EntityCacheUtil.getResult(PlanModelImpl.ENTITY_CACHE_ENABLED,
						PlanImpl.class, plan.getPrimaryKey()) == null) {
				cacheResult(plan);
			}
			else {
				plan.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all plans.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(PlanImpl.class.getName());
		}

		EntityCacheUtil.clearCache(PlanImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the plan.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(Plan plan) {
		EntityCacheUtil.removeResult(PlanModelImpl.ENTITY_CACHE_ENABLED,
			PlanImpl.class, plan.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<Plan> plans) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Plan plan : plans) {
			EntityCacheUtil.removeResult(PlanModelImpl.ENTITY_CACHE_ENABLED,
				PlanImpl.class, plan.getPrimaryKey());
		}
	}

	/**
	 * Creates a new plan with the primary key. Does not add the plan to the database.
	 *
	 * @param planId the primary key for the new plan
	 * @return the new plan
	 */
	@Override
	public Plan create(long planId) {
		Plan plan = new PlanImpl();

		plan.setNew(true);
		plan.setPrimaryKey(planId);

		return plan;
	}

	/**
	 * Removes the plan with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param planId the primary key of the plan
	 * @return the plan that was removed
	 * @throws com.inikah.slayer.NoSuchPlanException if a plan with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Plan remove(long planId) throws NoSuchPlanException, SystemException {
		return remove((Serializable)planId);
	}

	/**
	 * Removes the plan with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the plan
	 * @return the plan that was removed
	 * @throws com.inikah.slayer.NoSuchPlanException if a plan with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Plan remove(Serializable primaryKey)
		throws NoSuchPlanException, SystemException {
		Session session = null;

		try {
			session = openSession();

			Plan plan = (Plan)session.get(PlanImpl.class, primaryKey);

			if (plan == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchPlanException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(plan);
		}
		catch (NoSuchPlanException nsee) {
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
	protected Plan removeImpl(Plan plan) throws SystemException {
		plan = toUnwrappedModel(plan);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(plan)) {
				plan = (Plan)session.get(PlanImpl.class, plan.getPrimaryKeyObj());
			}

			if (plan != null) {
				session.delete(plan);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (plan != null) {
			clearCache(plan);
		}

		return plan;
	}

	@Override
	public Plan updateImpl(com.inikah.slayer.model.Plan plan)
		throws SystemException {
		plan = toUnwrappedModel(plan);

		boolean isNew = plan.isNew();

		PlanModelImpl planModelImpl = (PlanModelImpl)plan;

		Session session = null;

		try {
			session = openSession();

			if (plan.isNew()) {
				session.save(plan);

				plan.setNew(false);
			}
			else {
				session.merge(plan);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !PlanModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		else {
			if ((planModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_COMPANYID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						planModelImpl.getOriginalCompanyId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_COMPANYID,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_COMPANYID,
					args);

				args = new Object[] { planModelImpl.getCompanyId() };

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_COMPANYID,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_COMPANYID,
					args);
			}
		}

		EntityCacheUtil.putResult(PlanModelImpl.ENTITY_CACHE_ENABLED,
			PlanImpl.class, plan.getPrimaryKey(), plan);

		return plan;
	}

	protected Plan toUnwrappedModel(Plan plan) {
		if (plan instanceof PlanImpl) {
			return plan;
		}

		PlanImpl planImpl = new PlanImpl();

		planImpl.setNew(plan.isNew());
		planImpl.setPrimaryKey(plan.getPrimaryKey());

		planImpl.setPlanId(plan.getPlanId());
		planImpl.setCompanyId(plan.getCompanyId());
		planImpl.setPlanName(plan.getPlanName());
		planImpl.setValidity(plan.getValidity());
		planImpl.setDiscount(plan.getDiscount());
		planImpl.setTimeToActivate(plan.getTimeToActivate());
		planImpl.setPriorityInListing(plan.getPriorityInListing());
		planImpl.setCompatibilityCheckEnabled(plan.isCompatibilityCheckEnabled());
		planImpl.setProposalsAllowed(plan.getProposalsAllowed());
		planImpl.setContactsAllowed(plan.getContactsAllowed());
		planImpl.setBookmarksAllowed(plan.getBookmarksAllowed());
		planImpl.setTowardsCharity(plan.getTowardsCharity());
		planImpl.setCharityDescription(plan.getCharityDescription());
		planImpl.setReferralBonus(plan.getReferralBonus());
		planImpl.setRenewalDiscount(plan.getRenewalDiscount());
		planImpl.setComments(plan.getComments());
		planImpl.setBorderColor(plan.getBorderColor());
		planImpl.setActive_(plan.isActive_());

		return planImpl;
	}

	/**
	 * Returns the plan with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the plan
	 * @return the plan
	 * @throws com.inikah.slayer.NoSuchPlanException if a plan with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Plan findByPrimaryKey(Serializable primaryKey)
		throws NoSuchPlanException, SystemException {
		Plan plan = fetchByPrimaryKey(primaryKey);

		if (plan == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchPlanException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return plan;
	}

	/**
	 * Returns the plan with the primary key or throws a {@link com.inikah.slayer.NoSuchPlanException} if it could not be found.
	 *
	 * @param planId the primary key of the plan
	 * @return the plan
	 * @throws com.inikah.slayer.NoSuchPlanException if a plan with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Plan findByPrimaryKey(long planId)
		throws NoSuchPlanException, SystemException {
		return findByPrimaryKey((Serializable)planId);
	}

	/**
	 * Returns the plan with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the plan
	 * @return the plan, or <code>null</code> if a plan with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Plan fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		Plan plan = (Plan)EntityCacheUtil.getResult(PlanModelImpl.ENTITY_CACHE_ENABLED,
				PlanImpl.class, primaryKey);

		if (plan == _nullPlan) {
			return null;
		}

		if (plan == null) {
			Session session = null;

			try {
				session = openSession();

				plan = (Plan)session.get(PlanImpl.class, primaryKey);

				if (plan != null) {
					cacheResult(plan);
				}
				else {
					EntityCacheUtil.putResult(PlanModelImpl.ENTITY_CACHE_ENABLED,
						PlanImpl.class, primaryKey, _nullPlan);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(PlanModelImpl.ENTITY_CACHE_ENABLED,
					PlanImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return plan;
	}

	/**
	 * Returns the plan with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param planId the primary key of the plan
	 * @return the plan, or <code>null</code> if a plan with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Plan fetchByPrimaryKey(long planId) throws SystemException {
		return fetchByPrimaryKey((Serializable)planId);
	}

	/**
	 * Returns all the plans.
	 *
	 * @return the plans
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Plan> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the plans.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.PlanModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of plans
	 * @param end the upper bound of the range of plans (not inclusive)
	 * @return the range of plans
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Plan> findAll(int start, int end) throws SystemException {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the plans.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.PlanModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of plans
	 * @param end the upper bound of the range of plans (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of plans
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Plan> findAll(int start, int end,
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

		List<Plan> list = (List<Plan>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_PLAN);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_PLAN;

				if (pagination) {
					sql = sql.concat(PlanModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<Plan>)QueryUtil.list(q, getDialect(), start,
							end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Plan>(list);
				}
				else {
					list = (List<Plan>)QueryUtil.list(q, getDialect(), start,
							end);
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
	 * Removes all the plans from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeAll() throws SystemException {
		for (Plan plan : findAll()) {
			remove(plan);
		}
	}

	/**
	 * Returns the number of plans.
	 *
	 * @return the number of plans
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

				Query q = session.createQuery(_SQL_COUNT_PLAN);

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
	 * Initializes the plan persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.util.service.ServiceProps.get(
						"value.object.listener.com.inikah.slayer.model.Plan")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<Plan>> listenersList = new ArrayList<ModelListener<Plan>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<Plan>)InstanceFactory.newInstance(
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
		EntityCacheUtil.removeCache(PlanImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_PLAN = "SELECT plan FROM Plan plan";
	private static final String _SQL_SELECT_PLAN_WHERE = "SELECT plan FROM Plan plan WHERE ";
	private static final String _SQL_COUNT_PLAN = "SELECT COUNT(plan) FROM Plan plan";
	private static final String _SQL_COUNT_PLAN_WHERE = "SELECT COUNT(plan) FROM Plan plan WHERE ";
	private static final String _ORDER_BY_ENTITY_ALIAS = "plan.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No Plan exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No Plan exists with the key {";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
				PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
	private static Log _log = LogFactoryUtil.getLog(PlanPersistenceImpl.class);
	private static Plan _nullPlan = new PlanImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<Plan> toCacheModel() {
				return _nullPlanCacheModel;
			}
		};

	private static CacheModel<Plan> _nullPlanCacheModel = new CacheModel<Plan>() {
			@Override
			public Plan toEntityModel() {
				return _nullPlan;
			}
		};
}