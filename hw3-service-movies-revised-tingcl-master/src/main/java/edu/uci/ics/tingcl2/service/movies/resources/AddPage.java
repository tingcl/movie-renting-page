package edu.uci.ics.tingcl2.service.movies.resources;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.tingcl2.service.movies.core.ValidateAddMovie;
import edu.uci.ics.tingcl2.service.movies.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.movies.models.AddMovieRequest;
import edu.uci.ics.tingcl2.service.movies.models.AddMovieResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;

@Path("add")
public class AddPage {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMovie(@Context HttpHeaders headers, String jsonText){
        ServiceLogger.LOGGER.info("Received request to add movies into database.");

        // HTTP header key:value pairs "email" and "sessionID"
        String sessionID = headers.getHeaderString("sessionID");
        String email = headers.getHeaderString("email");
        String transactionID = headers.getHeaderString("transactionID");

        ServiceLogger.LOGGER.info("email: " +  email);
        ServiceLogger.LOGGER.info("sessionID: " + sessionID);
        ServiceLogger.LOGGER.info("transactionID: " + transactionID);
        AddMovieRequest addMovieRequest;
        AddMovieResponse addMovieResponse;
        ObjectMapper mapper = new ObjectMapper();
        try{
            addMovieRequest = mapper.readValue(jsonText, AddMovieRequest.class);
            addMovieResponse = ValidateAddMovie.verify(email, sessionID, transactionID, addMovieRequest);
            return Response.status(Status.OK).entity(addMovieResponse).build();
        }
        catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                addMovieResponse = new AddMovieResponse(-2, null, null);
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                addMovieResponse = new AddMovieResponse(-3, null, null);
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
                addMovieResponse = new AddMovieResponse(-1, null, null);
            }
            return Response.status(Status.BAD_REQUEST).entity(addMovieResponse).build();
        }
    }
}
