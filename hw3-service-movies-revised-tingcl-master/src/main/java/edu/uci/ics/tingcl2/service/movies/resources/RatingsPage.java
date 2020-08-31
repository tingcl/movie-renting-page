package edu.uci.ics.tingcl2.service.movies.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.tingcl2.service.movies.core.ValidateRating;
import edu.uci.ics.tingcl2.service.movies.core.ValidateStarsIn;
import edu.uci.ics.tingcl2.service.movies.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.movies.models.RatingRequest;
import edu.uci.ics.tingcl2.service.movies.models.RatingResponse;
import edu.uci.ics.tingcl2.service.movies.models.StarsInRequest;
import edu.uci.ics.tingcl2.service.movies.models.StarsInResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;

@Path("rating")
public class RatingsPage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response incrementRating(@Context HttpHeaders headers, String jsonText) {
        // Query string search parameters
        ServiceLogger.LOGGER.info("Received add rating.");
        // HTTP header key:value pairs "email" and "sessionID"
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        RatingRequest ratingRequest;
        RatingResponse ratingResponse;
        ObjectMapper mapper = new ObjectMapper();
        try {
            ratingRequest = mapper.readValue(jsonText, RatingRequest.class);
            ratingResponse = ValidateRating.verify(email, sessionID, transactionID, ratingRequest);
            return Response.status(Status.OK).entity(ratingResponse).build();
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                ratingResponse = new RatingResponse(-3);
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                ratingResponse = new RatingResponse(-2);
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
                ratingResponse = new RatingResponse(-1);
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ratingResponse).build();
            }
            return Response.status(Status.BAD_REQUEST).entity(ratingResponse).build();
        }
    }
}
