package com.inikah.payment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import urn.ebay.api.PayPalAPI.PayPalAPIInterfaceServiceService;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutReq;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutRequestType;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutResponseType;
import urn.ebay.apis.CoreComponentTypes.BasicAmountType;
import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.ItemCategoryType;
import urn.ebay.apis.eBLBaseComponents.PaymentActionCodeType;
import urn.ebay.apis.eBLBaseComponents.PaymentDetailsItemType;
import urn.ebay.apis.eBLBaseComponents.PaymentDetailsType;
import urn.ebay.apis.eBLBaseComponents.SetExpressCheckoutRequestDetailsType;

import com.inikah.slayer.service.SysConfigLocalServiceUtil;
import com.inikah.util.SysConfigConstants;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.PortletURLFactoryUtil;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.core.rest.APIContext;
import com.paypal.core.rest.OAuthTokenCredential;
import com.paypal.core.rest.PayPalRESTException;
import com.paypal.exception.ClientActionRequiredException;
import com.paypal.exception.HttpErrorException;
import com.paypal.exception.InvalidCredentialException;
import com.paypal.exception.InvalidResponseDataException;
import com.paypal.exception.MissingCredentialException;
import com.paypal.exception.SSLConfigurationException;
import com.paypal.sdk.exceptions.OAuthException;

