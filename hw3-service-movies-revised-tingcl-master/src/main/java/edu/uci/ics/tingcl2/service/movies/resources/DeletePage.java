package edu.uci.ics.tingcl2.service.movies.resources;

import edu.uci.ics.tingcl2.service.movies.core.ValidateRemoveMovie;
import edu.uci.ics.tingcl2.service.movies.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.movies.models.RemoveMovieResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("delete")
public class DeletePage {
    @Path("{movieid}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMovieId(@Context HttpHeaders headers,
                                  @PathParam("movieid") String movieid){
        ServiceLogger.LOGGER.info("Received request to remove movies from database.");
        // HTTP header key:value pairs "email" and "sessionID"
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        RemoveMovieResponse removeMovieResponse;
        removeMovieResponse = ValidateRemoveMovie.verify(email, sessionID, transactionID, movieid);
        return Response.status(Status.OK).entity(removeMovieResponse).build();
    }
}
