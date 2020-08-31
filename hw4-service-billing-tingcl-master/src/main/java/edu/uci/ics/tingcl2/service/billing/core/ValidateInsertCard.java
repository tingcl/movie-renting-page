package edu.uci.ics.tingcl2.service.billing.core;

import edu.uci.ics.tingcl2.service.billing.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.billing.models.InsertCardModel;
import edu.uci.ics.tingcl2.service.billing.models.InsertCardRequestModel;

import java.util.Date;


public class ValidateInsertCard {
    public static InsertCardModel logicalHandler(InsertCardRequestModel requestModel){
        ServiceLogger.LOGGER.info("Checking card format for correctness");
        if(requestModel.getId().length() > 20 || requestModel.getId().length() < 16)
            return new InsertCardModel(321);
        if(!requestModel.getId().matches("[0-9]+"))
            return new InsertCardModel(322);
        Date currentDay = new Date();
        if(!requestModel.getExpiration().after(currentDay))
            return new InsertCardModel(323);
        if(BillingRecords.duplicateCardEntry(requestModel.getId()))
            return new InsertCardModel(325);
        BillingRecords.enterCard(requestModel);
        return new InsertCardModel(3200);
    }
}
