package com.inikah.util;

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
import javax.portlet.PortletURL;
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

import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portlet.PortletURLFactoryUtil;
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
				
		double itemAmount = ParamUtil.getDouble(portletRequest, "FINAL_PRICE");
		
		// add paypal charges
		itemAmount += (0.044 * itemAmount) + 0.30d;
		
		String paypalEnvironment = AppConfig.get(IConstants.PAYPAL_ENVIRONMENT);
		boolean sandbox = paypalEnvironment.equalsIgnoreCase(IConstants.PAYPAL_ENVIRONMENT_SANDBOX);
		
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
	 
	static PayPalAPIInterfaceServiceService service;
	
	static {
		Map<String, String> sdkConfig = new HashMap<String, String>();
		
		String paypalEnvironment = AppConfig.get(IConstants.PAYPAL_ENVIRONMENT);
				
		sdkConfig.put("mode", paypalEnvironment);
		sdkConfig.put("acct1.UserName", 
				AppConfig.get(paypalEnvironment + StringPool.PERIOD + IConstants.PAYPAL_MERCHANT_USERNAME));
		sdkConfig.put("acct1.Password", 
				AppConfig.get(paypalEnvironment + StringPool.PERIOD + IConstants.PAYPAL_MERCHANT_PASSWORD));
		sdkConfig.put("acct1.Signature",
				AppConfig.get(paypalEnvironment + StringPool.PERIOD + IConstants.PAYPAL_MERCHANT_SIGNATURE));
		service = new PayPalAPIInterfaceServiceService(sdkConfig);
		
		System.out.println("service ==> " + service);
	}	
}