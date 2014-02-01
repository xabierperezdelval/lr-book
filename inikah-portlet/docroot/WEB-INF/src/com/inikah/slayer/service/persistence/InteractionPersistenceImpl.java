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

import com.inikah.slayer.NoSuchInteractionException;
import com.inikah.slayer.model.Interaction;
import com.inikah.slayer.model.impl.InteractionImpl;
import com.inikah.slayer.model.impl.InteractionModelImpl;

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
 * The persistence implementation for the interaction service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Ahmed Hasan
 * @see InteractionPersistence
 * @see InteractionUtil
 * @generated
 */
public class InteractionPersistenceImpl extends BasePersistenceImpl<Interaction>
	implements InteractionPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link InteractionUtil} to access the interaction persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = InteractionImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(InteractionModelImpl.ENTITY_CACHE_ENABLED,
			InteractionModelImpl.FINDER_CACHE_ENABLED, InteractionImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(InteractionModelImpl.ENTITY_CACHE_ENABLED,
			InteractionModelImpl.FINDER_CACHE_ENABLED, InteractionImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(InteractionModelImpl.ENTITY_CACHE_ENABLED,
			InteractionModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_SOURCEID = new FinderPath(InteractionModelImpl.ENTITY_CACHE_ENABLED,
			InteractionModelImpl.FINDER_CACHE_ENABLED, InteractionImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findBySourceId",
			new String[] {
				Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_SOURCEID =
		new FinderPath(InteractionModelImpl.ENTITY_CACHE_ENABLED,
			InteractionModelImpl.FINDER_CACHE_ENABLED, InteractionImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findBySourceId",
			new String[] { Long.class.getName() },
			InteractionModelImpl.SOURCEID_COLUMN_BITMASK |
			InteractionModelImpl.PERFORMEDON_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_SOURCEID = new FinderPath(InteractionModelImpl.ENTITY_CACHE_ENABLED,
			InteractionModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countBySourceId",
			new String[] { Long.class.getName() });

	/**
	 * Returns all the interactions where sourceId = &#63;.
	 *
	 * @param sourceId the source ID
	 * @return the matching interactions
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Interaction> findBySourceId(long sourceId)
		throws SystemException {
		return findBySourceId(sourceId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the interactions where sourceId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.InteractionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param sourceId the source ID
	 * @param start the lower bound of the range of interactions
	 * @param end the upper bound of the range of interactions (not inclusive)
	 * @return the range of matching interactions
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Interaction> findBySourceId(long sourceId, int start, int end)
		throws SystemException {
		return findBySourceId(sourceId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the interactions where sourceId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.InteractionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param sourceId the source ID
	 * @param start the lower bound of the range of interactions
	 * @param end the upper bound of the range of interactions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching interactions
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Interaction> findBySourceId(long sourceId, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_SOURCEID;
			finderArgs = new Object[] { sourceId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_SOURCEID;
			finderArgs = new Object[] { sourceId, start, end, orderByComparator };
		}

		List<Interaction> list = (List<Interaction>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (Interaction interaction : list) {
				if ((sourceId != interaction.getSourceId())) {
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

			query.append(_SQL_SELECT_INTERACTION_WHERE);

			query.append(_FINDER_COLUMN_SOURCEID_SOURCEID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(InteractionModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(sourceId);

				if (!pagination) {
					list = (List<Interaction>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Interaction>(list);
				}
				else {
					list = (List<Interaction>)QueryUtil.list(q, getDialect(),
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
	 * Returns the first interaction in the ordered set where sourceId = &#63;.
	 *
	 * @param sourceId the source ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching interaction
	 * @throws com.inikah.slayer.NoSuchInteractionException if a matching interaction could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction findBySourceId_First(long sourceId,
		OrderByComparator orderByComparator)
		throws NoSuchInteractionException, SystemException {
		Interaction interaction = fetchBySourceId_First(sourceId,
				orderByComparator);

		if (interaction != null) {
			return interaction;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("sourceId=");
		msg.append(sourceId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInteractionException(msg.toString());
	}

	/**
	 * Returns the first interaction in the ordered set where sourceId = &#63;.
	 *
	 * @param sourceId the source ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching interaction, or <code>null</code> if a matching interaction could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction fetchBySourceId_First(long sourceId,
		OrderByComparator orderByComparator) throws SystemException {
		List<Interaction> list = findBySourceId(sourceId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last interaction in the ordered set where sourceId = &#63;.
	 *
	 * @param sourceId the source ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching interaction
	 * @throws com.inikah.slayer.NoSuchInteractionException if a matching interaction could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction findBySourceId_Last(long sourceId,
		OrderByComparator orderByComparator)
		throws NoSuchInteractionException, SystemException {
		Interaction interaction = fetchBySourceId_Last(sourceId,
				orderByComparator);

		if (interaction != null) {
			return interaction;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("sourceId=");
		msg.append(sourceId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInteractionException(msg.toString());
	}

	/**
	 * Returns the last interaction in the ordered set where sourceId = &#63;.
	 *
	 * @param sourceId the source ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching interaction, or <code>null</code> if a matching interaction could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction fetchBySourceId_Last(long sourceId,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countBySourceId(sourceId);

		if (count == 0) {
			return null;
		}

		List<Interaction> list = findBySourceId(sourceId, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the interactions before and after the current interaction in the ordered set where sourceId = &#63;.
	 *
	 * @param interactionId the primary key of the current interaction
	 * @param sourceId the source ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next interaction
	 * @throws com.inikah.slayer.NoSuchInteractionException if a interaction with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction[] findBySourceId_PrevAndNext(long interactionId,
		long sourceId, OrderByComparator orderByComparator)
		throws NoSuchInteractionException, SystemException {
		Interaction interaction = findByPrimaryKey(interactionId);

		Session session = null;

		try {
			session = openSession();

			Interaction[] array = new InteractionImpl[3];

			array[0] = getBySourceId_PrevAndNext(session, interaction,
					sourceId, orderByComparator, true);

			array[1] = interaction;

			array[2] = getBySourceId_PrevAndNext(session, interaction,
					sourceId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected Interaction getBySourceId_PrevAndNext(Session session,
		Interaction interaction, long sourceId,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_INTERACTION_WHERE);

		query.append(_FINDER_COLUMN_SOURCEID_SOURCEID_2);

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
			query.append(InteractionModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(sourceId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(interaction);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<Interaction> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the interactions where sourceId = &#63; from the database.
	 *
	 * @param sourceId the source ID
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeBySourceId(long sourceId) throws SystemException {
		for (Interaction interaction : findBySourceId(sourceId,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(interaction);
		}
	}

	/**
	 * Returns the number of interactions where sourceId = &#63;.
	 *
	 * @param sourceId the source ID
	 * @return the number of matching interactions
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countBySourceId(long sourceId) throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_SOURCEID;

		Object[] finderArgs = new Object[] { sourceId };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_INTERACTION_WHERE);

			query.append(_FINDER_COLUMN_SOURCEID_SOURCEID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(sourceId);

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

	private static final String _FINDER_COLUMN_SOURCEID_SOURCEID_2 = "interaction.sourceId = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_TARGETID = new FinderPath(InteractionModelImpl.ENTITY_CACHE_ENABLED,
			InteractionModelImpl.FINDER_CACHE_ENABLED, InteractionImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByTargetId",
			new String[] {
				Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_TARGETID =
		new FinderPath(InteractionModelImpl.ENTITY_CACHE_ENABLED,
			InteractionModelImpl.FINDER_CACHE_ENABLED, InteractionImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByTargetId",
			new String[] { Long.class.getName() },
			InteractionModelImpl.TARGETID_COLUMN_BITMASK |
			InteractionModelImpl.PERFORMEDON_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_TARGETID = new FinderPath(InteractionModelImpl.ENTITY_CACHE_ENABLED,
			InteractionModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByTargetId",
			new String[] { Long.class.getName() });

	/**
	 * Returns all the interactions where targetId = &#63;.
	 *
	 * @param targetId the target ID
	 * @return the matching interactions
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Interaction> findByTargetId(long targetId)
		throws SystemException {
		return findByTargetId(targetId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the interactions where targetId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.InteractionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param targetId the target ID
	 * @param start the lower bound of the range of interactions
	 * @param end the upper bound of the range of interactions (not inclusive)
	 * @return the range of matching interactions
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Interaction> findByTargetId(long targetId, int start, int end)
		throws SystemException {
		return findByTargetId(targetId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the interactions where targetId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.InteractionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param targetId the target ID
	 * @param start the lower bound of the range of interactions
	 * @param end the upper bound of the range of interactions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching interactions
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Interaction> findByTargetId(long targetId, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_TARGETID;
			finderArgs = new Object[] { targetId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_TARGETID;
			finderArgs = new Object[] { targetId, start, end, orderByComparator };
		}

		List<Interaction> list = (List<Interaction>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (Interaction interaction : list) {
				if ((targetId != interaction.getTargetId())) {
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

			query.append(_SQL_SELECT_INTERACTION_WHERE);

			query.append(_FINDER_COLUMN_TARGETID_TARGETID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(InteractionModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(targetId);

				if (!pagination) {
					list = (List<Interaction>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Interaction>(list);
				}
				else {
					list = (List<Interaction>)QueryUtil.list(q, getDialect(),
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
	 * Returns the first interaction in the ordered set where targetId = &#63;.
	 *
	 * @param targetId the target ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching interaction
	 * @throws com.inikah.slayer.NoSuchInteractionException if a matching interaction could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction findByTargetId_First(long targetId,
		OrderByComparator orderByComparator)
		throws NoSuchInteractionException, SystemException {
		Interaction interaction = fetchByTargetId_First(targetId,
				orderByComparator);

		if (interaction != null) {
			return interaction;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("targetId=");
		msg.append(targetId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInteractionException(msg.toString());
	}

	/**
	 * Returns the first interaction in the ordered set where targetId = &#63;.
	 *
	 * @param targetId the target ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching interaction, or <code>null</code> if a matching interaction could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction fetchByTargetId_First(long targetId,
		OrderByComparator orderByComparator) throws SystemException {
		List<Interaction> list = findByTargetId(targetId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last interaction in the ordered set where targetId = &#63;.
	 *
	 * @param targetId the target ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching interaction
	 * @throws com.inikah.slayer.NoSuchInteractionException if a matching interaction could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction findByTargetId_Last(long targetId,
		OrderByComparator orderByComparator)
		throws NoSuchInteractionException, SystemException {
		Interaction interaction = fetchByTargetId_Last(targetId,
				orderByComparator);

		if (interaction != null) {
			return interaction;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("targetId=");
		msg.append(targetId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInteractionException(msg.toString());
	}

	/**
	 * Returns the last interaction in the ordered set where targetId = &#63;.
	 *
	 * @param targetId the target ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching interaction, or <code>null</code> if a matching interaction could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction fetchByTargetId_Last(long targetId,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByTargetId(targetId);

		if (count == 0) {
			return null;
		}

		List<Interaction> list = findByTargetId(targetId, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the interactions before and after the current interaction in the ordered set where targetId = &#63;.
	 *
	 * @param interactionId the primary key of the current interaction
	 * @param targetId the target ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next interaction
	 * @throws com.inikah.slayer.NoSuchInteractionException if a interaction with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction[] findByTargetId_PrevAndNext(long interactionId,
		long targetId, OrderByComparator orderByComparator)
		throws NoSuchInteractionException, SystemException {
		Interaction interaction = findByPrimaryKey(interactionId);

		Session session = null;

		try {
			session = openSession();

			Interaction[] array = new InteractionImpl[3];

			array[0] = getByTargetId_PrevAndNext(session, interaction,
					targetId, orderByComparator, true);

			array[1] = interaction;

			array[2] = getByTargetId_PrevAndNext(session, interaction,
					targetId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected Interaction getByTargetId_PrevAndNext(Session session,
		Interaction interaction, long targetId,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_INTERACTION_WHERE);

		query.append(_FINDER_COLUMN_TARGETID_TARGETID_2);

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
			query.append(InteractionModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(targetId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(interaction);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<Interaction> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the interactions where targetId = &#63; from the database.
	 *
	 * @param targetId the target ID
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeByTargetId(long targetId) throws SystemException {
		for (Interaction interaction : findByTargetId(targetId,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(interaction);
		}
	}

	/**
	 * Returns the number of interactions where targetId = &#63;.
	 *
	 * @param targetId the target ID
	 * @return the number of matching interactions
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByTargetId(long targetId) throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_TARGETID;

		Object[] finderArgs = new Object[] { targetId };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_INTERACTION_WHERE);

			query.append(_FINDER_COLUMN_TARGETID_TARGETID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(targetId);

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

	private static final String _FINDER_COLUMN_TARGETID_TARGETID_2 = "interaction.targetId = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_SOURCEID_TARGETID =
		new FinderPath(InteractionModelImpl.ENTITY_CACHE_ENABLED,
			InteractionModelImpl.FINDER_CACHE_ENABLED, InteractionImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findBySourceId_TargetId",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_SOURCEID_TARGETID =
		new FinderPath(InteractionModelImpl.ENTITY_CACHE_ENABLED,
			InteractionModelImpl.FINDER_CACHE_ENABLED, InteractionImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findBySourceId_TargetId",
			new String[] { Long.class.getName(), Long.class.getName() },
			InteractionModelImpl.SOURCEID_COLUMN_BITMASK |
			InteractionModelImpl.TARGETID_COLUMN_BITMASK |
			InteractionModelImpl.PERFORMEDON_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_SOURCEID_TARGETID = new FinderPath(InteractionModelImpl.ENTITY_CACHE_ENABLED,
			InteractionModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countBySourceId_TargetId",
			new String[] { Long.class.getName(), Long.class.getName() });

	/**
	 * Returns all the interactions where sourceId = &#63; and targetId = &#63;.
	 *
	 * @param sourceId the source ID
	 * @param targetId the target ID
	 * @return the matching interactions
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Interaction> findBySourceId_TargetId(long sourceId,
		long targetId) throws SystemException {
		return findBySourceId_TargetId(sourceId, targetId, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the interactions where sourceId = &#63; and targetId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.InteractionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param sourceId the source ID
	 * @param targetId the target ID
	 * @param start the lower bound of the range of interactions
	 * @param end the upper bound of the range of interactions (not inclusive)
	 * @return the range of matching interactions
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Interaction> findBySourceId_TargetId(long sourceId,
		long targetId, int start, int end) throws SystemException {
		return findBySourceId_TargetId(sourceId, targetId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the interactions where sourceId = &#63; and targetId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.InteractionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param sourceId the source ID
	 * @param targetId the target ID
	 * @param start the lower bound of the range of interactions
	 * @param end the upper bound of the range of interactions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching interactions
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Interaction> findBySourceId_TargetId(long sourceId,
		long targetId, int start, int end, OrderByComparator orderByComparator)
		throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_SOURCEID_TARGETID;
			finderArgs = new Object[] { sourceId, targetId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_SOURCEID_TARGETID;
			finderArgs = new Object[] {
					sourceId, targetId,
					
					start, end, orderByComparator
				};
		}

		List<Interaction> list = (List<Interaction>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (Interaction interaction : list) {
				if ((sourceId != interaction.getSourceId()) ||
						(targetId != interaction.getTargetId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_INTERACTION_WHERE);

			query.append(_FINDER_COLUMN_SOURCEID_TARGETID_SOURCEID_2);

			query.append(_FINDER_COLUMN_SOURCEID_TARGETID_TARGETID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(InteractionModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(sourceId);

				qPos.add(targetId);

				if (!pagination) {
					list = (List<Interaction>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Interaction>(list);
				}
				else {
					list = (List<Interaction>)QueryUtil.list(q, getDialect(),
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
	 * Returns the first interaction in the ordered set where sourceId = &#63; and targetId = &#63;.
	 *
	 * @param sourceId the source ID
	 * @param targetId the target ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching interaction
	 * @throws com.inikah.slayer.NoSuchInteractionException if a matching interaction could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction findBySourceId_TargetId_First(long sourceId,
		long targetId, OrderByComparator orderByComparator)
		throws NoSuchInteractionException, SystemException {
		Interaction interaction = fetchBySourceId_TargetId_First(sourceId,
				targetId, orderByComparator);

		if (interaction != null) {
			return interaction;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("sourceId=");
		msg.append(sourceId);

		msg.append(", targetId=");
		msg.append(targetId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInteractionException(msg.toString());
	}

	/**
	 * Returns the first interaction in the ordered set where sourceId = &#63; and targetId = &#63;.
	 *
	 * @param sourceId the source ID
	 * @param targetId the target ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching interaction, or <code>null</code> if a matching interaction could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction fetchBySourceId_TargetId_First(long sourceId,
		long targetId, OrderByComparator orderByComparator)
		throws SystemException {
		List<Interaction> list = findBySourceId_TargetId(sourceId, targetId, 0,
				1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last interaction in the ordered set where sourceId = &#63; and targetId = &#63;.
	 *
	 * @param sourceId the source ID
	 * @param targetId the target ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching interaction
	 * @throws com.inikah.slayer.NoSuchInteractionException if a matching interaction could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction findBySourceId_TargetId_Last(long sourceId,
		long targetId, OrderByComparator orderByComparator)
		throws NoSuchInteractionException, SystemException {
		Interaction interaction = fetchBySourceId_TargetId_Last(sourceId,
				targetId, orderByComparator);

		if (interaction != null) {
			return interaction;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("sourceId=");
		msg.append(sourceId);

		msg.append(", targetId=");
		msg.append(targetId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInteractionException(msg.toString());
	}

	/**
	 * Returns the last interaction in the ordered set where sourceId = &#63; and targetId = &#63;.
	 *
	 * @param sourceId the source ID
	 * @param targetId the target ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching interaction, or <code>null</code> if a matching interaction could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction fetchBySourceId_TargetId_Last(long sourceId,
		long targetId, OrderByComparator orderByComparator)
		throws SystemException {
		int count = countBySourceId_TargetId(sourceId, targetId);

		if (count == 0) {
			return null;
		}

		List<Interaction> list = findBySourceId_TargetId(sourceId, targetId,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the interactions before and after the current interaction in the ordered set where sourceId = &#63; and targetId = &#63;.
	 *
	 * @param interactionId the primary key of the current interaction
	 * @param sourceId the source ID
	 * @param targetId the target ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next interaction
	 * @throws com.inikah.slayer.NoSuchInteractionException if a interaction with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction[] findBySourceId_TargetId_PrevAndNext(
		long interactionId, long sourceId, long targetId,
		OrderByComparator orderByComparator)
		throws NoSuchInteractionException, SystemException {
		Interaction interaction = findByPrimaryKey(interactionId);

		Session session = null;

		try {
			session = openSession();

			Interaction[] array = new InteractionImpl[3];

			array[0] = getBySourceId_TargetId_PrevAndNext(session, interaction,
					sourceId, targetId, orderByComparator, true);

			array[1] = interaction;

			array[2] = getBySourceId_TargetId_PrevAndNext(session, interaction,
					sourceId, targetId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected Interaction getBySourceId_TargetId_PrevAndNext(Session session,
		Interaction interaction, long sourceId, long targetId,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_INTERACTION_WHERE);

		query.append(_FINDER_COLUMN_SOURCEID_TARGETID_SOURCEID_2);

		query.append(_FINDER_COLUMN_SOURCEID_TARGETID_TARGETID_2);

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
			query.append(InteractionModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(sourceId);

		qPos.add(targetId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(interaction);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<Interaction> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the interactions where sourceId = &#63; and targetId = &#63; from the database.
	 *
	 * @param sourceId the source ID
	 * @param targetId the target ID
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeBySourceId_TargetId(long sourceId, long targetId)
		throws SystemException {
		for (Interaction interaction : findBySourceId_TargetId(sourceId,
				targetId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(interaction);
		}
	}

	/**
	 * Returns the number of interactions where sourceId = &#63; and targetId = &#63;.
	 *
	 * @param sourceId the source ID
	 * @param targetId the target ID
	 * @return the number of matching interactions
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countBySourceId_TargetId(long sourceId, long targetId)
		throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_SOURCEID_TARGETID;

		Object[] finderArgs = new Object[] { sourceId, targetId };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_INTERACTION_WHERE);

			query.append(_FINDER_COLUMN_SOURCEID_TARGETID_SOURCEID_2);

			query.append(_FINDER_COLUMN_SOURCEID_TARGETID_TARGETID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(sourceId);

				qPos.add(targetId);

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

	private static final String _FINDER_COLUMN_SOURCEID_TARGETID_SOURCEID_2 = "interaction.sourceId = ? AND ";
	private static final String _FINDER_COLUMN_SOURCEID_TARGETID_TARGETID_2 = "interaction.targetId = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_SOURCEID_OPERATION =
		new FinderPath(InteractionModelImpl.ENTITY_CACHE_ENABLED,
			InteractionModelImpl.FINDER_CACHE_ENABLED, InteractionImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findBySourceId_Operation",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_SOURCEID_OPERATION =
		new FinderPath(InteractionModelImpl.ENTITY_CACHE_ENABLED,
			InteractionModelImpl.FINDER_CACHE_ENABLED, InteractionImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findBySourceId_Operation",
			new String[] { Long.class.getName(), Integer.class.getName() },
			InteractionModelImpl.SOURCEID_COLUMN_BITMASK |
			InteractionModelImpl.OPERATION_COLUMN_BITMASK |
			InteractionModelImpl.PERFORMEDON_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_SOURCEID_OPERATION = new FinderPath(InteractionModelImpl.ENTITY_CACHE_ENABLED,
			InteractionModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countBySourceId_Operation",
			new String[] { Long.class.getName(), Integer.class.getName() });

	/**
	 * Returns all the interactions where sourceId = &#63; and operation = &#63;.
	 *
	 * @param sourceId the source ID
	 * @param operation the operation
	 * @return the matching interactions
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Interaction> findBySourceId_Operation(long sourceId,
		int operation) throws SystemException {
		return findBySourceId_Operation(sourceId, operation, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the interactions where sourceId = &#63; and operation = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.InteractionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param sourceId the source ID
	 * @param operation the operation
	 * @param start the lower bound of the range of interactions
	 * @param end the upper bound of the range of interactions (not inclusive)
	 * @return the range of matching interactions
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Interaction> findBySourceId_Operation(long sourceId,
		int operation, int start, int end) throws SystemException {
		return findBySourceId_Operation(sourceId, operation, start, end, null);
	}

	/**
	 * Returns an ordered range of all the interactions where sourceId = &#63; and operation = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.InteractionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param sourceId the source ID
	 * @param operation the operation
	 * @param start the lower bound of the range of interactions
	 * @param end the upper bound of the range of interactions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching interactions
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Interaction> findBySourceId_Operation(long sourceId,
		int operation, int start, int end, OrderByComparator orderByComparator)
		throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_SOURCEID_OPERATION;
			finderArgs = new Object[] { sourceId, operation };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_SOURCEID_OPERATION;
			finderArgs = new Object[] {
					sourceId, operation,
					
					start, end, orderByComparator
				};
		}

		List<Interaction> list = (List<Interaction>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (Interaction interaction : list) {
				if ((sourceId != interaction.getSourceId()) ||
						(operation != interaction.getOperation())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_INTERACTION_WHERE);

			query.append(_FINDER_COLUMN_SOURCEID_OPERATION_SOURCEID_2);

			query.append(_FINDER_COLUMN_SOURCEID_OPERATION_OPERATION_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(InteractionModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(sourceId);

				qPos.add(operation);

				if (!pagination) {
					list = (List<Interaction>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Interaction>(list);
				}
				else {
					list = (List<Interaction>)QueryUtil.list(q, getDialect(),
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
	 * Returns the first interaction in the ordered set where sourceId = &#63; and operation = &#63;.
	 *
	 * @param sourceId the source ID
	 * @param operation the operation
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching interaction
	 * @throws com.inikah.slayer.NoSuchInteractionException if a matching interaction could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction findBySourceId_Operation_First(long sourceId,
		int operation, OrderByComparator orderByComparator)
		throws NoSuchInteractionException, SystemException {
		Interaction interaction = fetchBySourceId_Operation_First(sourceId,
				operation, orderByComparator);

		if (interaction != null) {
			return interaction;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("sourceId=");
		msg.append(sourceId);

		msg.append(", operation=");
		msg.append(operation);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInteractionException(msg.toString());
	}

	/**
	 * Returns the first interaction in the ordered set where sourceId = &#63; and operation = &#63;.
	 *
	 * @param sourceId the source ID
	 * @param operation the operation
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching interaction, or <code>null</code> if a matching interaction could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction fetchBySourceId_Operation_First(long sourceId,
		int operation, OrderByComparator orderByComparator)
		throws SystemException {
		List<Interaction> list = findBySourceId_Operation(sourceId, operation,
				0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last interaction in the ordered set where sourceId = &#63; and operation = &#63;.
	 *
	 * @param sourceId the source ID
	 * @param operation the operation
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching interaction
	 * @throws com.inikah.slayer.NoSuchInteractionException if a matching interaction could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction findBySourceId_Operation_Last(long sourceId,
		int operation, OrderByComparator orderByComparator)
		throws NoSuchInteractionException, SystemException {
		Interaction interaction = fetchBySourceId_Operation_Last(sourceId,
				operation, orderByComparator);

		if (interaction != null) {
			return interaction;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("sourceId=");
		msg.append(sourceId);

		msg.append(", operation=");
		msg.append(operation);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInteractionException(msg.toString());
	}

	/**
	 * Returns the last interaction in the ordered set where sourceId = &#63; and operation = &#63;.
	 *
	 * @param sourceId the source ID
	 * @param operation the operation
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching interaction, or <code>null</code> if a matching interaction could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction fetchBySourceId_Operation_Last(long sourceId,
		int operation, OrderByComparator orderByComparator)
		throws SystemException {
		int count = countBySourceId_Operation(sourceId, operation);

		if (count == 0) {
			return null;
		}

		List<Interaction> list = findBySourceId_Operation(sourceId, operation,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the interactions before and after the current interaction in the ordered set where sourceId = &#63; and operation = &#63;.
	 *
	 * @param interactionId the primary key of the current interaction
	 * @param sourceId the source ID
	 * @param operation the operation
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next interaction
	 * @throws com.inikah.slayer.NoSuchInteractionException if a interaction with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction[] findBySourceId_Operation_PrevAndNext(
		long interactionId, long sourceId, int operation,
		OrderByComparator orderByComparator)
		throws NoSuchInteractionException, SystemException {
		Interaction interaction = findByPrimaryKey(interactionId);

		Session session = null;

		try {
			session = openSession();

			Interaction[] array = new InteractionImpl[3];

			array[0] = getBySourceId_Operation_PrevAndNext(session,
					interaction, sourceId, operation, orderByComparator, true);

			array[1] = interaction;

			array[2] = getBySourceId_Operation_PrevAndNext(session,
					interaction, sourceId, operation, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected Interaction getBySourceId_Operation_PrevAndNext(Session session,
		Interaction interaction, long sourceId, int operation,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_INTERACTION_WHERE);

		query.append(_FINDER_COLUMN_SOURCEID_OPERATION_SOURCEID_2);

		query.append(_FINDER_COLUMN_SOURCEID_OPERATION_OPERATION_2);

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
			query.append(InteractionModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(sourceId);

		qPos.add(operation);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(interaction);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<Interaction> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the interactions where sourceId = &#63; and operation = &#63; from the database.
	 *
	 * @param sourceId the source ID
	 * @param operation the operation
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeBySourceId_Operation(long sourceId, int operation)
		throws SystemException {
		for (Interaction interaction : findBySourceId_Operation(sourceId,
				operation, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(interaction);
		}
	}

	/**
	 * Returns the number of interactions where sourceId = &#63; and operation = &#63;.
	 *
	 * @param sourceId the source ID
	 * @param operation the operation
	 * @return the number of matching interactions
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countBySourceId_Operation(long sourceId, int operation)
		throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_SOURCEID_OPERATION;

		Object[] finderArgs = new Object[] { sourceId, operation };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_INTERACTION_WHERE);

			query.append(_FINDER_COLUMN_SOURCEID_OPERATION_SOURCEID_2);

			query.append(_FINDER_COLUMN_SOURCEID_OPERATION_OPERATION_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(sourceId);

				qPos.add(operation);

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

	private static final String _FINDER_COLUMN_SOURCEID_OPERATION_SOURCEID_2 = "interaction.sourceId = ? AND ";
	private static final String _FINDER_COLUMN_SOURCEID_OPERATION_OPERATION_2 = "interaction.operation = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_TARGETID_OPERATION =
		new FinderPath(InteractionModelImpl.ENTITY_CACHE_ENABLED,
			InteractionModelImpl.FINDER_CACHE_ENABLED, InteractionImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByTargetId_Operation",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_TARGETID_OPERATION =
		new FinderPath(InteractionModelImpl.ENTITY_CACHE_ENABLED,
			InteractionModelImpl.FINDER_CACHE_ENABLED, InteractionImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"findByTargetId_Operation",
			new String[] { Long.class.getName(), Integer.class.getName() },
			InteractionModelImpl.TARGETID_COLUMN_BITMASK |
			InteractionModelImpl.OPERATION_COLUMN_BITMASK |
			InteractionModelImpl.PERFORMEDON_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_TARGETID_OPERATION = new FinderPath(InteractionModelImpl.ENTITY_CACHE_ENABLED,
			InteractionModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByTargetId_Operation",
			new String[] { Long.class.getName(), Integer.class.getName() });

	/**
	 * Returns all the interactions where targetId = &#63; and operation = &#63;.
	 *
	 * @param targetId the target ID
	 * @param operation the operation
	 * @return the matching interactions
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Interaction> findByTargetId_Operation(long targetId,
		int operation) throws SystemException {
		return findByTargetId_Operation(targetId, operation, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the interactions where targetId = &#63; and operation = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.InteractionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param targetId the target ID
	 * @param operation the operation
	 * @param start the lower bound of the range of interactions
	 * @param end the upper bound of the range of interactions (not inclusive)
	 * @return the range of matching interactions
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Interaction> findByTargetId_Operation(long targetId,
		int operation, int start, int end) throws SystemException {
		return findByTargetId_Operation(targetId, operation, start, end, null);
	}

	/**
	 * Returns an ordered range of all the interactions where targetId = &#63; and operation = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.InteractionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param targetId the target ID
	 * @param operation the operation
	 * @param start the lower bound of the range of interactions
	 * @param end the upper bound of the range of interactions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching interactions
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Interaction> findByTargetId_Operation(long targetId,
		int operation, int start, int end, OrderByComparator orderByComparator)
		throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_TARGETID_OPERATION;
			finderArgs = new Object[] { targetId, operation };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_TARGETID_OPERATION;
			finderArgs = new Object[] {
					targetId, operation,
					
					start, end, orderByComparator
				};
		}

		List<Interaction> list = (List<Interaction>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (Interaction interaction : list) {
				if ((targetId != interaction.getTargetId()) ||
						(operation != interaction.getOperation())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_INTERACTION_WHERE);

			query.append(_FINDER_COLUMN_TARGETID_OPERATION_TARGETID_2);

			query.append(_FINDER_COLUMN_TARGETID_OPERATION_OPERATION_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(InteractionModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(targetId);

				qPos.add(operation);

				if (!pagination) {
					list = (List<Interaction>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Interaction>(list);
				}
				else {
					list = (List<Interaction>)QueryUtil.list(q, getDialect(),
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
	 * Returns the first interaction in the ordered set where targetId = &#63; and operation = &#63;.
	 *
	 * @param targetId the target ID
	 * @param operation the operation
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching interaction
	 * @throws com.inikah.slayer.NoSuchInteractionException if a matching interaction could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction findByTargetId_Operation_First(long targetId,
		int operation, OrderByComparator orderByComparator)
		throws NoSuchInteractionException, SystemException {
		Interaction interaction = fetchByTargetId_Operation_First(targetId,
				operation, orderByComparator);

		if (interaction != null) {
			return interaction;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("targetId=");
		msg.append(targetId);

		msg.append(", operation=");
		msg.append(operation);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInteractionException(msg.toString());
	}

	/**
	 * Returns the first interaction in the ordered set where targetId = &#63; and operation = &#63;.
	 *
	 * @param targetId the target ID
	 * @param operation the operation
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching interaction, or <code>null</code> if a matching interaction could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction fetchByTargetId_Operation_First(long targetId,
		int operation, OrderByComparator orderByComparator)
		throws SystemException {
		List<Interaction> list = findByTargetId_Operation(targetId, operation,
				0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last interaction in the ordered set where targetId = &#63; and operation = &#63;.
	 *
	 * @param targetId the target ID
	 * @param operation the operation
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching interaction
	 * @throws com.inikah.slayer.NoSuchInteractionException if a matching interaction could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction findByTargetId_Operation_Last(long targetId,
		int operation, OrderByComparator orderByComparator)
		throws NoSuchInteractionException, SystemException {
		Interaction interaction = fetchByTargetId_Operation_Last(targetId,
				operation, orderByComparator);

		if (interaction != null) {
			return interaction;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("targetId=");
		msg.append(targetId);

		msg.append(", operation=");
		msg.append(operation);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInteractionException(msg.toString());
	}

	/**
	 * Returns the last interaction in the ordered set where targetId = &#63; and operation = &#63;.
	 *
	 * @param targetId the target ID
	 * @param operation the operation
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching interaction, or <code>null</code> if a matching interaction could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction fetchByTargetId_Operation_Last(long targetId,
		int operation, OrderByComparator orderByComparator)
		throws SystemException {
		int count = countByTargetId_Operation(targetId, operation);

		if (count == 0) {
			return null;
		}

		List<Interaction> list = findByTargetId_Operation(targetId, operation,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the interactions before and after the current interaction in the ordered set where targetId = &#63; and operation = &#63;.
	 *
	 * @param interactionId the primary key of the current interaction
	 * @param targetId the target ID
	 * @param operation the operation
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next interaction
	 * @throws com.inikah.slayer.NoSuchInteractionException if a interaction with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction[] findByTargetId_Operation_PrevAndNext(
		long interactionId, long targetId, int operation,
		OrderByComparator orderByComparator)
		throws NoSuchInteractionException, SystemException {
		Interaction interaction = findByPrimaryKey(interactionId);

		Session session = null;

		try {
			session = openSession();

			Interaction[] array = new InteractionImpl[3];

			array[0] = getByTargetId_Operation_PrevAndNext(session,
					interaction, targetId, operation, orderByComparator, true);

			array[1] = interaction;

			array[2] = getByTargetId_Operation_PrevAndNext(session,
					interaction, targetId, operation, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected Interaction getByTargetId_Operation_PrevAndNext(Session session,
		Interaction interaction, long targetId, int operation,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_INTERACTION_WHERE);

		query.append(_FINDER_COLUMN_TARGETID_OPERATION_TARGETID_2);

		query.append(_FINDER_COLUMN_TARGETID_OPERATION_OPERATION_2);

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
			query.append(InteractionModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(targetId);

		qPos.add(operation);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(interaction);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<Interaction> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the interactions where targetId = &#63; and operation = &#63; from the database.
	 *
	 * @param targetId the target ID
	 * @param operation the operation
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeByTargetId_Operation(long targetId, int operation)
		throws SystemException {
		for (Interaction interaction : findByTargetId_Operation(targetId,
				operation, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(interaction);
		}
	}

	/**
	 * Returns the number of interactions where targetId = &#63; and operation = &#63;.
	 *
	 * @param targetId the target ID
	 * @param operation the operation
	 * @return the number of matching interactions
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByTargetId_Operation(long targetId, int operation)
		throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_TARGETID_OPERATION;

		Object[] finderArgs = new Object[] { targetId, operation };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_INTERACTION_WHERE);

			query.append(_FINDER_COLUMN_TARGETID_OPERATION_TARGETID_2);

			query.append(_FINDER_COLUMN_TARGETID_OPERATION_OPERATION_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(targetId);

				qPos.add(operation);

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

	private static final String _FINDER_COLUMN_TARGETID_OPERATION_TARGETID_2 = "interaction.targetId = ? AND ";
	private static final String _FINDER_COLUMN_TARGETID_OPERATION_OPERATION_2 = "interaction.operation = ?";
	public static final FinderPath FINDER_PATH_FETCH_BY_SOURCEID_TARGETID_OPERATION =
		new FinderPath(InteractionModelImpl.ENTITY_CACHE_ENABLED,
			InteractionModelImpl.FINDER_CACHE_ENABLED, InteractionImpl.class,
			FINDER_CLASS_NAME_ENTITY, "fetchBySourceId_TargetId_Operation",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				Integer.class.getName()
			},
			InteractionModelImpl.SOURCEID_COLUMN_BITMASK |
			InteractionModelImpl.TARGETID_COLUMN_BITMASK |
			InteractionModelImpl.OPERATION_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_SOURCEID_TARGETID_OPERATION =
		new FinderPath(InteractionModelImpl.ENTITY_CACHE_ENABLED,
			InteractionModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countBySourceId_TargetId_Operation",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				Integer.class.getName()
			});

	/**
	 * Returns the interaction where sourceId = &#63; and targetId = &#63; and operation = &#63; or throws a {@link com.inikah.slayer.NoSuchInteractionException} if it could not be found.
	 *
	 * @param sourceId the source ID
	 * @param targetId the target ID
	 * @param operation the operation
	 * @return the matching interaction
	 * @throws com.inikah.slayer.NoSuchInteractionException if a matching interaction could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction findBySourceId_TargetId_Operation(long sourceId,
		long targetId, int operation)
		throws NoSuchInteractionException, SystemException {
		Interaction interaction = fetchBySourceId_TargetId_Operation(sourceId,
				targetId, operation);

		if (interaction == null) {
			StringBundler msg = new StringBundler(8);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("sourceId=");
			msg.append(sourceId);

			msg.append(", targetId=");
			msg.append(targetId);

			msg.append(", operation=");
			msg.append(operation);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchInteractionException(msg.toString());
		}

		return interaction;
	}

	/**
	 * Returns the interaction where sourceId = &#63; and targetId = &#63; and operation = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param sourceId the source ID
	 * @param targetId the target ID
	 * @param operation the operation
	 * @return the matching interaction, or <code>null</code> if a matching interaction could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction fetchBySourceId_TargetId_Operation(long sourceId,
		long targetId, int operation) throws SystemException {
		return fetchBySourceId_TargetId_Operation(sourceId, targetId,
			operation, true);
	}

	/**
	 * Returns the interaction where sourceId = &#63; and targetId = &#63; and operation = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param sourceId the source ID
	 * @param targetId the target ID
	 * @param operation the operation
	 * @param retrieveFromCache whether to use the finder cache
	 * @return the matching interaction, or <code>null</code> if a matching interaction could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction fetchBySourceId_TargetId_Operation(long sourceId,
		long targetId, int operation, boolean retrieveFromCache)
		throws SystemException {
		Object[] finderArgs = new Object[] { sourceId, targetId, operation };

		Object result = null;

		if (retrieveFromCache) {
			result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_SOURCEID_TARGETID_OPERATION,
					finderArgs, this);
		}

		if (result instanceof Interaction) {
			Interaction interaction = (Interaction)result;

			if ((sourceId != interaction.getSourceId()) ||
					(targetId != interaction.getTargetId()) ||
					(operation != interaction.getOperation())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(5);

			query.append(_SQL_SELECT_INTERACTION_WHERE);

			query.append(_FINDER_COLUMN_SOURCEID_TARGETID_OPERATION_SOURCEID_2);

			query.append(_FINDER_COLUMN_SOURCEID_TARGETID_OPERATION_TARGETID_2);

			query.append(_FINDER_COLUMN_SOURCEID_TARGETID_OPERATION_OPERATION_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(sourceId);

				qPos.add(targetId);

				qPos.add(operation);

				List<Interaction> list = q.list();

				if (list.isEmpty()) {
					FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_SOURCEID_TARGETID_OPERATION,
						finderArgs, list);
				}
				else {
					if ((list.size() > 1) && _log.isWarnEnabled()) {
						_log.warn(
							"InteractionPersistenceImpl.fetchBySourceId_TargetId_Operation(long, long, int, boolean) with parameters (" +
							StringUtil.merge(finderArgs) +
							") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
					}

					Interaction interaction = list.get(0);

					result = interaction;

					cacheResult(interaction);

					if ((interaction.getSourceId() != sourceId) ||
							(interaction.getTargetId() != targetId) ||
							(interaction.getOperation() != operation)) {
						FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_SOURCEID_TARGETID_OPERATION,
							finderArgs, interaction);
					}
				}
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_SOURCEID_TARGETID_OPERATION,
					finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (Interaction)result;
		}
	}

	/**
	 * Removes the interaction where sourceId = &#63; and targetId = &#63; and operation = &#63; from the database.
	 *
	 * @param sourceId the source ID
	 * @param targetId the target ID
	 * @param operation the operation
	 * @return the interaction that was removed
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction removeBySourceId_TargetId_Operation(long sourceId,
		long targetId, int operation)
		throws NoSuchInteractionException, SystemException {
		Interaction interaction = findBySourceId_TargetId_Operation(sourceId,
				targetId, operation);

		return remove(interaction);
	}

	/**
	 * Returns the number of interactions where sourceId = &#63; and targetId = &#63; and operation = &#63;.
	 *
	 * @param sourceId the source ID
	 * @param targetId the target ID
	 * @param operation the operation
	 * @return the number of matching interactions
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countBySourceId_TargetId_Operation(long sourceId, long targetId,
		int operation) throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_SOURCEID_TARGETID_OPERATION;

		Object[] finderArgs = new Object[] { sourceId, targetId, operation };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_COUNT_INTERACTION_WHERE);

			query.append(_FINDER_COLUMN_SOURCEID_TARGETID_OPERATION_SOURCEID_2);

			query.append(_FINDER_COLUMN_SOURCEID_TARGETID_OPERATION_TARGETID_2);

			query.append(_FINDER_COLUMN_SOURCEID_TARGETID_OPERATION_OPERATION_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(sourceId);

				qPos.add(targetId);

				qPos.add(operation);

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

	private static final String _FINDER_COLUMN_SOURCEID_TARGETID_OPERATION_SOURCEID_2 =
		"interaction.sourceId = ? AND ";
	private static final String _FINDER_COLUMN_SOURCEID_TARGETID_OPERATION_TARGETID_2 =
		"interaction.targetId = ? AND ";
	private static final String _FINDER_COLUMN_SOURCEID_TARGETID_OPERATION_OPERATION_2 =
		"interaction.operation = ?";

	public InteractionPersistenceImpl() {
		setModelClass(Interaction.class);
	}

	/**
	 * Caches the interaction in the entity cache if it is enabled.
	 *
	 * @param interaction the interaction
	 */
	@Override
	public void cacheResult(Interaction interaction) {
		EntityCacheUtil.putResult(InteractionModelImpl.ENTITY_CACHE_ENABLED,
			InteractionImpl.class, interaction.getPrimaryKey(), interaction);

		FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_SOURCEID_TARGETID_OPERATION,
			new Object[] {
				interaction.getSourceId(), interaction.getTargetId(),
				interaction.getOperation()
			}, interaction);

		interaction.resetOriginalValues();
	}

	/**
	 * Caches the interactions in the entity cache if it is enabled.
	 *
	 * @param interactions the interactions
	 */
	@Override
	public void cacheResult(List<Interaction> interactions) {
		for (Interaction interaction : interactions) {
			if (EntityCacheUtil.getResult(
						InteractionModelImpl.ENTITY_CACHE_ENABLED,
						InteractionImpl.class, interaction.getPrimaryKey()) == null) {
				cacheResult(interaction);
			}
			else {
				interaction.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all interactions.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(InteractionImpl.class.getName());
		}

		EntityCacheUtil.clearCache(InteractionImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the interaction.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(Interaction interaction) {
		EntityCacheUtil.removeResult(InteractionModelImpl.ENTITY_CACHE_ENABLED,
			InteractionImpl.class, interaction.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		clearUniqueFindersCache(interaction);
	}

	@Override
	public void clearCache(List<Interaction> interactions) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Interaction interaction : interactions) {
			EntityCacheUtil.removeResult(InteractionModelImpl.ENTITY_CACHE_ENABLED,
				InteractionImpl.class, interaction.getPrimaryKey());

			clearUniqueFindersCache(interaction);
		}
	}

	protected void cacheUniqueFindersCache(Interaction interaction) {
		if (interaction.isNew()) {
			Object[] args = new Object[] {
					interaction.getSourceId(), interaction.getTargetId(),
					interaction.getOperation()
				};

			FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_SOURCEID_TARGETID_OPERATION,
				args, Long.valueOf(1));
			FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_SOURCEID_TARGETID_OPERATION,
				args, interaction);
		}
		else {
			InteractionModelImpl interactionModelImpl = (InteractionModelImpl)interaction;

			if ((interactionModelImpl.getColumnBitmask() &
					FINDER_PATH_FETCH_BY_SOURCEID_TARGETID_OPERATION.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						interaction.getSourceId(), interaction.getTargetId(),
						interaction.getOperation()
					};

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_SOURCEID_TARGETID_OPERATION,
					args, Long.valueOf(1));
				FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_SOURCEID_TARGETID_OPERATION,
					args, interaction);
			}
		}
	}

	protected void clearUniqueFindersCache(Interaction interaction) {
		InteractionModelImpl interactionModelImpl = (InteractionModelImpl)interaction;

		Object[] args = new Object[] {
				interaction.getSourceId(), interaction.getTargetId(),
				interaction.getOperation()
			};

		FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_SOURCEID_TARGETID_OPERATION,
			args);
		FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_SOURCEID_TARGETID_OPERATION,
			args);

		if ((interactionModelImpl.getColumnBitmask() &
				FINDER_PATH_FETCH_BY_SOURCEID_TARGETID_OPERATION.getColumnBitmask()) != 0) {
			args = new Object[] {
					interactionModelImpl.getOriginalSourceId(),
					interactionModelImpl.getOriginalTargetId(),
					interactionModelImpl.getOriginalOperation()
				};

			FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_SOURCEID_TARGETID_OPERATION,
				args);
			FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_SOURCEID_TARGETID_OPERATION,
				args);
		}
	}

	/**
	 * Creates a new interaction with the primary key. Does not add the interaction to the database.
	 *
	 * @param interactionId the primary key for the new interaction
	 * @return the new interaction
	 */
	@Override
	public Interaction create(long interactionId) {
		Interaction interaction = new InteractionImpl();

		interaction.setNew(true);
		interaction.setPrimaryKey(interactionId);

		return interaction;
	}

	/**
	 * Removes the interaction with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param interactionId the primary key of the interaction
	 * @return the interaction that was removed
	 * @throws com.inikah.slayer.NoSuchInteractionException if a interaction with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction remove(long interactionId)
		throws NoSuchInteractionException, SystemException {
		return remove((Serializable)interactionId);
	}

	/**
	 * Removes the interaction with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the interaction
	 * @return the interaction that was removed
	 * @throws com.inikah.slayer.NoSuchInteractionException if a interaction with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction remove(Serializable primaryKey)
		throws NoSuchInteractionException, SystemException {
		Session session = null;

		try {
			session = openSession();

			Interaction interaction = (Interaction)session.get(InteractionImpl.class,
					primaryKey);

			if (interaction == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchInteractionException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(interaction);
		}
		catch (NoSuchInteractionException nsee) {
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
	protected Interaction removeImpl(Interaction interaction)
		throws SystemException {
		interaction = toUnwrappedModel(interaction);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(interaction)) {
				interaction = (Interaction)session.get(InteractionImpl.class,
						interaction.getPrimaryKeyObj());
			}

			if (interaction != null) {
				session.delete(interaction);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (interaction != null) {
			clearCache(interaction);
		}

		return interaction;
	}

	@Override
	public Interaction updateImpl(
		com.inikah.slayer.model.Interaction interaction)
		throws SystemException {
		interaction = toUnwrappedModel(interaction);

		boolean isNew = interaction.isNew();

		InteractionModelImpl interactionModelImpl = (InteractionModelImpl)interaction;

		Session session = null;

		try {
			session = openSession();

			if (interaction.isNew()) {
				session.save(interaction);

				interaction.setNew(false);
			}
			else {
				session.merge(interaction);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !InteractionModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		else {
			if ((interactionModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_SOURCEID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						interactionModelImpl.getOriginalSourceId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_SOURCEID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_SOURCEID,
					args);

				args = new Object[] { interactionModelImpl.getSourceId() };

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_SOURCEID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_SOURCEID,
					args);
			}

			if ((interactionModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_TARGETID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						interactionModelImpl.getOriginalTargetId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_TARGETID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_TARGETID,
					args);

				args = new Object[] { interactionModelImpl.getTargetId() };

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_TARGETID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_TARGETID,
					args);
			}

			if ((interactionModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_SOURCEID_TARGETID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						interactionModelImpl.getOriginalSourceId(),
						interactionModelImpl.getOriginalTargetId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_SOURCEID_TARGETID,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_SOURCEID_TARGETID,
					args);

				args = new Object[] {
						interactionModelImpl.getSourceId(),
						interactionModelImpl.getTargetId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_SOURCEID_TARGETID,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_SOURCEID_TARGETID,
					args);
			}

			if ((interactionModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_SOURCEID_OPERATION.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						interactionModelImpl.getOriginalSourceId(),
						interactionModelImpl.getOriginalOperation()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_SOURCEID_OPERATION,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_SOURCEID_OPERATION,
					args);

				args = new Object[] {
						interactionModelImpl.getSourceId(),
						interactionModelImpl.getOperation()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_SOURCEID_OPERATION,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_SOURCEID_OPERATION,
					args);
			}

			if ((interactionModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_TARGETID_OPERATION.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						interactionModelImpl.getOriginalTargetId(),
						interactionModelImpl.getOriginalOperation()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_TARGETID_OPERATION,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_TARGETID_OPERATION,
					args);

				args = new Object[] {
						interactionModelImpl.getTargetId(),
						interactionModelImpl.getOperation()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_TARGETID_OPERATION,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_TARGETID_OPERATION,
					args);
			}
		}

		EntityCacheUtil.putResult(InteractionModelImpl.ENTITY_CACHE_ENABLED,
			InteractionImpl.class, interaction.getPrimaryKey(), interaction);

		clearUniqueFindersCache(interaction);
		cacheUniqueFindersCache(interaction);

		return interaction;
	}

	protected Interaction toUnwrappedModel(Interaction interaction) {
		if (interaction instanceof InteractionImpl) {
			return interaction;
		}

		InteractionImpl interactionImpl = new InteractionImpl();

		interactionImpl.setNew(interaction.isNew());
		interactionImpl.setPrimaryKey(interaction.getPrimaryKey());

		interactionImpl.setInteractionId(interaction.getInteractionId());
		interactionImpl.setSourceId(interaction.getSourceId());
		interactionImpl.setTargetId(interaction.getTargetId());
		interactionImpl.setOperation(interaction.getOperation());
		interactionImpl.setPerformedOn(interaction.getPerformedOn());
		interactionImpl.setParentId(interaction.getParentId());

		return interactionImpl;
	}

	/**
	 * Returns the interaction with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the interaction
	 * @return the interaction
	 * @throws com.inikah.slayer.NoSuchInteractionException if a interaction with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction findByPrimaryKey(Serializable primaryKey)
		throws NoSuchInteractionException, SystemException {
		Interaction interaction = fetchByPrimaryKey(primaryKey);

		if (interaction == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchInteractionException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return interaction;
	}

	/**
	 * Returns the interaction with the primary key or throws a {@link com.inikah.slayer.NoSuchInteractionException} if it could not be found.
	 *
	 * @param interactionId the primary key of the interaction
	 * @return the interaction
	 * @throws com.inikah.slayer.NoSuchInteractionException if a interaction with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction findByPrimaryKey(long interactionId)
		throws NoSuchInteractionException, SystemException {
		return findByPrimaryKey((Serializable)interactionId);
	}

	/**
	 * Returns the interaction with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the interaction
	 * @return the interaction, or <code>null</code> if a interaction with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		Interaction interaction = (Interaction)EntityCacheUtil.getResult(InteractionModelImpl.ENTITY_CACHE_ENABLED,
				InteractionImpl.class, primaryKey);

		if (interaction == _nullInteraction) {
			return null;
		}

		if (interaction == null) {
			Session session = null;

			try {
				session = openSession();

				interaction = (Interaction)session.get(InteractionImpl.class,
						primaryKey);

				if (interaction != null) {
					cacheResult(interaction);
				}
				else {
					EntityCacheUtil.putResult(InteractionModelImpl.ENTITY_CACHE_ENABLED,
						InteractionImpl.class, primaryKey, _nullInteraction);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(InteractionModelImpl.ENTITY_CACHE_ENABLED,
					InteractionImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return interaction;
	}

	/**
	 * Returns the interaction with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param interactionId the primary key of the interaction
	 * @return the interaction, or <code>null</code> if a interaction with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Interaction fetchByPrimaryKey(long interactionId)
		throws SystemException {
		return fetchByPrimaryKey((Serializable)interactionId);
	}

	/**
	 * Returns all the interactions.
	 *
	 * @return the interactions
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Interaction> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the interactions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.InteractionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of interactions
	 * @param end the upper bound of the range of interactions (not inclusive)
	 * @return the range of interactions
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Interaction> findAll(int start, int end)
		throws SystemException {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the interactions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.InteractionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of interactions
	 * @param end the upper bound of the range of interactions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of interactions
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Interaction> findAll(int start, int end,
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

		List<Interaction> list = (List<Interaction>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_INTERACTION);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_INTERACTION;

				if (pagination) {
					sql = sql.concat(InteractionModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<Interaction>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Interaction>(list);
				}
				else {
					list = (List<Interaction>)QueryUtil.list(q, getDialect(),
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
	 * Removes all the interactions from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeAll() throws SystemException {
		for (Interaction interaction : findAll()) {
			remove(interaction);
		}
	}

	/**
	 * Returns the number of interactions.
	 *
	 * @return the number of interactions
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

				Query q = session.createQuery(_SQL_COUNT_INTERACTION);

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
	 * Initializes the interaction persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.util.service.ServiceProps.get(
						"value.object.listener.com.inikah.slayer.model.Interaction")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<Interaction>> listenersList = new ArrayList<ModelListener<Interaction>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<Interaction>)InstanceFactory.newInstance(
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
		EntityCacheUtil.removeCache(InteractionImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_INTERACTION = "SELECT interaction FROM Interaction interaction";
	private static final String _SQL_SELECT_INTERACTION_WHERE = "SELECT interaction FROM Interaction interaction WHERE ";
	private static final String _SQL_COUNT_INTERACTION = "SELECT COUNT(interaction) FROM Interaction interaction";
	private static final String _SQL_COUNT_INTERACTION_WHERE = "SELECT COUNT(interaction) FROM Interaction interaction WHERE ";
	private static final String _ORDER_BY_ENTITY_ALIAS = "interaction.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No Interaction exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No Interaction exists with the key {";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
				PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
	private static Log _log = LogFactoryUtil.getLog(InteractionPersistenceImpl.class);
	private static Interaction _nullInteraction = new InteractionImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<Interaction> toCacheModel() {
				return _nullInteractionCacheModel;
			}
		};

	private static CacheModel<Interaction> _nullInteractionCacheModel = new CacheModel<Interaction>() {
			@Override
			public Interaction toEntityModel() {
				return _nullInteraction;
			}
		};
}