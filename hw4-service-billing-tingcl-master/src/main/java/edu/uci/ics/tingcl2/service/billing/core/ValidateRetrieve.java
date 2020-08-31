package edu.uci.ics.tingcl2.service.billing.core;

import edu.uci.ics.tingcl2.service.billing.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.billing.models.CartModel;
import edu.uci.ics.tingcl2.service.billing.models.RetrieveModel;
import edu.uci.ics.tingcl2.service.billing.models.RetrieveRequestModel;

public class ValidateRetrieve {
    public static RetrieveModel logicalHandler(RetrieveRequestModel requestModel){
        ServiceLogger.LOGGER.info("Checking provided email for correctness");
        if(requestModel.getEmail().length() > 50)
            return new RetrieveModel(-10, null);
        if(!requestModel.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"))
            return new RetrieveModel(-11, null);
        ServiceLogger.LOGGER.info("Deep checking for logical correctness.");
        if(!BillingRecords.emailHasCartItems(requestModel.getEmail()))
            return new RetrieveModel(312, null);
        CartModel[] items = BillingRecords.listUserCart(requestModel.getEmail());
        return new RetrieveModel(3130, items);
    }
}
