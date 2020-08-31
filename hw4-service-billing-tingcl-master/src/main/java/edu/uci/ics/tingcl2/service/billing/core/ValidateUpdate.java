package edu.uci.ics.tingcl2.service.billing.core;

import edu.uci.ics.tingcl2.service.billing.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.billing.models.InsertRequestModel;
import edu.uci.ics.tingcl2.service.billing.models.UpdateModel;

public class ValidateUpdate {
    public static UpdateModel logicalHandler(InsertRequestModel requestModel){
        ServiceLogger.LOGGER.info("Checking provided email for correctness");
        if(requestModel.getEmail().length() > 50)
            return new UpdateModel(-10);
        if(!requestModel.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"))
            return new UpdateModel(-11);
        ServiceLogger.LOGGER.info("Deep checking for logical correctness.");
        if(requestModel.getQuantity() < 1)
            return new UpdateModel(33);
        if(!BillingRecords.insertedDuplicate(requestModel))
            return new UpdateModel(312);
        BillingRecords.updateCart(requestModel);
        return new UpdateModel(3110);
    }
}
