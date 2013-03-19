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

package com.mpower.slayer.service;

import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.kernel.util.ReferenceRegistry;
import com.liferay.portal.service.InvokableLocalService;

/**
 * The utility for the invitation card local service. This utility wraps {@link com.mpower.slayer.service.impl.InvitationCardLocalServiceImpl} and is the primary access point for service operations in application layer code running on the local server.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see InvitationCardLocalService
 * @see com.mpower.slayer.service.base.InvitationCardLocalServiceBaseImpl
 * @see com.mpower.slayer.service.impl.InvitationCardLocalServiceImpl
 * @generated
 */
public class InvitationCardLocalServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.mpower.slayer.service.impl.InvitationCardLocalServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	* Adds the invitation card to the database. Also notifies the appropriate model listeners.
	*
	* @param invitationCard the invitation card
	* @return the invitation card that was added
	* @throws SystemException if a system exception occurred
	*/
	public static com.mpower.slayer.model.InvitationCard addInvitationCard(
		com.mpower.slayer.model.InvitationCard invitationCard)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().addInvitationCard(invitationCard);
	}

	/**
	* Creates a new invitation card with the primary key. Does not add the invitation card to the database.
	*
	* @param invitationId the primary key for the new invitation card
	* @return the new invitation card
	*/
	public static com.mpower.slayer.model.InvitationCard createInvitationCard(
		long invitationId) {
		return getService().createInvitationCard(invitationId);
	}

	/**
	* Deletes the invitation card with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param invitationId the primary key of the invitation card
	* @return the invitation card that was removed
	* @throws PortalException if a invitation card with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.mpower.slayer.model.InvitationCard deleteInvitationCard(
		long invitationId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService().deleteInvitationCard(invitationId);
	}

	/**
	* Deletes the invitation card from the database. Also notifies the appropriate model listeners.
	*
	* @param invitationCard the invitation card
	* @return the invitation card that was removed
	* @throws SystemException if a system exception occurred
	*/
	public static com.mpower.slayer.model.InvitationCard deleteInvitationCard(
		com.mpower.slayer.model.InvitationCard invitationCard)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().deleteInvitationCard(invitationCard);
	}

	public static com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return getService().dynamicQuery();
	}

	/**
	* Performs a dynamic query on the database and returns the matching rows.
	*
	* @param dynamicQuery the dynamic query
	* @return the matching rows
	* @throws SystemException if a system exception occurred
	*/
	@SuppressWarnings("rawtypes")
	public static java.util.List dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().dynamicQuery(dynamicQuery);
	}

	/**
	* Performs a dynamic query on the database and returns a range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @return the range of matching rows
	* @throws SystemException if a system exception occurred
	*/
	@SuppressWarnings("rawtypes")
	public static java.util.List dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) throws com.liferay.portal.kernel.exception.SystemException {
		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	/**
	* Performs a dynamic query on the database and returns an ordered range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching rows
	* @throws SystemException if a system exception occurred
	*/
	@SuppressWarnings("rawtypes")
	public static java.util.List dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService()
				   .dynamicQuery(dynamicQuery, start, end, orderByComparator);
	}

	/**
	* Returns the number of rows that match the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @return the number of rows that match the dynamic query
	* @throws SystemException if a system exception occurred
	*/
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().dynamicQueryCount(dynamicQuery);
	}

	public static com.mpower.slayer.model.InvitationCard fetchInvitationCard(
		long invitationId)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().fetchInvitationCard(invitationId);
	}

	/**
	* Returns the invitation card with the primary key.
	*
	* @param invitationId the primary key of the invitation card
	* @return the invitation card
	* @throws PortalException if a invitation card with the primary key could not be found
	* @throws SystemException if a system exception occurred
	*/
	public static com.mpower.slayer.model.InvitationCard getInvitationCard(
		long invitationId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService().getInvitationCard(invitationId);
	}

	public static com.liferay.portal.model.PersistedModel getPersistedModel(
		java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService().getPersistedModel(primaryKeyObj);
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
	public static java.util.List<com.mpower.slayer.model.InvitationCard> getInvitationCards(
		int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().getInvitationCards(start, end);
	}

	/**
	* Returns the number of invitation cards.
	*
	* @return the number of invitation cards
	* @throws SystemException if a system exception occurred
	*/
	public static int getInvitationCardsCount()
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().getInvitationCardsCount();
	}

	/**
	* Updates the invitation card in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param invitationCard the invitation card
	* @return the invitation card that was updated
	* @throws SystemException if a system exception occurred
	*/
	public static com.mpower.slayer.model.InvitationCard updateInvitationCard(
		com.mpower.slayer.model.InvitationCard invitationCard)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().updateInvitationCard(invitationCard);
	}

	/**
	* Updates the invitation card in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param invitationCard the invitation card
	* @param merge whether to merge the invitation card with the current session. See {@link com.liferay.portal.service.persistence.BatchSession#update(com.liferay.portal.kernel.dao.orm.Session, com.liferay.portal.model.BaseModel, boolean)} for an explanation.
	* @return the invitation card that was updated
	* @throws SystemException if a system exception occurred
	*/
	public static com.mpower.slayer.model.InvitationCard updateInvitationCard(
		com.mpower.slayer.model.InvitationCard invitationCard, boolean merge)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().updateInvitationCard(invitationCard, merge);
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	public static java.lang.String getBeanIdentifier() {
		return getService().getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	public static void setBeanIdentifier(java.lang.String beanIdentifier) {
		getService().setBeanIdentifier(beanIdentifier);
	}

	public static java.lang.Object invokeMethod(java.lang.String name,
		java.lang.String[] parameterTypes, java.lang.Object[] arguments)
		throws java.lang.Throwable {
		return getService().invokeMethod(name, parameterTypes, arguments);
	}

	public static boolean notAnExistingUser(java.lang.String emailAdress,
		javax.portlet.ActionRequest actionrequest) {
		return getService().notAnExistingUser(emailAdress, actionrequest);
	}

	public static boolean notAlreadyInvited(java.lang.String emailAdress,
		javax.portlet.ActionRequest actionrequest) {
		return getService().notAlreadyInvited(emailAdress, actionrequest);
	}

	public static com.mpower.slayer.model.InvitationCard findByUserIdAndInviteeEmail(
		long userId, java.lang.String inviteeEmail)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException {
		return getService().findByUserIdAndInviteeEmail(userId, inviteeEmail);
	}

	public static java.util.List<com.mpower.slayer.model.InvitationCard> findbystatus(
		int status) throws com.liferay.portal.kernel.exception.SystemException {
		return getService().findbystatus(status);
	}

	public static java.util.List<com.mpower.slayer.model.InvitationCard> findByUserIdSatus(
		long userId, int status)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().findByUserIdSatus(userId, status);
	}

	public static java.util.List<com.mpower.slayer.model.UserRank> findByCount()
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().findByCount();
	}

	public static com.mpower.slayer.model.InvitationCard userId(long userId,
		java.lang.String inviteeEmail)
		throws com.liferay.portal.kernel.exception.SystemException,
			com.mpower.slayer.NoSuchInvitationCardException {
		return getService().userId(userId, inviteeEmail);
	}

	public static com.mpower.slayer.model.InvitationCard invitationAccepted(
		long inviteeId, java.lang.String emailAddress)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().invitationAccepted(inviteeId, emailAddress);
	}

	public static void clearService() {
		_service = null;
	}

	public static InvitationCardLocalService getService() {
		if (_service == null) {
			InvokableLocalService invokableLocalService = (InvokableLocalService)PortletBeanLocatorUtil.locate(ClpSerializer.getServletContextName(),
					InvitationCardLocalService.class.getName());

			if (invokableLocalService instanceof InvitationCardLocalService) {
				_service = (InvitationCardLocalService)invokableLocalService;
			}
			else {
				_service = new InvitationCardLocalServiceClp(invokableLocalService);
			}

			ReferenceRegistry.registerReference(InvitationCardLocalServiceUtil.class,
				"_service");
		}

		return _service;
	}

	/**
	 * @deprecated
	 */
	public void setService(InvitationCardLocalService service) {
	}

	private static InvitationCardLocalService _service;
}