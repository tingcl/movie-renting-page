package edu.uci.ics.tingcl2.service.movies.core;

import edu.uci.ics.tingcl2.service.movies.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.movies.models.MovieGenreResponse;
import edu.uci.ics.tingcl2.service.movies.models.VerifyPrivilegeResponse;


public class ValidateGetMovieGenre {
    public static MovieGenreResponse verify(String email, String sessionID, String transactionID, String movieid) {
        ServiceLogger.LOGGER.info("Verifying request to retrieve movie genres");
        VerifyPrivilegeResponse response = VerifyUserPrivilege.hasAuthorization(email, 3);
        ServiceLogger.LOGGER.info("Checking result code.");
        ServiceLogger.LOGGER.info(response.getResultCode() + "");
        if(response.getResultCode() != 140) {
            ServiceLogger.LOGGER.info("User does not have sufficient privilege rejecting request...");
            return new MovieGenreResponse(141, null, response);
        }
        if(!MovieRecords.movieAlreadyExistId(movieid)){
            return new MovieGenreResponse(211, null, null);
        }
        return new MovieGenreResponse(219, MovieRecords.getMovieGenreList(movieid), null);
    }
}
