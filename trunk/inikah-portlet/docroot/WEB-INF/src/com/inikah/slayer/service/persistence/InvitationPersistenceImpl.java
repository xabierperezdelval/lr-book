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

import com.inikah.slayer.NoSuchInvitationException;
import com.inikah.slayer.model.Invitation;
import com.inikah.slayer.model.impl.InvitationImpl;
import com.inikah.slayer.model.impl.InvitationModelImpl;

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
 * The persistence implementation for the invitation service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Ahmed Hasan
 * @see InvitationPersistence
 * @see InvitationUtil
 * @generated
 */
public class InvitationPersistenceImpl extends BasePersistenceImpl<Invitation>
	implements InvitationPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link InvitationUtil} to access the invitation persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = InvitationImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(InvitationModelImpl.ENTITY_CACHE_ENABLED,
			InvitationModelImpl.FINDER_CACHE_ENABLED, InvitationImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(InvitationModelImpl.ENTITY_CACHE_ENABLED,
			InvitationModelImpl.FINDER_CACHE_ENABLED, InvitationImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(InvitationModelImpl.ENTITY_CACHE_ENABLED,
			InvitationModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_STATUS = new FinderPath(InvitationModelImpl.ENTITY_CACHE_ENABLED,
			InvitationModelImpl.FINDER_CACHE_ENABLED, InvitationImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByStatus",
			new String[] {
				Integer.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_STATUS =
		new FinderPath(InvitationModelImpl.ENTITY_CACHE_ENABLED,
			InvitationModelImpl.FINDER_CACHE_ENABLED, InvitationImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByStatus",
			new String[] { Integer.class.getName() },
			InvitationModelImpl.STATUS_COLUMN_BITMASK |
			InvitationModelImpl.CREATEDATE_COLUMN_BITMASK |
			InvitationModelImpl.MODIFIEDDATE_COLUMN_BITMASK |
			InvitationModelImpl.INVITEEEMAIL_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_STATUS = new FinderPath(InvitationModelImpl.ENTITY_CACHE_ENABLED,
			InvitationModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByStatus",
			new String[] { Integer.class.getName() });

	/**
	 * Returns all the invitations where status = &#63;.
	 *
	 * @param status the status
	 * @return the matching invitations
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Invitation> findByStatus(int status) throws SystemException {
		return findByStatus(status, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the invitations where status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.InvitationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param status the status
	 * @param start the lower bound of the range of invitations
	 * @param end the upper bound of the range of invitations (not inclusive)
	 * @return the range of matching invitations
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Invitation> findByStatus(int status, int start, int end)
		throws SystemException {
		return findByStatus(status, start, end, null);
	}

	/**
	 * Returns an ordered range of all the invitations where status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.InvitationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param status the status
	 * @param start the lower bound of the range of invitations
	 * @param end the upper bound of the range of invitations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching invitations
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Invitation> findByStatus(int status, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_STATUS;
			finderArgs = new Object[] { status };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_STATUS;
			finderArgs = new Object[] { status, start, end, orderByComparator };
		}

		List<Invitation> list = (List<Invitation>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (Invitation invitation : list) {
				if ((status != invitation.getStatus())) {
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

			query.append(_SQL_SELECT_INVITATION_WHERE);

			query.append(_FINDER_COLUMN_STATUS_STATUS_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(InvitationModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(status);

				if (!pagination) {
					list = (List<Invitation>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Invitation>(list);
				}
				else {
					list = (List<Invitation>)QueryUtil.list(q, getDialect(),
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
	 * Returns the first invitation in the ordered set where status = &#63;.
	 *
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching invitation
	 * @throws com.inikah.slayer.NoSuchInvitationException if a matching invitation could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation findByStatus_First(int status,
		OrderByComparator orderByComparator)
		throws NoSuchInvitationException, SystemException {
		Invitation invitation = fetchByStatus_First(status, orderByComparator);

		if (invitation != null) {
			return invitation;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("status=");
		msg.append(status);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInvitationException(msg.toString());
	}

	/**
	 * Returns the first invitation in the ordered set where status = &#63;.
	 *
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching invitation, or <code>null</code> if a matching invitation could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation fetchByStatus_First(int status,
		OrderByComparator orderByComparator) throws SystemException {
		List<Invitation> list = findByStatus(status, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last invitation in the ordered set where status = &#63;.
	 *
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching invitation
	 * @throws com.inikah.slayer.NoSuchInvitationException if a matching invitation could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation findByStatus_Last(int status,
		OrderByComparator orderByComparator)
		throws NoSuchInvitationException, SystemException {
		Invitation invitation = fetchByStatus_Last(status, orderByComparator);

		if (invitation != null) {
			return invitation;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("status=");
		msg.append(status);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInvitationException(msg.toString());
	}

	/**
	 * Returns the last invitation in the ordered set where status = &#63;.
	 *
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching invitation, or <code>null</code> if a matching invitation could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation fetchByStatus_Last(int status,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByStatus(status);

		if (count == 0) {
			return null;
		}

		List<Invitation> list = findByStatus(status, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the invitations before and after the current invitation in the ordered set where status = &#63;.
	 *
	 * @param invitationId the primary key of the current invitation
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next invitation
	 * @throws com.inikah.slayer.NoSuchInvitationException if a invitation with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation[] findByStatus_PrevAndNext(long invitationId, int status,
		OrderByComparator orderByComparator)
		throws NoSuchInvitationException, SystemException {
		Invitation invitation = findByPrimaryKey(invitationId);

		Session session = null;

		try {
			session = openSession();

			Invitation[] array = new InvitationImpl[3];

			array[0] = getByStatus_PrevAndNext(session, invitation, status,
					orderByComparator, true);

			array[1] = invitation;

			array[2] = getByStatus_PrevAndNext(session, invitation, status,
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

	protected Invitation getByStatus_PrevAndNext(Session session,
		Invitation invitation, int status, OrderByComparator orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_INVITATION_WHERE);

		query.append(_FINDER_COLUMN_STATUS_STATUS_2);

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
			query.append(InvitationModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(status);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(invitation);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<Invitation> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the invitations where status = &#63; from the database.
	 *
	 * @param status the status
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeByStatus(int status) throws SystemException {
		for (Invitation invitation : findByStatus(status, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, null)) {
			remove(invitation);
		}
	}

	/**
	 * Returns the number of invitations where status = &#63;.
	 *
	 * @param status the status
	 * @return the number of matching invitations
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByStatus(int status) throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_STATUS;

		Object[] finderArgs = new Object[] { status };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_INVITATION_WHERE);

			query.append(_FINDER_COLUMN_STATUS_STATUS_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(status);

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

	private static final String _FINDER_COLUMN_STATUS_STATUS_2 = "invitation.status = ?";
	public static final FinderPath FINDER_PATH_FETCH_BY_USERID_INVITEEEMAIL = new FinderPath(InvitationModelImpl.ENTITY_CACHE_ENABLED,
			InvitationModelImpl.FINDER_CACHE_ENABLED, InvitationImpl.class,
			FINDER_CLASS_NAME_ENTITY, "fetchByUserId_InviteeEmail",
			new String[] { Long.class.getName(), String.class.getName() },
			InvitationModelImpl.USERID_COLUMN_BITMASK |
			InvitationModelImpl.INVITEEEMAIL_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_USERID_INVITEEEMAIL = new FinderPath(InvitationModelImpl.ENTITY_CACHE_ENABLED,
			InvitationModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByUserId_InviteeEmail",
			new String[] { Long.class.getName(), String.class.getName() });

	/**
	 * Returns the invitation where userId = &#63; and inviteeEmail = &#63; or throws a {@link com.inikah.slayer.NoSuchInvitationException} if it could not be found.
	 *
	 * @param userId the user ID
	 * @param inviteeEmail the invitee email
	 * @return the matching invitation
	 * @throws com.inikah.slayer.NoSuchInvitationException if a matching invitation could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation findByUserId_InviteeEmail(long userId, String inviteeEmail)
		throws NoSuchInvitationException, SystemException {
		Invitation invitation = fetchByUserId_InviteeEmail(userId, inviteeEmail);

		if (invitation == null) {
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

			throw new NoSuchInvitationException(msg.toString());
		}

		return invitation;
	}

	/**
	 * Returns the invitation where userId = &#63; and inviteeEmail = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param userId the user ID
	 * @param inviteeEmail the invitee email
	 * @return the matching invitation, or <code>null</code> if a matching invitation could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation fetchByUserId_InviteeEmail(long userId,
		String inviteeEmail) throws SystemException {
		return fetchByUserId_InviteeEmail(userId, inviteeEmail, true);
	}

	/**
	 * Returns the invitation where userId = &#63; and inviteeEmail = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param userId the user ID
	 * @param inviteeEmail the invitee email
	 * @param retrieveFromCache whether to use the finder cache
	 * @return the matching invitation, or <code>null</code> if a matching invitation could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation fetchByUserId_InviteeEmail(long userId,
		String inviteeEmail, boolean retrieveFromCache)
		throws SystemException {
		Object[] finderArgs = new Object[] { userId, inviteeEmail };

		Object result = null;

		if (retrieveFromCache) {
			result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_USERID_INVITEEEMAIL,
					finderArgs, this);
		}

		if (result instanceof Invitation) {
			Invitation invitation = (Invitation)result;

			if ((userId != invitation.getUserId()) ||
					!Validator.equals(inviteeEmail, invitation.getInviteeEmail())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_SELECT_INVITATION_WHERE);

			query.append(_FINDER_COLUMN_USERID_INVITEEEMAIL_USERID_2);

			boolean bindInviteeEmail = false;

			if (inviteeEmail == null) {
				query.append(_FINDER_COLUMN_USERID_INVITEEEMAIL_INVITEEEMAIL_1);
			}
			else if (inviteeEmail.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_USERID_INVITEEEMAIL_INVITEEEMAIL_3);
			}
			else {
				bindInviteeEmail = true;

				query.append(_FINDER_COLUMN_USERID_INVITEEEMAIL_INVITEEEMAIL_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				if (bindInviteeEmail) {
					qPos.add(inviteeEmail);
				}

				List<Invitation> list = q.list();

				if (list.isEmpty()) {
					FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERID_INVITEEEMAIL,
						finderArgs, list);
				}
				else {
					if ((list.size() > 1) && _log.isWarnEnabled()) {
						_log.warn(
							"InvitationPersistenceImpl.fetchByUserId_InviteeEmail(long, String, boolean) with parameters (" +
							StringUtil.merge(finderArgs) +
							") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
					}

					Invitation invitation = list.get(0);

					result = invitation;

					cacheResult(invitation);

					if ((invitation.getUserId() != userId) ||
							(invitation.getInviteeEmail() == null) ||
							!invitation.getInviteeEmail().equals(inviteeEmail)) {
						FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERID_INVITEEEMAIL,
							finderArgs, invitation);
					}
				}
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_USERID_INVITEEEMAIL,
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
			return (Invitation)result;
		}
	}

	/**
	 * Removes the invitation where userId = &#63; and inviteeEmail = &#63; from the database.
	 *
	 * @param userId the user ID
	 * @param inviteeEmail the invitee email
	 * @return the invitation that was removed
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation removeByUserId_InviteeEmail(long userId,
		String inviteeEmail) throws NoSuchInvitationException, SystemException {
		Invitation invitation = findByUserId_InviteeEmail(userId, inviteeEmail);

		return remove(invitation);
	}

	/**
	 * Returns the number of invitations where userId = &#63; and inviteeEmail = &#63;.
	 *
	 * @param userId the user ID
	 * @param inviteeEmail the invitee email
	 * @return the number of matching invitations
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByUserId_InviteeEmail(long userId, String inviteeEmail)
		throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_USERID_INVITEEEMAIL;

		Object[] finderArgs = new Object[] { userId, inviteeEmail };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_INVITATION_WHERE);

			query.append(_FINDER_COLUMN_USERID_INVITEEEMAIL_USERID_2);

			boolean bindInviteeEmail = false;

			if (inviteeEmail == null) {
				query.append(_FINDER_COLUMN_USERID_INVITEEEMAIL_INVITEEEMAIL_1);
			}
			else if (inviteeEmail.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_USERID_INVITEEEMAIL_INVITEEEMAIL_3);
			}
			else {
				bindInviteeEmail = true;

				query.append(_FINDER_COLUMN_USERID_INVITEEEMAIL_INVITEEEMAIL_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				if (bindInviteeEmail) {
					qPos.add(inviteeEmail);
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

	private static final String _FINDER_COLUMN_USERID_INVITEEEMAIL_USERID_2 = "invitation.userId = ? AND ";
	private static final String _FINDER_COLUMN_USERID_INVITEEEMAIL_INVITEEEMAIL_1 =
		"invitation.inviteeEmail IS NULL";
	private static final String _FINDER_COLUMN_USERID_INVITEEEMAIL_INVITEEEMAIL_2 =
		"invitation.inviteeEmail = ?";
	private static final String _FINDER_COLUMN_USERID_INVITEEEMAIL_INVITEEEMAIL_3 =
		"(invitation.inviteeEmail IS NULL OR invitation.inviteeEmail = '')";
	public static final FinderPath FINDER_PATH_FETCH_BY_INVITEEEMAIL = new FinderPath(InvitationModelImpl.ENTITY_CACHE_ENABLED,
			InvitationModelImpl.FINDER_CACHE_ENABLED, InvitationImpl.class,
			FINDER_CLASS_NAME_ENTITY, "fetchByInviteeEmail",
			new String[] { String.class.getName() },
			InvitationModelImpl.INVITEEEMAIL_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_INVITEEEMAIL = new FinderPath(InvitationModelImpl.ENTITY_CACHE_ENABLED,
			InvitationModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByInviteeEmail",
			new String[] { String.class.getName() });

	/**
	 * Returns the invitation where inviteeEmail = &#63; or throws a {@link com.inikah.slayer.NoSuchInvitationException} if it could not be found.
	 *
	 * @param inviteeEmail the invitee email
	 * @return the matching invitation
	 * @throws com.inikah.slayer.NoSuchInvitationException if a matching invitation could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation findByInviteeEmail(String inviteeEmail)
		throws NoSuchInvitationException, SystemException {
		Invitation invitation = fetchByInviteeEmail(inviteeEmail);

		if (invitation == null) {
			StringBundler msg = new StringBundler(4);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("inviteeEmail=");
			msg.append(inviteeEmail);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchInvitationException(msg.toString());
		}

		return invitation;
	}

	/**
	 * Returns the invitation where inviteeEmail = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param inviteeEmail the invitee email
	 * @return the matching invitation, or <code>null</code> if a matching invitation could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation fetchByInviteeEmail(String inviteeEmail)
		throws SystemException {
		return fetchByInviteeEmail(inviteeEmail, true);
	}

	/**
	 * Returns the invitation where inviteeEmail = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param inviteeEmail the invitee email
	 * @param retrieveFromCache whether to use the finder cache
	 * @return the matching invitation, or <code>null</code> if a matching invitation could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation fetchByInviteeEmail(String inviteeEmail,
		boolean retrieveFromCache) throws SystemException {
		Object[] finderArgs = new Object[] { inviteeEmail };

		Object result = null;

		if (retrieveFromCache) {
			result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_INVITEEEMAIL,
					finderArgs, this);
		}

		if (result instanceof Invitation) {
			Invitation invitation = (Invitation)result;

			if (!Validator.equals(inviteeEmail, invitation.getInviteeEmail())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_SELECT_INVITATION_WHERE);

			boolean bindInviteeEmail = false;

			if (inviteeEmail == null) {
				query.append(_FINDER_COLUMN_INVITEEEMAIL_INVITEEEMAIL_1);
			}
			else if (inviteeEmail.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_INVITEEEMAIL_INVITEEEMAIL_3);
			}
			else {
				bindInviteeEmail = true;

				query.append(_FINDER_COLUMN_INVITEEEMAIL_INVITEEEMAIL_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindInviteeEmail) {
					qPos.add(inviteeEmail);
				}

				List<Invitation> list = q.list();

				if (list.isEmpty()) {
					FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_INVITEEEMAIL,
						finderArgs, list);
				}
				else {
					if ((list.size() > 1) && _log.isWarnEnabled()) {
						_log.warn(
							"InvitationPersistenceImpl.fetchByInviteeEmail(String, boolean) with parameters (" +
							StringUtil.merge(finderArgs) +
							") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
					}

					Invitation invitation = list.get(0);

					result = invitation;

					cacheResult(invitation);

					if ((invitation.getInviteeEmail() == null) ||
							!invitation.getInviteeEmail().equals(inviteeEmail)) {
						FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_INVITEEEMAIL,
							finderArgs, invitation);
					}
				}
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_INVITEEEMAIL,
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
			return (Invitation)result;
		}
	}

	/**
	 * Removes the invitation where inviteeEmail = &#63; from the database.
	 *
	 * @param inviteeEmail the invitee email
	 * @return the invitation that was removed
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation removeByInviteeEmail(String inviteeEmail)
		throws NoSuchInvitationException, SystemException {
		Invitation invitation = findByInviteeEmail(inviteeEmail);

		return remove(invitation);
	}

	/**
	 * Returns the number of invitations where inviteeEmail = &#63;.
	 *
	 * @param inviteeEmail the invitee email
	 * @return the number of matching invitations
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByInviteeEmail(String inviteeEmail)
		throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_INVITEEEMAIL;

		Object[] finderArgs = new Object[] { inviteeEmail };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_INVITATION_WHERE);

			boolean bindInviteeEmail = false;

			if (inviteeEmail == null) {
				query.append(_FINDER_COLUMN_INVITEEEMAIL_INVITEEEMAIL_1);
			}
			else if (inviteeEmail.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_INVITEEEMAIL_INVITEEEMAIL_3);
			}
			else {
				bindInviteeEmail = true;

				query.append(_FINDER_COLUMN_INVITEEEMAIL_INVITEEEMAIL_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindInviteeEmail) {
					qPos.add(inviteeEmail);
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

	private static final String _FINDER_COLUMN_INVITEEEMAIL_INVITEEEMAIL_1 = "invitation.inviteeEmail IS NULL AND invitation.status = 1";
	private static final String _FINDER_COLUMN_INVITEEEMAIL_INVITEEEMAIL_2 = "invitation.inviteeEmail = ? AND invitation.status = 1";
	private static final String _FINDER_COLUMN_INVITEEEMAIL_INVITEEEMAIL_3 = "(invitation.inviteeEmail IS NULL OR invitation.inviteeEmail = '') AND invitation.status = 1";
	public static final FinderPath FINDER_PATH_FETCH_BY_REGISTEREDEMAIL = new FinderPath(InvitationModelImpl.ENTITY_CACHE_ENABLED,
			InvitationModelImpl.FINDER_CACHE_ENABLED, InvitationImpl.class,
			FINDER_CLASS_NAME_ENTITY, "fetchByRegisteredEmail",
			new String[] { String.class.getName() },
			InvitationModelImpl.REGISTEREDEMAIL_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_REGISTEREDEMAIL = new FinderPath(InvitationModelImpl.ENTITY_CACHE_ENABLED,
			InvitationModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByRegisteredEmail", new String[] { String.class.getName() });

	/**
	 * Returns the invitation where registeredEmail = &#63; or throws a {@link com.inikah.slayer.NoSuchInvitationException} if it could not be found.
	 *
	 * @param registeredEmail the registered email
	 * @return the matching invitation
	 * @throws com.inikah.slayer.NoSuchInvitationException if a matching invitation could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation findByRegisteredEmail(String registeredEmail)
		throws NoSuchInvitationException, SystemException {
		Invitation invitation = fetchByRegisteredEmail(registeredEmail);

		if (invitation == null) {
			StringBundler msg = new StringBundler(4);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("registeredEmail=");
			msg.append(registeredEmail);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchInvitationException(msg.toString());
		}

		return invitation;
	}

	/**
	 * Returns the invitation where registeredEmail = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param registeredEmail the registered email
	 * @return the matching invitation, or <code>null</code> if a matching invitation could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation fetchByRegisteredEmail(String registeredEmail)
		throws SystemException {
		return fetchByRegisteredEmail(registeredEmail, true);
	}

	/**
	 * Returns the invitation where registeredEmail = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param registeredEmail the registered email
	 * @param retrieveFromCache whether to use the finder cache
	 * @return the matching invitation, or <code>null</code> if a matching invitation could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation fetchByRegisteredEmail(String registeredEmail,
		boolean retrieveFromCache) throws SystemException {
		Object[] finderArgs = new Object[] { registeredEmail };

		Object result = null;

		if (retrieveFromCache) {
			result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_REGISTEREDEMAIL,
					finderArgs, this);
		}

		if (result instanceof Invitation) {
			Invitation invitation = (Invitation)result;

			if (!Validator.equals(registeredEmail,
						invitation.getRegisteredEmail())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_SELECT_INVITATION_WHERE);

			boolean bindRegisteredEmail = false;

			if (registeredEmail == null) {
				query.append(_FINDER_COLUMN_REGISTEREDEMAIL_REGISTEREDEMAIL_1);
			}
			else if (registeredEmail.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_REGISTEREDEMAIL_REGISTEREDEMAIL_3);
			}
			else {
				bindRegisteredEmail = true;

				query.append(_FINDER_COLUMN_REGISTEREDEMAIL_REGISTEREDEMAIL_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindRegisteredEmail) {
					qPos.add(registeredEmail);
				}

				List<Invitation> list = q.list();

				if (list.isEmpty()) {
					FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_REGISTEREDEMAIL,
						finderArgs, list);
				}
				else {
					if ((list.size() > 1) && _log.isWarnEnabled()) {
						_log.warn(
							"InvitationPersistenceImpl.fetchByRegisteredEmail(String, boolean) with parameters (" +
							StringUtil.merge(finderArgs) +
							") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
					}

					Invitation invitation = list.get(0);

					result = invitation;

					cacheResult(invitation);

					if ((invitation.getRegisteredEmail() == null) ||
							!invitation.getRegisteredEmail()
										   .equals(registeredEmail)) {
						FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_REGISTEREDEMAIL,
							finderArgs, invitation);
					}
				}
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_REGISTEREDEMAIL,
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
			return (Invitation)result;
		}
	}

	/**
	 * Removes the invitation where registeredEmail = &#63; from the database.
	 *
	 * @param registeredEmail the registered email
	 * @return the invitation that was removed
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation removeByRegisteredEmail(String registeredEmail)
		throws NoSuchInvitationException, SystemException {
		Invitation invitation = findByRegisteredEmail(registeredEmail);

		return remove(invitation);
	}

	/**
	 * Returns the number of invitations where registeredEmail = &#63;.
	 *
	 * @param registeredEmail the registered email
	 * @return the number of matching invitations
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByRegisteredEmail(String registeredEmail)
		throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_REGISTEREDEMAIL;

		Object[] finderArgs = new Object[] { registeredEmail };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_INVITATION_WHERE);

			boolean bindRegisteredEmail = false;

			if (registeredEmail == null) {
				query.append(_FINDER_COLUMN_REGISTEREDEMAIL_REGISTEREDEMAIL_1);
			}
			else if (registeredEmail.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_REGISTEREDEMAIL_REGISTEREDEMAIL_3);
			}
			else {
				bindRegisteredEmail = true;

				query.append(_FINDER_COLUMN_REGISTEREDEMAIL_REGISTEREDEMAIL_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindRegisteredEmail) {
					qPos.add(registeredEmail);
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

	private static final String _FINDER_COLUMN_REGISTEREDEMAIL_REGISTEREDEMAIL_1 =
		"invitation.registeredEmail IS NULL AND invitation.status = 1";
	private static final String _FINDER_COLUMN_REGISTEREDEMAIL_REGISTEREDEMAIL_2 =
		"invitation.registeredEmail = ? AND invitation.status = 1";
	private static final String _FINDER_COLUMN_REGISTEREDEMAIL_REGISTEREDEMAIL_3 =
		"(invitation.registeredEmail IS NULL OR invitation.registeredEmail = '') AND invitation.status = 1";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_USERID = new FinderPath(InvitationModelImpl.ENTITY_CACHE_ENABLED,
			InvitationModelImpl.FINDER_CACHE_ENABLED, InvitationImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUserId",
			new String[] {
				Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID =
		new FinderPath(InvitationModelImpl.ENTITY_CACHE_ENABLED,
			InvitationModelImpl.FINDER_CACHE_ENABLED, InvitationImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUserId",
			new String[] { Long.class.getName() },
			InvitationModelImpl.USERID_COLUMN_BITMASK |
			InvitationModelImpl.CREATEDATE_COLUMN_BITMASK |
			InvitationModelImpl.MODIFIEDDATE_COLUMN_BITMASK |
			InvitationModelImpl.INVITEEEMAIL_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_USERID = new FinderPath(InvitationModelImpl.ENTITY_CACHE_ENABLED,
			InvitationModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUserId",
			new String[] { Long.class.getName() });

	/**
	 * Returns all the invitations where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the matching invitations
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Invitation> findByUserId(long userId) throws SystemException {
		return findByUserId(userId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the invitations where userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.InvitationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param start the lower bound of the range of invitations
	 * @param end the upper bound of the range of invitations (not inclusive)
	 * @return the range of matching invitations
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Invitation> findByUserId(long userId, int start, int end)
		throws SystemException {
		return findByUserId(userId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the invitations where userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.InvitationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param start the lower bound of the range of invitations
	 * @param end the upper bound of the range of invitations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching invitations
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Invitation> findByUserId(long userId, int start, int end,
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

		List<Invitation> list = (List<Invitation>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (Invitation invitation : list) {
				if ((userId != invitation.getUserId())) {
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

			query.append(_SQL_SELECT_INVITATION_WHERE);

			query.append(_FINDER_COLUMN_USERID_USERID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(InvitationModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				if (!pagination) {
					list = (List<Invitation>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Invitation>(list);
				}
				else {
					list = (List<Invitation>)QueryUtil.list(q, getDialect(),
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
	 * Returns the first invitation in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching invitation
	 * @throws com.inikah.slayer.NoSuchInvitationException if a matching invitation could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation findByUserId_First(long userId,
		OrderByComparator orderByComparator)
		throws NoSuchInvitationException, SystemException {
		Invitation invitation = fetchByUserId_First(userId, orderByComparator);

		if (invitation != null) {
			return invitation;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInvitationException(msg.toString());
	}

	/**
	 * Returns the first invitation in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching invitation, or <code>null</code> if a matching invitation could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation fetchByUserId_First(long userId,
		OrderByComparator orderByComparator) throws SystemException {
		List<Invitation> list = findByUserId(userId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last invitation in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching invitation
	 * @throws com.inikah.slayer.NoSuchInvitationException if a matching invitation could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation findByUserId_Last(long userId,
		OrderByComparator orderByComparator)
		throws NoSuchInvitationException, SystemException {
		Invitation invitation = fetchByUserId_Last(userId, orderByComparator);

		if (invitation != null) {
			return invitation;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInvitationException(msg.toString());
	}

	/**
	 * Returns the last invitation in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching invitation, or <code>null</code> if a matching invitation could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation fetchByUserId_Last(long userId,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByUserId(userId);

		if (count == 0) {
			return null;
		}

		List<Invitation> list = findByUserId(userId, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the invitations before and after the current invitation in the ordered set where userId = &#63;.
	 *
	 * @param invitationId the primary key of the current invitation
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next invitation
	 * @throws com.inikah.slayer.NoSuchInvitationException if a invitation with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation[] findByUserId_PrevAndNext(long invitationId,
		long userId, OrderByComparator orderByComparator)
		throws NoSuchInvitationException, SystemException {
		Invitation invitation = findByPrimaryKey(invitationId);

		Session session = null;

		try {
			session = openSession();

			Invitation[] array = new InvitationImpl[3];

			array[0] = getByUserId_PrevAndNext(session, invitation, userId,
					orderByComparator, true);

			array[1] = invitation;

			array[2] = getByUserId_PrevAndNext(session, invitation, userId,
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

	protected Invitation getByUserId_PrevAndNext(Session session,
		Invitation invitation, long userId,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_INVITATION_WHERE);

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
			query.append(InvitationModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(userId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(invitation);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<Invitation> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the invitations where userId = &#63; from the database.
	 *
	 * @param userId the user ID
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeByUserId(long userId) throws SystemException {
		for (Invitation invitation : findByUserId(userId, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, null)) {
			remove(invitation);
		}
	}

	/**
	 * Returns the number of invitations where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the number of matching invitations
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

			query.append(_SQL_COUNT_INVITATION_WHERE);

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

	private static final String _FINDER_COLUMN_USERID_USERID_2 = "invitation.userId = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_USERID_STATUS =
		new FinderPath(InvitationModelImpl.ENTITY_CACHE_ENABLED,
			InvitationModelImpl.FINDER_CACHE_ENABLED, InvitationImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUserId_Status",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID_STATUS =
		new FinderPath(InvitationModelImpl.ENTITY_CACHE_ENABLED,
			InvitationModelImpl.FINDER_CACHE_ENABLED, InvitationImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUserId_Status",
			new String[] { Long.class.getName(), Integer.class.getName() },
			InvitationModelImpl.USERID_COLUMN_BITMASK |
			InvitationModelImpl.STATUS_COLUMN_BITMASK |
			InvitationModelImpl.CREATEDATE_COLUMN_BITMASK |
			InvitationModelImpl.MODIFIEDDATE_COLUMN_BITMASK |
			InvitationModelImpl.INVITEEEMAIL_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_USERID_STATUS = new FinderPath(InvitationModelImpl.ENTITY_CACHE_ENABLED,
			InvitationModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUserId_Status",
			new String[] { Long.class.getName(), Integer.class.getName() });

	/**
	 * Returns all the invitations where userId = &#63; and status = &#63;.
	 *
	 * @param userId the user ID
	 * @param status the status
	 * @return the matching invitations
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Invitation> findByUserId_Status(long userId, int status)
		throws SystemException {
		return findByUserId_Status(userId, status, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the invitations where userId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.InvitationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param status the status
	 * @param start the lower bound of the range of invitations
	 * @param end the upper bound of the range of invitations (not inclusive)
	 * @return the range of matching invitations
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Invitation> findByUserId_Status(long userId, int status,
		int start, int end) throws SystemException {
		return findByUserId_Status(userId, status, start, end, null);
	}

	/**
	 * Returns an ordered range of all the invitations where userId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.InvitationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param status the status
	 * @param start the lower bound of the range of invitations
	 * @param end the upper bound of the range of invitations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching invitations
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Invitation> findByUserId_Status(long userId, int status,
		int start, int end, OrderByComparator orderByComparator)
		throws SystemException {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
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

		List<Invitation> list = (List<Invitation>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (Invitation invitation : list) {
				if ((userId != invitation.getUserId()) ||
						(status != invitation.getStatus())) {
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

			query.append(_SQL_SELECT_INVITATION_WHERE);

			query.append(_FINDER_COLUMN_USERID_STATUS_USERID_2);

			query.append(_FINDER_COLUMN_USERID_STATUS_STATUS_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(InvitationModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				qPos.add(status);

				if (!pagination) {
					list = (List<Invitation>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Invitation>(list);
				}
				else {
					list = (List<Invitation>)QueryUtil.list(q, getDialect(),
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
	 * Returns the first invitation in the ordered set where userId = &#63; and status = &#63;.
	 *
	 * @param userId the user ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching invitation
	 * @throws com.inikah.slayer.NoSuchInvitationException if a matching invitation could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation findByUserId_Status_First(long userId, int status,
		OrderByComparator orderByComparator)
		throws NoSuchInvitationException, SystemException {
		Invitation invitation = fetchByUserId_Status_First(userId, status,
				orderByComparator);

		if (invitation != null) {
			return invitation;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("userId=");
		msg.append(userId);

		msg.append(", status=");
		msg.append(status);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInvitationException(msg.toString());
	}

	/**
	 * Returns the first invitation in the ordered set where userId = &#63; and status = &#63;.
	 *
	 * @param userId the user ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching invitation, or <code>null</code> if a matching invitation could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation fetchByUserId_Status_First(long userId, int status,
		OrderByComparator orderByComparator) throws SystemException {
		List<Invitation> list = findByUserId_Status(userId, status, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last invitation in the ordered set where userId = &#63; and status = &#63;.
	 *
	 * @param userId the user ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching invitation
	 * @throws com.inikah.slayer.NoSuchInvitationException if a matching invitation could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation findByUserId_Status_Last(long userId, int status,
		OrderByComparator orderByComparator)
		throws NoSuchInvitationException, SystemException {
		Invitation invitation = fetchByUserId_Status_Last(userId, status,
				orderByComparator);

		if (invitation != null) {
			return invitation;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("userId=");
		msg.append(userId);

		msg.append(", status=");
		msg.append(status);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchInvitationException(msg.toString());
	}

	/**
	 * Returns the last invitation in the ordered set where userId = &#63; and status = &#63;.
	 *
	 * @param userId the user ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching invitation, or <code>null</code> if a matching invitation could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation fetchByUserId_Status_Last(long userId, int status,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByUserId_Status(userId, status);

		if (count == 0) {
			return null;
		}

		List<Invitation> list = findByUserId_Status(userId, status, count - 1,
				count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the invitations before and after the current invitation in the ordered set where userId = &#63; and status = &#63;.
	 *
	 * @param invitationId the primary key of the current invitation
	 * @param userId the user ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next invitation
	 * @throws com.inikah.slayer.NoSuchInvitationException if a invitation with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation[] findByUserId_Status_PrevAndNext(long invitationId,
		long userId, int status, OrderByComparator orderByComparator)
		throws NoSuchInvitationException, SystemException {
		Invitation invitation = findByPrimaryKey(invitationId);

		Session session = null;

		try {
			session = openSession();

			Invitation[] array = new InvitationImpl[3];

			array[0] = getByUserId_Status_PrevAndNext(session, invitation,
					userId, status, orderByComparator, true);

			array[1] = invitation;

			array[2] = getByUserId_Status_PrevAndNext(session, invitation,
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

	protected Invitation getByUserId_Status_PrevAndNext(Session session,
		Invitation invitation, long userId, int status,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_INVITATION_WHERE);

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
		else {
			query.append(InvitationModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(userId);

		qPos.add(status);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(invitation);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<Invitation> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the invitations where userId = &#63; and status = &#63; from the database.
	 *
	 * @param userId the user ID
	 * @param status the status
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeByUserId_Status(long userId, int status)
		throws SystemException {
		for (Invitation invitation : findByUserId_Status(userId, status,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(invitation);
		}
	}

	/**
	 * Returns the number of invitations where userId = &#63; and status = &#63;.
	 *
	 * @param userId the user ID
	 * @param status the status
	 * @return the number of matching invitations
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByUserId_Status(long userId, int status)
		throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_USERID_STATUS;

		Object[] finderArgs = new Object[] { userId, status };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_INVITATION_WHERE);

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

	private static final String _FINDER_COLUMN_USERID_STATUS_USERID_2 = "invitation.userId = ? AND ";
	private static final String _FINDER_COLUMN_USERID_STATUS_STATUS_2 = "invitation.status = ?";
	public static final FinderPath FINDER_PATH_FETCH_BY_INVITEENEWUSERID = new FinderPath(InvitationModelImpl.ENTITY_CACHE_ENABLED,
			InvitationModelImpl.FINDER_CACHE_ENABLED, InvitationImpl.class,
			FINDER_CLASS_NAME_ENTITY, "fetchByInviteeNewUserId",
			new String[] { Long.class.getName() },
			InvitationModelImpl.INVITEENEWUSERID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_INVITEENEWUSERID = new FinderPath(InvitationModelImpl.ENTITY_CACHE_ENABLED,
			InvitationModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByInviteeNewUserId", new String[] { Long.class.getName() });

	/**
	 * Returns the invitation where inviteeNewUserId = &#63; or throws a {@link com.inikah.slayer.NoSuchInvitationException} if it could not be found.
	 *
	 * @param inviteeNewUserId the invitee new user ID
	 * @return the matching invitation
	 * @throws com.inikah.slayer.NoSuchInvitationException if a matching invitation could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation findByInviteeNewUserId(long inviteeNewUserId)
		throws NoSuchInvitationException, SystemException {
		Invitation invitation = fetchByInviteeNewUserId(inviteeNewUserId);

		if (invitation == null) {
			StringBundler msg = new StringBundler(4);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("inviteeNewUserId=");
			msg.append(inviteeNewUserId);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchInvitationException(msg.toString());
		}

		return invitation;
	}

	/**
	 * Returns the invitation where inviteeNewUserId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param inviteeNewUserId the invitee new user ID
	 * @return the matching invitation, or <code>null</code> if a matching invitation could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation fetchByInviteeNewUserId(long inviteeNewUserId)
		throws SystemException {
		return fetchByInviteeNewUserId(inviteeNewUserId, true);
	}

	/**
	 * Returns the invitation where inviteeNewUserId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param inviteeNewUserId the invitee new user ID
	 * @param retrieveFromCache whether to use the finder cache
	 * @return the matching invitation, or <code>null</code> if a matching invitation could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation fetchByInviteeNewUserId(long inviteeNewUserId,
		boolean retrieveFromCache) throws SystemException {
		Object[] finderArgs = new Object[] { inviteeNewUserId };

		Object result = null;

		if (retrieveFromCache) {
			result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_INVITEENEWUSERID,
					finderArgs, this);
		}

		if (result instanceof Invitation) {
			Invitation invitation = (Invitation)result;

			if ((inviteeNewUserId != invitation.getInviteeNewUserId())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_SELECT_INVITATION_WHERE);

			query.append(_FINDER_COLUMN_INVITEENEWUSERID_INVITEENEWUSERID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(inviteeNewUserId);

				List<Invitation> list = q.list();

				if (list.isEmpty()) {
					FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_INVITEENEWUSERID,
						finderArgs, list);
				}
				else {
					if ((list.size() > 1) && _log.isWarnEnabled()) {
						_log.warn(
							"InvitationPersistenceImpl.fetchByInviteeNewUserId(long, boolean) with parameters (" +
							StringUtil.merge(finderArgs) +
							") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
					}

					Invitation invitation = list.get(0);

					result = invitation;

					cacheResult(invitation);

					if ((invitation.getInviteeNewUserId() != inviteeNewUserId)) {
						FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_INVITEENEWUSERID,
							finderArgs, invitation);
					}
				}
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_INVITEENEWUSERID,
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
			return (Invitation)result;
		}
	}

	/**
	 * Removes the invitation where inviteeNewUserId = &#63; from the database.
	 *
	 * @param inviteeNewUserId the invitee new user ID
	 * @return the invitation that was removed
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation removeByInviteeNewUserId(long inviteeNewUserId)
		throws NoSuchInvitationException, SystemException {
		Invitation invitation = findByInviteeNewUserId(inviteeNewUserId);

		return remove(invitation);
	}

	/**
	 * Returns the number of invitations where inviteeNewUserId = &#63;.
	 *
	 * @param inviteeNewUserId the invitee new user ID
	 * @return the number of matching invitations
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public int countByInviteeNewUserId(long inviteeNewUserId)
		throws SystemException {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_INVITEENEWUSERID;

		Object[] finderArgs = new Object[] { inviteeNewUserId };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_INVITATION_WHERE);

			query.append(_FINDER_COLUMN_INVITEENEWUSERID_INVITEENEWUSERID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(inviteeNewUserId);

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

	private static final String _FINDER_COLUMN_INVITEENEWUSERID_INVITEENEWUSERID_2 =
		"invitation.inviteeNewUserId = ?";

	public InvitationPersistenceImpl() {
		setModelClass(Invitation.class);
	}

	/**
	 * Caches the invitation in the entity cache if it is enabled.
	 *
	 * @param invitation the invitation
	 */
	@Override
	public void cacheResult(Invitation invitation) {
		EntityCacheUtil.putResult(InvitationModelImpl.ENTITY_CACHE_ENABLED,
			InvitationImpl.class, invitation.getPrimaryKey(), invitation);

		FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERID_INVITEEEMAIL,
			new Object[] { invitation.getUserId(), invitation.getInviteeEmail() },
			invitation);

		FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_INVITEEEMAIL,
			new Object[] { invitation.getInviteeEmail() }, invitation);

		FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_REGISTEREDEMAIL,
			new Object[] { invitation.getRegisteredEmail() }, invitation);

		FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_INVITEENEWUSERID,
			new Object[] { invitation.getInviteeNewUserId() }, invitation);

		invitation.resetOriginalValues();
	}

	/**
	 * Caches the invitations in the entity cache if it is enabled.
	 *
	 * @param invitations the invitations
	 */
	@Override
	public void cacheResult(List<Invitation> invitations) {
		for (Invitation invitation : invitations) {
			if (EntityCacheUtil.getResult(
						InvitationModelImpl.ENTITY_CACHE_ENABLED,
						InvitationImpl.class, invitation.getPrimaryKey()) == null) {
				cacheResult(invitation);
			}
			else {
				invitation.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all invitations.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(InvitationImpl.class.getName());
		}

		EntityCacheUtil.clearCache(InvitationImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the invitation.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(Invitation invitation) {
		EntityCacheUtil.removeResult(InvitationModelImpl.ENTITY_CACHE_ENABLED,
			InvitationImpl.class, invitation.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		clearUniqueFindersCache(invitation);
	}

	@Override
	public void clearCache(List<Invitation> invitations) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Invitation invitation : invitations) {
			EntityCacheUtil.removeResult(InvitationModelImpl.ENTITY_CACHE_ENABLED,
				InvitationImpl.class, invitation.getPrimaryKey());

			clearUniqueFindersCache(invitation);
		}
	}

	protected void cacheUniqueFindersCache(Invitation invitation) {
		if (invitation.isNew()) {
			Object[] args = new Object[] {
					invitation.getUserId(), invitation.getInviteeEmail()
				};

			FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_USERID_INVITEEEMAIL,
				args, Long.valueOf(1));
			FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERID_INVITEEEMAIL,
				args, invitation);

			args = new Object[] { invitation.getInviteeEmail() };

			FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_INVITEEEMAIL, args,
				Long.valueOf(1));
			FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_INVITEEEMAIL, args,
				invitation);

			args = new Object[] { invitation.getRegisteredEmail() };

			FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_REGISTEREDEMAIL,
				args, Long.valueOf(1));
			FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_REGISTEREDEMAIL,
				args, invitation);

			args = new Object[] { invitation.getInviteeNewUserId() };

			FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_INVITEENEWUSERID,
				args, Long.valueOf(1));
			FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_INVITEENEWUSERID,
				args, invitation);
		}
		else {
			InvitationModelImpl invitationModelImpl = (InvitationModelImpl)invitation;

			if ((invitationModelImpl.getColumnBitmask() &
					FINDER_PATH_FETCH_BY_USERID_INVITEEEMAIL.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						invitation.getUserId(), invitation.getInviteeEmail()
					};

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_USERID_INVITEEEMAIL,
					args, Long.valueOf(1));
				FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_USERID_INVITEEEMAIL,
					args, invitation);
			}

			if ((invitationModelImpl.getColumnBitmask() &
					FINDER_PATH_FETCH_BY_INVITEEEMAIL.getColumnBitmask()) != 0) {
				Object[] args = new Object[] { invitation.getInviteeEmail() };

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_INVITEEEMAIL,
					args, Long.valueOf(1));
				FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_INVITEEEMAIL,
					args, invitation);
			}

			if ((invitationModelImpl.getColumnBitmask() &
					FINDER_PATH_FETCH_BY_REGISTEREDEMAIL.getColumnBitmask()) != 0) {
				Object[] args = new Object[] { invitation.getRegisteredEmail() };

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_REGISTEREDEMAIL,
					args, Long.valueOf(1));
				FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_REGISTEREDEMAIL,
					args, invitation);
			}

			if ((invitationModelImpl.getColumnBitmask() &
					FINDER_PATH_FETCH_BY_INVITEENEWUSERID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] { invitation.getInviteeNewUserId() };

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_INVITEENEWUSERID,
					args, Long.valueOf(1));
				FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_INVITEENEWUSERID,
					args, invitation);
			}
		}
	}

	protected void clearUniqueFindersCache(Invitation invitation) {
		InvitationModelImpl invitationModelImpl = (InvitationModelImpl)invitation;

		Object[] args = new Object[] {
				invitation.getUserId(), invitation.getInviteeEmail()
			};

		FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID_INVITEEEMAIL,
			args);
		FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_USERID_INVITEEEMAIL,
			args);

		if ((invitationModelImpl.getColumnBitmask() &
				FINDER_PATH_FETCH_BY_USERID_INVITEEEMAIL.getColumnBitmask()) != 0) {
			args = new Object[] {
					invitationModelImpl.getOriginalUserId(),
					invitationModelImpl.getOriginalInviteeEmail()
				};

			FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID_INVITEEEMAIL,
				args);
			FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_USERID_INVITEEEMAIL,
				args);
		}

		args = new Object[] { invitation.getInviteeEmail() };

		FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_INVITEEEMAIL, args);
		FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_INVITEEEMAIL, args);

		if ((invitationModelImpl.getColumnBitmask() &
				FINDER_PATH_FETCH_BY_INVITEEEMAIL.getColumnBitmask()) != 0) {
			args = new Object[] { invitationModelImpl.getOriginalInviteeEmail() };

			FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_INVITEEEMAIL, args);
			FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_INVITEEEMAIL, args);
		}

		args = new Object[] { invitation.getRegisteredEmail() };

		FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_REGISTEREDEMAIL, args);
		FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_REGISTEREDEMAIL, args);

		if ((invitationModelImpl.getColumnBitmask() &
				FINDER_PATH_FETCH_BY_REGISTEREDEMAIL.getColumnBitmask()) != 0) {
			args = new Object[] { invitationModelImpl.getOriginalRegisteredEmail() };

			FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_REGISTEREDEMAIL,
				args);
			FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_REGISTEREDEMAIL,
				args);
		}

		args = new Object[] { invitation.getInviteeNewUserId() };

		FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_INVITEENEWUSERID, args);
		FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_INVITEENEWUSERID, args);

		if ((invitationModelImpl.getColumnBitmask() &
				FINDER_PATH_FETCH_BY_INVITEENEWUSERID.getColumnBitmask()) != 0) {
			args = new Object[] {
					invitationModelImpl.getOriginalInviteeNewUserId()
				};

			FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_INVITEENEWUSERID,
				args);
			FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_INVITEENEWUSERID,
				args);
		}
	}

	/**
	 * Creates a new invitation with the primary key. Does not add the invitation to the database.
	 *
	 * @param invitationId the primary key for the new invitation
	 * @return the new invitation
	 */
	@Override
	public Invitation create(long invitationId) {
		Invitation invitation = new InvitationImpl();

		invitation.setNew(true);
		invitation.setPrimaryKey(invitationId);

		return invitation;
	}

	/**
	 * Removes the invitation with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param invitationId the primary key of the invitation
	 * @return the invitation that was removed
	 * @throws com.inikah.slayer.NoSuchInvitationException if a invitation with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation remove(long invitationId)
		throws NoSuchInvitationException, SystemException {
		return remove((Serializable)invitationId);
	}

	/**
	 * Removes the invitation with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the invitation
	 * @return the invitation that was removed
	 * @throws com.inikah.slayer.NoSuchInvitationException if a invitation with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation remove(Serializable primaryKey)
		throws NoSuchInvitationException, SystemException {
		Session session = null;

		try {
			session = openSession();

			Invitation invitation = (Invitation)session.get(InvitationImpl.class,
					primaryKey);

			if (invitation == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchInvitationException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(invitation);
		}
		catch (NoSuchInvitationException nsee) {
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
	protected Invitation removeImpl(Invitation invitation)
		throws SystemException {
		invitation = toUnwrappedModel(invitation);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(invitation)) {
				invitation = (Invitation)session.get(InvitationImpl.class,
						invitation.getPrimaryKeyObj());
			}

			if (invitation != null) {
				session.delete(invitation);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (invitation != null) {
			clearCache(invitation);
		}

		return invitation;
	}

	@Override
	public Invitation updateImpl(com.inikah.slayer.model.Invitation invitation)
		throws SystemException {
		invitation = toUnwrappedModel(invitation);

		boolean isNew = invitation.isNew();

		InvitationModelImpl invitationModelImpl = (InvitationModelImpl)invitation;

		Session session = null;

		try {
			session = openSession();

			if (invitation.isNew()) {
				session.save(invitation);

				invitation.setNew(false);
			}
			else {
				session.merge(invitation);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !InvitationModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		else {
			if ((invitationModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_STATUS.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						invitationModelImpl.getOriginalStatus()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_STATUS, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_STATUS,
					args);

				args = new Object[] { invitationModelImpl.getStatus() };

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_STATUS, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_STATUS,
					args);
			}

			if ((invitationModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						invitationModelImpl.getOriginalUserId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID,
					args);

				args = new Object[] { invitationModelImpl.getUserId() };

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID,
					args);
			}

			if ((invitationModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID_STATUS.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						invitationModelImpl.getOriginalUserId(),
						invitationModelImpl.getOriginalStatus()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID_STATUS,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID_STATUS,
					args);

				args = new Object[] {
						invitationModelImpl.getUserId(),
						invitationModelImpl.getStatus()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID_STATUS,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID_STATUS,
					args);
			}
		}

		EntityCacheUtil.putResult(InvitationModelImpl.ENTITY_CACHE_ENABLED,
			InvitationImpl.class, invitation.getPrimaryKey(), invitation);

		clearUniqueFindersCache(invitation);
		cacheUniqueFindersCache(invitation);

		return invitation;
	}

	protected Invitation toUnwrappedModel(Invitation invitation) {
		if (invitation instanceof InvitationImpl) {
			return invitation;
		}

		InvitationImpl invitationImpl = new InvitationImpl();

		invitationImpl.setNew(invitation.isNew());
		invitationImpl.setPrimaryKey(invitation.getPrimaryKey());

		invitationImpl.setInvitationId(invitation.getInvitationId());
		invitationImpl.setCompanyId(invitation.getCompanyId());
		invitationImpl.setUserId(invitation.getUserId());
		invitationImpl.setUserName(invitation.getUserName());
		invitationImpl.setCreateDate(invitation.getCreateDate());
		invitationImpl.setModifiedDate(invitation.getModifiedDate());
		invitationImpl.setInviteeEmail(invitation.getInviteeEmail());
		invitationImpl.setRegisteredEmail(invitation.getRegisteredEmail());
		invitationImpl.setStatus(invitation.getStatus());
		invitationImpl.setInviteeNewUserId(invitation.getInviteeNewUserId());
		invitationImpl.setInvitationChain(invitation.getInvitationChain());

		return invitationImpl;
	}

	/**
	 * Returns the invitation with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the invitation
	 * @return the invitation
	 * @throws com.inikah.slayer.NoSuchInvitationException if a invitation with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation findByPrimaryKey(Serializable primaryKey)
		throws NoSuchInvitationException, SystemException {
		Invitation invitation = fetchByPrimaryKey(primaryKey);

		if (invitation == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchInvitationException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return invitation;
	}

	/**
	 * Returns the invitation with the primary key or throws a {@link com.inikah.slayer.NoSuchInvitationException} if it could not be found.
	 *
	 * @param invitationId the primary key of the invitation
	 * @return the invitation
	 * @throws com.inikah.slayer.NoSuchInvitationException if a invitation with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation findByPrimaryKey(long invitationId)
		throws NoSuchInvitationException, SystemException {
		return findByPrimaryKey((Serializable)invitationId);
	}

	/**
	 * Returns the invitation with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the invitation
	 * @return the invitation, or <code>null</code> if a invitation with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		Invitation invitation = (Invitation)EntityCacheUtil.getResult(InvitationModelImpl.ENTITY_CACHE_ENABLED,
				InvitationImpl.class, primaryKey);

		if (invitation == _nullInvitation) {
			return null;
		}

		if (invitation == null) {
			Session session = null;

			try {
				session = openSession();

				invitation = (Invitation)session.get(InvitationImpl.class,
						primaryKey);

				if (invitation != null) {
					cacheResult(invitation);
				}
				else {
					EntityCacheUtil.putResult(InvitationModelImpl.ENTITY_CACHE_ENABLED,
						InvitationImpl.class, primaryKey, _nullInvitation);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(InvitationModelImpl.ENTITY_CACHE_ENABLED,
					InvitationImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return invitation;
	}

	/**
	 * Returns the invitation with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param invitationId the primary key of the invitation
	 * @return the invitation, or <code>null</code> if a invitation with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public Invitation fetchByPrimaryKey(long invitationId)
		throws SystemException {
		return fetchByPrimaryKey((Serializable)invitationId);
	}

	/**
	 * Returns all the invitations.
	 *
	 * @return the invitations
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Invitation> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the invitations.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.InvitationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of invitations
	 * @param end the upper bound of the range of invitations (not inclusive)
	 * @return the range of invitations
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Invitation> findAll(int start, int end)
		throws SystemException {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the invitations.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.inikah.slayer.model.impl.InvitationModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of invitations
	 * @param end the upper bound of the range of invitations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of invitations
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public List<Invitation> findAll(int start, int end,
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

		List<Invitation> list = (List<Invitation>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_INVITATION);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_INVITATION;

				if (pagination) {
					sql = sql.concat(InvitationModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<Invitation>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = new UnmodifiableList<Invitation>(list);
				}
				else {
					list = (List<Invitation>)QueryUtil.list(q, getDialect(),
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
	 * Removes all the invitations from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public void removeAll() throws SystemException {
		for (Invitation invitation : findAll()) {
			remove(invitation);
		}
	}

	/**
	 * Returns the number of invitations.
	 *
	 * @return the number of invitations
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

				Query q = session.createQuery(_SQL_COUNT_INVITATION);

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
	 * Initializes the invitation persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.util.service.ServiceProps.get(
						"value.object.listener.com.inikah.slayer.model.Invitation")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<Invitation>> listenersList = new ArrayList<ModelListener<Invitation>>();

				for (String listenerClassName : listenerClassNames) {
					listenersList.add((ModelListener<Invitation>)InstanceFactory.newInstance(
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
		EntityCacheUtil.removeCache(InvitationImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_INVITATION = "SELECT invitation FROM Invitation invitation";
	private static final String _SQL_SELECT_INVITATION_WHERE = "SELECT invitation FROM Invitation invitation WHERE ";
	private static final String _SQL_COUNT_INVITATION = "SELECT COUNT(invitation) FROM Invitation invitation";
	private static final String _SQL_COUNT_INVITATION_WHERE = "SELECT COUNT(invitation) FROM Invitation invitation WHERE ";
	private static final String _ORDER_BY_ENTITY_ALIAS = "invitation.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No Invitation exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No Invitation exists with the key {";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
				PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
	private static Log _log = LogFactoryUtil.getLog(InvitationPersistenceImpl.class);
	private static Invitation _nullInvitation = new InvitationImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<Invitation> toCacheModel() {
				return _nullInvitationCacheModel;
			}
		};

	private static CacheModel<Invitation> _nullInvitationCacheModel = new CacheModel<Invitation>() {
			@Override
			public Invitation toEntityModel() {
				return _nullInvitation;
			}
		};
}