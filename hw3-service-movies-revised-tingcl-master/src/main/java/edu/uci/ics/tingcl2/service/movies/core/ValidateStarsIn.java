package edu.uci.ics.tingcl2.service.movies.core;

import edu.uci.ics.tingcl2.service.movies.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.movies.models.*;

public class ValidateStarsIn {
    public static StarsInResponse verify(String email, String sessionID, String transactionId, StarsInRequest starsInRequest){
        ServiceLogger.LOGGER.info("Verifying request to add to stars in...");
        if(VerifyUserPrivilege.hasAuthorization(email, 3).getResultCode() != 140) {
            ServiceLogger.LOGGER.info("User does not have sufficient privilege ...");
            return new StarsInResponse(141);
        }
        if(!MovieRecords.movieIdAlreadyExist(starsInRequest.getMovieid())){
            return new StarsInResponse(211);
        }
        if(MovieRecords.starsInAlreadyExist(starsInRequest.getStarid(), starsInRequest.getMovieid())){
            return new StarsInResponse(232);
        }
        return MovieRecords.addStarsIn(starsInRequest);

    }

}
