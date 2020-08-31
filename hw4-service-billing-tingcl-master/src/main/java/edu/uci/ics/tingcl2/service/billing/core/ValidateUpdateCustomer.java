package edu.uci.ics.tingcl2.service.billing.core;


import edu.uci.ics.tingcl2.service.billing.models.InsertCustomerRequestModel;
import edu.uci.ics.tingcl2.service.billing.models.UpdateCustomerModel;

public class ValidateUpdateCustomer {
    public static UpdateCustomerModel logicalHandler(InsertCustomerRequestModel requestModel){
        if(requestModel.getCcId().length() < 16 || requestModel.getCcId().length() > 20)
            return new UpdateCustomerModel(321);
        if(!requestModel.getCcId().matches("[0-9]+"))
            return new UpdateCustomerModel(322);
        if(!BillingRecords.duplicateCardEntry(requestModel.getCcId()))
            return new UpdateCustomerModel(331);
        if(!BillingRecords.customerAlready(requestModel.getEmail()))
            return new UpdateCustomerModel(332);
        BillingRecords.updateCustomer(requestModel);
        return new UpdateCustomerModel(3310);
    }
}
