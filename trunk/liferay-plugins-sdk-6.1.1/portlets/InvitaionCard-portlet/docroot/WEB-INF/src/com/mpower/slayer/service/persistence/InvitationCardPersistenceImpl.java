/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.mpower.slayer.service.persistence;

import com.liferay.portal.NoSuchModelException;
import com.liferay.portal.kernel.bean.BeanReference;
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
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.service.persistence.BatchSessionUtil;
import com.liferay.portal.service.persistence.ResourcePersistence;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import com.mpower.slayer.NoSuchInvitationCardException;
import com.mpower.slayer.model.InvitationCard;
import com.mpower.slayer.model.impl.InvitationCardImpl;
import com.mpower.slayer.model.impl.InvitationCardModelImpl;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The persistence implementation for the invitation card service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see InvitationCardPersistence
 * @see InvitationCardUtil
 * @generated
 */
public class InvitationCardPersistenceImpl extends BasePersistenceImpl<InvitationCard>
	implements InvitationCardPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link InvitationCardUtil} to access the invitation card persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = InvitationCardImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_FINDBYSTATUS =
		new FinderPath(InvitationCardModelImpl.ENTITY_CACHE_ENABLED,
			InvitationCardModelImpl.FINDER_CACHE_ENABLED,
			InvitationCardImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByfindByStatus",
			new String[] {
				Integer.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_FINDBYSTATUS =
		new FinderPath(InvitationCardModelImpl.ENTITY_CACHE_ENABLED,
			InvitationCardModelImpl.FINDER_CACHE_ENABLED,
			InvitationCardImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByfindByStatus",
			new String[] { Integer.class.getName() },
			InvitationCardModelImpl.STATUS_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_FINDBYSTATUS = new FinderPath(InvitationCardModelImpl.ENTITY_CACHE_ENABLED,
			InvitationCardModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByfindByStatus",
			new String[] { Integer.class.getName() });
	public static final FinderPath FINDER_PATH_FETCH_BY_USERID_INVITEEEMAIL = new FinderPath(InvitationCardModelImpl.ENTITY_CACHE_ENABLED,
			InvitationCardModelImpl.FINDER_CACHE_ENABLED,
			InvitationCardImpl.class, FINDER_CLASS_NAME_ENTITY,
			"fetchByuserId_InviteeEmail",
			new String[] { Long.class.getName(), String.class.getName() },
			InvitationCardModelImpl.USERID_COLUMN_BITMASK |
			InvitationCardModelImpl.INVITEEEMAIL_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_USERID_INVITEEEMAIL = new FinderPath(InvitationCardModelImpl.ENTITY_CACHE_ENABLED,
			InvitationCardModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByuserId_InviteeEmail",
			new String[] { Long.class.getName(), String.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_USERID = new FinderPath(InvitationCardModelImpl.ENTITY_CACHE_ENABLED,
			InvitationCardModelImpl.FINDER_CACHE_ENABLED,
			InvitationCardImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByuserId",
			new String[] {
				Long.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID =
		new FinderPath(InvitationCardModelImpl.ENTITY_CACHE_ENABLED,
			InvitationCardModelImpl.FINDER_CACHE_ENABLED,
			InvitationCardImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByuserId",
			new String[] { Long.class.getName() },
			InvitationCardModelImpl.USERID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_USERID = new FinderPath(InvitationCardModelImpl.ENTITY_CACHE_ENABLED,
			InvitationCardModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByuserId",
			new String[] { Long.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_USERID_STATUS =
		new FinderPath(InvitationCardModelImpl.ENTITY_CACHE_ENABLED,
			InvitationCardModelImpl.FINDER_CACHE_ENABLED,
			InvitationCardImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByuserId_Status",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID_STATUS =
		new FinderPath(InvitationCardModelImpl.ENTITY_CACHE_ENABLED,
			InvitationCardModelImpl.FINDER_CACHE_ENABLED,
			InvitationCardImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByuserId_Status",
			new String[] { Long.class.getName(), Integer.class.getName() },
			InvitationCardModelImpl.USERID_COLUMN_BITMASK |
			InvitationCardModelImpl.STATUS_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_USERID_STATUS = new FinderPath(InvitationCardModelImpl.ENTITY_CACHE_ENABLED,
			InvitationCardModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByuserId_Status",
			new String[] { Long.class.getName(), Integer.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(InvitationCardModelImpl.ENTITY_CACHE_ENABLED,
			InvitationCardModelImpl.FINDER_CACHE_ENABLED,
			InvitationCardImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(InvitationCardModelImpl.ENTITY_CACHE_ENABLED,
			InvitationCardModelImpl.FINDER_CACHE_ENABLED,
			InvitationCardImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(InvitationCardModelImpl.ENTITY_CACHE_ENABLED,
			InvitationCardModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);

	/**
	 * Caches the invitation card in the entity cache if it is enabled.
	 *
	 * @param invitationCard the invitation card
	 */
	public void cacheResult(InvitationCard invitationCard) {
		EntityCacheUtil.putResult(InvitationCardModelImpl.ENTITY_CACHE_ENABLED,
			InvitationCardImpl.class, invitationCard.getPrimaryKey(),
			invitationCard);

		FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERID_INVITEEEMAIL,
			new Object[] {
				Long.valueOf(invitationCard.getUserId()),
				
			invitationCard.getInviteeEmail()
			}, invitationCard);

		invitationCard.resetOriginalValues();
	}

	/**
	 * Caches the invitation cards in the entity cache if it is enabled.
	 *
	 * @param invitationCards the invitation cards
	 */
	public void cacheResult(List<InvitationCard> invitationCards) {
		for (InvitationCard invitationCard : invitationCards) {
			if (EntityCacheUtil.getResult(
						InvitationCardModelImpl.ENTITY_CACHE_ENABLED,
						InvitationCardImpl.class, invitationCard.getPrimaryKey()) == null) {
				cacheResult(invitationCard);
			}
			else {
				invitationCard.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all invitation cards.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(InvitationCardImpl.class.getName());
		}

		EntityCacheUtil.clearCache(InvitationCardImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the invitation card.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(InvitationCard invitationCard) {
		EntityCacheUtil.removeResult(InvitationCardModelImpl.ENTITY_CACHE_ENABLED,
			InvitationCardImpl.class, invitationCard.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		clearUniqueFindersCache(invitationCard);
	}

	@Override
	public void clearCache(List<InvitationCard> invitationCards) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (InvitationCard invitationCard : invitationCards) {
			EntityCacheUtil.removeResult(InvitationCardModelImpl.ENTITY_CACHE_ENABLED,
				InvitationCardImpl.class, invitationCard.getPrimaryKey());

			clearUniqueFindersCache(invitationCard);
		}
	}

	protected void clearUniqueFindersCache(InvitationCard invitationCard) {
		FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_USERID_INVITEEEMAIL,
			new Object[] {
				Long.valueOf(invitationCard.getUserId()),
				
			invitationCard.getInviteeEmail()
			});
	}

	/**
	 * Creates a new invitation card with the primary key. Does not add the invitation card to the database.
	 *
	 * @param invitationId the primary key for the new invitation card
	 * @return the new invitation card
	 */
	public InvitationCard create(long invitationId) {
		InvitationCard invitationCard = new InvitationCardImpl();

		invitationCard.setNew(true);
		invitationCard.setPrimaryKey(invitationId);

		return invitationCard;
	}

	/**
	 * Removes the invitation card with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param invitationId the primary key of the invitation card
	 * @return the invitation card that was removed
	 * @throws com.mpower.slayer.NoSuchInvitationCardException if a invitation card with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public InvitationCard remove(long invitationId)
		throws NoSuchInvitationCardException, SystemException {
		return remove(Long.valueOf(invitationId));
	}

	/**
	 * Removes the invitation card with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the invitation card
	 * @return the invitation card that was removed
	 * @throws com.mpower.slayer.NoSuchInvitationCardException if a invitation card with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public InvitationCard remove(Serializable primaryKey)
		throws NoSuchInvitationCardException, SystemException {
		Session session = null;

		try {
			session = openSession();

			InvitationCard invitationCard = (InvitationCard)session.get(InvitationCardImpl.class,
					primaryKey);

			if (invitationCard == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchInvitationCardException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(invitationCard);
		}
		catch (NoSuchInvitationCardException nsee) {
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
	protected InvitationCard removeImpl(InvitationCard invitationCard)
		throws SystemException {
		invitationCard = toUnwrappedModel(invitationCard);

		Session session = null;

		try {
			session = openSession();

			BatchSessionUtil.delete(session, invitationCard);
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		clearCache(invitationCard);

		return invitationCard;
	}

	@Override
	public InvitationCard updateImpl(
		com.mpower.slayer.model.InvitationCard invitationCard, boolean merge)
		throws SystemException {
		invitationCard = toUnwrappedModel(invitationCard);

		boolean isNew = invitationCard.isNew();

		InvitationCardModelImpl invitationCardModelImpl = (InvitationCardModelImpl)invitationCard;

		Session session = null;

		try {
			session = openSession();

			BatchSessionUtil.update(session, invitationCard, merge);

			invitationCard.setNew(false);
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !InvitationCardModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		else {
			if ((invitationCardModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_FINDBYSTATUS.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						Integer.valueOf(invitationCardModelImpl.getOriginalStatus())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_FINDBYSTATUS,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_FINDBYSTATUS,
					args);

				args = new Object[] {
						Integer.valueOf(invitationCardModelImpl.getStatus())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_FINDBYSTATUS,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_FINDBYSTATUS,
					args);
			}

			if ((invitationCardModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						Long.valueOf(invitationCardModelImpl.getOriginalUserId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID,
					args);

				args = new Object[] {
						Long.valueOf(invitationCardModelImpl.getUserId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID,
					args);
			}

			if ((invitationCardModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID_STATUS.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						Long.valueOf(invitationCardModelImpl.getOriginalUserId()),
						Integer.valueOf(invitationCardModelImpl.getOriginalStatus())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID_STATUS,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID_STATUS,
					args);

				args = new Object[] {
						Long.valueOf(invitationCardModelImpl.getUserId()),
						Integer.valueOf(invitationCardModelImpl.getStatus())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID_STATUS,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID_STATUS,
					args);
			}
		}

		EntityCacheUtil.putResult(InvitationCardModelImpl.ENTITY_CACHE_ENABLED,
			InvitationCardImpl.class, invitationCard.getPrimaryKey(),
			invitationCard);

		if (isNew) {
			FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERID_INVITEEEMAIL,
				new Object[] {
					Long.valueOf(invitationCard.getUserId()),
					
				invitationCard.getInviteeEmail()
				}, invitationCard);
		}
		else {
			if ((invitationCardModelImpl.getColumnBitmask() &
					FINDER_PATH_FETCH_BY_USERID_INVITEEEMAIL.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						Long.valueOf(invitationCardModelImpl.getOriginalUserId()),
						
						invitationCardModelImpl.getOriginalInviteeEmail()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID_INVITEEEMAIL,
					args);

				FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_USERID_INVITEEEMAIL,
					args);

				FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERID_INVITEEEMAIL,
					new Object[] {
						Long.valueOf(invitationCard.getUserId()),
						
					invitationCard.getInviteeEmail()
					}, invitationCard);
			}
		}

		return invitationCard;
	}

	protected InvitationCard toUnwrappedModel(InvitationCard invitationCard) {
		if (invitationCard instanceof InvitationCardImpl) {
			return invitationCard;
		}

		InvitationCardImpl invitationCardImpl = new InvitationCardImpl();

		invitationCardImpl.setNew(invitationCard.isNew());
		invitationCardImpl.setPrimaryKey(invitationCard.getPrimaryKey());

		invitationCardImpl.setInvitationId(invitationCard.getInvitationId());
		invitationCardImpl.setCompanyId(invitationCard.getCompanyId());
		invitationCardImpl.setGroupId(invitationCard.getGroupId());
		invitationCardImpl.setUserId(invitationCard.getUserId());
		invitationCardImpl.setUserName(invitationCard.getUserName());
		invitationCardImpl.setCreateDate(invitationCard.getCreateDate());
		invitationCardImpl.setModifiedDate(invitationCard.getModifiedDate());
		invitationCardImpl.setInviteeEmail(invitationCard.getInviteeEmail());
		invitationCardImpl.setStatus(invitationCard.getStatus());
		invitationCardImpl.setInviteeNewUserId(invitationCard.getInviteeNewUserId());

		return invitationCardImpl;
	}

	/**
	 * Returns the invitation card with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the invitation card
	 * @return the invitation card
	 * @throws com.liferay.portal.NoSuchModelException if a invitation card with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public InvitationCard findByPrimaryKey(Serializable primaryKey)
		throws NoSuchModelException, SystemException {
		return findByPrimaryKey(((Long)primaryKey).longValue());
	}

	/**
	 * Returns the invitation card with the primary key or throws a {@link com.mpower.slayer.NoSuchInvitationCardException} if it could not be found.
	 *
	 * @param invitationId the primary key of the invitation card
	 * @return the invitation card
	 * @throws com.mpower.slayer.NoSuchInvitationCardException if a invitation card with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public InvitationCard findByPrimaryKey(long invitationId)
		throws NoSuchInvitationCardException, SystemException {
		InvitationCard invitationCard = fetchByPrimaryKey(invitationId);

		if (invitationCard == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + invitationId);
			}

			throw new NoSuchInvitationCardException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				invitationId);
		}

		return invitationCard;
	}

	/**
	 * Returns the invitation card with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the invitation card
	 * @return the invitation card, or <code>null</code> if a invitation card with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public InvitationCard fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		return fetchByPrimaryKey(((Long)primaryKey).longValue());
	}

	/**
	 * Returns the invitation card with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param invitationId the primary key of the invitation card
	 * @return the invitation card, or <code>null</code> if a invitation card with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public InvitationCard fetchByPrimaryKey(long invitationId)
		throws SystemException {
		InvitationCard invitationCard = (InvitationCard)EntityCacheUtil.getResult(InvitationCardModelImpl.ENTITY_CACHE_ENABLED,
				InvitationCardImpl.class, invitationId);

		if (invitationCard == _nullInvitationCard) {
			return null;
		}

		if (invitationCard == null) {
			Session session = null;

			boolean hasException = false;

			try {
				session = openSession();

				invitationCard = (InvitationCard)session.get(InvitationCardImpl.class,
						Long.valueOf(invitationId));
			}
			catch (Exception e) {
				hasException = true;

				throw processException(e);
			}
			finally {
				if (invitationCard != null) {
					cacheResult(invitationCard);
				}
				else if (!hasException) {
					EntityCacheUtil.putResult(InvitationCardModelImpl.ENTITY_CACHE_ENABLED,
						InvitationCardImpl.class, invitationId,
						_nullInvitationCard);
				}

				closeSession(session);
			}
		}

		return invitationCard;
	}

	/**
	 * Returns all the invitation cards where status = &#63;.
	 *
	 * @param status the status
	 * @return the matching invitation cards
	 * @throws SystemException if a system exception occurred
	 */
	public List<InvitationCard> findByfindByStatus(int status)
		throws SystemException {
		return findByfindByStatus(status, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the invitation cards where status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param status the status
	 * @param start the lower bound of the range of invitation cards
	 * @param end the upper bound of the range of invitation cards (not inclusive)
	 * @return the range of matching invitation cards
	 * @throws SystemException if a system exception occurred
	 */
	public List<InvitationCard> findByfindByStatus(int status, int start,
		int end) throws SystemException {
		return findByfindByStatus(status, start, end, null);
	}

	/**
	 * Returns an ordered range of all the invitation cards where status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param status the status
	 * @param start the lower bound of the range of invitation cards
	 * @param end the upper bound of the range of invitation cards (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching invitation cards
	 * @throws SystemException if a system exception occurred
	 */
	public List<InvitationCard> findByfindByStatus(int status, int start,
		int end, OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_FINDBYSTATUS;
			finderArgs = new Object[] { status };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_FINDBYSTATUS;
			finderArgs = new Object[] { status, start, end, orderByComparator };
		}

		List<InvitationCard> list = (List<InvitationCard>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (InvitationCard invitationCard : list) {
				if ((status != invitationCard.getStatus())) {
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
				query = new StringBundler(2);
			}

			query.append(_SQL_SELECT_INVITATIONCARD_WHERE);

			query.append(_FINDER_COLUMN_FINDBYSTATUS_STATUS_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(status);

				list = (List<InvitationCard>)QueryUtil.list(q, getDialect(),
						start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first invitation card in the ordered set where status = &#63;.
	 *
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching invitation card
	 * @throws com.mpower.slayer.NoSuchInvitationCardException if a matching invitation card could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public InvitationCard findByfindByStatus_First(int status,
		OrderByComparator orderByComparator)
		throws NoSuchInvitationCardException, SystemException {
		InvitationCard invitationCard = fetchByfindByStatus_First(status,
				orderByComparator);

		if (invitationCard != null) {
			return invitationCard;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("status=");
		msg.append(status);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInvitationCardException(msg.toString());
	}

	/**
	 * Returns the first invitation card in the ordered set where status = &#63;.
	 *
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching invitation card, or <code>null</code> if a matching invitation card could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public InvitationCard fetchByfindByStatus_First(int status,
		OrderByComparator orderByComparator) throws SystemException {
		List<InvitationCard> list = findByfindByStatus(status, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last invitation card in the ordered set where status = &#63;.
	 *
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching invitation card
	 * @throws com.mpower.slayer.NoSuchInvitationCardException if a matching invitation card could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public InvitationCard findByfindByStatus_Last(int status,
		OrderByComparator orderByComparator)
		throws NoSuchInvitationCardException, SystemException {
		InvitationCard invitationCard = fetchByfindByStatus_Last(status,
				orderByComparator);

		if (invitationCard != null) {
			return invitationCard;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("status=");
		msg.append(status);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInvitationCardException(msg.toString());
	}

	/**
	 * Returns the last invitation card in the ordered set where status = &#63;.
	 *
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching invitation card, or <code>null</code> if a matching invitation card could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public InvitationCard fetchByfindByStatus_Last(int status,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByfindByStatus(status);

		List<InvitationCard> list = findByfindByStatus(status, count - 1,
				count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the invitation cards before and after the current invitation card in the ordered set where status = &#63;.
	 *
	 * @param invitationId the primary key of the current invitation card
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next invitation card
	 * @throws com.mpower.slayer.NoSuchInvitationCardException if a invitation card with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public InvitationCard[] findByfindByStatus_PrevAndNext(long invitationId,
		int status, OrderByComparator orderByComparator)
		throws NoSuchInvitationCardException, SystemException {
		InvitationCard invitationCard = findByPrimaryKey(invitationId);

		Session session = null;

		try {
			session = openSession();

			InvitationCard[] array = new InvitationCardImpl[3];

			array[0] = getByfindByStatus_PrevAndNext(session, invitationCard,
					status, orderByComparator, true);

			array[1] = invitationCard;

			array[2] = getByfindByStatus_PrevAndNext(session, invitationCard,
					status, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected InvitationCard getByfindByStatus_PrevAndNext(Session session,
		InvitationCard invitationCard, int status,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_INVITATIONCARD_WHERE);

		query.append(_FINDER_COLUMN_FINDBYSTATUS_STATUS_2);

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

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(status);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(invitationCard);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<InvitationCard> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns the invitation card where userId = &#63; and inviteeEmail = &#63; or throws a {@link com.mpower.slayer.NoSuchInvitationCardException} if it could not be found.
	 *
	 * @param userId the user ID
	 * @param inviteeEmail the invitee email
	 * @return the matching invitation card
	 * @throws com.mpower.slayer.NoSuchInvitationCardException if a matching invitation card could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public InvitationCard findByuserId_InviteeEmail(long userId,
		String inviteeEmail)
		throws NoSuchInvitationCardException, SystemException {
		InvitationCard invitationCard = fetchByuserId_InviteeEmail(userId,
				inviteeEmail);

		if (invitationCard == null) {
			StringBundler msg = new StringBundler(6);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("userId=");
			msg.append(userId);

			msg.append(", inviteeEmail=");
			msg.append(inviteeEmail);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchInvitationCardException(msg.toString());
		}

		return invitationCard;
	}

	/**
	 * Returns the invitation card where userId = &#63; and inviteeEmail = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param userId the user ID
	 * @param inviteeEmail the invitee email
	 * @return the matching invitation card, or <code>null</code> if a matching invitation card could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public InvitationCard fetchByuserId_InviteeEmail(long userId,
		String inviteeEmail) throws SystemException {
		return fetchByuserId_InviteeEmail(userId, inviteeEmail, true);
	}

	/**
	 * Returns the invitation card where userId = &#63; and inviteeEmail = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param userId the user ID
	 * @param inviteeEmail the invitee email
	 * @param retrieveFromCache whether to use the finder cache
	 * @return the matching invitation card, or <code>null</code> if a matching invitation card could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public InvitationCard fetchByuserId_InviteeEmail(long userId,
		String inviteeEmail, boolean retrieveFromCache)
		throws SystemException {
		Object[] finderArgs = new Object[] { userId, inviteeEmail };

		Object result = null;

		if (retrieveFromCache) {
			result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_USERID_INVITEEEMAIL,
					finderArgs, this);
		}

		if (result instanceof InvitationCard) {
			InvitationCard invitationCard = (InvitationCard)result;

			if ((userId != invitationCard.getUserId()) ||
					!Validator.equals(inviteeEmail,
						invitationCard.getInviteeEmail())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_SELECT_INVITATIONCARD_WHERE);

			query.append(_FINDER_COLUMN_USERID_INVITEEEMAIL_USERID_2);

			if (inviteeEmail == null) {
				query.append(_FINDER_COLUMN_USERID_INVITEEEMAIL_INVITEEEMAIL_1);
			}
			else {
				if (inviteeEmail.equals(StringPool.BLANK)) {
					query.append(_FINDER_COLUMN_USERID_INVITEEEMAIL_INVITEEEMAIL_3);
				}
				else {
					query.append(_FINDER_COLUMN_USERID_INVITEEEMAIL_INVITEEEMAIL_2);
				}
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				if (inviteeEmail != null) {
					qPos.add(inviteeEmail);
				}

				List<InvitationCard> list = q.list();

				result = list;

				InvitationCard invitationCard = null;

				if (list.isEmpty()) {
					FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERID_INVITEEEMAIL,
						finderArgs, list);
				}
				else {
					invitationCard = list.get(0);

					cacheResult(invitationCard);

					if ((invitationCard.getUserId() != userId) ||
							(invitationCard.getInviteeEmail() == null) ||
							!invitationCard.getInviteeEmail()
											   .equals(inviteeEmail)) {
						FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERID_INVITEEEMAIL,
							finderArgs, invitationCard);
					}
				}

				return invitationCard;
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (result == null) {
					FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_USERID_INVITEEEMAIL,
						finderArgs);
				}

				closeSession(session);
			}
		}
		else {
			if (result instanceof List<?>) {
				return null;
			}
			else {
				return (InvitationCard)result;
			}
		}
	}

	/**
	 * Returns all the invitation cards where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the matching invitation cards
	 * @throws SystemException if a system exception occurred
	 */
	public List<InvitationCard> findByuserId(long userId)
		throws SystemException {
		return findByuserId(userId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the invitation cards where userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param start the lower bound of the range of invitation cards
	 * @param end the upper bound of the range of invitation cards (not inclusive)
	 * @return the range of matching invitation cards
	 * @throws SystemException if a system exception occurred
	 */
	public List<InvitationCard> findByuserId(long userId, int start, int end)
		throws SystemException {
		return findByuserId(userId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the invitation cards where userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param start the lower bound of the range of invitation cards
	 * @param end the upper bound of the range of invitation cards (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching invitation cards
	 * @throws SystemException if a system exception occurred
	 */
	public List<InvitationCard> findByuserId(long userId, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID;
			finderArgs = new Object[] { userId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_USERID;
			finderArgs = new Object[] { userId, start, end, orderByComparator };
		}

		List<InvitationCard> list = (List<InvitationCard>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (InvitationCard invitationCard : list) {
				if ((userId != invitationCard.getUserId())) {
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
				query = new StringBundler(2);
			}

			query.append(_SQL_SELECT_INVITATIONCARD_WHERE);

			query.append(_FINDER_COLUMN_USERID_USERID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				list = (List<InvitationCard>)QueryUtil.list(q, getDialect(),
						start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first invitation card in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching invitation card
	 * @throws com.mpower.slayer.NoSuchInvitationCardException if a matching invitation card could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public InvitationCard findByuserId_First(long userId,
		OrderByComparator orderByComparator)
		throws NoSuchInvitationCardException, SystemException {
		InvitationCard invitationCard = fetchByuserId_First(userId,
				orderByComparator);

		if (invitationCard != null) {
			return invitationCard;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInvitationCardException(msg.toString());
	}

	/**
	 * Returns the first invitation card in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching invitation card, or <code>null</code> if a matching invitation card could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public InvitationCard fetchByuserId_First(long userId,
		OrderByComparator orderByComparator) throws SystemException {
		List<InvitationCard> list = findByuserId(userId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last invitation card in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching invitation card
	 * @throws com.mpower.slayer.NoSuchInvitationCardException if a matching invitation card could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public InvitationCard findByuserId_Last(long userId,
		OrderByComparator orderByComparator)
		throws NoSuchInvitationCardException, SystemException {
		InvitationCard invitationCard = fetchByuserId_Last(userId,
				orderByComparator);

		if (invitationCard != null) {
			return invitationCard;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInvitationCardException(msg.toString());
	}

	/**
	 * Returns the last invitation card in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching invitation card, or <code>null</code> if a matching invitation card could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public InvitationCard fetchByuserId_Last(long userId,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByuserId(userId);

		List<InvitationCard> list = findByuserId(userId, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the invitation cards before and after the current invitation card in the ordered set where userId = &#63;.
	 *
	 * @param invitationId the primary key of the current invitation card
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next invitation card
	 * @throws com.mpower.slayer.NoSuchInvitationCardException if a invitation card with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public InvitationCard[] findByuserId_PrevAndNext(long invitationId,
		long userId, OrderByComparator orderByComparator)
		throws NoSuchInvitationCardException, SystemException {
		InvitationCard invitationCard = findByPrimaryKey(invitationId);

		Session session = null;

		try {
			session = openSession();

			InvitationCard[] array = new InvitationCardImpl[3];

			array[0] = getByuserId_PrevAndNext(session, invitationCard, userId,
					orderByComparator, true);

			array[1] = invitationCard;

			array[2] = getByuserId_PrevAndNext(session, invitationCard, userId,
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

	protected InvitationCard getByuserId_PrevAndNext(Session session,
		InvitationCard invitationCard, long userId,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_INVITATIONCARD_WHERE);

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

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(userId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(invitationCard);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<InvitationCard> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the invitation cards where userId = &#63; and status = &#63;.
	 *
	 * @param userId the user ID
	 * @param status the status
	 * @return the matching invitation cards
	 * @throws SystemException if a system exception occurred
	 */
	public List<InvitationCard> findByuserId_Status(long userId, int status)
		throws SystemException {
		return findByuserId_Status(userId, status, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the invitation cards where userId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param status the status
	 * @param start the lower bound of the range of invitation cards
	 * @param end the upper bound of the range of invitation cards (not inclusive)
	 * @return the range of matching invitation cards
	 * @throws SystemException if a system exception occurred
	 */
	public List<InvitationCard> findByuserId_Status(long userId, int status,
		int start, int end) throws SystemException {
		return findByuserId_Status(userId, status, start, end, null);
	}

	/**
	 * Returns an ordered range of all the invitation cards where userId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param status the status
	 * @param start the lower bound of the range of invitation cards
	 * @param end the upper bound of the range of invitation cards (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching invitation cards
	 * @throws SystemException if a system exception occurred
	 */
	public List<InvitationCard> findByuserId_Status(long userId, int status,
		int start, int end, OrderByComparator orderByComparator)
		throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID_STATUS;
			finderArgs = new Object[] { userId, status };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_USERID_STATUS;
			finderArgs = new Object[] {
					userId, status,
					
					start, end, orderByComparator
				};
		}

		List<InvitationCard> list = (List<InvitationCard>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (InvitationCard invitationCard : list) {
				if ((userId != invitationCard.getUserId()) ||
						(status != invitationCard.getStatus())) {
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
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_INVITATIONCARD_WHERE);

			query.append(_FINDER_COLUMN_USERID_STATUS_USERID_2);

			query.append(_FINDER_COLUMN_USERID_STATUS_STATUS_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				qPos.add(status);

				list = (List<InvitationCard>)QueryUtil.list(q, getDialect(),
						start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first invitation card in the ordered set where userId = &#63; and status = &#63;.
	 *
	 * @param userId the user ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching invitation card
	 * @throws com.mpower.slayer.NoSuchInvitationCardException if a matching invitation card could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public InvitationCard findByuserId_Status_First(long userId, int status,
		OrderByComparator orderByComparator)
		throws NoSuchInvitationCardException, SystemException {
		InvitationCard invitationCard = fetchByuserId_Status_First(userId,
				status, orderByComparator);

		if (invitationCard != null) {
			return invitationCard;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("userId=");
		msg.append(userId);

		msg.append(", status=");
		msg.append(status);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInvitationCardException(msg.toString());
	}

	/**
	 * Returns the first invitation card in the ordered set where userId = &#63; and status = &#63;.
	 *
	 * @param userId the user ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching invitation card, or <code>null</code> if a matching invitation card could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public InvitationCard fetchByuserId_Status_First(long userId, int status,
		OrderByComparator orderByComparator) throws SystemException {
		List<InvitationCard> list = findByuserId_Status(userId, status, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last invitation card in the ordered set where userId = &#63; and status = &#63;.
	 *
	 * @param userId the user ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching invitation card
	 * @throws com.mpower.slayer.NoSuchInvitationCardException if a matching invitation card could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public InvitationCard findByuserId_Status_Last(long userId, int status,
		OrderByComparator orderByComparator)
		throws NoSuchInvitationCardException, SystemException {
		InvitationCard invitationCard = fetchByuserId_Status_Last(userId,
				status, orderByComparator);

		if (invitationCard != null) {
			return invitationCard;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("userId=");
		msg.append(userId);

		msg.append(", status=");
		msg.append(status);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInvitationCardException(msg.toString());
	}

	/**
	 * Returns the last invitation card in the ordered set where userId = &#63; and status = &#63;.
	 *
	 * @param userId the user ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching invitation card, or <code>null</code> if a matching invitation card could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public InvitationCard fetchByuserId_Status_Last(long userId, int status,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByuserId_Status(userId, status);

		List<InvitationCard> list = findByuserId_Status(userId, status,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the invitation cards before and after the current invitation card in the ordered set where userId = &#63; and status = &#63;.
	 *
	 * @param invitationId the primary key of the current invitation card
	 * @param userId the user ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next invitation card
	 * @throws com.mpower.slayer.NoSuchInvitationCardException if a invitation card with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public InvitationCard[] findByuserId_Status_PrevAndNext(long invitationId,
		long userId, int status, OrderByComparator orderByComparator)
		throws NoSuchInvitationCardException, SystemException {
		InvitationCard invitationCard = findByPrimaryKey(invitationId);

		Session session = null;

		try {
			session = openSession();

			InvitationCard[] array = new InvitationCardImpl[3];

			array[0] = getByuserId_Status_PrevAndNext(session, invitationCard,
					userId, status, orderByComparator, true);

			array[1] = invitationCard;

			array[2] = getByuserId_Status_PrevAndNext(session, invitationCard,
					userId, status, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected InvitationCard getByuserId_Status_PrevAndNext(Session session,
		InvitationCard invitationCard, long userId, int status,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_INVITATIONCARD_WHERE);

		query.append(_FINDER_COLUMN_USERID_STATUS_USERID_2);

		query.append(_FINDER_COLUMN_USERID_STATUS_STATUS_2);

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

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(userId);

		qPos.add(status);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(invitationCard);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<InvitationCard> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the invitation cards.
	 *
	 * @return the invitation cards
	 * @throws SystemException if a system exception occurred
	 */
	public List<InvitationCard> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the invitation cards.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param start the lower bound of the range of invitation cards
	 * @param end the upper bound of the range of invitation cards (not inclusive)
	 * @return the range of invitation cards
	 * @throws SystemException if a system exception occurred
	 */
	public List<InvitationCard> findAll(int start, int end)
		throws SystemException {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the invitation cards.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param start the lower bound of the range of invitation cards
	 * @param end the upper bound of the range of invitation cards (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of invitation cards
	 * @throws SystemException if a system exception occurred
	 */
	public List<InvitationCard> findAll(int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = new Object[] { start, end, orderByComparator };

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL;
			finderArgs = FINDER_ARGS_EMPTY;
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_ALL;
			finderArgs = new Object[] { start, end, orderByComparator };
		}

		List<InvitationCard> list = (List<InvitationCard>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_INVITATIONCARD);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_INVITATIONCARD;
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (orderByComparator == null) {
					list = (List<InvitationCard>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);
				}
				else {
					list = (List<InvitationCard>)QueryUtil.list(q,
							getDialect(), start, end);
				}
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the invitation cards where status = &#63; from the database.
	 *
	 * @param status the status
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByfindByStatus(int status) throws SystemException {
		for (InvitationCard invitationCard : findByfindByStatus(status)) {
			remove(invitationCard);
		}
	}

	/**
	 * Removes the invitation card where userId = &#63; and inviteeEmail = &#63; from the database.
	 *
	 * @param userId the user ID
	 * @param inviteeEmail the invitee email
	 * @return the invitation card that was removed
	 * @throws SystemException if a system exception occurred
	 */
	public InvitationCard removeByuserId_InviteeEmail(long userId,
		String inviteeEmail)
		throws NoSuchInvitationCardException, SystemException {
		InvitationCard invitationCard = findByuserId_InviteeEmail(userId,
				inviteeEmail);

		return remove(invitationCard);
	}

	/**
	 * Removes all the invitation cards where userId = &#63; from the database.
	 *
	 * @param userId the user ID
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByuserId(long userId) throws SystemException {
		for (InvitationCard invitationCard : findByuserId(userId)) {
			remove(invitationCard);
		}
	}

	/**
	 * Removes all the invitation cards where userId = &#63; and status = &#63; from the database.
	 *
	 * @param userId the user ID
	 * @param status the status
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByuserId_Status(long userId, int status)
		throws SystemException {
		for (InvitationCard invitationCard : findByuserId_Status(userId, status)) {
			remove(invitationCard);
		}
	}

	/**
	 * Removes all the invitation cards from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	public void removeAll() throws SystemException {
		for (InvitationCard invitationCard : findAll()) {
			remove(invitationCard);
		}
	}

	/**
	 * Returns the number of invitation cards where status = &#63;.
	 *
	 * @param status the status
	 * @return the number of matching invitation cards
	 * @throws SystemException if a system exception occurred
	 */
	public int countByfindByStatus(int status) throws SystemException {
		Object[] finderArgs = new Object[] { status };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_FINDBYSTATUS,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_INVITATIONCARD_WHERE);

			query.append(_FINDER_COLUMN_FINDBYSTATUS_STATUS_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(status);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_FINDBYSTATUS,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of invitation cards where userId = &#63; and inviteeEmail = &#63;.
	 *
	 * @param userId the user ID
	 * @param inviteeEmail the invitee email
	 * @return the number of matching invitation cards
	 * @throws SystemException if a system exception occurred
	 */
	public int countByuserId_InviteeEmail(long userId, String inviteeEmail)
		throws SystemException {
		Object[] finderArgs = new Object[] { userId, inviteeEmail };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_USERID_INVITEEEMAIL,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_INVITATIONCARD_WHERE);

			query.append(_FINDER_COLUMN_USERID_INVITEEEMAIL_USERID_2);

			if (inviteeEmail == null) {
				query.append(_FINDER_COLUMN_USERID_INVITEEEMAIL_INVITEEEMAIL_1);
			}
			else {
				if (inviteeEmail.equals(StringPool.BLANK)) {
					query.append(_FINDER_COLUMN_USERID_INVITEEEMAIL_INVITEEEMAIL_3);
				}
				else {
					query.append(_FINDER_COLUMN_USERID_INVITEEEMAIL_INVITEEEMAIL_2);
				}
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				if (inviteeEmail != null) {
					qPos.add(inviteeEmail);
				}

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_USERID_INVITEEEMAIL,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of invitation cards where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the number of matching invitation cards
	 * @throws SystemException if a system exception occurred
	 */
	public int countByuserId(long userId) throws SystemException {
		Object[] finderArgs = new Object[] { userId };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_USERID,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_INVITATIONCARD_WHERE);

			query.append(_FINDER_COLUMN_USERID_USERID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_USERID,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of invitation cards where userId = &#63; and status = &#63;.
	 *
	 * @param userId the user ID
	 * @param status the status
	 * @return the number of matching invitation cards
	 * @throws SystemException if a system exception occurred
	 */
	public int countByuserId_Status(long userId, int status)
		throws SystemException {
		Object[] finderArgs = new Object[] { userId, status };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_USERID_STATUS,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_INVITATIONCARD_WHERE);

			query.append(_FINDER_COLUMN_USERID_STATUS_USERID_2);

			query.append(_FINDER_COLUMN_USERID_STATUS_STATUS_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				qPos.add(status);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_USERID_STATUS,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of invitation cards.
	 *
	 * @return the number of invitation cards
	 * @throws SystemException if a system exception occurred
	 */
	public int countAll() throws SystemException {
		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_INVITATIONCARD);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Initializes the invitation card persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.util.service.ServiceProps.get(
						"value.object.listener.com.mpower.slayer.model.InvitationCard")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<InvitationCard>> listenersList = new ArrayList<ModelListener<InvitationCard>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<InvitationCard>)InstanceFactory.newInstance(
							listenerClassName));
				}

				listeners = listenersList.toArray(new ModelListener[listenersList.size()]);
			}
			catch (Exception e) {
				_log.error(e);
			}
		}
	}

	public void destroy() {
		EntityCacheUtil.removeCache(InvitationCardImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@BeanReference(type = InvitationCardPersistence.class)
	protected InvitationCardPersistence invitationCardPersistence;
	@BeanReference(type = ResourcePersistence.class)
	protected ResourcePersistence resourcePersistence;
	@BeanReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
	private static final String _SQL_SELECT_INVITATIONCARD = "SELECT invitationCard FROM InvitationCard invitationCard";
	private static final String _SQL_SELECT_INVITATIONCARD_WHERE = "SELECT invitationCard FROM InvitationCard invitationCard WHERE ";
	private static final String _SQL_COUNT_INVITATIONCARD = "SELECT COUNT(invitationCard) FROM InvitationCard invitationCard";
	private static final String _SQL_COUNT_INVITATIONCARD_WHERE = "SELECT COUNT(invitationCard) FROM InvitationCard invitationCard WHERE ";
	private static final String _FINDER_COLUMN_FINDBYSTATUS_STATUS_2 = "invitationCard.status = ?";
	private static final String _FINDER_COLUMN_USERID_INVITEEEMAIL_USERID_2 = "invitationCard.userId = ? AND ";
	private static final String _FINDER_COLUMN_USERID_INVITEEEMAIL_INVITEEEMAIL_1 =
		"invitationCard.inviteeEmail IS NULL";
	private static final String _FINDER_COLUMN_USERID_INVITEEEMAIL_INVITEEEMAIL_2 =
		"invitationCard.inviteeEmail = ?";
	private static final String _FINDER_COLUMN_USERID_INVITEEEMAIL_INVITEEEMAIL_3 =
		"(invitationCard.inviteeEmail IS NULL OR invitationCard.inviteeEmail = ?)";
	private static final String _FINDER_COLUMN_USERID_USERID_2 = "invitationCard.userId = ?";
	private static final String _FINDER_COLUMN_USERID_STATUS_USERID_2 = "invitationCard.userId = ? AND ";
	private static final String _FINDER_COLUMN_USERID_STATUS_STATUS_2 = "invitationCard.status = ?";
	private static final String _ORDER_BY_ENTITY_ALIAS = "invitationCard.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No InvitationCard exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No InvitationCard exists with the key {";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
				PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
	private static Log _log = LogFactoryUtil.getLog(InvitationCardPersistenceImpl.class);
	private static InvitationCard _nullInvitationCard = new InvitationCardImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<InvitationCard> toCacheModel() {
				return _nullInvitationCardCacheModel;
			}
		};

	private static CacheModel<InvitationCard> _nullInvitationCardCacheModel = new CacheModel<InvitationCard>() {
			public InvitationCard toEntityModel() {
				return _nullInvitationCard;
			}
		};
}