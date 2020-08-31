package edu.uci.ics.tingcl2.service.billing.core;

import com.paypal.api.payments.Sale;
import edu.uci.ics.tingcl2.service.billing.models.*;

import java.util.ArrayList;
import java.util.List;

public class ValidateOrderRetrieve {
    public static OrderRetrieveModel logicalHandler(OrderRequestModel requestModel){
        if(!BillingRecords.customerAlready(requestModel.getEmail()))
            return new OrderRetrieveModel(332, null);
        // list of all transaction ids related to email given
        List<String> transactionIds = BillingRecords.getTransactionId(requestModel.getEmail());
        List<TransactionModel> temp = new ArrayList<>();
        if(transactionIds.isEmpty())
            return new OrderRetrieveModel(3410, null);
        for(String id : transactionIds){
            temp.add(makeTransaction(id, requestModel.getEmail()));
        }
        TransactionModel[] response = new TransactionModel[temp.size()];
        response = temp.toArray(response);
        return new OrderRetrieveModel(3410, response);
    }
    public static TransactionModel makeTransaction(String id, String email){
        Sale info = PayPalClient.getSale(id);
        AmountModel amount = new AmountModel(info.getAmount().getTotal(), info.getAmount().getCurrency());
        BillingModel[] items = BillingRecords.listUserOrders(email, id);
        TransactionFeeModel fee = new TransactionFeeModel(info.getTransactionFee().getValue(), info.getTransactionFee().getCurrency());
        TransactionModel model = new TransactionModel(info.getId(), info.getState(), amount, fee,
                info.getCreateTime(), info.getUpdateTime(), items);
        //TransactionModel model = new TransactionModel(items, info.getId(), info.getState(), amount, fee,
        //        info.getCreateTime(), info.getUpdateTime());
        return model;
    }
}
