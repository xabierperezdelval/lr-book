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

import com.inikah.slayer.NoSuchProfileException;
import com.inikah.slayer.model.Profile;
import com.inikah.slayer.model.impl.ProfileImpl;
import com.inikah.slayer.model.impl.ProfileModelImpl;

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
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The persistence implementation for the profile service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Ahmed Hasan
 * @see ProfilePersistence
 * @see ProfileUtil
 * @generated
 */
public class ProfilePersistenceImpl extends BasePersistenceImpl<Profile>
	implements ProfilePersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link ProfileUtil} to access the profile persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = ProfileImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(ProfileModelImpl.ENTITY_CACHE_ENABLED,
			ProfileModelImpl.FINDER_CACHE_ENABLED, ProfileImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(ProfileModelImpl.ENTITY_CACHE_ENABLED,
			ProfileModelImpl.FINDER_CACHE_ENABLED, ProfileImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(ProfileModelImpl.ENTITY_CACHE_ENABLED,
			ProfileModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_USERID = new FinderPath(ProfileModelImpl.ENTITY_CACHE_ENABLED,
			ProfileModelImpl.FINDER_CACHE_ENABLED, ProfileImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUserId",
			new String[] {
				Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID =
		new FinderPath(ProfileModelImpl.ENTITY_CACHE_ENABLED,
			ProfileModelImpl.FINDER_CACHE_ENABLED, ProfileImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUserId",
			new String[] { Long.class.getName() },
			ProfileModelImpl.USERID_COLUMN_BITMASK |
			ProfileModelImpl.OWNERLASTLOGIN_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_USERID = new FinderPath(ProfileModelImpl.ENTITY_CACHE_ENABLED,
			ProfileModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUserId",
			new String[] { Long.class.getName() });

	/**
	 * Returns all the profiles where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the matching profiles
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Profile> findByUserId(long userId) throws SystemException {
		return findByUserId(userId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the profiles where userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.ProfileModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param start the lower bound of the range of profiles
	 * @param end the upper bound of the range of profiles (not inclusive)
	 * @return the range of matching profiles
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Profile> findByUserId(long userId, int start, int end)
		throws SystemException {
		return findByUserId(userId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the profiles where userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.ProfileModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param start the lower bound of the range of profiles
	 * @param end the upper bound of the range of profiles (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching profiles
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Profile> findByUserId(long userId, int start, int end,
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

		List<Profile> list = (List<Profile>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (Profile profile : list) {
				if ((userId != profile.getUserId())) {
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

			query.append(_SQL_SELECT_PROFILE_WHERE);

			query.append(_FINDER_COLUMN_USERID_USERID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(ProfileModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				if (!pagination) {
					list = (List<Profile>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Profile>(list);
				}
				else {
					list = (List<Profile>)QueryUtil.list(q, getDialect(),
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
	 * Returns the first profile in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching profile
	 * @throws com.inikah.slayer.NoSuchProfileException if a matching profile could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Profile findByUserId_First(long userId,
		OrderByComparator orderByComparator)
		throws NoSuchProfileException, SystemException {
		Profile profile = fetchByUserId_First(userId, orderByComparator);

		if (profile != null) {
			return profile;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchProfileException(msg.toString());
	}

	/**
	 * Returns the first profile in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching profile, or <code>null</code> if a matching profile could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Profile fetchByUserId_First(long userId,
		OrderByComparator orderByComparator) throws SystemException {
		List<Profile> list = findByUserId(userId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last profile in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching profile
	 * @throws com.inikah.slayer.NoSuchProfileException if a matching profile could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Profile findByUserId_Last(long userId,
		OrderByComparator orderByComparator)
		throws NoSuchProfileException, SystemException {
		Profile profile = fetchByUserId_Last(userId, orderByComparator);

		if (profile != null) {
			return profile;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchProfileException(msg.toString());
	}

	/**
	 * Returns the last profile in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching profile, or <code>null</code> if a matching profile could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Profile fetchByUserId_Last(long userId,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByUserId(userId);

		if (count == 0) {
			return null;
		}

		List<Profile> list = findByUserId(userId, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the profiles before and after the current profile in the ordered set where userId = &#63;.
	 *
	 * @param profileId the primary key of the current profile
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next profile
	 * @throws com.inikah.slayer.NoSuchProfileException if a profile with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Profile[] findByUserId_PrevAndNext(long profileId, long userId,
		OrderByComparator orderByComparator)
		throws NoSuchProfileException, SystemException {
		Profile profile = findByPrimaryKey(profileId);

		Session session = null;

		try {
			session = openSession();

			Profile[] array = new ProfileImpl[3];

			array[0] = getByUserId_PrevAndNext(session, profile, userId,
					orderByComparator, true);

			array[1] = profile;

			array[2] = getByUserId_PrevAndNext(session, profile, userId,
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

	protected Profile getByUserId_PrevAndNext(Session session, Profile profile,
		long userId, OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_PROFILE_WHERE);

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
			query.append(ProfileModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(userId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(profile);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<Profile> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the profiles where userId = &#63; from the database.
	 *
	 * @param userId the user ID
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeByUserId(long userId) throws SystemException {
		for (Profile profile : findByUserId(userId, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, null)) {
			remove(profile);
		}
	}

	/**
	 * Returns the number of profiles where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the number of matching profiles
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

			query.append(_SQL_COUNT_PROFILE_WHERE);

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

	private static final String _FINDER_COLUMN_USERID_USERID_2 = "profile.userId = ?";
	public static final FinderPath FINDER_PATH_FETCH_BY_USERID_EMAILADDRESS = new FinderPath(ProfileModelImpl.ENTITY_CACHE_ENABLED,
			ProfileModelImpl.FINDER_CACHE_ENABLED, ProfileImpl.class,
			FINDER_CLASS_NAME_ENTITY, "fetchByUserId_EmailAddress",
			new String[] { Long.class.getName(), String.class.getName() },
			ProfileModelImpl.USERID_COLUMN_BITMASK |
			ProfileModelImpl.EMAILADDRESS_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_USERID_EMAILADDRESS = new FinderPath(ProfileModelImpl.ENTITY_CACHE_ENABLED,
			ProfileModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByUserId_EmailAddress",
			new String[] { Long.class.getName(), String.class.getName() });

	/**
	 * Returns the profile where userId = &#63; and emailAddress = &#63; or throws a {@link com.inikah.slayer.NoSuchProfileException} if it could not be found.
	 *
	 * @param userId the user ID
	 * @param emailAddress the email address
	 * @return the matching profile
	 * @throws com.inikah.slayer.NoSuchProfileException if a matching profile could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Profile findByUserId_EmailAddress(long userId, String emailAddress)
		throws NoSuchProfileException, SystemException {
		Profile profile = fetchByUserId_EmailAddress(userId, emailAddress);

		if (profile == null) {
			StringBundler msg = new StringBundler(6);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("userId=");
			msg.append(userId);

			msg.append(", emailAddress=");
			msg.append(emailAddress);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchProfileException(msg.toString());
		}

		return profile;
	}

	/**
	 * Returns the profile where userId = &#63; and emailAddress = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param userId the user ID
	 * @param emailAddress the email address
	 * @return the matching profile, or <code>null</code> if a matching profile could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Profile fetchByUserId_EmailAddress(long userId, String emailAddress)
		throws SystemException {
		return fetchByUserId_EmailAddress(userId, emailAddress, true);
	}

	/**
	 * Returns the profile where userId = &#63; and emailAddress = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param userId the user ID
	 * @param emailAddress the email address
	 * @param retrieveFromCache whether to use the finder cache
	 * @return the matching profile, or <code>null</code> if a matching profile could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Profile fetchByUserId_EmailAddress(long userId, String emailAddress,
		boolean retrieveFromCache) throws SystemException {
		Object[] finderArgs = new Object[] { userId, emailAddress };

		Object result = null;

		if (retrieveFromCache) {
			result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_USERID_EMAILADDRESS,
					finderArgs, this);
		}

		if (result instanceof Profile) {
			Profile profile = (Profile)result;

			if ((userId != profile.getUserId()) ||
					!Validator.equals(emailAddress, profile.getEmailAddress())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_SELECT_PROFILE_WHERE);

			query.append(_FINDER_COLUMN_USERID_EMAILADDRESS_USERID_2);

			boolean bindEmailAddress = false;

			if (emailAddress == null) {
				query.append(_FINDER_COLUMN_USERID_EMAILADDRESS_EMAILADDRESS_1);
			}
			else if (emailAddress.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_USERID_EMAILADDRESS_EMAILADDRESS_3);
			}
			else {
				bindEmailAddress = true;

				query.append(_FINDER_COLUMN_USERID_EMAILADDRESS_EMAILADDRESS_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				if (bindEmailAddress) {
					qPos.add(emailAddress);
				}

				List<Profile> list = q.list();

				if (list.isEmpty()) {
					FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERID_EMAILADDRESS,
						finderArgs, list);
				}
				else {
					if ((list.size() > 1) && _log.isWarnEnabled()) {
						_log.warn(
							"ProfilePersistenceImpl.fetchByUserId_EmailAddress(long, String, boolean) with parameters (" +
							StringUtil.merge(finderArgs) +
							") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
					}

					Profile profile = list.get(0);

					result = profile;

					cacheResult(profile);

					if ((profile.getUserId() != userId) ||
							(profile.getEmailAddress() == null) ||
							!profile.getEmailAddress().equals(emailAddress)) {
						FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERID_EMAILADDRESS,
							finderArgs, profile);
					}
				}
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_USERID_EMAILADDRESS,
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
			return (Profile)result;
		}
	}

	/**
	 * Removes the profile where userId = &#63; and emailAddress = &#63; from the database.
	 *
	 * @param userId the user ID
	 * @param emailAddress the email address
	 * @return the profile that was removed
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Profile removeByUserId_EmailAddress(long userId, String emailAddress)
		throws NoSuchProfileException, SystemException {
		Profile profile = findByUserId_EmailAddress(userId, emailAddress);

		return remove(profile);
	}

	/**
	 * Returns the number of profiles where userId = &#63; and emailAddress = &#63;.
	 *
	 * @param userId the user ID
	 * @param emailAddress the email address
	 * @return the number of matching profiles
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByUserId_EmailAddress(long userId, String emailAddress)
		throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_USERID_EMAILADDRESS;

		Object[] finderArgs = new Object[] { userId, emailAddress };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_PROFILE_WHERE);

			query.append(_FINDER_COLUMN_USERID_EMAILADDRESS_USERID_2);

			boolean bindEmailAddress = false;

			if (emailAddress == null) {
				query.append(_FINDER_COLUMN_USERID_EMAILADDRESS_EMAILADDRESS_1);
			}
			else if (emailAddress.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_USERID_EMAILADDRESS_EMAILADDRESS_3);
			}
			else {
				bindEmailAddress = true;

				query.append(_FINDER_COLUMN_USERID_EMAILADDRESS_EMAILADDRESS_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				if (bindEmailAddress) {
					qPos.add(emailAddress);
				}

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

	private static final String _FINDER_COLUMN_USERID_EMAILADDRESS_USERID_2 = "profile.userId = ? AND ";
	private static final String _FINDER_COLUMN_USERID_EMAILADDRESS_EMAILADDRESS_1 =
		"profile.emailAddress IS NULL AND profile.status = 0";
	private static final String _FINDER_COLUMN_USERID_EMAILADDRESS_EMAILADDRESS_2 =
		"profile.emailAddress = ? AND profile.status = 0";
	private static final String _FINDER_COLUMN_USERID_EMAILADDRESS_EMAILADDRESS_3 =
		"(profile.emailAddress IS NULL OR profile.emailAddress = '') AND profile.status = 0";
	public static final FinderPath FINDER_PATH_FETCH_BY_USERID_PROFILEID = new FinderPath(ProfileModelImpl.ENTITY_CACHE_ENABLED,
			ProfileModelImpl.FINDER_CACHE_ENABLED, ProfileImpl.class,
			FINDER_CLASS_NAME_ENTITY, "fetchByUserId_ProfileId",
			new String[] { Long.class.getName(), Long.class.getName() },
			ProfileModelImpl.USERID_COLUMN_BITMASK |
			ProfileModelImpl.PROFILEID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_USERID_PROFILEID = new FinderPath(ProfileModelImpl.ENTITY_CACHE_ENABLED,
			ProfileModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByUserId_ProfileId",
			new String[] { Long.class.getName(), Long.class.getName() });

	/**
	 * Returns the profile where userId = &#63; and profileId = &#63; or throws a {@link com.inikah.slayer.NoSuchProfileException} if it could not be found.
	 *
	 * @param userId the user ID
	 * @param profileId the profile ID
	 * @return the matching profile
	 * @throws com.inikah.slayer.NoSuchProfileException if a matching profile could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Profile findByUserId_ProfileId(long userId, long profileId)
		throws NoSuchProfileException, SystemException {
		Profile profile = fetchByUserId_ProfileId(userId, profileId);

		if (profile == null) {
			StringBundler msg = new StringBundler(6);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("userId=");
			msg.append(userId);

			msg.append(", profileId=");
			msg.append(profileId);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchProfileException(msg.toString());
		}

		return profile;
	}

	/**
	 * Returns the profile where userId = &#63; and profileId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param userId the user ID
	 * @param profileId the profile ID
	 * @return the matching profile, or <code>null</code> if a matching profile could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Profile fetchByUserId_ProfileId(long userId, long profileId)
		throws SystemException {
		return fetchByUserId_ProfileId(userId, profileId, true);
	}

	/**
	 * Returns the profile where userId = &#63; and profileId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param userId the user ID
	 * @param profileId the profile ID
	 * @param retrieveFromCache whether to use the finder cache
	 * @return the matching profile, or <code>null</code> if a matching profile could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Profile fetchByUserId_ProfileId(long userId, long profileId,
		boolean retrieveFromCache) throws SystemException {
		Object[] finderArgs = new Object[] { userId, profileId };

		Object result = null;

		if (retrieveFromCache) {
			result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_USERID_PROFILEID,
					finderArgs, this);
		}

		if (result instanceof Profile) {
			Profile profile = (Profile)result;

			if ((userId != profile.getUserId()) ||
					(profileId != profile.getProfileId())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_SELECT_PROFILE_WHERE);

			query.append(_FINDER_COLUMN_USERID_PROFILEID_USERID_2);

			query.append(_FINDER_COLUMN_USERID_PROFILEID_PROFILEID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				qPos.add(profileId);

				List<Profile> list = q.list();

				if (list.isEmpty()) {
					FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERID_PROFILEID,
						finderArgs, list);
				}
				else {
					if ((list.size() > 1) && _log.isWarnEnabled()) {
						_log.warn(
							"ProfilePersistenceImpl.fetchByUserId_ProfileId(long, long, boolean) with parameters (" +
							StringUtil.merge(finderArgs) +
							") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
					}

					Profile profile = list.get(0);

					result = profile;

					cacheResult(profile);

					if ((profile.getUserId() != userId) ||
							(profile.getProfileId() != profileId)) {
						FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERID_PROFILEID,
							finderArgs, profile);
					}
				}
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_USERID_PROFILEID,
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
			return (Profile)result;
		}
	}

	/**
	 * Removes the profile where userId = &#63; and profileId = &#63; from the database.
	 *
	 * @param userId the user ID
	 * @param profileId the profile ID
	 * @return the profile that was removed
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Profile removeByUserId_ProfileId(long userId, long profileId)
		throws NoSuchProfileException, SystemException {
		Profile profile = findByUserId_ProfileId(userId, profileId);

		return remove(profile);
	}

	/**
	 * Returns the number of profiles where userId = &#63; and profileId = &#63;.
	 *
	 * @param userId the user ID
	 * @param profileId the profile ID
	 * @return the number of matching profiles
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByUserId_ProfileId(long userId, long profileId)
		throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_USERID_PROFILEID;

		Object[] finderArgs = new Object[] { userId, profileId };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_PROFILE_WHERE);

			query.append(_FINDER_COLUMN_USERID_PROFILEID_USERID_2);

			query.append(_FINDER_COLUMN_USERID_PROFILEID_PROFILEID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

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

	private static final String _FINDER_COLUMN_USERID_PROFILEID_USERID_2 = "profile.userId = ? AND ";
	private static final String _FINDER_COLUMN_USERID_PROFILEID_PROFILEID_2 = "profile.profileId = ?";
	public static final FinderPath FINDER_PATH_FETCH_BY_USERID_CREATEDFOR = new FinderPath(ProfileModelImpl.ENTITY_CACHE_ENABLED,
			ProfileModelImpl.FINDER_CACHE_ENABLED, ProfileImpl.class,
			FINDER_CLASS_NAME_ENTITY, "fetchByUserId_CreatedFor",
			new String[] { Long.class.getName(), Integer.class.getName() },
			ProfileModelImpl.USERID_COLUMN_BITMASK |
			ProfileModelImpl.CREATEDFOR_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_USERID_CREATEDFOR = new FinderPath(ProfileModelImpl.ENTITY_CACHE_ENABLED,
			ProfileModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByUserId_CreatedFor",
			new String[] { Long.class.getName(), Integer.class.getName() });

	/**
	 * Returns the profile where userId = &#63; and createdFor = &#63; or throws a {@link com.inikah.slayer.NoSuchProfileException} if it could not be found.
	 *
	 * @param userId the user ID
	 * @param createdFor the created for
	 * @return the matching profile
	 * @throws com.inikah.slayer.NoSuchProfileException if a matching profile could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Profile findByUserId_CreatedFor(long userId, int createdFor)
		throws NoSuchProfileException, SystemException {
		Profile profile = fetchByUserId_CreatedFor(userId, createdFor);

		if (profile == null) {
			StringBundler msg = new StringBundler(6);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("userId=");
			msg.append(userId);

			msg.append(", createdFor=");
			msg.append(createdFor);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchProfileException(msg.toString());
		}

		return profile;
	}

	/**
	 * Returns the profile where userId = &#63; and createdFor = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param userId the user ID
	 * @param createdFor the created for
	 * @return the matching profile, or <code>null</code> if a matching profile could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Profile fetchByUserId_CreatedFor(long userId, int createdFor)
		throws SystemException {
		return fetchByUserId_CreatedFor(userId, createdFor, true);
	}

	/**
	 * Returns the profile where userId = &#63; and createdFor = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param userId the user ID
	 * @param createdFor the created for
	 * @param retrieveFromCache whether to use the finder cache
	 * @return the matching profile, or <code>null</code> if a matching profile could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Profile fetchByUserId_CreatedFor(long userId, int createdFor,
		boolean retrieveFromCache) throws SystemException {
		Object[] finderArgs = new Object[] { userId, createdFor };

		Object result = null;

		if (retrieveFromCache) {
			result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_USERID_CREATEDFOR,
					finderArgs, this);
		}

		if (result instanceof Profile) {
			Profile profile = (Profile)result;

			if ((userId != profile.getUserId()) ||
					(createdFor != profile.getCreatedFor())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_SELECT_PROFILE_WHERE);

			query.append(_FINDER_COLUMN_USERID_CREATEDFOR_USERID_2);

			query.append(_FINDER_COLUMN_USERID_CREATEDFOR_CREATEDFOR_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				qPos.add(createdFor);

				List<Profile> list = q.list();

				if (list.isEmpty()) {
					FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERID_CREATEDFOR,
						finderArgs, list);
				}
				else {
					if ((list.size() > 1) && _log.isWarnEnabled()) {
						_log.warn(
							"ProfilePersistenceImpl.fetchByUserId_CreatedFor(long, int, boolean) with parameters (" +
							StringUtil.merge(finderArgs) +
							") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
					}

					Profile profile = list.get(0);

					result = profile;

					cacheResult(profile);

					if ((profile.getUserId() != userId) ||
							(profile.getCreatedFor() != createdFor)) {
						FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERID_CREATEDFOR,
							finderArgs, profile);
					}
				}
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_USERID_CREATEDFOR,
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
			return (Profile)result;
		}
	}

	/**
	 * Removes the profile where userId = &#63; and createdFor = &#63; from the database.
	 *
	 * @param userId the user ID
	 * @param createdFor the created for
	 * @return the profile that was removed
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Profile removeByUserId_CreatedFor(long userId, int createdFor)
		throws NoSuchProfileException, SystemException {
		Profile profile = findByUserId_CreatedFor(userId, createdFor);

		return remove(profile);
	}

	/**
	 * Returns the number of profiles where userId = &#63; and createdFor = &#63;.
	 *
	 * @param userId the user ID
	 * @param createdFor the created for
	 * @return the number of matching profiles
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByUserId_CreatedFor(long userId, int createdFor)
		throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_USERID_CREATEDFOR;

		Object[] finderArgs = new Object[] { userId, createdFor };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_PROFILE_WHERE);

			query.append(_FINDER_COLUMN_USERID_CREATEDFOR_USERID_2);

			query.append(_FINDER_COLUMN_USERID_CREATEDFOR_CREATEDFOR_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				qPos.add(createdFor);

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

	private static final String _FINDER_COLUMN_USERID_CREATEDFOR_USERID_2 = "profile.userId = ? AND ";
	private static final String _FINDER_COLUMN_USERID_CREATEDFOR_CREATEDFOR_2 = "profile.createdFor = ?";

	public ProfilePersistenceImpl() {
		setModelClass(Profile.class);
	}

	/**
	 * Caches the profile in the entity cache if it is enabled.
	 *
	 * @param profile the profile
	 */
	@Override
	public void cacheResult(Profile profile) {
		EntityCacheUtil.putResult(ProfileModelImpl.ENTITY_CACHE_ENABLED,
			ProfileImpl.class, profile.getPrimaryKey(), profile);

		FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERID_EMAILADDRESS,
			new Object[] { profile.getUserId(), profile.getEmailAddress() },
			profile);

		FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERID_PROFILEID,
			new Object[] { profile.getUserId(), profile.getProfileId() },
			profile);

		FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERID_CREATEDFOR,
			new Object[] { profile.getUserId(), profile.getCreatedFor() },
			profile);

		profile.resetOriginalValues();
	}

	/**
	 * Caches the profiles in the entity cache if it is enabled.
	 *
	 * @param profiles the profiles
	 */
	@Override
	public void cacheResult(List<Profile> profiles) {
		for (Profile profile : profiles) {
			if (EntityCacheUtil.getResult(
						ProfileModelImpl.ENTITY_CACHE_ENABLED,
						ProfileImpl.class, profile.getPrimaryKey()) == null) {
				cacheResult(profile);
			}
			else {
				profile.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all profiles.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(ProfileImpl.class.getName());
		}

		EntityCacheUtil.clearCache(ProfileImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the profile.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(Profile profile) {
		EntityCacheUtil.removeResult(ProfileModelImpl.ENTITY_CACHE_ENABLED,
			ProfileImpl.class, profile.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		clearUniqueFindersCache(profile);
	}

	@Override
	public void clearCache(List<Profile> profiles) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Profile profile : profiles) {
			EntityCacheUtil.removeResult(ProfileModelImpl.ENTITY_CACHE_ENABLED,
				ProfileImpl.class, profile.getPrimaryKey());

			clearUniqueFindersCache(profile);
		}
	}

	protected void cacheUniqueFindersCache(Profile profile) {
		if (profile.isNew()) {
			Object[] args = new Object[] {
					profile.getUserId(), profile.getEmailAddress()
				};

			FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_USERID_EMAILADDRESS,
				args, Long.valueOf(1));
			FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERID_EMAILADDRESS,
				args, profile);

			args = new Object[] { profile.getUserId(), profile.getProfileId() };

			FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_USERID_PROFILEID,
				args, Long.valueOf(1));
			FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERID_PROFILEID,
				args, profile);

			args = new Object[] { profile.getUserId(), profile.getCreatedFor() };

			FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_USERID_CREATEDFOR,
				args, Long.valueOf(1));
			FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERID_CREATEDFOR,
				args, profile);
		}
		else {
			ProfileModelImpl profileModelImpl = (ProfileModelImpl)profile;

			if ((profileModelImpl.getColumnBitmask() &
					FINDER_PATH_FETCH_BY_USERID_EMAILADDRESS.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						profile.getUserId(), profile.getEmailAddress()
					};

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_USERID_EMAILADDRESS,
					args, Long.valueOf(1));
				FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERID_EMAILADDRESS,
					args, profile);
			}

			if ((profileModelImpl.getColumnBitmask() &
					FINDER_PATH_FETCH_BY_USERID_PROFILEID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						profile.getUserId(), profile.getProfileId()
					};

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_USERID_PROFILEID,
					args, Long.valueOf(1));
				FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERID_PROFILEID,
					args, profile);
			}

			if ((profileModelImpl.getColumnBitmask() &
					FINDER_PATH_FETCH_BY_USERID_CREATEDFOR.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						profile.getUserId(), profile.getCreatedFor()
					};

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_USERID_CREATEDFOR,
					args, Long.valueOf(1));
				FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERID_CREATEDFOR,
					args, profile);
			}
		}
	}

	protected void clearUniqueFindersCache(Profile profile) {
		ProfileModelImpl profileModelImpl = (ProfileModelImpl)profile;

		Object[] args = new Object[] {
				profile.getUserId(), profile.getEmailAddress()
			};

		FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID_EMAILADDRESS,
			args);
		FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_USERID_EMAILADDRESS,
			args);

		if ((profileModelImpl.getColumnBitmask() &
				FINDER_PATH_FETCH_BY_USERID_EMAILADDRESS.getColumnBitmask()) != 0) {
			args = new Object[] {
					profileModelImpl.getOriginalUserId(),
					profileModelImpl.getOriginalEmailAddress()
				};

			FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID_EMAILADDRESS,
				args);
			FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_USERID_EMAILADDRESS,
				args);
		}

		args = new Object[] { profile.getUserId(), profile.getProfileId() };

		FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID_PROFILEID, args);
		FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_USERID_PROFILEID, args);

		if ((profileModelImpl.getColumnBitmask() &
				FINDER_PATH_FETCH_BY_USERID_PROFILEID.getColumnBitmask()) != 0) {
			args = new Object[] {
					profileModelImpl.getOriginalUserId(),
					profileModelImpl.getOriginalProfileId()
				};

			FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID_PROFILEID,
				args);
			FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_USERID_PROFILEID,
				args);
		}

		args = new Object[] { profile.getUserId(), profile.getCreatedFor() };

		FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID_CREATEDFOR,
			args);
		FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_USERID_CREATEDFOR,
			args);

		if ((profileModelImpl.getColumnBitmask() &
				FINDER_PATH_FETCH_BY_USERID_CREATEDFOR.getColumnBitmask()) != 0) {
			args = new Object[] {
					profileModelImpl.getOriginalUserId(),
					profileModelImpl.getOriginalCreatedFor()
				};

			FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID_CREATEDFOR,
				args);
			FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_USERID_CREATEDFOR,
				args);
		}
	}

	/**
	 * Creates a new profile with the primary key. Does not add the profile to the database.
	 *
	 * @param profileId the primary key for the new profile
	 * @return the new profile
	 */
	@Override
	public Profile create(long profileId) {
		Profile profile = new ProfileImpl();

		profile.setNew(true);
		profile.setPrimaryKey(profileId);

		return profile;
	}

	/**
	 * Removes the profile with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param profileId the primary key of the profile
	 * @return the profile that was removed
	 * @throws com.inikah.slayer.NoSuchProfileException if a profile with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Profile remove(long profileId)
		throws NoSuchProfileException, SystemException {
		return remove((Serializable)profileId);
	}

	/**
	 * Removes the profile with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the profile
	 * @return the profile that was removed
	 * @throws com.inikah.slayer.NoSuchProfileException if a profile with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Profile remove(Serializable primaryKey)
		throws NoSuchProfileException, SystemException {
		Session session = null;

		try {
			session = openSession();

			Profile profile = (Profile)session.get(ProfileImpl.class, primaryKey);

			if (profile == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchProfileException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(profile);
		}
		catch (NoSuchProfileException nsee) {
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
	protected Profile removeImpl(Profile profile) throws SystemException {
		profile = toUnwrappedModel(profile);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(profile)) {
				profile = (Profile)session.get(ProfileImpl.class,
						profile.getPrimaryKeyObj());
			}

			if (profile != null) {
				session.delete(profile);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (profile != null) {
			clearCache(profile);
		}

		return profile;
	}

	@Override
	public Profile updateImpl(com.inikah.slayer.model.Profile profile)
		throws SystemException {
		profile = toUnwrappedModel(profile);

		boolean isNew = profile.isNew();

		ProfileModelImpl profileModelImpl = (ProfileModelImpl)profile;

		Session session = null;

		try {
			session = openSession();

			if (profile.isNew()) {
				session.save(profile);

				profile.setNew(false);
			}
			else {
				session.merge(profile);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !ProfileModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		else {
			if ((profileModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						profileModelImpl.getOriginalUserId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID,
					args);

				args = new Object[] { profileModelImpl.getUserId() };

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID,
					args);
			}
		}

		EntityCacheUtil.putResult(ProfileModelImpl.ENTITY_CACHE_ENABLED,
			ProfileImpl.class, profile.getPrimaryKey(), profile);

		clearUniqueFindersCache(profile);
		cacheUniqueFindersCache(profile);

		return profile;
	}

	protected Profile toUnwrappedModel(Profile profile) {
		if (profile instanceof ProfileImpl) {
			return profile;
		}

		ProfileImpl profileImpl = new ProfileImpl();

		profileImpl.setNew(profile.isNew());
		profileImpl.setPrimaryKey(profile.getPrimaryKey());

		profileImpl.setProfileId(profile.getProfileId());
		profileImpl.setCompanyId(profile.getCompanyId());
		profileImpl.setUserId(profile.getUserId());
		profileImpl.setUserName(profile.getUserName());
		profileImpl.setCreateDate(profile.getCreateDate());
		profileImpl.setModifiedDate(profile.getModifiedDate());
		profileImpl.setGroupId(profile.getGroupId());
		profileImpl.setOwnerLastLogin(profile.getOwnerLastLogin());
		profileImpl.setCreatedFor(profile.getCreatedFor());
		profileImpl.setProfileName(profile.getProfileName());
		profileImpl.setProfileCode(profile.getProfileCode());
		profileImpl.setBride(profile.isBride());
		profileImpl.setMaritalStatus(profile.getMaritalStatus());
		profileImpl.setBornOn(profile.getBornOn());
		profileImpl.setReMarriageReason(profile.getReMarriageReason());
		profileImpl.setSons(profile.getSons());
		profileImpl.setDaughters(profile.getDaughters());
		profileImpl.setHeight(profile.getHeight());
		profileImpl.setWeight(profile.getWeight());
		profileImpl.setComplexion(profile.getComplexion());
		profileImpl.setMotherTongue(profile.getMotherTongue());
		profileImpl.setEmailAddress(profile.getEmailAddress());
		profileImpl.setResidingCountry(profile.getResidingCountry());
		profileImpl.setResidingState(profile.getResidingState());
		profileImpl.setResidingCity(profile.getResidingCity());
		profileImpl.setResidingArea(profile.getResidingArea());
		profileImpl.setNearbyMasjid(profile.getNearbyMasjid());
		profileImpl.setEducation(profile.getEducation());
		profileImpl.setEducationOther(profile.getEducationOther());
		profileImpl.setEducationDetail(profile.getEducationDetail());
		profileImpl.setEducationSchool(profile.getEducationSchool());
		profileImpl.setReligiousEducation(profile.getReligiousEducation());
		profileImpl.setReligiousEducationOther(profile.getReligiousEducationOther());
		profileImpl.setReligiousEducationDetail(profile.getReligiousEducationDetail());
		profileImpl.setReligiousEducationSchool(profile.getReligiousEducationSchool());
		profileImpl.setProfession(profile.getProfession());
		profileImpl.setProfessionOther(profile.getProfessionOther());
		profileImpl.setProfessionDetail(profile.getProfessionDetail());
		profileImpl.setOrganization(profile.getOrganization());
		profileImpl.setIncome(profile.getIncome());
		profileImpl.setIncomeFrequency(profile.getIncomeFrequency());
		profileImpl.setPayZakath(profile.isPayZakath());
		profileImpl.setCountryOfBirth(profile.getCountryOfBirth());
		profileImpl.setStateOfBirth(profile.getStateOfBirth());
		profileImpl.setCityOfBirth(profile.getCityOfBirth());
		profileImpl.setCanSpeak(profile.getCanSpeak());
		profileImpl.setDescription(profile.getDescription());
		profileImpl.setExpectation(profile.getExpectation());
		profileImpl.setHobbies(profile.getHobbies());
		profileImpl.setPhysicallyChallenged(profile.isPhysicallyChallenged());
		profileImpl.setPhysicallyChallengedDetails(profile.getPhysicallyChallengedDetails());
		profileImpl.setPerformedHaj(profile.isPerformedHaj());
		profileImpl.setRevertedToIslam(profile.isRevertedToIslam());
		profileImpl.setMuslimSince(profile.getMuslimSince());
		profileImpl.setHasNoFather(profile.isHasNoFather());
		profileImpl.setHasNoMother(profile.isHasNoMother());
		profileImpl.setStatus(profile.getStatus());
		profileImpl.setValidUpto(profile.getValidUpto());
		profileImpl.setAllowNonSingleProposals(profile.isAllowNonSingleProposals());
		profileImpl.setPortraitId(profile.getPortraitId());

		return profileImpl;
	}

	/**
	 * Returns the profile with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the profile
	 * @return the profile
	 * @throws com.inikah.slayer.NoSuchProfileException if a profile with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Profile findByPrimaryKey(Serializable primaryKey)
		throws NoSuchProfileException, SystemException {
		Profile profile = fetchByPrimaryKey(primaryKey);

		if (profile == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchProfileException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return profile;
	}

	/**
	 * Returns the profile with the primary key or throws a {@link com.inikah.slayer.NoSuchProfileException} if it could not be found.
	 *
	 * @param profileId the primary key of the profile
	 * @return the profile
	 * @throws com.inikah.slayer.NoSuchProfileException if a profile with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Profile findByPrimaryKey(long profileId)
		throws NoSuchProfileException, SystemException {
		return findByPrimaryKey((Serializable)profileId);
	}

	/**
	 * Returns the profile with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the profile
	 * @return the profile, or <code>null</code> if a profile with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Profile fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		Profile profile = (Profile)EntityCacheUtil.getResult(ProfileModelImpl.ENTITY_CACHE_ENABLED,
				ProfileImpl.class, primaryKey);

		if (profile == _nullProfile) {
			return null;
		}

		if (profile == null) {
			Session session = null;

			try {
				session = openSession();

				profile = (Profile)session.get(ProfileImpl.class, primaryKey);

				if (profile != null) {
					cacheResult(profile);
				}
				else {
					EntityCacheUtil.putResult(ProfileModelImpl.ENTITY_CACHE_ENABLED,
						ProfileImpl.class, primaryKey, _nullProfile);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(ProfileModelImpl.ENTITY_CACHE_ENABLED,
					ProfileImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return profile;
	}

	/**
	 * Returns the profile with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param profileId the primary key of the profile
	 * @return the profile, or <code>null</code> if a profile with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Profile fetchByPrimaryKey(long profileId) throws SystemException {
		return fetchByPrimaryKey((Serializable)profileId);
	}

	/**
	 * Returns all the profiles.
	 *
	 * @return the profiles
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Profile> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the profiles.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.ProfileModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of profiles
	 * @param end the upper bound of the range of profiles (not inclusive)
	 * @return the range of profiles
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Profile> findAll(int start, int end) throws SystemException {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the profiles.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.ProfileModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of profiles
	 * @param end the upper bound of the range of profiles (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of profiles
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Profile> findAll(int start, int end,
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

		List<Profile> list = (List<Profile>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_PROFILE);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_PROFILE;

				if (pagination) {
					sql = sql.concat(ProfileModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<Profile>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Profile>(list);
				}
				else {
					list = (List<Profile>)QueryUtil.list(q, getDialect(),
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
	 * Removes all the profiles from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeAll() throws SystemException {
		for (Profile profile : findAll()) {
			remove(profile);
		}
	}

	/**
	 * Returns the number of profiles.
	 *
	 * @return the number of profiles
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

				Query q = session.createQuery(_SQL_COUNT_PROFILE);

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
	 * Initializes the profile persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.util.service.ServiceProps.get(
						"value.object.listener.com.inikah.slayer.model.Profile")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<Profile>> listenersList = new ArrayList<ModelListener<Profile>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<Profile>)InstanceFactory.newInstance(
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
		EntityCacheUtil.removeCache(ProfileImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_PROFILE = "SELECT profile FROM Profile profile";
	private static final String _SQL_SELECT_PROFILE_WHERE = "SELECT profile FROM Profile profile WHERE ";
	private static final String _SQL_COUNT_PROFILE = "SELECT COUNT(profile) FROM Profile profile";
	private static final String _SQL_COUNT_PROFILE_WHERE = "SELECT COUNT(profile) FROM Profile profile WHERE ";
	private static final String _ORDER_BY_ENTITY_ALIAS = "profile.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No Profile exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No Profile exists with the key {";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
				PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
	private static Log _log = LogFactoryUtil.getLog(ProfilePersistenceImpl.class);
	private static Profile _nullProfile = new ProfileImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<Profile> toCacheModel() {
				return _nullProfileCacheModel;
			}
		};

	private static CacheModel<Profile> _nullProfileCacheModel = new CacheModel<Profile>() {
			@Override
			public Profile toEntityModel() {
				return _nullProfile;
			}
		};
}