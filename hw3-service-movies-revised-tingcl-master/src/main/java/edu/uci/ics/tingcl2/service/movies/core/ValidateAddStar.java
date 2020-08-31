package edu.uci.ics.tingcl2.service.movies.core;

import edu.uci.ics.tingcl2.service.movies.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.movies.models.*;


public class ValidateAddStar {
    public static AddStarResponse verify(String email, String sessionID, String transactionId, AddStarRequest addStarRequest){
        ServiceLogger.LOGGER.info("Verifying request to add to star..");
        if(VerifyUserPrivilege.hasAuthorization(email, 3).getResultCode() != 140) {
            ServiceLogger.LOGGER.info("User does not have sufficient privilege ...");
            return new AddStarResponse(141);
        }
        if(MovieRecords.starExists(addStarRequest)){
            ServiceLogger.LOGGER.info("HI ...");
            return new AddStarResponse(221);
        }
        return MovieRecords.addNewStar(addStarRequest);
    }

}
