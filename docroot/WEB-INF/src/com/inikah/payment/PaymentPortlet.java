package com.inikah.payment;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import com.inikah.slayer.model.Payment;
import com.inikah.slayer.service.PaymentLocalServiceUtil;
import com.inikah.util.IConstants;
import com.inikah.util.PayPalUtil;
import com.liferay.portal.kernel.exception.SystemException;
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
			PaymentLocalServiceUtil.init(profileId, planId);
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
			//String paymentMode = ParamUtil.getString(actionRequest, "paymentMode");
		}
		
		actionResponse.setRenderParameter("jspPage", "/html/payment/thanks.jsp");
	}
	
	public void paypalComplete(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		
		long paymentId = ParamUtil.getLong(actionRequest, "paymentId");
		
		if (paymentId == 0l) return;
		
		Payment payment = null;
		try {
			payment = PaymentLocalServiceUtil.fetchPayment(paymentId);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(payment)) return;
		
		String token = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(actionRequest)).getParameter("token");
		String payerId = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(actionRequest)).getParameter("PayerID");
		String acknowledgement = PayPalUtil.executePayment(token, payerId, payment.getAmount());
		
		if (acknowledgement.equalsIgnoreCase("SUCCESS")) {
	
			StringBuilder sb = new StringBuilder();
			sb.append("TOKEN: ").append(token);
			sb.append("##").append("PAYER-ID:").append(payerId);
			
			long userId = PortalUtil.getUserId(actionRequest);
			
			PaymentLocalServiceUtil.paymentDone(userId, paymentId, IConstants.PAYMODE_PAYPAL_SIGNIN, sb.toString());
			actionResponse.setRenderParameter("jspPage", "/html/payment/paypal-success.jsp");
		} else {
			// forward to another place. 
		}
	}
}