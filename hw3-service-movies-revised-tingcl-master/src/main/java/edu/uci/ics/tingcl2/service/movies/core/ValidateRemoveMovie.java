package edu.uci.ics.tingcl2.service.movies.core;

import edu.uci.ics.tingcl2.service.movies.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.movies.models.*;

public class ValidateRemoveMovie {
    public static RemoveMovieResponse verify(String email, String sessionID, String transactionID, String movieid) {
        ServiceLogger.LOGGER.info("Verifying remove movie request...");

        if (VerifyUserPrivilege.hasAuthorization(email, 3).getResultCode() != 140) {
            ServiceLogger.LOGGER.info("User does not have sufficient privilege remove request...");
            return new RemoveMovieResponse(141);
        }
        ServiceLogger.LOGGER.info("Authorized, allowed to change database...");
        if(!MovieRecords.canRemove(movieid)){
            ServiceLogger.LOGGER.info("Movie does not exist");
            return new RemoveMovieResponse(241);
        }
        if(MovieRecords.alreadyRemoved(movieid)){
            ServiceLogger.LOGGER.info("Movie has already been removed from site");
            return new RemoveMovieResponse(242);
        }
        MovieRecords.removeMovie(movieid);
        return new RemoveMovieResponse(240);
    }
}
