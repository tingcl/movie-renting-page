package edu.uci.ics.tingcl2.service.movies.core;

import edu.uci.ics.tingcl2.service.movies.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.movies.models.*;


public class ValidateAddMovie {
    public static AddMovieResponse verify(String email, String sessionID, String transactionId, AddMovieRequest addMovieRequest){
        ServiceLogger.LOGGER.info("Verifying request to add to database...");
        if(email != null) {
            if (VerifyUserPrivilege.hasAuthorization(email, 3).getResultCode() != 140) {
                ServiceLogger.LOGGER.info("User does not have sufficient privilege rejecting hidden request...");
                return new AddMovieResponse(141, null, null);
            }
        }
        else{
            return new AddMovieResponse(141, null, null);
        }
        ServiceLogger.LOGGER.info("Sufficient privilege to add movie...");
        return MovieRecords.addNewMovie(addMovieRequest);
    }

}
