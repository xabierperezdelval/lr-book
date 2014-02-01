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

import com.inikah.slayer.NoSuchMyLanguageException;
import com.inikah.slayer.model.MyLanguage;
import com.inikah.slayer.model.impl.MyLanguageImpl;
import com.inikah.slayer.model.impl.MyLanguageModelImpl;

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
 * The persistence implementation for the my language service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Ahmed Hasan
 * @see MyLanguagePersistence
 * @see MyLanguageUtil
 * @generated
 */
public class MyLanguagePersistenceImpl extends BasePersistenceImpl<MyLanguage>
	implements MyLanguagePersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link MyLanguageUtil} to access the my language persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = MyLanguageImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(MyLanguageModelImpl.ENTITY_CACHE_ENABLED,
			MyLanguageModelImpl.FINDER_CACHE_ENABLED, MyLanguageImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(MyLanguageModelImpl.ENTITY_CACHE_ENABLED,
			MyLanguageModelImpl.FINDER_CACHE_ENABLED, MyLanguageImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(MyLanguageModelImpl.ENTITY_CACHE_ENABLED,
			MyLanguageModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_COUNTRYID =
		new FinderPath(MyLanguageModelImpl.ENTITY_CACHE_ENABLED,
			MyLanguageModelImpl.FINDER_CACHE_ENABLED, MyLanguageImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByCountryId",
			new String[] {
				Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_COUNTRYID =
		new FinderPath(MyLanguageModelImpl.ENTITY_CACHE_ENABLED,
			MyLanguageModelImpl.FINDER_CACHE_ENABLED, MyLanguageImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByCountryId",
			new String[] { Long.class.getName() },
			MyLanguageModelImpl.COUNTRYID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_COUNTRYID = new FinderPath(MyLanguageModelImpl.ENTITY_CACHE_ENABLED,
			MyLanguageModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByCountryId",
			new String[] { Long.class.getName() });

	/**
	 * Returns all the my languages where countryId = &#63;.
	 *
	 * @param countryId the country ID
	 * @return the matching my languages
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<MyLanguage> findByCountryId(long countryId)
		throws SystemException {
		return findByCountryId(countryId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the my languages where countryId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.MyLanguageModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param countryId the country ID
	 * @param start the lower bound of the range of my languages
	 * @param end the upper bound of the range of my languages (not inclusive)
	 * @return the range of matching my languages
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<MyLanguage> findByCountryId(long countryId, int start, int end)
		throws SystemException {
		return findByCountryId(countryId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the my languages where countryId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.MyLanguageModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param countryId the country ID
	 * @param start the lower bound of the range of my languages
	 * @param end the upper bound of the range of my languages (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching my languages
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<MyLanguage> findByCountryId(long countryId, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_COUNTRYID;
			finderArgs = new Object[] { countryId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_COUNTRYID;
			finderArgs = new Object[] { countryId, start, end, orderByComparator };
		}

		List<MyLanguage> list = (List<MyLanguage>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (MyLanguage myLanguage : list) {
				if ((countryId != myLanguage.getCountryId())) {
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

			query.append(_SQL_SELECT_MYLANGUAGE_WHERE);

			query.append(_FINDER_COLUMN_COUNTRYID_COUNTRYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(MyLanguageModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(countryId);

				if (!pagination) {
					list = (List<MyLanguage>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<MyLanguage>(list);
				}
				else {
					list = (List<MyLanguage>)QueryUtil.list(q, getDialect(),
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
	 * Returns the first my language in the ordered set where countryId = &#63;.
	 *
	 * @param countryId the country ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching my language
	 * @throws com.inikah.slayer.NoSuchMyLanguageException if a matching my language could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public MyLanguage findByCountryId_First(long countryId,
		OrderByComparator orderByComparator)
		throws NoSuchMyLanguageException, SystemException {
		MyLanguage myLanguage = fetchByCountryId_First(countryId,
				orderByComparator);

		if (myLanguage != null) {
			return myLanguage;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("countryId=");
		msg.append(countryId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchMyLanguageException(msg.toString());
	}

	/**
	 * Returns the first my language in the ordered set where countryId = &#63;.
	 *
	 * @param countryId the country ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching my language, or <code>null</code> if a matching my language could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public MyLanguage fetchByCountryId_First(long countryId,
		OrderByComparator orderByComparator) throws SystemException {
		List<MyLanguage> list = findByCountryId(countryId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last my language in the ordered set where countryId = &#63;.
	 *
	 * @param countryId the country ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching my language
	 * @throws com.inikah.slayer.NoSuchMyLanguageException if a matching my language could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public MyLanguage findByCountryId_Last(long countryId,
		OrderByComparator orderByComparator)
		throws NoSuchMyLanguageException, SystemException {
		MyLanguage myLanguage = fetchByCountryId_Last(countryId,
				orderByComparator);

		if (myLanguage != null) {
			return myLanguage;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("countryId=");
		msg.append(countryId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchMyLanguageException(msg.toString());
	}

	/**
	 * Returns the last my language in the ordered set where countryId = &#63;.
	 *
	 * @param countryId the country ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching my language, or <code>null</code> if a matching my language could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public MyLanguage fetchByCountryId_Last(long countryId,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByCountryId(countryId);

		if (count == 0) {
			return null;
		}

		List<MyLanguage> list = findByCountryId(countryId, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the my languages before and after the current my language in the ordered set where countryId = &#63;.
	 *
	 * @param languageId the primary key of the current my language
	 * @param countryId the country ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next my language
	 * @throws com.inikah.slayer.NoSuchMyLanguageException if a my language with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public MyLanguage[] findByCountryId_PrevAndNext(long languageId,
		long countryId, OrderByComparator orderByComparator)
		throws NoSuchMyLanguageException, SystemException {
		MyLanguage myLanguage = findByPrimaryKey(languageId);

		Session session = null;

		try {
			session = openSession();

			MyLanguage[] array = new MyLanguageImpl[3];

			array[0] = getByCountryId_PrevAndNext(session, myLanguage,
					countryId, orderByComparator, true);

			array[1] = myLanguage;

			array[2] = getByCountryId_PrevAndNext(session, myLanguage,
					countryId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected MyLanguage getByCountryId_PrevAndNext(Session session,
		MyLanguage myLanguage, long countryId,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_MYLANGUAGE_WHERE);

		query.append(_FINDER_COLUMN_COUNTRYID_COUNTRYID_2);

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
			query.append(MyLanguageModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(countryId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(myLanguage);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<MyLanguage> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the my languages where countryId = &#63; from the database.
	 *
	 * @param countryId the country ID
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeByCountryId(long countryId) throws SystemException {
		for (MyLanguage myLanguage : findByCountryId(countryId,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(myLanguage);
		}
	}

	/**
	 * Returns the number of my languages where countryId = &#63;.
	 *
	 * @param countryId the country ID
	 * @return the number of matching my languages
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByCountryId(long countryId) throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_COUNTRYID;

		Object[] finderArgs = new Object[] { countryId };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_MYLANGUAGE_WHERE);

			query.append(_FINDER_COLUMN_COUNTRYID_COUNTRYID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(countryId);

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

	private static final String _FINDER_COLUMN_COUNTRYID_COUNTRYID_2 = "myLanguage.countryId = ?";

	public MyLanguagePersistenceImpl() {
		setModelClass(MyLanguage.class);
	}

	/**
	 * Caches the my language in the entity cache if it is enabled.
	 *
	 * @param myLanguage the my language
	 */
	@Override
	public void cacheResult(MyLanguage myLanguage) {
		EntityCacheUtil.putResult(MyLanguageModelImpl.ENTITY_CACHE_ENABLED,
			MyLanguageImpl.class, myLanguage.getPrimaryKey(), myLanguage);

		myLanguage.resetOriginalValues();
	}

	/**
	 * Caches the my languages in the entity cache if it is enabled.
	 *
	 * @param myLanguages the my languages
	 */
	@Override
	public void cacheResult(List<MyLanguage> myLanguages) {
		for (MyLanguage myLanguage : myLanguages) {
			if (EntityCacheUtil.getResult(
						MyLanguageModelImpl.ENTITY_CACHE_ENABLED,
						MyLanguageImpl.class, myLanguage.getPrimaryKey()) == null) {
				cacheResult(myLanguage);
			}
			else {
				myLanguage.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all my languages.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(MyLanguageImpl.class.getName());
		}

		EntityCacheUtil.clearCache(MyLanguageImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the my language.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(MyLanguage myLanguage) {
		EntityCacheUtil.removeResult(MyLanguageModelImpl.ENTITY_CACHE_ENABLED,
			MyLanguageImpl.class, myLanguage.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<MyLanguage> myLanguages) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (MyLanguage myLanguage : myLanguages) {
			EntityCacheUtil.removeResult(MyLanguageModelImpl.ENTITY_CACHE_ENABLED,
				MyLanguageImpl.class, myLanguage.getPrimaryKey());
		}
	}

	/**
	 * Creates a new my language with the primary key. Does not add the my language to the database.
	 *
	 * @param languageId the primary key for the new my language
	 * @return the new my language
	 */
	@Override
	public MyLanguage create(long languageId) {
		MyLanguage myLanguage = new MyLanguageImpl();

		myLanguage.setNew(true);
		myLanguage.setPrimaryKey(languageId);

		return myLanguage;
	}

	/**
	 * Removes the my language with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param languageId the primary key of the my language
	 * @return the my language that was removed
	 * @throws com.inikah.slayer.NoSuchMyLanguageException if a my language with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public MyLanguage remove(long languageId)
		throws NoSuchMyLanguageException, SystemException {
		return remove((Serializable)languageId);
	}

	/**
	 * Removes the my language with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the my language
	 * @return the my language that was removed
	 * @throws com.inikah.slayer.NoSuchMyLanguageException if a my language with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public MyLanguage remove(Serializable primaryKey)
		throws NoSuchMyLanguageException, SystemException {
		Session session = null;

		try {
			session = openSession();

			MyLanguage myLanguage = (MyLanguage)session.get(MyLanguageImpl.class,
					primaryKey);

			if (myLanguage == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchMyLanguageException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(myLanguage);
		}
		catch (NoSuchMyLanguageException nsee) {
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
	protected MyLanguage removeImpl(MyLanguage myLanguage)
		throws SystemException {
		myLanguage = toUnwrappedModel(myLanguage);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(myLanguage)) {
				myLanguage = (MyLanguage)session.get(MyLanguageImpl.class,
						myLanguage.getPrimaryKeyObj());
			}

			if (myLanguage != null) {
				session.delete(myLanguage);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (myLanguage != null) {
			clearCache(myLanguage);
		}

		return myLanguage;
	}

	@Override
	public MyLanguage updateImpl(com.inikah.slayer.model.MyLanguage myLanguage)
		throws SystemException {
		myLanguage = toUnwrappedModel(myLanguage);

		boolean isNew = myLanguage.isNew();

		MyLanguageModelImpl myLanguageModelImpl = (MyLanguageModelImpl)myLanguage;

		Session session = null;

		try {
			session = openSession();

			if (myLanguage.isNew()) {
				session.save(myLanguage);

				myLanguage.setNew(false);
			}
			else {
				session.merge(myLanguage);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !MyLanguageModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		else {
			if ((myLanguageModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_COUNTRYID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						myLanguageModelImpl.getOriginalCountryId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_COUNTRYID,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_COUNTRYID,
					args);

				args = new Object[] { myLanguageModelImpl.getCountryId() };

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_COUNTRYID,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_COUNTRYID,
					args);
			}
		}

		EntityCacheUtil.putResult(MyLanguageModelImpl.ENTITY_CACHE_ENABLED,
			MyLanguageImpl.class, myLanguage.getPrimaryKey(), myLanguage);

		return myLanguage;
	}

	protected MyLanguage toUnwrappedModel(MyLanguage myLanguage) {
		if (myLanguage instanceof MyLanguageImpl) {
			return myLanguage;
		}

		MyLanguageImpl myLanguageImpl = new MyLanguageImpl();

		myLanguageImpl.setNew(myLanguage.isNew());
		myLanguageImpl.setPrimaryKey(myLanguage.getPrimaryKey());

		myLanguageImpl.setLanguageId(myLanguage.getLanguageId());
		myLanguageImpl.setCountryId(myLanguage.getCountryId());
		myLanguageImpl.setRegionId(myLanguage.getRegionId());
		myLanguageImpl.setLanguage(myLanguage.getLanguage());

		return myLanguageImpl;
	}

	/**
	 * Returns the my language with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the my language
	 * @return the my language
	 * @throws com.inikah.slayer.NoSuchMyLanguageException if a my language with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public MyLanguage findByPrimaryKey(Serializable primaryKey)
		throws NoSuchMyLanguageException, SystemException {
		MyLanguage myLanguage = fetchByPrimaryKey(primaryKey);

		if (myLanguage == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchMyLanguageException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return myLanguage;
	}

	/**
	 * Returns the my language with the primary key or throws a {@link com.inikah.slayer.NoSuchMyLanguageException} if it could not be found.
	 *
	 * @param languageId the primary key of the my language
	 * @return the my language
	 * @throws com.inikah.slayer.NoSuchMyLanguageException if a my language with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public MyLanguage findByPrimaryKey(long languageId)
		throws NoSuchMyLanguageException, SystemException {
		return findByPrimaryKey((Serializable)languageId);
	}

	/**
	 * Returns the my language with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the my language
	 * @return the my language, or <code>null</code> if a my language with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public MyLanguage fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		MyLanguage myLanguage = (MyLanguage)EntityCacheUtil.getResult(MyLanguageModelImpl.ENTITY_CACHE_ENABLED,
				MyLanguageImpl.class, primaryKey);

		if (myLanguage == _nullMyLanguage) {
			return null;
		}

		if (myLanguage == null) {
			Session session = null;

			try {
				session = openSession();

				myLanguage = (MyLanguage)session.get(MyLanguageImpl.class,
						primaryKey);

				if (myLanguage != null) {
					cacheResult(myLanguage);
				}
				else {
					EntityCacheUtil.putResult(MyLanguageModelImpl.ENTITY_CACHE_ENABLED,
						MyLanguageImpl.class, primaryKey, _nullMyLanguage);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(MyLanguageModelImpl.ENTITY_CACHE_ENABLED,
					MyLanguageImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return myLanguage;
	}

	/**
	 * Returns the my language with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param languageId the primary key of the my language
	 * @return the my language, or <code>null</code> if a my language with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public MyLanguage fetchByPrimaryKey(long languageId)
		throws SystemException {
		return fetchByPrimaryKey((Serializable)languageId);
	}

	/**
	 * Returns all the my languages.
	 *
	 * @return the my languages
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<MyLanguage> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the my languages.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.MyLanguageModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of my languages
	 * @param end the upper bound of the range of my languages (not inclusive)
	 * @return the range of my languages
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<MyLanguage> findAll(int start, int end)
		throws SystemException {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the my languages.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.MyLanguageModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of my languages
	 * @param end the upper bound of the range of my languages (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of my languages
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<MyLanguage> findAll(int start, int end,
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

		List<MyLanguage> list = (List<MyLanguage>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_MYLANGUAGE);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_MYLANGUAGE;

				if (pagination) {
					sql = sql.concat(MyLanguageModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<MyLanguage>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<MyLanguage>(list);
				}
				else {
					list = (List<MyLanguage>)QueryUtil.list(q, getDialect(),
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
	 * Removes all the my languages from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeAll() throws SystemException {
		for (MyLanguage myLanguage : findAll()) {
			remove(myLanguage);
		}
	}

	/**
	 * Returns the number of my languages.
	 *
	 * @return the number of my languages
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

				Query q = session.createQuery(_SQL_COUNT_MYLANGUAGE);

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
	 * Initializes the my language persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.util.service.ServiceProps.get(
						"value.object.listener.com.inikah.slayer.model.MyLanguage")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<MyLanguage>> listenersList = new ArrayList<ModelListener<MyLanguage>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<MyLanguage>)InstanceFactory.newInstance(
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
		EntityCacheUtil.removeCache(MyLanguageImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_MYLANGUAGE = "SELECT myLanguage FROM MyLanguage myLanguage";
	private static final String _SQL_SELECT_MYLANGUAGE_WHERE = "SELECT myLanguage FROM MyLanguage myLanguage WHERE ";
	private static final String _SQL_COUNT_MYLANGUAGE = "SELECT COUNT(myLanguage) FROM MyLanguage myLanguage";
	private static final String _SQL_COUNT_MYLANGUAGE_WHERE = "SELECT COUNT(myLanguage) FROM MyLanguage myLanguage WHERE ";
	private static final String _ORDER_BY_ENTITY_ALIAS = "myLanguage.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No MyLanguage exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No MyLanguage exists with the key {";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
				PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
	private static Log _log = LogFactoryUtil.getLog(MyLanguagePersistenceImpl.class);
	private static MyLanguage _nullMyLanguage = new MyLanguageImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<MyLanguage> toCacheModel() {
				return _nullMyLanguageCacheModel;
			}
		};

	private static CacheModel<MyLanguage> _nullMyLanguageCacheModel = new CacheModel<MyLanguage>() {
			@Override
			public MyLanguage toEntityModel() {
				return _nullMyLanguage;
			}
		};
}