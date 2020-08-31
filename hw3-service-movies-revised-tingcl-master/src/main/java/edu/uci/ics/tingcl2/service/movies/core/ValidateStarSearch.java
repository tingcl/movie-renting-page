package edu.uci.ics.tingcl2.service.movies.core;

import edu.uci.ics.tingcl2.service.movies.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.movies.models.SearchStarRequest;
import edu.uci.ics.tingcl2.service.movies.models.SearchStarResponse;

public class ValidateStarSearch {
    public static SearchStarResponse verify(String email, String sessionID, String transactionId, SearchStarRequest searchStarRequest) {
        ServiceLogger.LOGGER.info("Verifying star request.");
        SearchStarResponse searchStarResponse = MovieRecords.searchStars(searchStarRequest);
        return searchStarResponse;
    }
}
