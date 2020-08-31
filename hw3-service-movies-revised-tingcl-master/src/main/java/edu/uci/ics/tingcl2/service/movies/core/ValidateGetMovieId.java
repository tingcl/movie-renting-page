package edu.uci.ics.tingcl2.service.movies.core;

import edu.uci.ics.tingcl2.service.movies.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.movies.models.GetMovieIdResponse;
import edu.uci.ics.tingcl2.service.movies.models.VerifyPrivilegeResponse;


public class ValidateGetMovieId {
    public static GetMovieIdResponse verify(String email, String sessionID, String transactionID, String id) {
        ServiceLogger.LOGGER.info("Verifying request to retrieve movie by ID...");
        boolean privileged = false;
        VerifyPrivilegeResponse response = null;
        if(email != null){
            response = VerifyUserPrivilege.hasAuthorization(email, 4);
            if(response.getResultCode() == 140) {
                ServiceLogger.LOGGER.info("User have sufficient privilege rejecting hidden request...");
                privileged = true;
            }
            else{
                ServiceLogger.LOGGER.info("User have insufficient privilege rejecting hidden request...");
            }
        }
        else{
            ServiceLogger.LOGGER.info("No email provided resorting to default permissions...");
        }
        GetMovieIdResponse getMovieIdResponse = MovieRecords.movieIdSearchQuery(id, privileged, response);
        return getMovieIdResponse;
    }
}
