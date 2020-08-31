package edu.uci.ics.tingcl2.service.movies.core;

import edu.uci.ics.tingcl2.service.movies.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.movies.models.*;

public class ValidateAddGenre {
    public static AddGenreResponse verify(String email, String sessionID, String transactionID, AddGenreRequest addGenreRequest) {
        ServiceLogger.LOGGER.info("Verifying add to genres request...");

        if (VerifyUserPrivilege.hasAuthorization(email, 3).getResultCode() != 140) {
            ServiceLogger.LOGGER.info("User does not have sufficient privilege rejecting hidden request...");
            return new AddGenreResponse(141);
        }
        ServiceLogger.LOGGER.info("Authorized allowed to change database...");
        if(MovieRecords.genreAlreadyExists(addGenreRequest.getName())){
            return new AddGenreResponse(218);
        }
        MovieRecords.addGenre(addGenreRequest.getName());
        return new AddGenreResponse(217);
    }
}