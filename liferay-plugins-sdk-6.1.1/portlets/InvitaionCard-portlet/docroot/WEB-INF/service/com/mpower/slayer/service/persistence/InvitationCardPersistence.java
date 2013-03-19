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

import com.liferay.portal.service.persistence.BasePersistence;

import com.mpower.slayer.model.InvitationCard;

/**
 * The persistence interface for the invitation card service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see InvitationCardPersistenceImpl
 * @see InvitationCardUtil
 * @generated
 */
public interface InvitationCardPersistence extends BasePersistence<InvitationCard> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link InvitationCardUtil} to access the invitation card persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	* Caches the invitation card in the entity cache if it is enabled.
	*
	* @param invitationCard the invitation card
	*/
	public void cacheResult(
		com.mpower.slayer.model.InvitationCard invitationCard);

	/**
	* Caches the invitation cards in the entity cache if it is enabled.
	*
	* @param invitationCards the invitation cards
	*/
	public void cacheResult(
		java.util.List<com.mpower.slayer.model.InvitationCard> invitationCards);

	/**
	* Creates a new invitation card with the primary key. Does not add the invitation card to the database.
	*
	* @param invitationId the primary key for the new invitation card
	* @return the new invitation card
	*/
	public com.mpower.slayer.model.InvitationCard create(long invitationId);

	/**
	* Removes the invitation card with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param invitationId the primary key of the invitation card
	* @return the invitation card that was removed
	* @throws com.mpower.slayer.NoSuchInvitationCardException if a invitation card with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.mpower.slayer.model.InvitationCard remove(long invitationId)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException;

	public com.mpower.slayer.model.InvitationCard updateImpl(
		com.mpower.slayer.model.InvitationCard invitationCard, boolean merge)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the invitation card with the primary key or throws a {@link com.mpower.slayer.NoSuchInvitationCardException} if it could not be found.
	*
	* @param invitationId the primary key of the invitation card
	* @return the invitation card
	* @throws com.mpower.slayer.NoSuchInvitationCardException if a invitation card with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.mpower.slayer.model.InvitationCard findByPrimaryKey(
		long invitationId)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException;

	/**
	* Returns the invitation card with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param invitationId the primary key of the invitation card
	* @return the invitation card, or <code>null</code> if a invitation card with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.mpower.slayer.model.InvitationCard fetchByPrimaryKey(
		long invitationId)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns all the invitation cards where status = &#63;.
	*
	* @param status the status
	* @return the matching invitation cards
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.mpower.slayer.model.InvitationCard> findByfindByStatus(
		int status) throws com.liferay.portal.kernel.exception.SystemException;

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
	public java.util.List<com.mpower.slayer.model.InvitationCard> findByfindByStatus(
		int status, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public java.util.List<com.mpower.slayer.model.InvitationCard> findByfindByStatus(
		int status, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the first invitation card in the ordered set where status = &#63;.
	*
	* @param status the status
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching invitation card
	* @throws com.mpower.slayer.NoSuchInvitationCardException if a matching invitation card could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.mpower.slayer.model.InvitationCard findByfindByStatus_First(
		int status,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException;

	/**
	* Returns the first invitation card in the ordered set where status = &#63;.
	*
	* @param status the status
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching invitation card, or <code>null</code> if a matching invitation card could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.mpower.slayer.model.InvitationCard fetchByfindByStatus_First(
		int status,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the last invitation card in the ordered set where status = &#63;.
	*
	* @param status the status
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching invitation card
	* @throws com.mpower.slayer.NoSuchInvitationCardException if a matching invitation card could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.mpower.slayer.model.InvitationCard findByfindByStatus_Last(
		int status,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException;

	/**
	* Returns the last invitation card in the ordered set where status = &#63;.
	*
	* @param status the status
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching invitation card, or <code>null</code> if a matching invitation card could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.mpower.slayer.model.InvitationCard fetchByfindByStatus_Last(
		int status,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public com.mpower.slayer.model.InvitationCard[] findByfindByStatus_PrevAndNext(
		long invitationId, int status,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException;

	/**
	* Returns the invitation card where userId = &#63; and inviteeEmail = &#63; or throws a {@link com.mpower.slayer.NoSuchInvitationCardException} if it could not be found.
	*
	* @param userId the user ID
	* @param inviteeEmail the invitee email
	* @return the matching invitation card
	* @throws com.mpower.slayer.NoSuchInvitationCardException if a matching invitation card could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.mpower.slayer.model.InvitationCard findByuserId_InviteeEmail(
		long userId, java.lang.String inviteeEmail)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException;

	/**
	* Returns the invitation card where userId = &#63; and inviteeEmail = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	*
	* @param userId the user ID
	* @param inviteeEmail the invitee email
	* @return the matching invitation card, or <code>null</code> if a matching invitation card could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.mpower.slayer.model.InvitationCard fetchByuserId_InviteeEmail(
		long userId, java.lang.String inviteeEmail)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the invitation card where userId = &#63; and inviteeEmail = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	*
	* @param userId the user ID
	* @param inviteeEmail the invitee email
	* @param retrieveFromCache whether to use the finder cache
	* @return the matching invitation card, or <code>null</code> if a matching invitation card could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.mpower.slayer.model.InvitationCard fetchByuserId_InviteeEmail(
		long userId, java.lang.String inviteeEmail, boolean retrieveFromCache)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns all the invitation cards where userId = &#63;.
	*
	* @param userId the user ID
	* @return the matching invitation cards
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.mpower.slayer.model.InvitationCard> findByuserId(
		long userId) throws com.liferay.portal.kernel.exception.SystemException;

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
	public java.util.List<com.mpower.slayer.model.InvitationCard> findByuserId(
		long userId, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public java.util.List<com.mpower.slayer.model.InvitationCard> findByuserId(
		long userId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the first invitation card in the ordered set where userId = &#63;.
	*
	* @param userId the user ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching invitation card
	* @throws com.mpower.slayer.NoSuchInvitationCardException if a matching invitation card could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.mpower.slayer.model.InvitationCard findByuserId_First(
		long userId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException;

	/**
	* Returns the first invitation card in the ordered set where userId = &#63;.
	*
	* @param userId the user ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching invitation card, or <code>null</code> if a matching invitation card could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.mpower.slayer.model.InvitationCard fetchByuserId_First(
		long userId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the last invitation card in the ordered set where userId = &#63;.
	*
	* @param userId the user ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching invitation card
	* @throws com.mpower.slayer.NoSuchInvitationCardException if a matching invitation card could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.mpower.slayer.model.InvitationCard findByuserId_Last(
		long userId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException;

	/**
	* Returns the last invitation card in the ordered set where userId = &#63;.
	*
	* @param userId the user ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching invitation card, or <code>null</code> if a matching invitation card could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.mpower.slayer.model.InvitationCard fetchByuserId_Last(
		long userId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public com.mpower.slayer.model.InvitationCard[] findByuserId_PrevAndNext(
		long invitationId, long userId,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException;

	/**
	* Returns all the invitation cards where userId = &#63; and status = &#63;.
	*
	* @param userId the user ID
	* @param status the status
	* @return the matching invitation cards
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.mpower.slayer.model.InvitationCard> findByuserId_Status(
		long userId, int status)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public java.util.List<com.mpower.slayer.model.InvitationCard> findByuserId_Status(
		long userId, int status, int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public java.util.List<com.mpower.slayer.model.InvitationCard> findByuserId_Status(
		long userId, int status, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public com.mpower.slayer.model.InvitationCard findByuserId_Status_First(
		long userId, int status,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException;

	/**
	* Returns the first invitation card in the ordered set where userId = &#63; and status = &#63;.
	*
	* @param userId the user ID
	* @param status the status
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching invitation card, or <code>null</code> if a matching invitation card could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.mpower.slayer.model.InvitationCard fetchByuserId_Status_First(
		long userId, int status,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public com.mpower.slayer.model.InvitationCard findByuserId_Status_Last(
		long userId, int status,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException;

	/**
	* Returns the last invitation card in the ordered set where userId = &#63; and status = &#63;.
	*
	* @param userId the user ID
	* @param status the status
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching invitation card, or <code>null</code> if a matching invitation card could not be found
	* @throws SystemException if a system exception occurred
	*/
	public com.mpower.slayer.model.InvitationCard fetchByuserId_Status_Last(
		long userId, int status,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public com.mpower.slayer.model.InvitationCard[] findByuserId_Status_PrevAndNext(
		long invitationId, long userId, int status,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException;

	/**
	* Returns all the invitation cards.
	*
	* @return the invitation cards
	* @throws SystemException if a system exception occurred
	*/
	public java.util.List<com.mpower.slayer.model.InvitationCard> findAll()
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public java.util.List<com.mpower.slayer.model.InvitationCard> findAll(
		int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException;

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
	public java.util.List<com.mpower.slayer.model.InvitationCard> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Removes all the invitation cards where status = &#63; from the database.
	*
	* @param status the status
	* @throws SystemException if a system exception occurred
	*/
	public void removeByfindByStatus(int status)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Removes the invitation card where userId = &#63; and inviteeEmail = &#63; from the database.
	*
	* @param userId the user ID
	* @param inviteeEmail the invitee email
	* @return the invitation card that was removed
	* @throws SystemException if a system exception occurred
	*/
	public com.mpower.slayer.model.InvitationCard removeByuserId_InviteeEmail(
		long userId, java.lang.String inviteeEmail)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException;

	/**
	* Removes all the invitation cards where userId = &#63; from the database.
	*
	* @param userId the user ID
	* @throws SystemException if a system exception occurred
	*/
	public void removeByuserId(long userId)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Removes all the invitation cards where userId = &#63; and status = &#63; from the database.
	*
	* @param userId the user ID
	* @param status the status
	* @throws SystemException if a system exception occurred
	*/
	public void removeByuserId_Status(long userId, int status)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Removes all the invitation cards from the database.
	*
	* @throws SystemException if a system exception occurred
	*/
	public void removeAll()
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of invitation cards where status = &#63;.
	*
	* @param status the status
	* @return the number of matching invitation cards
	* @throws SystemException if a system exception occurred
	*/
	public int countByfindByStatus(int status)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of invitation cards where userId = &#63; and inviteeEmail = &#63;.
	*
	* @param userId the user ID
	* @param inviteeEmail the invitee email
	* @return the number of matching invitation cards
	* @throws SystemException if a system exception occurred
	*/
	public int countByuserId_InviteeEmail(long userId,
		java.lang.String inviteeEmail)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of invitation cards where userId = &#63;.
	*
	* @param userId the user ID
	* @return the number of matching invitation cards
	* @throws SystemException if a system exception occurred
	*/
	public int countByuserId(long userId)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of invitation cards where userId = &#63; and status = &#63;.
	*
	* @param userId the user ID
	* @param status the status
	* @return the number of matching invitation cards
	* @throws SystemException if a system exception occurred
	*/
	public int countByuserId_Status(long userId, int status)
		throws com.liferay.portal.kernel.exception.SystemException;

	/**
	* Returns the number of invitation cards.
	*
	* @return the number of invitation cards
	* @throws SystemException if a system exception occurred
	*/
	public int countAll()
		throws com.liferay.portal.kernel.exception.SystemException;
}