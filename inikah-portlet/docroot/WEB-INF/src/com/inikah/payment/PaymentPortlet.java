package com.inikah.payment;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.inikah.slayer.model.Payment;
import com.inikah.slayer.model.Profile;
import com.inikah.slayer.service.PaymentLocalServiceUtil;
import com.inikah.slayer.service.ProfileLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class PaymentPortlet
 */
public class PaymentPortlet extends MVCPortlet {
 
	@Override
	public void render(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {

		long profileId = ParamUtil.getLong(request, "profileId");
				
		PortletSession portletSession = request.getPortletSession();
		if (Validator.isNull(portletSession.getAttribute("PROFILE"))) {
			Profile profile = null;
			try {
				profile = ProfileLocalServiceUtil.fetchProfile(profileId);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			if (Validator.isNotNull(profile)) {
				portletSession.setAttribute("PROFILE", profile);
			}
		}
		
		super.render(request, response);
	}
	

	public void showPaymentOptions(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {

		System.out.println("show payment options....");
		
		long planId = ParamUtil.getLong(actionRequest, "planId", 0l);
		long profileId = ParamUtil.getLong(actionRequest, "profileId", 0l);
		
		long paymentId = 0;
		if (planId > 0l && profileId > 0l) {
			Payment payment = PaymentLocalServiceUtil.init(profileId, planId);
			paymentId = payment.getPaymentId();
		}
		
		actionRequest.setAttribute("paymentId", paymentId);
		actionResponse.setRenderParameter("jspPage", "/html/payment/options.jsp");
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
			
			if (paymentMode.equalsIgnoreCase(PaymentConstants.MODE_CHEQUE_DD)) {
				
			}
		}
		
		actionResponse.setRenderParameter("jspPage", "/html/payment/thanks.jsp");
	}
}