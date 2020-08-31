package edu.uci.ics.tingcl2.service.billing.core;

import edu.uci.ics.tingcl2.service.billing.models.CustomerModel;

import edu.uci.ics.tingcl2.service.billing.models.RetreiveCustomerModel;
import edu.uci.ics.tingcl2.service.billing.models.RetrieveCustomerRequestModel;

public class ValidateRetrieveCustomer {
    public static RetreiveCustomerModel logicalHandler(RetrieveCustomerRequestModel requestModel){
        if(!BillingRecords.customerAlready(requestModel.getEmail()))
            return new RetreiveCustomerModel(332, null);
        CustomerModel customer = BillingRecords.getCustomerInfo(requestModel);
        return new RetreiveCustomerModel(3320, customer);
    }
}
