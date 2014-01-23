package com.inikah.payment;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;

import com.inikah.slayer.model.Payment;
import com.inikah.slayer.model.Profile;
import com.inikah.slayer.service.PaymentLocalServiceUtil;
import com.inikah.util.IConstants;
import com.inikah.util.PayPalUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class PaymentPortlet
 */
public class PaymentPortlet extends MVCPortlet {
	
	public void savePlan(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		
		long planId = ParamUtil.getLong(actionRequest, "planId", 0l);
		long profileId = ParamUtil.getLong(actionRequest, "profileId", 0l);
		
		if (planId > 0l && profileId > 0l) {
			
			Profile profile = (Profile) actionRequest.getPortletSession()
					.getAttribute("SEL_PROFILE",
							PortletSession.APPLICATION_SCOPE);
			
			PaymentLocalServiceUtil.init(profile, planId);
		}
	}
	
	public void finalPay(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {

		long paymentId = ParamUtil.getLong(actionRequest, "paymentId");
		
		Payment payment = null;
		try {
			payment = PaymentLocalServiceUtil.fetchPayment(paymentId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNotNull(payment)) {
			String paymentMode = ParamUtil.getString(actionRequest, "paymentMode");
			
			if (paymentMode.equalsIgnoreCase(IConstants.PAY_MODE_CHEQUE_DD)) {
				
			}
		}
		
		actionResponse.setRenderParameter("jspPage", "/html/payment/thanks.jsp");
	}
	
	public void paypalComplete(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		
		String token = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(actionRequest)).getParameter("token");
		String payerId = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(actionRequest)).getParameter("PayerID");
		
		PortletSession portletSession = actionRequest.getPortletSession();
		double amount = GetterUtil.getDouble(portletSession.getAttribute("FINAL_AMOUNT"));
		
		String acknowledgement = PayPalUtil.executePayment(token, payerId, amount);
		
		if (acknowledgement.equalsIgnoreCase("SUCCESS")) {
			PaymentLocalServiceUtil.reward(17222, 4, 100d);
		}
		
		actionResponse.setRenderParameter("jspPage", "/html/payment/paypal-success.jsp");
	}
}