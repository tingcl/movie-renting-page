package edu.uci.ics.tingcl2.service.billing.core;


import edu.uci.ics.tingcl2.service.billing.models.InsertCustomerModel;
import edu.uci.ics.tingcl2.service.billing.models.InsertCustomerRequestModel;

public class ValidateInsertCustomer {
    public static InsertCustomerModel logicalHandler(InsertCustomerRequestModel requestModel){
        if(requestModel.getCcId().length() < 16 || requestModel.getCcId().length() > 20)
            return new InsertCustomerModel(321);
        if(!requestModel.getCcId().matches("[0-9]+"))
            return new InsertCustomerModel(322);
        if(!BillingRecords.duplicateCardEntry(requestModel.getCcId()))
            return new InsertCustomerModel(331);
        if(BillingRecords.customerAlready(requestModel.getEmail()))
            return new InsertCustomerModel(333);
        BillingRecords.insertCustomer(requestModel);
        return new InsertCustomerModel(3300);
    }
}
