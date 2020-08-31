package edu.uci.ics.tingcl2.service.billing.core;

import edu.uci.ics.tingcl2.service.billing.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.billing.models.*;

public class ValidateDeleteCard {
    public static DeleteCardModel logicalHandler(DeleteCardRequestModel requestModel){
        ServiceLogger.LOGGER.info("Checking card format for surface correctness");
        if(requestModel.getId().length() > 20 || requestModel.getId().length() < 16)
            return new DeleteCardModel(321);
        if(!requestModel.getId().matches("[0-9]+"))
            return new DeleteCardModel(322);
        if(!BillingRecords.duplicateCardEntry(requestModel.getId()))
            return new DeleteCardModel(324);
        BillingRecords.deleteCard(requestModel);
        return new DeleteCardModel(3220);
    }
}
