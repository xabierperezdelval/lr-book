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

import com.inikah.slayer.NoSuchCurrencyException;
import com.inikah.slayer.model.Currency;
import com.inikah.slayer.model.impl.CurrencyImpl;
import com.inikah.slayer.model.impl.CurrencyModelImpl;

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
 * The persistence implementation for the currency service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Ahmed Hasan
 * @see CurrencyPersistence
 * @see CurrencyUtil
 * @generated
 */
public class CurrencyPersistenceImpl extends BasePersistenceImpl<Currency>
	implements CurrencyPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link CurrencyUtil} to access the currency persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = CurrencyImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(CurrencyModelImpl.ENTITY_CACHE_ENABLED,
			CurrencyModelImpl.FINDER_CACHE_ENABLED, CurrencyImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(CurrencyModelImpl.ENTITY_CACHE_ENABLED,
			CurrencyModelImpl.FINDER_CACHE_ENABLED, CurrencyImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(CurrencyModelImpl.ENTITY_CACHE_ENABLED,
			CurrencyModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);

	public CurrencyPersistenceImpl() {
		setModelClass(Currency.class);
	}

	/**
	 * Caches the currency in the entity cache if it is enabled.
	 *
	 * @param currency the currency
	 */
	@Override
	public void cacheResult(Currency currency) {
		EntityCacheUtil.putResult(CurrencyModelImpl.ENTITY_CACHE_ENABLED,
			CurrencyImpl.class, currency.getPrimaryKey(), currency);

		currency.resetOriginalValues();
	}

	/**
	 * Caches the currencies in the entity cache if it is enabled.
	 *
	 * @param currencies the currencies
	 */
	@Override
	public void cacheResult(List<Currency> currencies) {
		for (Currency currency : currencies) {
			if (EntityCacheUtil.getResult(
						CurrencyModelImpl.ENTITY_CACHE_ENABLED,
						CurrencyImpl.class, currency.getPrimaryKey()) == null) {
				cacheResult(currency);
			}
			else {
				currency.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all currencies.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(CurrencyImpl.class.getName());
		}

		EntityCacheUtil.clearCache(CurrencyImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the currency.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(Currency currency) {
		EntityCacheUtil.removeResult(CurrencyModelImpl.ENTITY_CACHE_ENABLED,
			CurrencyImpl.class, currency.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<Currency> currencies) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Currency currency : currencies) {
			EntityCacheUtil.removeResult(CurrencyModelImpl.ENTITY_CACHE_ENABLED,
				CurrencyImpl.class, currency.getPrimaryKey());
		}
	}

	/**
	 * Creates a new currency with the primary key. Does not add the currency to the database.
	 *
	 * @param countryId the primary key for the new currency
	 * @return the new currency
	 */
	@Override
	public Currency create(long countryId) {
		Currency currency = new CurrencyImpl();

		currency.setNew(true);
		currency.setPrimaryKey(countryId);

		return currency;
	}

	/**
	 * Removes the currency with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param countryId the primary key of the currency
	 * @return the currency that was removed
	 * @throws com.inikah.slayer.NoSuchCurrencyException if a currency with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Currency remove(long countryId)
		throws NoSuchCurrencyException, SystemException {
		return remove((Serializable)countryId);
	}

	/**
	 * Removes the currency with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the currency
	 * @return the currency that was removed
	 * @throws com.inikah.slayer.NoSuchCurrencyException if a currency with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Currency remove(Serializable primaryKey)
		throws NoSuchCurrencyException, SystemException {
		Session session = null;

		try {
			session = openSession();

			Currency currency = (Currency)session.get(CurrencyImpl.class,
					primaryKey);

			if (currency == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchCurrencyException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(currency);
		}
		catch (NoSuchCurrencyException nsee) {
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
	protected Currency removeImpl(Currency currency) throws SystemException {
		currency = toUnwrappedModel(currency);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(currency)) {
				currency = (Currency)session.get(CurrencyImpl.class,
						currency.getPrimaryKeyObj());
			}

			if (currency != null) {
				session.delete(currency);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (currency != null) {
			clearCache(currency);
		}

		return currency;
	}

	@Override
	public Currency updateImpl(com.inikah.slayer.model.Currency currency)
		throws SystemException {
		currency = toUnwrappedModel(currency);

		boolean isNew = currency.isNew();

		Session session = null;

		try {
			session = openSession();

			if (currency.isNew()) {
				session.save(currency);

				currency.setNew(false);
			}
			else {
				session.merge(currency);
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

		EntityCacheUtil.putResult(CurrencyModelImpl.ENTITY_CACHE_ENABLED,
			CurrencyImpl.class, currency.getPrimaryKey(), currency);

		return currency;
	}

	protected Currency toUnwrappedModel(Currency currency) {
		if (currency instanceof CurrencyImpl) {
			return currency;
		}

		CurrencyImpl currencyImpl = new CurrencyImpl();

		currencyImpl.setNew(currency.isNew());
		currencyImpl.setPrimaryKey(currency.getPrimaryKey());

		currencyImpl.setCountryId(currency.getCountryId());
		currencyImpl.setCurrencyCode(currency.getCurrencyCode());
		currencyImpl.setCurrencySymbol(currency.getCurrencySymbol());
		currencyImpl.setCurrencyName(currency.getCurrencyName());
		currencyImpl.setMainUnit(currency.getMainUnit());
		currencyImpl.setSubUnit(currency.getSubUnit());
		currencyImpl.setToDollars(currency.getToDollars());
		currencyImpl.setPppFactor(currency.getPppFactor());

		return currencyImpl;
	}

	/**
	 * Returns the currency with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the currency
	 * @return the currency
	 * @throws com.inikah.slayer.NoSuchCurrencyException if a currency with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Currency findByPrimaryKey(Serializable primaryKey)
		throws NoSuchCurrencyException, SystemException {
		Currency currency = fetchByPrimaryKey(primaryKey);

		if (currency == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchCurrencyException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return currency;
	}

	/**
	 * Returns the currency with the primary key or throws a {@link com.inikah.slayer.NoSuchCurrencyException} if it could not be found.
	 *
	 * @param countryId the primary key of the currency
	 * @return the currency
	 * @throws com.inikah.slayer.NoSuchCurrencyException if a currency with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Currency findByPrimaryKey(long countryId)
		throws NoSuchCurrencyException, SystemException {
		return findByPrimaryKey((Serializable)countryId);
	}

	/**
	 * Returns the currency with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the currency
	 * @return the currency, or <code>null</code> if a currency with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Currency fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		Currency currency = (Currency)EntityCacheUtil.getResult(CurrencyModelImpl.ENTITY_CACHE_ENABLED,
				CurrencyImpl.class, primaryKey);

		if (currency == _nullCurrency) {
			return null;
		}

		if (currency == null) {
			Session session = null;

			try {
				session = openSession();

				currency = (Currency)session.get(CurrencyImpl.class, primaryKey);

				if (currency != null) {
					cacheResult(currency);
				}
				else {
					EntityCacheUtil.putResult(CurrencyModelImpl.ENTITY_CACHE_ENABLED,
						CurrencyImpl.class, primaryKey, _nullCurrency);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(CurrencyModelImpl.ENTITY_CACHE_ENABLED,
					CurrencyImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return currency;
	}

	/**
	 * Returns the currency with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param countryId the primary key of the currency
	 * @return the currency, or <code>null</code> if a currency with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Currency fetchByPrimaryKey(long countryId) throws SystemException {
		return fetchByPrimaryKey((Serializable)countryId);
	}

	/**
	 * Returns all the currencies.
	 *
	 * @return the currencies
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Currency> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the currencies.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.CurrencyModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of currencies
	 * @param end the upper bound of the range of currencies (not inclusive)
	 * @return the range of currencies
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Currency> findAll(int start, int end) throws SystemException {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the currencies.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.CurrencyModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of currencies
	 * @param end the upper bound of the range of currencies (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of currencies
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Currency> findAll(int start, int end,
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

		List<Currency> list = (List<Currency>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_CURRENCY);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_CURRENCY;

				if (pagination) {
					sql = sql.concat(CurrencyModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<Currency>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Currency>(list);
				}
				else {
					list = (List<Currency>)QueryUtil.list(q, getDialect(),
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
	 * Removes all the currencies from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeAll() throws SystemException {
		for (Currency currency : findAll()) {
			remove(currency);
		}
	}

	/**
	 * Returns the number of currencies.
	 *
	 * @return the number of currencies
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

				Query q = session.createQuery(_SQL_COUNT_CURRENCY);

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
	 * Initializes the currency persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.util.service.ServiceProps.get(
						"value.object.listener.com.inikah.slayer.model.Currency")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<Currency>> listenersList = new ArrayList<ModelListener<Currency>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<Currency>)InstanceFactory.newInstance(
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
		EntityCacheUtil.removeCache(CurrencyImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_CURRENCY = "SELECT currency FROM Currency currency";
	private static final String _SQL_COUNT_CURRENCY = "SELECT COUNT(currency) FROM Currency currency";
	private static final String _ORDER_BY_ENTITY_ALIAS = "currency.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No Currency exists with the primary key ";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
				PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
	private static Log _log = LogFactoryUtil.getLog(CurrencyPersistenceImpl.class);
	private static Currency _nullCurrency = new CurrencyImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<Currency> toCacheModel() {
				return _nullCurrencyCacheModel;
			}
		};

	private static CacheModel<Currency> _nullCurrencyCacheModel = new CacheModel<Currency>() {
			@Override
			public Currency toEntityModel() {
				return _nullCurrency;
			}
		};
}