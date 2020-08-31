package edu.uci.ics.tingcl2.service.billing.core;

import edu.uci.ics.tingcl2.service.billing.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.billing.models.OrderModel;
import edu.uci.ics.tingcl2.service.billing.models.OrderRequestModel;
import edu.uci.ics.tingcl2.service.billing.models.PayPalClient;

public class ValidateOrder {
    public static OrderModel logicalHandler(OrderRequestModel requestModel){
        if(!BillingRecords.customerAlready(requestModel.getEmail()))
            return new OrderModel(332, null, null);
        if(!BillingRecords.emailHasCartItems(requestModel.getEmail()))
            return new OrderModel(341, null, null);
        // creating Payment object
        String sum = Float.toString(BillingRecords.getSum(requestModel.getEmail()));
        String redirectUrl = PayPalClient.createPayment(sum);
        if(redirectUrl == null)
            return new OrderModel(342, null, null);
        ServiceLogger.LOGGER.info("Stuck.");
        ServiceLogger.LOGGER.info("Redirect url: " + redirectUrl);
        String token = redirectUrl.substring(redirectUrl.length() - 20);
        ServiceLogger.LOGGER.info("Token: " + token);
        BillingRecords.checkoutCart(requestModel, token);
        return new OrderModel(3400, redirectUrl, token);
    }
}
