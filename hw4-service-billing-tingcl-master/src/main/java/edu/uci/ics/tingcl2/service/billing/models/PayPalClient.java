package edu.uci.ics.tingcl2.service.billing.models;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import edu.uci.ics.tingcl2.service.billing.BillingService;
import edu.uci.ics.tingcl2.service.billing.logger.ServiceLogger;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class PayPalClient {
    private static String clientId = "ATfzzbeWxpPXEk76rpzvjdPaZ-ZIrRXzSygMBwhfuvca5QxIWqkSWrBYUwy7oo4bLyxClTWaHzWPHxhk";
    private static String clientSecret = "EOcSrQsIDllmGzkSnffSSLTdxhMx1AybNDK3Pp9JTFFZkc3zOB3muz4BOuav7SPIN4ANGeHQX_Cd5DXA";

    public static String createPayment(String sum) {
        ServiceLogger.LOGGER.info("Creating payment object.");
        Amount amount = new Amount("USD", sum);
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        URI uri = UriBuilder.fromUri(BillingService.getConfigs().getScheme()
                + BillingService.getConfigs().getHostName() +
                BillingService.getConfigs().getPath()).port(BillingService.getConfigs().getPort()).build();
        RedirectUrls redirectUrls = new RedirectUrls();
        ServiceLogger.LOGGER.info(uri.toString());
        redirectUrls.setCancelUrl(uri.toString() + "/order/cancel");
        redirectUrls.setReturnUrl(uri.toString() + "/order/complete");
        payment.setRedirectUrls(redirectUrls);
        Payment createdPayment;
        try {
            String redirectUrl = "";
            APIContext apiContext = new APIContext(clientId, clientSecret, "sandbox");
            createdPayment = payment.create(apiContext);
            ServiceLogger.LOGGER.info(createdPayment.toString());
            if (createdPayment != null) {
                List<Links> links = createdPayment.getLinks();
                for (Links link : links) {
                    if (link.getRel().equals("approval_url")) {
                        redirectUrl = link.getHref();
                        break;
                    }
                }
                ServiceLogger.LOGGER.info("Payment object created successfully.");
                return redirectUrl;
            }
        } catch(PayPalRESTException e){
                ServiceLogger.LOGGER.warning("Payment error occurred!");
        }
        return null;
    }
    public static String executePayment(String paymentId, String PayerID){
        String transactionId = null;
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(PayerID);
        try {
            APIContext apiContext = new APIContext(clientId, clientSecret, "sandbox");
            Payment createdPayment = payment.execute(apiContext, paymentExecution);
            System.out.println(createdPayment.toString());
            transactionId = createdPayment.getTransactions().get(0).getRelatedResources().get(0).getSale().getId();
        } catch (PayPalRESTException e) {
            System.err.println(e.getDetails());
        }
        return transactionId;
    }
    public static Sale getSale(String saleId) {
        Sale sale = null;
        try {
            APIContext apiContext = new APIContext(clientId, clientSecret, "sandbox");
            sale = Sale.get(apiContext, saleId);
            ServiceLogger.LOGGER.info(sale.toString());
        } catch (PayPalRESTException e) {
            System.err.println(e.getDetails());
        }
        return sale;
    }
}
