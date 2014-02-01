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

import com.inikah.slayer.NoSuchRelativeException;
import com.inikah.slayer.model.Relative;
import com.inikah.slayer.model.impl.RelativeImpl;
import com.inikah.slayer.model.impl.RelativeModelImpl;

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
 * The persistence implementation for the relative service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Ahmed Hasan
 * @see RelativePersistence
 * @see RelativeUtil
 * @generated
 */
public class RelativePersistenceImpl extends BasePersistenceImpl<Relative>
	implements RelativePersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link RelativeUtil} to access the relative persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = RelativeImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(RelativeModelImpl.ENTITY_CACHE_ENABLED,
			RelativeModelImpl.FINDER_CACHE_ENABLED, RelativeImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(RelativeModelImpl.ENTITY_CACHE_ENABLED,
			RelativeModelImpl.FINDER_CACHE_ENABLED, RelativeImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(RelativeModelImpl.ENTITY_CACHE_ENABLED,
			RelativeModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_RELATIONSHIP =
		new FinderPath(RelativeModelImpl.ENTITY_CACHE_ENABLED,
			RelativeModelImpl.FINDER_CACHE_ENABLED, RelativeImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByRelationship",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_RELATIONSHIP =
		new FinderPath(RelativeModelImpl.ENTITY_CACHE_ENABLED,
			RelativeModelImpl.FINDER_CACHE_ENABLED, RelativeImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByRelationship",
			new String[] { Long.class.getName(), Integer.class.getName() },
			RelativeModelImpl.PROFILEID_COLUMN_BITMASK |
			RelativeModelImpl.RELATIONSHIP_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_RELATIONSHIP = new FinderPath(RelativeModelImpl.ENTITY_CACHE_ENABLED,
			RelativeModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByRelationship",
			new String[] { Long.class.getName(), Integer.class.getName() });

	/**
	 * Returns all the relatives where profileId = &#63; and relationship = &#63;.
	 *
	 * @param profileId the profile ID
	 * @param relationship the relationship
	 * @return the matching relatives
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Relative> findByRelationship(long profileId, int relationship)
		throws SystemException {
		return findByRelationship(profileId, relationship, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the relatives where profileId = &#63; and relationship = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.RelativeModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param profileId the profile ID
	 * @param relationship the relationship
	 * @param start the lower bound of the range of relatives
	 * @param end the upper bound of the range of relatives (not inclusive)
	 * @return the range of matching relatives
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Relative> findByRelationship(long profileId, int relationship,
		int start, int end) throws SystemException {
		return findByRelationship(profileId, relationship, start, end, null);
	}

	/**
	 * Returns an ordered range of all the relatives where profileId = &#63; and relationship = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.RelativeModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param profileId the profile ID
	 * @param relationship the relationship
	 * @param start the lower bound of the range of relatives
	 * @param end the upper bound of the range of relatives (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching relatives
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Relative> findByRelationship(long profileId, int relationship,
		int start, int end, OrderByComparator orderByComparator)
		throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_RELATIONSHIP;
			finderArgs = new Object[] { profileId, relationship };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_RELATIONSHIP;
			finderArgs = new Object[] {
					profileId, relationship,
					
					start, end, orderByComparator
				};
		}

		List<Relative> list = (List<Relative>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (Relative relative : list) {
				if ((profileId != relative.getProfileId()) ||
						(relationship != relative.getRelationship())) {
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

			query.append(_SQL_SELECT_RELATIVE_WHERE);

			query.append(_FINDER_COLUMN_RELATIONSHIP_PROFILEID_2);

			query.append(_FINDER_COLUMN_RELATIONSHIP_RELATIONSHIP_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(RelativeModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(profileId);

				qPos.add(relationship);

				if (!pagination) {
					list = (List<Relative>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Relative>(list);
				}
				else {
					list = (List<Relative>)QueryUtil.list(q, getDialect(),
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
	 * Returns the first relative in the ordered set where profileId = &#63; and relationship = &#63;.
	 *
	 * @param profileId the profile ID
	 * @param relationship the relationship
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching relative
	 * @throws com.inikah.slayer.NoSuchRelativeException if a matching relative could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Relative findByRelationship_First(long profileId, int relationship,
		OrderByComparator orderByComparator)
		throws NoSuchRelativeException, SystemException {
		Relative relative = fetchByRelationship_First(profileId, relationship,
				orderByComparator);

		if (relative != null) {
			return relative;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("profileId=");
		msg.append(profileId);

		msg.append(", relationship=");
		msg.append(relationship);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchRelativeException(msg.toString());
	}

	/**
	 * Returns the first relative in the ordered set where profileId = &#63; and relationship = &#63;.
	 *
	 * @param profileId the profile ID
	 * @param relationship the relationship
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching relative, or <code>null</code> if a matching relative could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Relative fetchByRelationship_First(long profileId, int relationship,
		OrderByComparator orderByComparator) throws SystemException {
		List<Relative> list = findByRelationship(profileId, relationship, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last relative in the ordered set where profileId = &#63; and relationship = &#63;.
	 *
	 * @param profileId the profile ID
	 * @param relationship the relationship
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching relative
	 * @throws com.inikah.slayer.NoSuchRelativeException if a matching relative could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Relative findByRelationship_Last(long profileId, int relationship,
		OrderByComparator orderByComparator)
		throws NoSuchRelativeException, SystemException {
		Relative relative = fetchByRelationship_Last(profileId, relationship,
				orderByComparator);

		if (relative != null) {
			return relative;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("profileId=");
		msg.append(profileId);

		msg.append(", relationship=");
		msg.append(relationship);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchRelativeException(msg.toString());
	}

	/**
	 * Returns the last relative in the ordered set where profileId = &#63; and relationship = &#63;.
	 *
	 * @param profileId the profile ID
	 * @param relationship the relationship
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching relative, or <code>null</code> if a matching relative could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Relative fetchByRelationship_Last(long profileId, int relationship,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByRelationship(profileId, relationship);

		if (count == 0) {
			return null;
		}

		List<Relative> list = findByRelationship(profileId, relationship,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the relatives before and after the current relative in the ordered set where profileId = &#63; and relationship = &#63;.
	 *
	 * @param relativeId the primary key of the current relative
	 * @param profileId the profile ID
	 * @param relationship the relationship
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next relative
	 * @throws com.inikah.slayer.NoSuchRelativeException if a relative with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Relative[] findByRelationship_PrevAndNext(long relativeId,
		long profileId, int relationship, OrderByComparator orderByComparator)
		throws NoSuchRelativeException, SystemException {
		Relative relative = findByPrimaryKey(relativeId);

		Session session = null;

		try {
			session = openSession();

			Relative[] array = new RelativeImpl[3];

			array[0] = getByRelationship_PrevAndNext(session, relative,
					profileId, relationship, orderByComparator, true);

			array[1] = relative;

			array[2] = getByRelationship_PrevAndNext(session, relative,
					profileId, relationship, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected Relative getByRelationship_PrevAndNext(Session session,
		Relative relative, long profileId, int relationship,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_RELATIVE_WHERE);

		query.append(_FINDER_COLUMN_RELATIONSHIP_PROFILEID_2);

		query.append(_FINDER_COLUMN_RELATIONSHIP_RELATIONSHIP_2);

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
			query.append(RelativeModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(profileId);

		qPos.add(relationship);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(relative);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<Relative> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the relatives where profileId = &#63; and relationship = &#63; from the database.
	 *
	 * @param profileId the profile ID
	 * @param relationship the relationship
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeByRelationship(long profileId, int relationship)
		throws SystemException {
		for (Relative relative : findByRelationship(profileId, relationship,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(relative);
		}
	}

	/**
	 * Returns the number of relatives where profileId = &#63; and relationship = &#63;.
	 *
	 * @param profileId the profile ID
	 * @param relationship the relationship
	 * @return the number of matching relatives
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByRelationship(long profileId, int relationship)
		throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_RELATIONSHIP;

		Object[] finderArgs = new Object[] { profileId, relationship };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_RELATIVE_WHERE);

			query.append(_FINDER_COLUMN_RELATIONSHIP_PROFILEID_2);

			query.append(_FINDER_COLUMN_RELATIONSHIP_RELATIONSHIP_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(profileId);

				qPos.add(relationship);

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

	private static final String _FINDER_COLUMN_RELATIONSHIP_PROFILEID_2 = "relative.profileId = ? AND ";
	private static final String _FINDER_COLUMN_RELATIONSHIP_RELATIONSHIP_2 = "relative.relationship = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_PROFILEID =
		new FinderPath(RelativeModelImpl.ENTITY_CACHE_ENABLED,
			RelativeModelImpl.FINDER_CACHE_ENABLED, RelativeImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByProfileId",
			new String[] {
				Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_PROFILEID =
		new FinderPath(RelativeModelImpl.ENTITY_CACHE_ENABLED,
			RelativeModelImpl.FINDER_CACHE_ENABLED, RelativeImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByProfileId",
			new String[] { Long.class.getName() },
			RelativeModelImpl.PROFILEID_COLUMN_BITMASK |
			RelativeModelImpl.RELATIONSHIP_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_PROFILEID = new FinderPath(RelativeModelImpl.ENTITY_CACHE_ENABLED,
			RelativeModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByProfileId",
			new String[] { Long.class.getName() });

	/**
	 * Returns all the relatives where profileId = &#63;.
	 *
	 * @param profileId the profile ID
	 * @return the matching relatives
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Relative> findByProfileId(long profileId)
		throws SystemException {
		return findByProfileId(profileId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the relatives where profileId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.RelativeModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param profileId the profile ID
	 * @param start the lower bound of the range of relatives
	 * @param end the upper bound of the range of relatives (not inclusive)
	 * @return the range of matching relatives
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Relative> findByProfileId(long profileId, int start, int end)
		throws SystemException {
		return findByProfileId(profileId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the relatives where profileId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.RelativeModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param profileId the profile ID
	 * @param start the lower bound of the range of relatives
	 * @param end the upper bound of the range of relatives (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching relatives
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Relative> findByProfileId(long profileId, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_PROFILEID;
			finderArgs = new Object[] { profileId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_PROFILEID;
			finderArgs = new Object[] { profileId, start, end, orderByComparator };
		}

		List<Relative> list = (List<Relative>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (Relative relative : list) {
				if ((profileId != relative.getProfileId())) {
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

			query.append(_SQL_SELECT_RELATIVE_WHERE);

			query.append(_FINDER_COLUMN_PROFILEID_PROFILEID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(RelativeModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(profileId);

				if (!pagination) {
					list = (List<Relative>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Relative>(list);
				}
				else {
					list = (List<Relative>)QueryUtil.list(q, getDialect(),
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
	 * Returns the first relative in the ordered set where profileId = &#63;.
	 *
	 * @param profileId the profile ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching relative
	 * @throws com.inikah.slayer.NoSuchRelativeException if a matching relative could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Relative findByProfileId_First(long profileId,
		OrderByComparator orderByComparator)
		throws NoSuchRelativeException, SystemException {
		Relative relative = fetchByProfileId_First(profileId, orderByComparator);

		if (relative != null) {
			return relative;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("profileId=");
		msg.append(profileId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchRelativeException(msg.toString());
	}

	/**
	 * Returns the first relative in the ordered set where profileId = &#63;.
	 *
	 * @param profileId the profile ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching relative, or <code>null</code> if a matching relative could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Relative fetchByProfileId_First(long profileId,
		OrderByComparator orderByComparator) throws SystemException {
		List<Relative> list = findByProfileId(profileId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last relative in the ordered set where profileId = &#63;.
	 *
	 * @param profileId the profile ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching relative
	 * @throws com.inikah.slayer.NoSuchRelativeException if a matching relative could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Relative findByProfileId_Last(long profileId,
		OrderByComparator orderByComparator)
		throws NoSuchRelativeException, SystemException {
		Relative relative = fetchByProfileId_Last(profileId, orderByComparator);

		if (relative != null) {
			return relative;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("profileId=");
		msg.append(profileId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchRelativeException(msg.toString());
	}

	/**
	 * Returns the last relative in the ordered set where profileId = &#63;.
	 *
	 * @param profileId the profile ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching relative, or <code>null</code> if a matching relative could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Relative fetchByProfileId_Last(long profileId,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByProfileId(profileId);

		if (count == 0) {
			return null;
		}

		List<Relative> list = findByProfileId(profileId, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the relatives before and after the current relative in the ordered set where profileId = &#63;.
	 *
	 * @param relativeId the primary key of the current relative
	 * @param profileId the profile ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next relative
	 * @throws com.inikah.slayer.NoSuchRelativeException if a relative with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Relative[] findByProfileId_PrevAndNext(long relativeId,
		long profileId, OrderByComparator orderByComparator)
		throws NoSuchRelativeException, SystemException {
		Relative relative = findByPrimaryKey(relativeId);

		Session session = null;

		try {
			session = openSession();

			Relative[] array = new RelativeImpl[3];

			array[0] = getByProfileId_PrevAndNext(session, relative, profileId,
					orderByComparator, true);

			array[1] = relative;

			array[2] = getByProfileId_PrevAndNext(session, relative, profileId,
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

	protected Relative getByProfileId_PrevAndNext(Session session,
		Relative relative, long profileId, OrderByComparator orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_RELATIVE_WHERE);

		query.append(_FINDER_COLUMN_PROFILEID_PROFILEID_2);

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
			query.append(RelativeModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(profileId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(relative);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<Relative> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the relatives where profileId = &#63; from the database.
	 *
	 * @param profileId the profile ID
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeByProfileId(long profileId) throws SystemException {
		for (Relative relative : findByProfileId(profileId, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, null)) {
			remove(relative);
		}
	}

	/**
	 * Returns the number of relatives where profileId = &#63;.
	 *
	 * @param profileId the profile ID
	 * @return the number of matching relatives
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByProfileId(long profileId) throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_PROFILEID;

		Object[] finderArgs = new Object[] { profileId };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_RELATIVE_WHERE);

			query.append(_FINDER_COLUMN_PROFILEID_PROFILEID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(profileId);

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

	private static final String _FINDER_COLUMN_PROFILEID_PROFILEID_2 = "relative.profileId = ?";

	public RelativePersistenceImpl() {
		setModelClass(Relative.class);
	}

	/**
	 * Caches the relative in the entity cache if it is enabled.
	 *
	 * @param relative the relative
	 */
	@Override
	public void cacheResult(Relative relative) {
		EntityCacheUtil.putResult(RelativeModelImpl.ENTITY_CACHE_ENABLED,
			RelativeImpl.class, relative.getPrimaryKey(), relative);

		relative.resetOriginalValues();
	}

	/**
	 * Caches the relatives in the entity cache if it is enabled.
	 *
	 * @param relatives the relatives
	 */
	@Override
	public void cacheResult(List<Relative> relatives) {
		for (Relative relative : relatives) {
			if (EntityCacheUtil.getResult(
						RelativeModelImpl.ENTITY_CACHE_ENABLED,
						RelativeImpl.class, relative.getPrimaryKey()) == null) {
				cacheResult(relative);
			}
			else {
				relative.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all relatives.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(RelativeImpl.class.getName());
		}

		EntityCacheUtil.clearCache(RelativeImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the relative.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(Relative relative) {
		EntityCacheUtil.removeResult(RelativeModelImpl.ENTITY_CACHE_ENABLED,
			RelativeImpl.class, relative.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<Relative> relatives) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Relative relative : relatives) {
			EntityCacheUtil.removeResult(RelativeModelImpl.ENTITY_CACHE_ENABLED,
				RelativeImpl.class, relative.getPrimaryKey());
		}
	}

	/**
	 * Creates a new relative with the primary key. Does not add the relative to the database.
	 *
	 * @param relativeId the primary key for the new relative
	 * @return the new relative
	 */
	@Override
	public Relative create(long relativeId) {
		Relative relative = new RelativeImpl();

		relative.setNew(true);
		relative.setPrimaryKey(relativeId);

		return relative;
	}

	/**
	 * Removes the relative with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param relativeId the primary key of the relative
	 * @return the relative that was removed
	 * @throws com.inikah.slayer.NoSuchRelativeException if a relative with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Relative remove(long relativeId)
		throws NoSuchRelativeException, SystemException {
		return remove((Serializable)relativeId);
	}

	/**
	 * Removes the relative with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the relative
	 * @return the relative that was removed
	 * @throws com.inikah.slayer.NoSuchRelativeException if a relative with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Relative remove(Serializable primaryKey)
		throws NoSuchRelativeException, SystemException {
		Session session = null;

		try {
			session = openSession();

			Relative relative = (Relative)session.get(RelativeImpl.class,
					primaryKey);

			if (relative == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchRelativeException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(relative);
		}
		catch (NoSuchRelativeException nsee) {
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
	protected Relative removeImpl(Relative relative) throws SystemException {
		relative = toUnwrappedModel(relative);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(relative)) {
				relative = (Relative)session.get(RelativeImpl.class,
						relative.getPrimaryKeyObj());
			}

			if (relative != null) {
				session.delete(relative);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (relative != null) {
			clearCache(relative);
		}

		return relative;
	}

	@Override
	public Relative updateImpl(com.inikah.slayer.model.Relative relative)
		throws SystemException {
		relative = toUnwrappedModel(relative);

		boolean isNew = relative.isNew();

		RelativeModelImpl relativeModelImpl = (RelativeModelImpl)relative;

		Session session = null;

		try {
			session = openSession();

			if (relative.isNew()) {
				session.save(relative);

				relative.setNew(false);
			}
			else {
				session.merge(relative);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !RelativeModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		else {
			if ((relativeModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_RELATIONSHIP.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						relativeModelImpl.getOriginalProfileId(),
						relativeModelImpl.getOriginalRelationship()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_RELATIONSHIP,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_RELATIONSHIP,
					args);

				args = new Object[] {
						relativeModelImpl.getProfileId(),
						relativeModelImpl.getRelationship()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_RELATIONSHIP,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_RELATIONSHIP,
					args);
			}

			if ((relativeModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_PROFILEID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						relativeModelImpl.getOriginalProfileId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_PROFILEID,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_PROFILEID,
					args);

				args = new Object[] { relativeModelImpl.getProfileId() };

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_PROFILEID,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_PROFILEID,
					args);
			}
		}

		EntityCacheUtil.putResult(RelativeModelImpl.ENTITY_CACHE_ENABLED,
			RelativeImpl.class, relative.getPrimaryKey(), relative);

		return relative;
	}

	protected Relative toUnwrappedModel(Relative relative) {
		if (relative instanceof RelativeImpl) {
			return relative;
		}

		RelativeImpl relativeImpl = new RelativeImpl();

		relativeImpl.setNew(relative.isNew());
		relativeImpl.setPrimaryKey(relative.getPrimaryKey());

		relativeImpl.setRelativeId(relative.getRelativeId());
		relativeImpl.setProfileId(relative.getProfileId());
		relativeImpl.setName(relative.getName());
		relativeImpl.setUnMarried(relative.isUnMarried());
		relativeImpl.setPassedAway(relative.isPassedAway());
		relativeImpl.setRelationship(relative.getRelationship());
		relativeImpl.setAge(relative.getAge());
		relativeImpl.setResidingIn(relative.getResidingIn());
		relativeImpl.setEmailAddress(relative.getEmailAddress());
		relativeImpl.setProfession(relative.getProfession());
		relativeImpl.setCreateDate(relative.getCreateDate());
		relativeImpl.setModifiedDate(relative.getModifiedDate());
		relativeImpl.setOwner(relative.isOwner());
		relativeImpl.setYounger(relative.isYounger());

		return relativeImpl;
	}

	/**
	 * Returns the relative with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the relative
	 * @return the relative
	 * @throws com.inikah.slayer.NoSuchRelativeException if a relative with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Relative findByPrimaryKey(Serializable primaryKey)
		throws NoSuchRelativeException, SystemException {
		Relative relative = fetchByPrimaryKey(primaryKey);

		if (relative == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchRelativeException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return relative;
	}

	/**
	 * Returns the relative with the primary key or throws a {@link com.inikah.slayer.NoSuchRelativeException} if it could not be found.
	 *
	 * @param relativeId the primary key of the relative
	 * @return the relative
	 * @throws com.inikah.slayer.NoSuchRelativeException if a relative with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Relative findByPrimaryKey(long relativeId)
		throws NoSuchRelativeException, SystemException {
		return findByPrimaryKey((Serializable)relativeId);
	}

	/**
	 * Returns the relative with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the relative
	 * @return the relative, or <code>null</code> if a relative with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Relative fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		Relative relative = (Relative)EntityCacheUtil.getResult(RelativeModelImpl.ENTITY_CACHE_ENABLED,
				RelativeImpl.class, primaryKey);

		if (relative == _nullRelative) {
			return null;
		}

		if (relative == null) {
			Session session = null;

			try {
				session = openSession();

				relative = (Relative)session.get(RelativeImpl.class, primaryKey);

				if (relative != null) {
					cacheResult(relative);
				}
				else {
					EntityCacheUtil.putResult(RelativeModelImpl.ENTITY_CACHE_ENABLED,
						RelativeImpl.class, primaryKey, _nullRelative);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(RelativeModelImpl.ENTITY_CACHE_ENABLED,
					RelativeImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return relative;
	}

	/**
	 * Returns the relative with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param relativeId the primary key of the relative
	 * @return the relative, or <code>null</code> if a relative with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Relative fetchByPrimaryKey(long relativeId)
		throws SystemException {
		return fetchByPrimaryKey((Serializable)relativeId);
	}

	/**
	 * Returns all the relatives.
	 *
	 * @return the relatives
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Relative> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the relatives.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.RelativeModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of relatives
	 * @param end the upper bound of the range of relatives (not inclusive)
	 * @return the range of relatives
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Relative> findAll(int start, int end) throws SystemException {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the relatives.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.RelativeModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of relatives
	 * @param end the upper bound of the range of relatives (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of relatives
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Relative> findAll(int start, int end,
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

		List<Relative> list = (List<Relative>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_RELATIVE);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_RELATIVE;

				if (pagination) {
					sql = sql.concat(RelativeModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<Relative>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Relative>(list);
				}
				else {
					list = (List<Relative>)QueryUtil.list(q, getDialect(),
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
	 * Removes all the relatives from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeAll() throws SystemException {
		for (Relative relative : findAll()) {
			remove(relative);
		}
	}

	/**
	 * Returns the number of relatives.
	 *
	 * @return the number of relatives
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

				Query q = session.createQuery(_SQL_COUNT_RELATIVE);

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
	 * Initializes the relative persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.util.service.ServiceProps.get(
						"value.object.listener.com.inikah.slayer.model.Relative")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<Relative>> listenersList = new ArrayList<ModelListener<Relative>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<Relative>)InstanceFactory.newInstance(
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
		EntityCacheUtil.removeCache(RelativeImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_RELATIVE = "SELECT relative FROM Relative relative";
	private static final String _SQL_SELECT_RELATIVE_WHERE = "SELECT relative FROM Relative relative WHERE ";
	private static final String _SQL_COUNT_RELATIVE = "SELECT COUNT(relative) FROM Relative relative";
	private static final String _SQL_COUNT_RELATIVE_WHERE = "SELECT COUNT(relative) FROM Relative relative WHERE ";
	private static final String _ORDER_BY_ENTITY_ALIAS = "relative.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No Relative exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No Relative exists with the key {";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
				PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
	private static Log _log = LogFactoryUtil.getLog(RelativePersistenceImpl.class);
	private static Relative _nullRelative = new RelativeImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<Relative> toCacheModel() {
				return _nullRelativeCacheModel;
			}
		};

	private static CacheModel<Relative> _nullRelativeCacheModel = new CacheModel<Relative>() {
			@Override
			public Relative toEntityModel() {
				return _nullRelative;
			}
		};
}