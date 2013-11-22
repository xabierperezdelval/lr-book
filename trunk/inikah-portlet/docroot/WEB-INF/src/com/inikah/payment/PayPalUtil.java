package com.inikah.payment;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import urn.ebay.api.PayPalAPI.DoExpressCheckoutPaymentReq;
import urn.ebay.api.PayPalAPI.DoExpressCheckoutPaymentRequestType;
import urn.ebay.api.PayPalAPI.DoExpressCheckoutPaymentResponseType;
import urn.ebay.api.PayPalAPI.PayPalAPIInterfaceServiceService;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutReq;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutRequestType;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutResponseType;
import urn.ebay.apis.CoreComponentTypes.BasicAmountType;
import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.DoExpressCheckoutPaymentRequestDetailsType;
import urn.ebay.apis.eBLBaseComponents.ItemCategoryType;
import urn.ebay.apis.eBLBaseComponents.PaymentActionCodeType;
import urn.ebay.apis.eBLBaseComponents.PaymentDetailsItemType;
import urn.ebay.apis.eBLBaseComponents.PaymentDetailsType;
import urn.ebay.apis.eBLBaseComponents.SetExpressCheckoutRequestDetailsType;

import com.inikah.slayer.service.ConfigServiceUtil;
import com.inikah.util.ConfigConstants;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.PortletURLFactoryUtil;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
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
	public static String getCheckoutURL(PortletRequest portletRequest, long plid) {
		
		String token = null;
				
		PortletSession portletSession = portletRequest.getPortletSession();
		double itemAmount = GetterUtil.getDouble(portletSession.getAttribute("FINAL_AMOUNT"));
		
		// add paypal charges
		itemAmount += (0.044 * itemAmount) + 0.30d;
		
		String paypalEnvironment = ConfigServiceUtil.get(ConfigConstants.PAYPAL_ENVIRONMENT);
		boolean sandbox = paypalEnvironment.equalsIgnoreCase(ConfigConstants.PAYPAL_ENVIRONMENT_SANDBOX);
		
		PaymentDetailsType paymentDetails = new PaymentDetailsType();
		paymentDetails.setPaymentAction(PaymentActionCodeType.fromValue("Sale"));
		
		PaymentDetailsItemType item = new PaymentDetailsItemType();
		BasicAmountType amt = new BasicAmountType();
		amt.setCurrencyID(CurrencyCodeType.fromValue("USD"));
		
		String amountRounded = (new DecimalFormat("0.00")).format(itemAmount);
		
		amt.setValue(amountRounded);
		int itemQuantity = 1;
		item.setQuantity(itemQuantity);
		item.setName("Profile - Gold Plan");
		item.setAmount(amt);
		item.setItemCategory(ItemCategoryType.DIGITAL);
		item.setDescription("For the subscription of profile");
			
		List<PaymentDetailsItemType> lineItems = new ArrayList<PaymentDetailsItemType>();
		lineItems.add(item);
		paymentDetails.setPaymentDetailsItem(lineItems);
		
		BasicAmountType orderTotal = new BasicAmountType();
		orderTotal.setCurrencyID(CurrencyCodeType.fromValue("USD"));
		orderTotal.setValue(amountRounded); 
		paymentDetails.setOrderTotal(orderTotal);
		
		List<PaymentDetailsType> paymentDetailsList = new ArrayList<PaymentDetailsType>();
		paymentDetailsList.add(paymentDetails);

		SetExpressCheckoutRequestDetailsType setExpressCheckoutRequestDetails = new SetExpressCheckoutRequestDetailsType();
		
		PortletURL returnURL = PortletURLFactoryUtil.create(portletRequest, "payment_WAR_inikahportlet", plid, PortletRequest.ACTION_PHASE);
		returnURL.setParameter(ActionRequest.ACTION_NAME, "paypalComplete");
		
		try {
			returnURL.setPortletMode(PortletMode.VIEW);
		} catch (PortletModeException e) {
			e.printStackTrace();
		}
		try {
			returnURL.setWindowState(LiferayWindowState.EXCLUSIVE);
		} catch (WindowStateException e) {
			e.printStackTrace();
		}		
		
		setExpressCheckoutRequestDetails.setReturnURL(returnURL.toString());
		
		String cancelURL = "https://devtools-paypal.com/minibrowser.html?cancel=true";
		setExpressCheckoutRequestDetails.setCancelURL(cancelURL);

		setExpressCheckoutRequestDetails.setPaymentDetails(paymentDetailsList);

		SetExpressCheckoutRequestType setExpressCheckoutRequest = new SetExpressCheckoutRequestType(setExpressCheckoutRequestDetails);
		setExpressCheckoutRequest.setVersion("104.0");

		SetExpressCheckoutReq setExpressCheckoutReq = new SetExpressCheckoutReq();
		setExpressCheckoutReq.setSetExpressCheckoutRequest(setExpressCheckoutRequest);

		
		PayPalAPIInterfaceServiceService service = getInterfaceService();
		
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
	
	public static String executePayment(String token, String payerId, double amount) {
		
		String acknowlegment = StringPool.BLANK;
		
		amount += (0.044 * amount) + 0.30d;
		
		PaymentDetailsType paymentDetail = new PaymentDetailsType();
		paymentDetail.setNotifyURL("http://replaceIpnUrl.com");
		BasicAmountType orderTotal = new BasicAmountType();
		
		orderTotal.setValue(String.format("%.2f", amount));
		orderTotal.setCurrencyID(CurrencyCodeType.fromValue("USD"));
		paymentDetail.setOrderTotal(orderTotal);
		paymentDetail.setPaymentAction(PaymentActionCodeType.fromValue("Sale"));
		List<PaymentDetailsType> paymentDetails = new ArrayList<PaymentDetailsType>();
		paymentDetails.add(paymentDetail);
						
		DoExpressCheckoutPaymentRequestDetailsType doExpressCheckoutPaymentRequestDetails = new DoExpressCheckoutPaymentRequestDetailsType();
		doExpressCheckoutPaymentRequestDetails.setToken(token);
		doExpressCheckoutPaymentRequestDetails.setPayerID(payerId);
		doExpressCheckoutPaymentRequestDetails.setPaymentDetails(paymentDetails);
	
		DoExpressCheckoutPaymentRequestType doExpressCheckoutPaymentRequest = new DoExpressCheckoutPaymentRequestType(doExpressCheckoutPaymentRequestDetails);
		doExpressCheckoutPaymentRequest.setVersion("104.0");
	
		DoExpressCheckoutPaymentReq doExpressCheckoutPaymentReq = new DoExpressCheckoutPaymentReq();
		doExpressCheckoutPaymentReq.setDoExpressCheckoutPaymentRequest(doExpressCheckoutPaymentRequest);
	
		PayPalAPIInterfaceServiceService service = getInterfaceService();

		try {
			DoExpressCheckoutPaymentResponseType doExpressCheckoutPaymentResponse = service.doExpressCheckoutPayment(doExpressCheckoutPaymentReq);
			acknowlegment = doExpressCheckoutPaymentResponse.getAck().toString();
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
		
		return acknowlegment;
	}

	private static PayPalAPIInterfaceServiceService getInterfaceService() {
		Map<String, String> sdkConfig = new HashMap<String, String>();
		
		String paypalEnvironment = ConfigServiceUtil.get(ConfigConstants.PAYPAL_ENVIRONMENT);
		
		sdkConfig.put("mode", paypalEnvironment);
		sdkConfig.put("acct1.UserName", 
				ConfigServiceUtil.get(paypalEnvironment + StringPool.PERIOD + ConfigConstants.PAYPAL_MERCHANT_USERNAME));
		sdkConfig.put("acct1.Password", 
				ConfigServiceUtil.get(paypalEnvironment + StringPool.PERIOD + ConfigConstants.PAYPAL_MERCHANT_PASSWORD));
		sdkConfig.put("acct1.Signature",
				ConfigServiceUtil.get(paypalEnvironment + StringPool.PERIOD + ConfigConstants.PAYPAL_MERCHANT_SIGNATURE));
		return new PayPalAPIInterfaceServiceService(sdkConfig);
	}
	
	public static String getCheckoutURL1(PortletRequest portletRequest, long plid, String cancelURL) {
		String href = StringPool.BLANK;
		String paypalEnvironment = ConfigServiceUtil.get(ConfigConstants.PAYPAL_ENVIRONMENT);
		
		System.out.println("paypalEnvironment ==> " + paypalEnvironment);
		
		PortletSession portletSession = portletRequest.getPortletSession();
		double itemAmount = GetterUtil.getDouble(portletSession.getAttribute("FINAL_AMOUNT"));		
		
		itemAmount += (0.044 * itemAmount) + 0.30d;
		
		boolean sandbox = paypalEnvironment.equals(ConfigConstants.PAYPAL_ENVIRONMENT_SANDBOX);
		
		Map<String, String> sdkConfig = new HashMap<String, String>();
		sdkConfig.put("mode", paypalEnvironment);

		String accessToken = StringPool.BLANK;
		try {
			String oauthClientId = ConfigServiceUtil.get(paypalEnvironment + StringPool.PERIOD + ConfigConstants.PAYPAL_OAUTH_CLIENTID);
			String oauthSecret = ConfigServiceUtil.get(paypalEnvironment + StringPool.PERIOD + ConfigConstants.PAYPAL_OAUTH_SECRET);
			accessToken = new OAuthTokenCredential(oauthClientId, oauthSecret, sdkConfig).getAccessToken();
		} catch (PayPalRESTException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNotNull(accessToken)) {
			APIContext apiContext = new APIContext(accessToken);
			apiContext.setConfigurationMap(sdkConfig);

			Amount amount = new Amount();
			amount.setCurrency("USD");
			
			String amountRounded = (new DecimalFormat("0.00")).format(itemAmount);
			System.out.println("amountRounded ==> " + amountRounded);
			amount.setTotal(amountRounded);

			Transaction transaction = new Transaction();
			transaction.setDescription("This is the description for the <b>profile</b>. <hr/> text after the line");
			transaction.setAmount(amount);
			
			List<Item> items = new ArrayList<Item>();
			
			Item item = new Item();
			item.setCurrency("USD");
			item.setName("This is a sample item");
			item.setPrice(amountRounded);
			item.setQuantity("1");
			item.setSku("UNIQUE");
			
			ItemList itemList = new ItemList();
			itemList.setItems(items);
			transaction.setItemList(itemList);

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
			
			PortletURL returnURL = PortletURLFactoryUtil.create(portletRequest, "payment_WAR_inikahportlet", plid, PortletRequest.ACTION_PHASE);
			returnURL.setParameter(ActionRequest.ACTION_NAME, "paypalComplete");
			returnURL.setParameter("jspPage", "/html/payment/thanks.jsp");
			
			try {
				returnURL.setPortletMode(PortletMode.VIEW);
			} catch (PortletModeException e2) {
				e2.printStackTrace();
			}
			try {
				returnURL.setWindowState(WindowState.NORMAL);
			} catch (WindowStateException e1) {
				e1.printStackTrace();
			}
			
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