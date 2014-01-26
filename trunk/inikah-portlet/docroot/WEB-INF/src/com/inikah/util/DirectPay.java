package com.inikah.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Contact;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ContactLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.CreditCard;
import com.paypal.api.payments.CreditCardToken;
import com.paypal.api.payments.FundingInstrument;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.core.rest.APIContext;
import com.paypal.core.rest.OAuthTokenCredential;
import com.paypal.core.rest.PayPalRESTException;

public class DirectPay {
	
	static Map<String, String> sdkConfig = null;
	static {
		sdkConfig = new HashMap<String, String>();
		sdkConfig.put("mode", AppConfig.get(IConstants.PAYPAL_ENVIRONMENT));
	}
	
	private static String getAccessToken() {	
		String accessToken = StringPool.BLANK;
		
		try {
			
			String ENVT = AppConfig.get(IConstants.PAYPAL_ENVIRONMENT);
			
			accessToken = new OAuthTokenCredential(
					AppConfig.get(ENVT + StringPool.PERIOD + IConstants.CFG_PAYPAL_CLIENT_ID),
					AppConfig.get(ENVT + StringPool.PERIOD + IConstants.CFG_PAYPAL_CLIENT_SECRET),
					sdkConfig).getAccessToken();
			
		} catch (PayPalRESTException e) {
			e.printStackTrace();
		}
		
		return accessToken;
	}
	
	public static String saveCreditCardToken(long userId, String type,
			String number, int expireMonth, int expireYear, String firstName,
			String lastName) {
		
		String accessToken = getAccessToken();
		
		System.out.println("accessToken ==> " + accessToken);
		
		APIContext apiContext = new APIContext(accessToken);
		apiContext.setConfigurationMap(sdkConfig);

		CreditCard creditCard = new CreditCard();
		
		creditCard.setType(type); // "visa"
		creditCard.setNumber(number); // "4446283280247004"
		creditCard.setExpireMonth(expireMonth); // 11
		creditCard.setExpireYear(expireYear); // 2018 
		creditCard.setFirstName(firstName); // "Joe"
		creditCard.setLastName(lastName); // "Shopper"

		CreditCard createdCreditCard = null;
		try {
			createdCreditCard = creditCard.create(apiContext);
		} catch (PayPalRESTException e) {
			e.printStackTrace();
		}
		
		try {
			User user = UserLocalServiceUtil.fetchUser(userId);
			Contact contact = ContactLocalServiceUtil.fetchContact(user.getContactId());
			contact.setEmployeeNumber(createdCreditCard.getId());
			
			int length = number.length();
			
			contact.setYmSn(number.substring(length-4));
			ContactLocalServiceUtil.updateContact(contact);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return accessToken;
	}	
		
	public static void chargeCreditCard(long userId, String price, String accessToken) {
		
		String creditCardId = StringPool.BLANK;
		try {
			User user = UserLocalServiceUtil.fetchUser(userId);
			
			Contact contact = ContactLocalServiceUtil.fetchContact(user.getContactId());
			
			creditCardId = contact.getEmployeeNumber();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		CreditCardToken creditCardToken = new CreditCardToken();
		creditCardToken.setCreditCardId(creditCardId);

		FundingInstrument fundingInstrument = new FundingInstrument();
		fundingInstrument.setCreditCardToken(creditCardToken);

		List<FundingInstrument> fundingInstrumentList = new ArrayList<FundingInstrument>();
		fundingInstrumentList.add(fundingInstrument);

		Payer payer = new Payer();
		payer.setFundingInstruments(fundingInstrumentList);
		payer.setPaymentMethod("credit_card");

		Amount amount = new Amount();
		amount.setCurrency("USD");
		amount.setTotal(price);

		Transaction transaction = new Transaction();
		transaction.setDescription("creating a direct payment with credit card");
		transaction.setAmount(amount);

		List<Transaction> transactions = new ArrayList<Transaction>();
		transactions.add(transaction);

		Payment payment = new Payment();
		payment.setIntent("sale");
		payment.setPayer(payer);
		payment.setTransactions(transactions);
		
		APIContext apiContext = new APIContext(getAccessToken());
		apiContext.setConfigurationMap(sdkConfig);		

		try {
			Payment createdPayment = payment.create(apiContext);
			
			System.out.println("createdPayment ==> " + createdPayment);	
		} catch (PayPalRESTException e) {
			e.printStackTrace();
		}
	}
}