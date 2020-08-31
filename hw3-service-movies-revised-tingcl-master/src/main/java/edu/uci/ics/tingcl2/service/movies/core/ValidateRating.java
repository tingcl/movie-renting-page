package edu.uci.ics.tingcl2.service.movies.core;

import edu.uci.ics.tingcl2.service.movies.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.movies.models.*;


public class ValidateRating {
    public static RatingResponse verify(String email, String sessionID, String transactionId, RatingRequest ratingRequest){
        ServiceLogger.LOGGER.info("Verifying request to add vote to movie..");
        if(!MovieRecords.movieAlreadyExistId(ratingRequest.getId())){
            return new RatingResponse(211);
        }
        if(ratingRequest.getRating() > 10.0 || ratingRequest.getRating() < 0.0){
            return new RatingResponse(251);
        }
        return MovieRecords.addRating(ratingRequest);
    }
}
