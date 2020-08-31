package edu.uci.ics.tingcl2.service.movies.core;

import edu.uci.ics.tingcl2.service.movies.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.movies.models.GetStarIdResponse;

public class ValidateSearchStarId {
    public static GetStarIdResponse verify(String email, String sessionID, String transactionId, String id) {
        ServiceLogger.LOGGER.info("Verifying star id request.");
        GetStarIdResponse getStarIdResponse = MovieRecords.searchStarId(id);
        return getStarIdResponse;
    }
}