package edu.uci.ics.tingcl2.service.billing.core;

import edu.uci.ics.tingcl2.service.billing.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.billing.models.DeleteModel;
import edu.uci.ics.tingcl2.service.billing.models.DeleteRequestModel;

public class ValidateDelete {
    public static DeleteModel logicalHandler(DeleteRequestModel requestModel){
        ServiceLogger.LOGGER.info("Checking provided email for correctness");
        if(requestModel.getEmail().length() > 50)
            return new DeleteModel(-10);
        if(!requestModel.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"))
            return new DeleteModel(-11);
        ServiceLogger.LOGGER.info("Deep checking for logical correctness.");
        if(!BillingRecords.itemExist(requestModel))
            return new DeleteModel(312);
        BillingRecords.deleteCartItem(requestModel);
        return new DeleteModel(3120);
    }
}
