package edu.uci.ics.tingcl2.service.movies.resources;

import edu.uci.ics.tingcl2.service.movies.core.ValidateGetMovieId;
import edu.uci.ics.tingcl2.service.movies.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.movies.models.GetMovieIdResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("get")
public class GetPage {
    @Path("{movieid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieId(@Context HttpHeaders headers,
                               @PathParam("movieid") String movieid){
        // Query string search parameters
        ServiceLogger.LOGGER.info("Received request to search for movie of " + movieid);

        // HTTP header key:value pairs "email" and "sessionID"
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        ServiceLogger.LOGGER.info("email: " +  email);
        ServiceLogger.LOGGER.info("sessionID: " + sessionID);
        ServiceLogger.LOGGER.info("transactionID: " + transactionID);

        GetMovieIdResponse getMovieIdResponse = ValidateGetMovieId.verify(email, sessionID, transactionID, movieid);

        return Response.status(Status.OK).entity(getMovieIdResponse).build();
    }
}