package edu.uci.ics.tingcl2.service.movies.core;

import edu.uci.ics.tingcl2.service.movies.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.movies.models.SearchMovieRequest;
import edu.uci.ics.tingcl2.service.movies.models.SearchMovieResponse;

public class ValidateMoviesSearch {
    public static SearchMovieResponse verify(String email, String sessionID, String transactionId, SearchMovieRequest searchMovieRequest) {
        ServiceLogger.LOGGER.info("Verifying user has privilege of 3 or higher");
        if(email != null){
            if (VerifyUserPrivilege.hasAuthorization(email, 3).getResultCode() != 140) {
                ServiceLogger.LOGGER.info("User does not have sufficient privilege rejecting hidden request...");
                searchMovieRequest.setHidden(false);
            }
            else{
                ServiceLogger.LOGGER.info("User does have sufficient privilege allowing hidden request...");
            }
        }
        else{
            ServiceLogger.LOGGER.info("User did not provide email. Falling back on default permissions...");
            searchMovieRequest.setHidden(false);
        }
        ServiceLogger.LOGGER.info("Beginning movie search now...");
        SearchMovieResponse searchMovieResponse = MovieRecords.movieSearchQuery(searchMovieRequest);
        return searchMovieResponse;
    }
}
