package edu.uci.ics.tingcl2.service.billing.core;

import edu.uci.ics.tingcl2.service.billing.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.billing.models.InsertCardRequestModel;
import edu.uci.ics.tingcl2.service.billing.models.UpdateCardModel;

import java.util.Date;

public class ValidateUpdateCard {
    public static UpdateCardModel logicalHandler(InsertCardRequestModel requestModel){
        ServiceLogger.LOGGER.info("Checking card format for surface correctness");
        if(requestModel.getId().length() > 20 || requestModel.getId().length() < 16)
            return new UpdateCardModel(321);
        if(!requestModel.getId().matches("[0-9]+"))
            return new UpdateCardModel(322);
        Date currentDay = new Date();
        if(!requestModel.getExpiration().after(currentDay))
            return new UpdateCardModel(323);
        if(!BillingRecords.duplicateCardEntry(requestModel.getId()))
            return new UpdateCardModel(324);
        BillingRecords.updateCard(requestModel);
        return new UpdateCardModel(3210);
    }
}
