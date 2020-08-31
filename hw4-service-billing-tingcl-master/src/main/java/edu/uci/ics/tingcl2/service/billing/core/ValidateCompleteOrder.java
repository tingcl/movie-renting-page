package edu.uci.ics.tingcl2.service.billing.core;

import edu.uci.ics.tingcl2.service.billing.models.CompleteOrderModel;
import edu.uci.ics.tingcl2.service.billing.models.PayPalClient;

public class ValidateCompleteOrder {
    public static CompleteOrderModel logicalHandler(String paymentId, String token, String PayerID){
        String transactionId;
        if(!BillingRecords.tokenFound(token))
            return new CompleteOrderModel(3421);
        if((transactionId = PayPalClient.executePayment(paymentId, PayerID)) == null){
            return new CompleteOrderModel(3422);
        }
        BillingRecords.updateTransactionId(transactionId, token);
        return new CompleteOrderModel(3420);
    }
}
