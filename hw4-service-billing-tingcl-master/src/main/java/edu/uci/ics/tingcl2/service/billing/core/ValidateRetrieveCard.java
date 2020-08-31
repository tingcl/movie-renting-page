package edu.uci.ics.tingcl2.service.billing.core;

import edu.uci.ics.tingcl2.service.billing.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.billing.models.CardModel;
import edu.uci.ics.tingcl2.service.billing.models.DeleteCardRequestModel;

import edu.uci.ics.tingcl2.service.billing.models.RetrieveCardModel;


public class ValidateRetrieveCard {
    public static RetrieveCardModel logicalHandler(DeleteCardRequestModel requestModel){
        ServiceLogger.LOGGER.info("Checking card format for correctness");
        if(requestModel.getId().length() < 16 || requestModel.getId().length() > 20)
            return new RetrieveCardModel(321, null);
        if(!requestModel.getId().matches("[0-9]+"))
            return new RetrieveCardModel(322, null);
        if(!BillingRecords.duplicateCardEntry(requestModel.getId()))
            return new RetrieveCardModel(324, null);
        CardModel creditcard = BillingRecords.getCardInfo(requestModel);
        return new RetrieveCardModel(3230, creditcard);
    }
}
