package com.inikah.payment;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

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
import com.liferay.portal.kernel.io.unsync.UnsyncBufferedReader;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.UnsyncPrintWriterPool;
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
	
	public void sendToPaypal(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {

		String invoice = null;

		String query = "cmd=_notify-validate";

		Enumeration<String> enu = actionRequest.getParameterNames();

		while (enu.hasMoreElements()) {
			String name = enu.nextElement();

			String value = actionRequest.getParameter(name);

			query = query + "&" + name + "=" + HttpUtil.encodeURL(value);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Sending response to PayPal " + query);
		}

		URL url = new URL("https://www.paypal.com/cgi-bin/webscr");

		URLConnection urlc = url.openConnection();

		urlc.setDoOutput(true);
		urlc.setRequestProperty(
			"Content-Type","application/x-www-form-urlencoded");

		PrintWriter pw = UnsyncPrintWriterPool.borrow(
			urlc.getOutputStream());

		pw.println(query);
		
		System.out.println("query ==> " + query);

		pw.close();

		UnsyncBufferedReader unsyncBufferedReader =
			new UnsyncBufferedReader(
				new InputStreamReader(urlc.getInputStream()));

		String payPalStatus = unsyncBufferedReader.readLine();
		
		System.out.println("payPalStatus ==> " + payPalStatus);

		unsyncBufferedReader.close();

		String itemName = ParamUtil.getString(actionRequest, "item_name");
		String itemNumber = ParamUtil.getString(actionRequest, "item_number");
		invoice = ParamUtil.getString(actionRequest, "invoice");
		String txnId = ParamUtil.getString(actionRequest, "txn_id");
		String paymentStatus = ParamUtil.getString(
				actionRequest, "payment_status");
		double paymentGross = ParamUtil.getDouble(actionRequest, "mc_gross");
		String receiverEmail = ParamUtil.getString(
				actionRequest, "receiver_email");
		String payerEmail = ParamUtil.getString(actionRequest, "payer_email");

		if (_log.isDebugEnabled()) {
			_log.debug("Receiving response from PayPal");
			_log.debug("Item name " + itemName);
			_log.debug("Item number " + itemNumber);
			_log.debug("Invoice " + invoice);
			_log.debug("Transaction ID " + txnId);
			_log.debug("Payment status " + paymentStatus);
			_log.debug("Payment gross " + paymentGross);
			_log.debug("Receiver email " + receiverEmail);
			_log.debug("Payer email " + payerEmail);
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		PaymentPortlet.class);

}