public class PayPalUtil {
	public static String getCheckoutURL2(HttpServletRequest request, long plid, double itemAmount, String cancelURL) {
		
		String token = null;
		
		String paypalEnvironment = SysConfigLocalServiceUtil.getValue(SysConfigConstants.PAYPAL_ENVIRONMENT);
		boolean sandbox = paypalEnvironment.equals(SysConfigConstants.PAYPAL_ENVIRONMENT_SANDBOX);
		
		PaymentDetailsType paymentDetails = new PaymentDetailsType();
		paymentDetails.setPaymentAction(PaymentActionCodeType.fromValue("Sale"));
		PaymentDetailsItemType item = new PaymentDetailsItemType();
		BasicAmountType amt = new BasicAmountType();
		amt.setCurrencyID(CurrencyCodeType.fromValue("USD"));
		amt.setValue(String.valueOf(itemAmount));
		int itemQuantity = 1;
		item.setQuantity(itemQuantity);
		item.setName("item");
		item.setAmount(amt);
		
		item.setItemCategory(ItemCategoryType.DIGITAL);
		item.setDescription("This is the description");
			
		List<PaymentDetailsItemType> lineItems = new ArrayList<PaymentDetailsItemType>();
		lineItems.add(item);
		paymentDetails.setPaymentDetailsItem(lineItems);
		BasicAmountType orderTotal = new BasicAmountType();
		orderTotal.setCurrencyID(CurrencyCodeType.fromValue("USD"));
		orderTotal.setValue(String.valueOf(itemAmount * itemQuantity)); 
		paymentDetails.setOrderTotal(orderTotal);
		List<PaymentDetailsType> paymentDetailsList = new ArrayList<PaymentDetailsType>();
		paymentDetailsList.add(paymentDetails);

		SetExpressCheckoutRequestDetailsType setExpressCheckoutRequestDetails = new SetExpressCheckoutRequestDetailsType();
		
		PortletURL returnURL = PortletURLFactoryUtil.create(request, "payment_WAR_inikahportlet", plid, PortletRequest.RENDER_PHASE);
		returnURL.setParameter("jspPage", "/html/payment/thanks.jsp");
		
		setExpressCheckoutRequestDetails.setReturnURL(returnURL.toString());
		
		if (sandbox) {
			cancelURL = "https://devtools-paypal.com/minibrowser.html?cancel=true";
		}
		setExpressCheckoutRequestDetails.setCancelURL(cancelURL);

		setExpressCheckoutRequestDetails.setPaymentDetails(paymentDetailsList);

		SetExpressCheckoutRequestType setExpressCheckoutRequest = new SetExpressCheckoutRequestType(setExpressCheckoutRequestDetails);
		setExpressCheckoutRequest.setVersion("104.0");

		SetExpressCheckoutReq setExpressCheckoutReq = new SetExpressCheckoutReq();
		setExpressCheckoutReq.setSetExpressCheckoutRequest(setExpressCheckoutRequest);

		Map<String, String> sdkConfig = new HashMap<String, String>();
		
		sdkConfig.put("mode", paypalEnvironment);
		sdkConfig.put("acct1.UserName", 
				SysConfigLocalServiceUtil.getValue(paypalEnvironment + StringPool.PERIOD + SysConfigConstants.PAYPAL_MERCHANT_USERNAME));
		sdkConfig.put("acct1.Password", 
				SysConfigLocalServiceUtil.getValue(paypalEnvironment + StringPool.PERIOD + SysConfigConstants.PAYPAL_MERCHANT_PASSWORD));
		sdkConfig.put("acct1.Signature",
				SysConfigLocalServiceUtil.getValue(paypalEnvironment + StringPool.PERIOD + SysConfigConstants.PAYPAL_MERCHANT_SIGNATURE));
		
		PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(sdkConfig);
		try {
			SetExpressCheckoutResponseType setExpressCheckoutResponse = service.setExpressCheckout(setExpressCheckoutReq);
			
			token = setExpressCheckoutResponse.getToken();
		} catch (SSLConfigurationException e) {
			e.printStackTrace();
		} catch (InvalidCredentialException e) {
			e.printStackTrace();
		} catch (HttpErrorException e) {
			e.printStackTrace();
		} catch (InvalidResponseDataException e) {
			e.printStackTrace();
		} catch (ClientActionRequiredException e) {
			e.printStackTrace();
		} catch (MissingCredentialException e) {
			e.printStackTrace();
		} catch (OAuthException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		String baseURL = "https://www.paypal.com/incontext?token=";
		
		if (sandbox) {
			baseURL = "https://www.sandbox.paypal.com/incontext?token=";
		}
		
		return  baseURL + token;
	}
	
	public static String getCheckoutURL1(HttpServletRequest request, long plid, double itemAmount, String cancelURL) {
		String href = StringPool.BLANK;
		String paypalEnvironment = SysConfigLocalServiceUtil.getValue(SysConfigConstants.PAYPAL_ENVIRONMENT);
		
		boolean sandbox = paypalEnvironment.equals(SysConfigConstants.PAYPAL_ENVIRONMENT_SANDBOX);
		
		Map<String, String> sdkConfig = new HashMap<String, String>();
		sdkConfig.put("mode", paypalEnvironment);

		String accessToken = StringPool.BLANK;
		try {
			String oauthClientId = SysConfigLocalServiceUtil.getValue(paypalEnvironment + StringPool.PERIOD + SysConfigConstants.PAYPAL_OAUTH_CLIENTID);
			String oauthSecret = SysConfigLocalServiceUtil.getValue(paypalEnvironment + StringPool.PERIOD + SysConfigConstants.PAYPAL_OAUTH_SECRET);
			accessToken = new OAuthTokenCredential(oauthClientId, oauthSecret, sdkConfig).getAccessToken();
		} catch (PayPalRESTException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNotNull(accessToken)) {
			APIContext apiContext = new APIContext(accessToken);
			apiContext.setConfigurationMap(sdkConfig);

			Amount amount = new Amount();
			amount.setCurrency("USD");
			amount.setTotal("1");

			Transaction transaction = new Transaction();
			transaction.setDescription("creating a payment");
			transaction.setAmount(amount);

			List<Transaction> transactions = new ArrayList<Transaction>();
			transactions.add(transaction);

			Payer payer = new Payer();
			payer.setPaymentMethod("paypal");

			Payment payment = new Payment();
			payment.setIntent("sale");
			payment.setPayer(payer);
			payment.setTransactions(transactions);
			RedirectUrls redirectUrls = new RedirectUrls();
			
			if (sandbox) {
				cancelURL = "https://devtools-paypal.com/minibrowser.html?cancel=true";
			}
			redirectUrls.setCancelUrl(cancelURL);
			
			PortletURL returnURL = PortletURLFactoryUtil.create(request, "payment_WAR_inikahportlet", plid, PortletRequest.RENDER_PHASE);
			returnURL.setParameter("jspPage", "/html/payment/thanks.jsp");
			
			redirectUrls.setReturnUrl(returnURL.toString());
			payment.setRedirectUrls(redirectUrls);

			try {
				Payment createdPayment = payment.create(apiContext);
				
				try {
					JSONObject jsonObject = JSONFactoryUtil.createJSONObject(createdPayment.toJSON());
					
					href = jsonObject.getJSONArray("links").getJSONObject(1).getString("href");					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (PayPalRESTException e) {
				e.printStackTrace();
			}
		}
	
		return href;
	}
}