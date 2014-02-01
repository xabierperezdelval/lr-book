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

import com.inikah.slayer.NoSuchConfigException;
import com.inikah.slayer.model.Config;
import com.inikah.slayer.model.impl.ConfigImpl;
import com.inikah.slayer.model.impl.ConfigModelImpl;

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
 * The persistence implementation for the config service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Ahmed Hasan
 * @see ConfigPersistence
 * @see ConfigUtil
 * @generated
 */
public class ConfigPersistenceImpl extends BasePersistenceImpl<Config>
	implements ConfigPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link ConfigUtil} to access the config persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = ConfigImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(ConfigModelImpl.ENTITY_CACHE_ENABLED,
			ConfigModelImpl.FINDER_CACHE_ENABLED, ConfigImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(ConfigModelImpl.ENTITY_CACHE_ENABLED,
			ConfigModelImpl.FINDER_CACHE_ENABLED, ConfigImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(ConfigModelImpl.ENTITY_CACHE_ENABLED,
			ConfigModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
	public static final FinderPath FINDER_PATH_FETCH_BY_NAME = new FinderPath(ConfigModelImpl.ENTITY_CACHE_ENABLED,
			ConfigModelImpl.FINDER_CACHE_ENABLED, ConfigImpl.class,
			FINDER_CLASS_NAME_ENTITY, "fetchByName",
			new String[] { String.class.getName() },
			ConfigModelImpl.NAME_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_NAME = new FinderPath(ConfigModelImpl.ENTITY_CACHE_ENABLED,
			ConfigModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByName",
			new String[] { String.class.getName() });

	/**
	 * Returns the config where name = &#63; or throws a {@link com.inikah.slayer.NoSuchConfigException} if it could not be found.
	 *
	 * @param name the name
	 * @return the matching config
	 * @throws com.inikah.slayer.NoSuchConfigException if a matching config could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Config findByName(String name)
		throws NoSuchConfigException, SystemException {
		Config config = fetchByName(name);

		if (config == null) {
			StringBundler msg = new StringBundler(4);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("name=");
			msg.append(name);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchConfigException(msg.toString());
		}

		return config;
	}

	/**
	 * Returns the config where name = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param name the name
	 * @return the matching config, or <code>null</code> if a matching config could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Config fetchByName(String name) throws SystemException {
		return fetchByName(name, true);
	}

	/**
	 * Returns the config where name = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param name the name
	 * @param retrieveFromCache whether to use the finder cache
	 * @return the matching config, or <code>null</code> if a matching config could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Config fetchByName(String name, boolean retrieveFromCache)
		throws SystemException {
		Object[] finderArgs = new Object[] { name };

		Object result = null;

		if (retrieveFromCache) {
			result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_NAME,
					finderArgs, this);
		}

		if (result instanceof Config) {
			Config config = (Config)result;

			if (!Validator.equals(name, config.getName())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_SELECT_CONFIG_WHERE);

			boolean bindName = false;

			if (name == null) {
				query.append(_FINDER_COLUMN_NAME_NAME_1);
			}
			else if (name.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_NAME_NAME_3);
			}
			else {
				bindName = true;

				query.append(_FINDER_COLUMN_NAME_NAME_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindName) {
					qPos.add(name);
				}

				List<Config> list = q.list();

				if (list.isEmpty()) {
					FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_NAME,
						finderArgs, list);
				}
				else {
					if ((list.size() > 1) && _log.isWarnEnabled()) {
						_log.warn(
							"ConfigPersistenceImpl.fetchByName(String, boolean) with parameters (" +
							StringUtil.merge(finderArgs) +
							") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
					}

					Config config = list.get(0);

					result = config;

					cacheResult(config);

					if ((config.getName() == null) ||
							!config.getName().equals(name)) {
						FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_NAME,
							finderArgs, config);
					}
				}
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_NAME,
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
			return (Config)result;
		}
	}

	/**
	 * Removes the config where name = &#63; from the database.
	 *
	 * @param name the name
	 * @return the config that was removed
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Config removeByName(String name)
		throws NoSuchConfigException, SystemException {
		Config config = findByName(name);

		return remove(config);
	}

	/**
	 * Returns the number of configs where name = &#63;.
	 *
	 * @param name the name
	 * @return the number of matching configs
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByName(String name) throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_NAME;

		Object[] finderArgs = new Object[] { name };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_CONFIG_WHERE);

			boolean bindName = false;

			if (name == null) {
				query.append(_FINDER_COLUMN_NAME_NAME_1);
			}
			else if (name.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_NAME_NAME_3);
			}
			else {
				bindName = true;

				query.append(_FINDER_COLUMN_NAME_NAME_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindName) {
					qPos.add(name);
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

	private static final String _FINDER_COLUMN_NAME_NAME_1 = "config.name IS NULL";
	private static final String _FINDER_COLUMN_NAME_NAME_2 = "config.name = ?";
	private static final String _FINDER_COLUMN_NAME_NAME_3 = "(config.name IS NULL OR config.name = '')";

	public ConfigPersistenceImpl() {
		setModelClass(Config.class);
	}

	/**
	 * Caches the config in the entity cache if it is enabled.
	 *
	 * @param config the config
	 */
	@Override
	public void cacheResult(Config config) {
		EntityCacheUtil.putResult(ConfigModelImpl.ENTITY_CACHE_ENABLED,
			ConfigImpl.class, config.getPrimaryKey(), config);

		FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_NAME,
			new Object[] { config.getName() }, config);

		config.resetOriginalValues();
	}

	/**
	 * Caches the configs in the entity cache if it is enabled.
	 *
	 * @param configs the configs
	 */
	@Override
	public void cacheResult(List<Config> configs) {
		for (Config config : configs) {
			if (EntityCacheUtil.getResult(
						ConfigModelImpl.ENTITY_CACHE_ENABLED, ConfigImpl.class,
						config.getPrimaryKey()) == null) {
				cacheResult(config);
			}
			else {
				config.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all configs.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(ConfigImpl.class.getName());
		}

		EntityCacheUtil.clearCache(ConfigImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the config.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(Config config) {
		EntityCacheUtil.removeResult(ConfigModelImpl.ENTITY_CACHE_ENABLED,
			ConfigImpl.class, config.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		clearUniqueFindersCache(config);
	}

	@Override
	public void clearCache(List<Config> configs) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Config config : configs) {
			EntityCacheUtil.removeResult(ConfigModelImpl.ENTITY_CACHE_ENABLED,
				ConfigImpl.class, config.getPrimaryKey());

			clearUniqueFindersCache(config);
		}
	}

	protected void cacheUniqueFindersCache(Config config) {
		if (config.isNew()) {
			Object[] args = new Object[] { config.getName() };

			FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_NAME, args,
				Long.valueOf(1));
			FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_NAME, args, config);
		}
		else {
			ConfigModelImpl configModelImpl = (ConfigModelImpl)config;

			if ((configModelImpl.getColumnBitmask() &
					FINDER_PATH_FETCH_BY_NAME.getColumnBitmask()) != 0) {
				Object[] args = new Object[] { config.getName() };

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_NAME, args,
					Long.valueOf(1));
				FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_NAME, args,
					config);
			}
		}
	}

	protected void clearUniqueFindersCache(Config config) {
		ConfigModelImpl configModelImpl = (ConfigModelImpl)config;

		Object[] args = new Object[] { config.getName() };

		FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_NAME, args);
		FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_NAME, args);

		if ((configModelImpl.getColumnBitmask() &
				FINDER_PATH_FETCH_BY_NAME.getColumnBitmask()) != 0) {
			args = new Object[] { configModelImpl.getOriginalName() };

			FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_NAME, args);
			FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_NAME, args);
		}
	}

	/**
	 * Creates a new config with the primary key. Does not add the config to the database.
	 *
	 * @param configId the primary key for the new config
	 * @return the new config
	 */
	@Override
	public Config create(long configId) {
		Config config = new ConfigImpl();

		config.setNew(true);
		config.setPrimaryKey(configId);

		return config;
	}

	/**
	 * Removes the config with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param configId the primary key of the config
	 * @return the config that was removed
	 * @throws com.inikah.slayer.NoSuchConfigException if a config with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Config remove(long configId)
		throws NoSuchConfigException, SystemException {
		return remove((Serializable)configId);
	}

	/**
	 * Removes the config with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the config
	 * @return the config that was removed
	 * @throws com.inikah.slayer.NoSuchConfigException if a config with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Config remove(Serializable primaryKey)
		throws NoSuchConfigException, SystemException {
		Session session = null;

		try {
			session = openSession();

			Config config = (Config)session.get(ConfigImpl.class, primaryKey);

			if (config == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchConfigException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(config);
		}
		catch (NoSuchConfigException nsee) {
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
	protected Config removeImpl(Config config) throws SystemException {
		config = toUnwrappedModel(config);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(config)) {
				config = (Config)session.get(ConfigImpl.class,
						config.getPrimaryKeyObj());
			}

			if (config != null) {
				session.delete(config);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (config != null) {
			clearCache(config);
		}

		return config;
	}

	@Override
	public Config updateImpl(com.inikah.slayer.model.Config config)
		throws SystemException {
		config = toUnwrappedModel(config);

		boolean isNew = config.isNew();

		Session session = null;

		try {
			session = openSession();

			if (config.isNew()) {
				session.save(config);

				config.setNew(false);
			}
			else {
				session.merge(config);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !ConfigModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		EntityCacheUtil.putResult(ConfigModelImpl.ENTITY_CACHE_ENABLED,
			ConfigImpl.class, config.getPrimaryKey(), config);

		clearUniqueFindersCache(config);
		cacheUniqueFindersCache(config);

		return config;
	}

	protected Config toUnwrappedModel(Config config) {
		if (config instanceof ConfigImpl) {
			return config;
		}

		ConfigImpl configImpl = new ConfigImpl();

		configImpl.setNew(config.isNew());
		configImpl.setPrimaryKey(config.getPrimaryKey());

		configImpl.setConfigId(config.getConfigId());
		configImpl.setName(config.getName());
		configImpl.setValue(config.getValue());

		return configImpl;
	}

	/**
	 * Returns the config with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the config
	 * @return the config
	 * @throws com.inikah.slayer.NoSuchConfigException if a config with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Config findByPrimaryKey(Serializable primaryKey)
		throws NoSuchConfigException, SystemException {
		Config config = fetchByPrimaryKey(primaryKey);

		if (config == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchConfigException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return config;
	}

	/**
	 * Returns the config with the primary key or throws a {@link com.inikah.slayer.NoSuchConfigException} if it could not be found.
	 *
	 * @param configId the primary key of the config
	 * @return the config
	 * @throws com.inikah.slayer.NoSuchConfigException if a config with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Config findByPrimaryKey(long configId)
		throws NoSuchConfigException, SystemException {
		return findByPrimaryKey((Serializable)configId);
	}

	/**
	 * Returns the config with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the config
	 * @return the config, or <code>null</code> if a config with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Config fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		Config config = (Config)EntityCacheUtil.getResult(ConfigModelImpl.ENTITY_CACHE_ENABLED,
				ConfigImpl.class, primaryKey);

		if (config == _nullConfig) {
			return null;
		}

		if (config == null) {
			Session session = null;

			try {
				session = openSession();

				config = (Config)session.get(ConfigImpl.class, primaryKey);

				if (config != null) {
					cacheResult(config);
				}
				else {
					EntityCacheUtil.putResult(ConfigModelImpl.ENTITY_CACHE_ENABLED,
						ConfigImpl.class, primaryKey, _nullConfig);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(ConfigModelImpl.ENTITY_CACHE_ENABLED,
					ConfigImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return config;
	}

	/**
	 * Returns the config with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param configId the primary key of the config
	 * @return the config, or <code>null</code> if a config with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Config fetchByPrimaryKey(long configId) throws SystemException {
		return fetchByPrimaryKey((Serializable)configId);
	}

	/**
	 * Returns all the configs.
	 *
	 * @return the configs
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Config> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the configs.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.ConfigModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of configs
	 * @param end the upper bound of the range of configs (not inclusive)
	 * @return the range of configs
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Config> findAll(int start, int end) throws SystemException {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the configs.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.ConfigModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of configs
	 * @param end the upper bound of the range of configs (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of configs
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Config> findAll(int start, int end,
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

		List<Config> list = (List<Config>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_CONFIG);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_CONFIG;

				if (pagination) {
					sql = sql.concat(ConfigModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<Config>)QueryUtil.list(q, getDialect(), start,
							end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Config>(list);
				}
				else {
					list = (List<Config>)QueryUtil.list(q, getDialect(), start,
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
	 * Removes all the configs from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeAll() throws SystemException {
		for (Config config : findAll()) {
			remove(config);
		}
	}

	/**
	 * Returns the number of configs.
	 *
	 * @return the number of configs
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

				Query q = session.createQuery(_SQL_COUNT_CONFIG);

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
	 * Initializes the config persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.util.service.ServiceProps.get(
						"value.object.listener.com.inikah.slayer.model.Config")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<Config>> listenersList = new ArrayList<ModelListener<Config>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<Config>)InstanceFactory.newInstance(
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
		EntityCacheUtil.removeCache(ConfigImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_CONFIG = "SELECT config FROM Config config";
	private static final String _SQL_SELECT_CONFIG_WHERE = "SELECT config FROM Config config WHERE ";
	private static final String _SQL_COUNT_CONFIG = "SELECT COUNT(config) FROM Config config";
	private static final String _SQL_COUNT_CONFIG_WHERE = "SELECT COUNT(config) FROM Config config WHERE ";
	private static final String _ORDER_BY_ENTITY_ALIAS = "config.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No Config exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No Config exists with the key {";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
				PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
	private static Log _log = LogFactoryUtil.getLog(ConfigPersistenceImpl.class);
	private static Config _nullConfig = new ConfigImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<Config> toCacheModel() {
				return _nullConfigCacheModel;
			}
		};

	private static CacheModel<Config> _nullConfigCacheModel = new CacheModel<Config>() {
			@Override
			public Config toEntityModel() {
				return _nullConfig;
			}
		};
}