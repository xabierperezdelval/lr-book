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

import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ReferenceRegistry;
import com.liferay.portal.service.ServiceContext;

import com.mpower.slayer.model.InvitationCard;

import java.util.List;

/**
 * The persistence utility for the invitation card service. This utility wraps {@link InvitationCardPersistenceImpl} and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see InvitationCardPersistence
 * @see InvitationCardPersistenceImpl
 * @generated
 */
public class InvitationCardUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#clearCache(com.liferay.portal.model.BaseModel)
	 */
	public static void clearCache(InvitationCard invitationCard) {
		getPersistence().clearCache(invitationCard);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public long countWithDynamicQuery(DynamicQuery dynamicQuery)
		throws SystemException {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<InvitationCard> findWithDynamicQuery(
		DynamicQuery dynamicQuery) throws SystemException {
		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<InvitationCard> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end)
		throws SystemException {
		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<InvitationCard> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		return getPersistence()
				   .findWithDynamicQuery(dynamicQuery, start, end,
			orderByComparator);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#update(com.liferay.portal.model.BaseModel, boolean)
	 */
	public static InvitationCard update(InvitationCard invitationCard,
		boolean merge) throws SystemException {
		return getPersistence().update(invitationCard, merge);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#update(com.liferay.portal.model.BaseModel, boolean, ServiceContext)
	 */
	public static InvitationCard update(InvitationCard invitationCard,
		boolean merge, ServiceContext serviceContext) throws SystemException {
		return getPersistence().update(invitationCard, merge, serviceContext);
	}

	/**
	* Caches the invitation card in the entity cache if it is enabled.
	*
	* @param invitationCard the invitation card
	*/
	public static void cacheResult(
		com.mpower.slayer.model.InvitationCard invitationCard) {
		getPersistence().cacheResult(invitationCard);
	}

	/**
	* Caches the invitation cards in the entity cache if it is enabled.
	*
	* @param invitationCards the invitation cards
	*/
	public static void cacheResult(
		java.util.List<com.mpower.slayer.model.InvitationCard> invitationCards) {
		getPersistence().cacheResult(invitationCards);
	}

	/**
	* Creates a new invitation card with the primary key. Does not add the invitation card to the database.
	*
	* @param invitationId the primary key for the new invitation card
	* @return the new invitation card
	*/
	public static com.mpower.slayer.model.InvitationCard create(
		long invitationId) {
		return getPersistence().create(invitationId);
	}

	/**
	* Removes the invitation card with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param invitationId the primary key of the invitation card
	* @return the invitation card that was removed
	* @throws com.mpower.slayer.NoSuchInvitationCardException if a invitation card with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.mpower.slayer.model.InvitationCard remove(
		long invitationId)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException {
		return getPersistence().remove(invitationId);
	}

	public static com.mpower.slayer.model.InvitationCard updateImpl(
		com.mpower.slayer.model.InvitationCard invitationCard, boolean merge)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().updateImpl(invitationCard, merge);
	}

	/**
	* Returns the invitation card with the primary key or throws a {@link com.mpower.slayer.NoSuchInvitationCardException} if it could not be found.
	*
	* @param invitationId the primary key of the invitation card
	* @return the invitation card
	* @throws com.mpower.slayer.NoSuchInvitationCardException if a invitation card with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.mpower.slayer.model.InvitationCard findByPrimaryKey(
		long invitationId)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException {
		return getPersistence().findByPrimaryKey(invitationId);
	}

	/**
	* Returns the invitation card with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param invitationId the primary key of the invitation card
	* @return the invitation card, or <code>null</code> if a invitation card with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.mpower.slayer.model.InvitationCard fetchByPrimaryKey(
		long invitationId)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().fetchByPrimaryKey(invitationId);
	}

	/**
	* Returns all the invitation cards where status = &#63;.
	*
	* @param status the status
	* @return the matching invitation cards
	* @throws SystemException if a system exception occurred
	*/
	public static java.util.List<com.mpower.slayer.model.InvitationCard> findByfindByStatus(
		int status) throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().findByfindByStatus(status);
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
	public static java.util.List<com.mpower.slayer.model.InvitationCard> findByfindByStatus(
		int status, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().findByfindByStatus(status, start, end);
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
	public static java.util.List<com.mpower.slayer.model.InvitationCard> findByfindByStatus(
		int status, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence()
				   .findByfindByStatus(status, start, end, orderByComparator);
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
	public static com.mpower.slayer.model.InvitationCard findByfindByStatus_First(
		int status,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException {
		return getPersistence()
				   .findByfindByStatus_First(status, orderByComparator);
	}

	/**
	* Returns the first invitation card in the ordered set where status = &#63;.
	*
	* @param status the status
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching invitation card, or <code>null</code> if a matching invitation card could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.mpower.slayer.model.InvitationCard fetchByfindByStatus_First(
		int status,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence()
				   .fetchByfindByStatus_First(status, orderByComparator);
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
	public static com.mpower.slayer.model.InvitationCard findByfindByStatus_Last(
		int status,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException {
		return getPersistence()
				   .findByfindByStatus_Last(status, orderByComparator);
	}

	/**
	* Returns the last invitation card in the ordered set where status = &#63;.
	*
	* @param status the status
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching invitation card, or <code>null</code> if a matching invitation card could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.mpower.slayer.model.InvitationCard fetchByfindByStatus_Last(
		int status,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence()
				   .fetchByfindByStatus_Last(status, orderByComparator);
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
	public static com.mpower.slayer.model.InvitationCard[] findByfindByStatus_PrevAndNext(
		long invitationId, int status,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException {
		return getPersistence()
				   .findByfindByStatus_PrevAndNext(invitationId, status,
			orderByComparator);
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
	public static com.mpower.slayer.model.InvitationCard findByuserId_InviteeEmail(
		long userId, java.lang.String inviteeEmail)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException {
		return getPersistence().findByuserId_InviteeEmail(userId, inviteeEmail);
	}

	/**
	* Returns the invitation card where userId = &#63; and inviteeEmail = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	*
	* @param userId the user ID
	* @param inviteeEmail the invitee email
	* @return the matching invitation card, or <code>null</code> if a matching invitation card could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.mpower.slayer.model.InvitationCard fetchByuserId_InviteeEmail(
		long userId, java.lang.String inviteeEmail)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().fetchByuserId_InviteeEmail(userId, inviteeEmail);
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
	public static com.mpower.slayer.model.InvitationCard fetchByuserId_InviteeEmail(
		long userId, java.lang.String inviteeEmail, boolean retrieveFromCache)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence()
				   .fetchByuserId_InviteeEmail(userId, inviteeEmail,
			retrieveFromCache);
	}

	/**
	* Returns all the invitation cards where userId = &#63;.
	*
	* @param userId the user ID
	* @return the matching invitation cards
	* @throws SystemException if a system exception occurred
	*/
	public static java.util.List<com.mpower.slayer.model.InvitationCard> findByuserId(
		long userId) throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().findByuserId(userId);
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
	public static java.util.List<com.mpower.slayer.model.InvitationCard> findByuserId(
		long userId, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().findByuserId(userId, start, end);
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
	public static java.util.List<com.mpower.slayer.model.InvitationCard> findByuserId(
		long userId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence()
				   .findByuserId(userId, start, end, orderByComparator);
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
	public static com.mpower.slayer.model.InvitationCard findByuserId_First(
		long userId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException {
		return getPersistence().findByuserId_First(userId, orderByComparator);
	}

	/**
	* Returns the first invitation card in the ordered set where userId = &#63;.
	*
	* @param userId the user ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching invitation card, or <code>null</code> if a matching invitation card could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.mpower.slayer.model.InvitationCard fetchByuserId_First(
		long userId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().fetchByuserId_First(userId, orderByComparator);
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
	public static com.mpower.slayer.model.InvitationCard findByuserId_Last(
		long userId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException {
		return getPersistence().findByuserId_Last(userId, orderByComparator);
	}

	/**
	* Returns the last invitation card in the ordered set where userId = &#63;.
	*
	* @param userId the user ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching invitation card, or <code>null</code> if a matching invitation card could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.mpower.slayer.model.InvitationCard fetchByuserId_Last(
		long userId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().fetchByuserId_Last(userId, orderByComparator);
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
	public static com.mpower.slayer.model.InvitationCard[] findByuserId_PrevAndNext(
		long invitationId, long userId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException {
		return getPersistence()
				   .findByuserId_PrevAndNext(invitationId, userId,
			orderByComparator);
	}

	/**
	* Returns all the invitation cards where userId = &#63; and status = &#63;.
	*
	* @param userId the user ID
	* @param status the status
	* @return the matching invitation cards
	* @throws SystemException if a system exception occurred
	*/
	public static java.util.List<com.mpower.slayer.model.InvitationCard> findByuserId_Status(
		long userId, int status)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().findByuserId_Status(userId, status);
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
	public static java.util.List<com.mpower.slayer.model.InvitationCard> findByuserId_Status(
		long userId, int status, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().findByuserId_Status(userId, status, start, end);
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
	public static java.util.List<com.mpower.slayer.model.InvitationCard> findByuserId_Status(
		long userId, int status, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence()
				   .findByuserId_Status(userId, status, start, end,
			orderByComparator);
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
	public static com.mpower.slayer.model.InvitationCard findByuserId_Status_First(
		long userId, int status,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException {
		return getPersistence()
				   .findByuserId_Status_First(userId, status, orderByComparator);
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
	public static com.mpower.slayer.model.InvitationCard fetchByuserId_Status_First(
		long userId, int status,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence()
				   .fetchByuserId_Status_First(userId, status, orderByComparator);
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
	public static com.mpower.slayer.model.InvitationCard findByuserId_Status_Last(
		long userId, int status,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException {
		return getPersistence()
				   .findByuserId_Status_Last(userId, status, orderByComparator);
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
	public static com.mpower.slayer.model.InvitationCard fetchByuserId_Status_Last(
		long userId, int status,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence()
				   .fetchByuserId_Status_Last(userId, status, orderByComparator);
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
	public static com.mpower.slayer.model.InvitationCard[] findByuserId_Status_PrevAndNext(
		long invitationId, long userId, int status,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException {
		return getPersistence()
				   .findByuserId_Status_PrevAndNext(invitationId, userId,
			status, orderByComparator);
	}

	/**
	* Returns all the invitation cards.
	*
	* @return the invitation cards
	* @throws SystemException if a system exception occurred
	*/
	public static java.util.List<com.mpower.slayer.model.InvitationCard> findAll()
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().findAll();
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
	public static java.util.List<com.mpower.slayer.model.InvitationCard> findAll(
		int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().findAll(start, end);
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
	public static java.util.List<com.mpower.slayer.model.InvitationCard> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	* Removes all the invitation cards where status = &#63; from the database.
	*
	* @param status the status
	* @throws SystemException if a system exception occurred
	*/
	public static void removeByfindByStatus(int status)
		throws com.liferay.portal.kernel.exception.SystemException {
		getPersistence().removeByfindByStatus(status);
	}

	/**
	* Removes the invitation card where userId = &#63; and inviteeEmail = &#63; from the database.
	*
	* @param userId the user ID
	* @param inviteeEmail the invitee email
	* @return the invitation card that was removed
	* @throws SystemException if a system exception occurred
	*/
	public static com.mpower.slayer.model.InvitationCard removeByuserId_InviteeEmail(
		long userId, java.lang.String inviteeEmail)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException {
		return getPersistence().removeByuserId_InviteeEmail(userId, inviteeEmail);
	}

	/**
	* Removes all the invitation cards where userId = &#63; from the database.
	*
	* @param userId the user ID
	* @throws SystemException if a system exception occurred
	*/
	public static void removeByuserId(long userId)
		throws com.liferay.portal.kernel.exception.SystemException {
		getPersistence().removeByuserId(userId);
	}

	/**
	* Removes all the invitation cards where userId = &#63; and status = &#63; from the database.
	*
	* @param userId the user ID
	* @param status the status
	* @throws SystemException if a system exception occurred
	*/
	public static void removeByuserId_Status(long userId, int status)
		throws com.liferay.portal.kernel.exception.SystemException {
		getPersistence().removeByuserId_Status(userId, status);
	}

	/**
	* Removes all the invitation cards from the database.
	*
	* @throws SystemException if a system exception occurred
	*/
	public static void removeAll()
		throws com.liferay.portal.kernel.exception.SystemException {
		getPersistence().removeAll();
	}

	/**
	* Returns the number of invitation cards where status = &#63;.
	*
	* @param status the status
	* @return the number of matching invitation cards
	* @throws SystemException if a system exception occurred
	*/
	public static int countByfindByStatus(int status)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().countByfindByStatus(status);
	}

	/**
	* Returns the number of invitation cards where userId = &#63; and inviteeEmail = &#63;.
	*
	* @param userId the user ID
	* @param inviteeEmail the invitee email
	* @return the number of matching invitation cards
	* @throws SystemException if a system exception occurred
	*/
	public static int countByuserId_InviteeEmail(long userId,
		java.lang.String inviteeEmail)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().countByuserId_InviteeEmail(userId, inviteeEmail);
	}

	/**
	* Returns the number of invitation cards where userId = &#63;.
	*
	* @param userId the user ID
	* @return the number of matching invitation cards
	* @throws SystemException if a system exception occurred
	*/
	public static int countByuserId(long userId)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().countByuserId(userId);
	}

	/**
	* Returns the number of invitation cards where userId = &#63; and status = &#63;.
	*
	* @param userId the user ID
	* @param status the status
	* @return the number of matching invitation cards
	* @throws SystemException if a system exception occurred
	*/
	public static int countByuserId_Status(long userId, int status)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().countByuserId_Status(userId, status);
	}

	/**
	* Returns the number of invitation cards.
	*
	* @return the number of invitation cards
	* @throws SystemException if a system exception occurred
	*/
	public static int countAll()
		throws com.liferay.portal.kernel.exception.SystemException {
		return getPersistence().countAll();
	}

	public static InvitationCardPersistence getPersistence() {
		if (_persistence == null) {
			_persistence = (InvitationCardPersistence)PortletBeanLocatorUtil.locate(com.mpower.slayer.service.ClpSerializer.getServletContextName(),
					InvitationCardPersistence.class.getName());

			ReferenceRegistry.registerReference(InvitationCardUtil.class,
				"_persistence");
		}

		return _persistence;
	}

	/**
	 * @deprecated
	 */
	public void setPersistence(InvitationCardPersistence persistence) {
	}

	private static InvitationCardPersistence _persistence;
}