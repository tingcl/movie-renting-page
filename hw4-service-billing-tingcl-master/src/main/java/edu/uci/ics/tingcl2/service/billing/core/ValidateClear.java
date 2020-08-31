package edu.uci.ics.tingcl2.service.billing.core;

import edu.uci.ics.tingcl2.service.billing.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.billing.models.ClearModel;
import edu.uci.ics.tingcl2.service.billing.models.RetrieveRequestModel;

public class ValidateClear {
    public static ClearModel logicalHandler(RetrieveRequestModel requestModel){
        ServiceLogger.LOGGER.info("Checking provided email for correctness");
        if(requestModel.getEmail().length() > 50)
            return new ClearModel(-10);
        if(!requestModel.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"))
            return new ClearModel(-11);
        BillingRecords.clearCart(requestModel.getEmail());
        return new ClearModel(3140);
    }
}
