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

import com.inikah.slayer.NoSuchMyKeyValueException;
import com.inikah.slayer.model.MyKeyValue;
import com.inikah.slayer.model.impl.MyKeyValueImpl;
import com.inikah.slayer.model.impl.MyKeyValueModelImpl;

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
 * The persistence implementation for the my key value service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Ahmed Hasan
 * @see MyKeyValuePersistence
 * @see MyKeyValueUtil
 * @generated
 */
public class MyKeyValuePersistenceImpl extends BasePersistenceImpl<MyKeyValue>
	implements MyKeyValuePersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link MyKeyValueUtil} to access the my key value persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = MyKeyValueImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(MyKeyValueModelImpl.ENTITY_CACHE_ENABLED,
			MyKeyValueModelImpl.FINDER_CACHE_ENABLED, MyKeyValueImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(MyKeyValueModelImpl.ENTITY_CACHE_ENABLED,
			MyKeyValueModelImpl.FINDER_CACHE_ENABLED, MyKeyValueImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(MyKeyValueModelImpl.ENTITY_CACHE_ENABLED,
			MyKeyValueModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);

	public MyKeyValuePersistenceImpl() {
		setModelClass(MyKeyValue.class);
	}

	/**
	 * Caches the my key value in the entity cache if it is enabled.
	 *
	 * @param myKeyValue the my key value
	 */
	@Override
	public void cacheResult(MyKeyValue myKeyValue) {
		EntityCacheUtil.putResult(MyKeyValueModelImpl.ENTITY_CACHE_ENABLED,
			MyKeyValueImpl.class, myKeyValue.getPrimaryKey(), myKeyValue);

		myKeyValue.resetOriginalValues();
	}

	/**
	 * Caches the my key values in the entity cache if it is enabled.
	 *
	 * @param myKeyValues the my key values
	 */
	@Override
	public void cacheResult(List<MyKeyValue> myKeyValues) {
		for (MyKeyValue myKeyValue : myKeyValues) {
			if (EntityCacheUtil.getResult(
						MyKeyValueModelImpl.ENTITY_CACHE_ENABLED,
						MyKeyValueImpl.class, myKeyValue.getPrimaryKey()) == null) {
				cacheResult(myKeyValue);
			}
			else {
				myKeyValue.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all my key values.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(MyKeyValueImpl.class.getName());
		}

		EntityCacheUtil.clearCache(MyKeyValueImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the my key value.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(MyKeyValue myKeyValue) {
		EntityCacheUtil.removeResult(MyKeyValueModelImpl.ENTITY_CACHE_ENABLED,
			MyKeyValueImpl.class, myKeyValue.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<MyKeyValue> myKeyValues) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (MyKeyValue myKeyValue : myKeyValues) {
			EntityCacheUtil.removeResult(MyKeyValueModelImpl.ENTITY_CACHE_ENABLED,
				MyKeyValueImpl.class, myKeyValue.getPrimaryKey());
		}
	}

	/**
	 * Creates a new my key value with the primary key. Does not add the my key value to the database.
	 *
	 * @param myKey the primary key for the new my key value
	 * @return the new my key value
	 */
	@Override
	public MyKeyValue create(long myKey) {
		MyKeyValue myKeyValue = new MyKeyValueImpl();

		myKeyValue.setNew(true);
		myKeyValue.setPrimaryKey(myKey);

		return myKeyValue;
	}

	/**
	 * Removes the my key value with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param myKey the primary key of the my key value
	 * @return the my key value that was removed
	 * @throws com.inikah.slayer.NoSuchMyKeyValueException if a my key value with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public MyKeyValue remove(long myKey)
		throws NoSuchMyKeyValueException, SystemException {
		return remove((Serializable)myKey);
	}

	/**
	 * Removes the my key value with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the my key value
	 * @return the my key value that was removed
	 * @throws com.inikah.slayer.NoSuchMyKeyValueException if a my key value with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public MyKeyValue remove(Serializable primaryKey)
		throws NoSuchMyKeyValueException, SystemException {
		Session session = null;

		try {
			session = openSession();

			MyKeyValue myKeyValue = (MyKeyValue)session.get(MyKeyValueImpl.class,
					primaryKey);

			if (myKeyValue == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchMyKeyValueException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(myKeyValue);
		}
		catch (NoSuchMyKeyValueException nsee) {
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
	protected MyKeyValue removeImpl(MyKeyValue myKeyValue)
		throws SystemException {
		myKeyValue = toUnwrappedModel(myKeyValue);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(myKeyValue)) {
				myKeyValue = (MyKeyValue)session.get(MyKeyValueImpl.class,
						myKeyValue.getPrimaryKeyObj());
			}

			if (myKeyValue != null) {
				session.delete(myKeyValue);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (myKeyValue != null) {
			clearCache(myKeyValue);
		}

		return myKeyValue;
	}

	@Override
	public MyKeyValue updateImpl(com.inikah.slayer.model.MyKeyValue myKeyValue)
		throws SystemException {
		myKeyValue = toUnwrappedModel(myKeyValue);

		boolean isNew = myKeyValue.isNew();

		Session session = null;

		try {
			session = openSession();

			if (myKeyValue.isNew()) {
				session.save(myKeyValue);

				myKeyValue.setNew(false);
			}
			else {
				session.merge(myKeyValue);
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

		EntityCacheUtil.putResult(MyKeyValueModelImpl.ENTITY_CACHE_ENABLED,
			MyKeyValueImpl.class, myKeyValue.getPrimaryKey(), myKeyValue);

		return myKeyValue;
	}

	protected MyKeyValue toUnwrappedModel(MyKeyValue myKeyValue) {
		if (myKeyValue instanceof MyKeyValueImpl) {
			return myKeyValue;
		}

		MyKeyValueImpl myKeyValueImpl = new MyKeyValueImpl();

		myKeyValueImpl.setNew(myKeyValue.isNew());
		myKeyValueImpl.setPrimaryKey(myKeyValue.getPrimaryKey());

		myKeyValueImpl.setMyKey(myKeyValue.getMyKey());
		myKeyValueImpl.setMyValue(myKeyValue.getMyValue());
		myKeyValueImpl.setMyName(myKeyValue.getMyName());

		return myKeyValueImpl;
	}

	/**
	 * Returns the my key value with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the my key value
	 * @return the my key value
	 * @throws com.inikah.slayer.NoSuchMyKeyValueException if a my key value with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public MyKeyValue findByPrimaryKey(Serializable primaryKey)
		throws NoSuchMyKeyValueException, SystemException {
		MyKeyValue myKeyValue = fetchByPrimaryKey(primaryKey);

		if (myKeyValue == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchMyKeyValueException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return myKeyValue;
	}

	/**
	 * Returns the my key value with the primary key or throws a {@link com.inikah.slayer.NoSuchMyKeyValueException} if it could not be found.
	 *
	 * @param myKey the primary key of the my key value
	 * @return the my key value
	 * @throws com.inikah.slayer.NoSuchMyKeyValueException if a my key value with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public MyKeyValue findByPrimaryKey(long myKey)
		throws NoSuchMyKeyValueException, SystemException {
		return findByPrimaryKey((Serializable)myKey);
	}

	/**
	 * Returns the my key value with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the my key value
	 * @return the my key value, or <code>null</code> if a my key value with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public MyKeyValue fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		MyKeyValue myKeyValue = (MyKeyValue)EntityCacheUtil.getResult(MyKeyValueModelImpl.ENTITY_CACHE_ENABLED,
				MyKeyValueImpl.class, primaryKey);

		if (myKeyValue == _nullMyKeyValue) {
			return null;
		}

		if (myKeyValue == null) {
			Session session = null;

			try {
				session = openSession();

				myKeyValue = (MyKeyValue)session.get(MyKeyValueImpl.class,
						primaryKey);

				if (myKeyValue != null) {
					cacheResult(myKeyValue);
				}
				else {
					EntityCacheUtil.putResult(MyKeyValueModelImpl.ENTITY_CACHE_ENABLED,
						MyKeyValueImpl.class, primaryKey, _nullMyKeyValue);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(MyKeyValueModelImpl.ENTITY_CACHE_ENABLED,
					MyKeyValueImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return myKeyValue;
	}

	/**
	 * Returns the my key value with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param myKey the primary key of the my key value
	 * @return the my key value, or <code>null</code> if a my key value with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public MyKeyValue fetchByPrimaryKey(long myKey) throws SystemException {
		return fetchByPrimaryKey((Serializable)myKey);
	}

	/**
	 * Returns all the my key values.
	 *
	 * @return the my key values
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<MyKeyValue> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the my key values.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.MyKeyValueModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of my key values
	 * @param end the upper bound of the range of my key values (not inclusive)
	 * @return the range of my key values
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<MyKeyValue> findAll(int start, int end)
		throws SystemException {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the my key values.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.MyKeyValueModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of my key values
	 * @param end the upper bound of the range of my key values (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of my key values
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<MyKeyValue> findAll(int start, int end,
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

		List<MyKeyValue> list = (List<MyKeyValue>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_MYKEYVALUE);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_MYKEYVALUE;

				if (pagination) {
					sql = sql.concat(MyKeyValueModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<MyKeyValue>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<MyKeyValue>(list);
				}
				else {
					list = (List<MyKeyValue>)QueryUtil.list(q, getDialect(),
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
	 * Removes all the my key values from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeAll() throws SystemException {
		for (MyKeyValue myKeyValue : findAll()) {
			remove(myKeyValue);
		}
	}

	/**
	 * Returns the number of my key values.
	 *
	 * @return the number of my key values
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

				Query q = session.createQuery(_SQL_COUNT_MYKEYVALUE);

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
	 * Initializes the my key value persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.util.service.ServiceProps.get(
						"value.object.listener.com.inikah.slayer.model.MyKeyValue")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<MyKeyValue>> listenersList = new ArrayList<ModelListener<MyKeyValue>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<MyKeyValue>)InstanceFactory.newInstance(
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
		EntityCacheUtil.removeCache(MyKeyValueImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_MYKEYVALUE = "SELECT myKeyValue FROM MyKeyValue myKeyValue";
	private static final String _SQL_COUNT_MYKEYVALUE = "SELECT COUNT(myKeyValue) FROM MyKeyValue myKeyValue";
	private static final String _ORDER_BY_ENTITY_ALIAS = "myKeyValue.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No MyKeyValue exists with the primary key ";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
				PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
	private static Log _log = LogFactoryUtil.getLog(MyKeyValuePersistenceImpl.class);
	private static MyKeyValue _nullMyKeyValue = new MyKeyValueImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<MyKeyValue> toCacheModel() {
				return _nullMyKeyValueCacheModel;
			}
		};

	private static CacheModel<MyKeyValue> _nullMyKeyValueCacheModel = new CacheModel<MyKeyValue>() {
			@Override
			public MyKeyValue toEntityModel() {
				return _nullMyKeyValue;
			}
		};
}