package edu.uci.ics.tingcl2.service.billing.core;

import edu.uci.ics.tingcl2.service.billing.models.InsertModel;
import edu.uci.ics.tingcl2.service.billing.models.InsertRequestModel;
import edu.uci.ics.tingcl2.service.billing.logger.ServiceLogger;

public class ValidateInsert {
    public static InsertModel logicalHandler(InsertRequestModel requestModel){
        ServiceLogger.LOGGER.info("Checking provided email for correctness");
        if(requestModel.getEmail().length() > 50)
            return new InsertModel(-10);
        if(!requestModel.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"))
            return new InsertModel(-11);
        ServiceLogger.LOGGER.info("Deep checking for logical correctness.");
        if(requestModel.getQuantity() < 1)
            return new InsertModel(33);
        if(BillingRecords.insertedDuplicate(requestModel))
            return new InsertModel(311);
        ServiceLogger.LOGGER.info("Successfully inserted  movie into cart.");
        BillingRecords.insertIntoCart(requestModel);
        return new InsertModel(3100);

    }

}
