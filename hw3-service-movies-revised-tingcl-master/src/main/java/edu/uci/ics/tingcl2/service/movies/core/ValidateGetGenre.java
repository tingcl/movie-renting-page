package edu.uci.ics.tingcl2.service.movies.core;

import edu.uci.ics.tingcl2.service.movies.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.movies.models.GetGenreResponse;
import edu.uci.ics.tingcl2.service.movies.models.VerifyPrivilegeResponse;


public class ValidateGetGenre {
    public static GetGenreResponse verify(String email, String sessionID, String transactionID) {
        ServiceLogger.LOGGER.info("Verifying request to retrieve genres");
        VerifyPrivilegeResponse response = VerifyUserPrivilege.hasAuthorization(email, 3);
        if(response.getResultCode() != 140) {
            ServiceLogger.LOGGER.info("User does not have sufficient privilege rejecting request...");
            return new GetGenreResponse(141, null, response);
        }
        return new GetGenreResponse(219, MovieRecords.getGenreList(), null);
    }
}
