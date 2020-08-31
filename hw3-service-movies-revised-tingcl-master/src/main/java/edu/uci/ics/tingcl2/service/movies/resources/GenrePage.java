package edu.uci.ics.tingcl2.service.movies.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.tingcl2.service.movies.core.ValidateAddGenre;
import edu.uci.ics.tingcl2.service.movies.core.ValidateGetGenre;
import edu.uci.ics.tingcl2.service.movies.core.ValidateGetMovieGenre;
import edu.uci.ics.tingcl2.service.movies.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.movies.models.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;

@Path("genre")
public class GenrePage {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllGenres(@Context HttpHeaders headers){
        ServiceLogger.LOGGER.info("Received request to retrieve all genres.");
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        GetGenreResponse getGenreResponse = ValidateGetGenre.verify(email, sessionID, transactionID);
        return Response.status(Status.OK).entity(getGenreResponse).build();
    }
    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addGenre(@Context HttpHeaders headers, String jsonText){
        ServiceLogger.LOGGER.info("Received request to add genres to database.");

        String sessionID = headers.getHeaderString("sessionID");
        String email = headers.getHeaderString("email");
        String transactionID = headers.getHeaderString("transactionID");

        AddGenreRequest addGenreRequest;
        AddGenreResponse addGenreResponse;
        ObjectMapper mapper = new ObjectMapper();
        try{
            addGenreRequest = mapper.readValue(jsonText, AddGenreRequest.class);
            addGenreResponse = ValidateAddGenre.verify(email, sessionID, transactionID, addGenreRequest);
            return Response.status(Status.OK).entity(addGenreResponse).build();
        }
        catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                addGenreResponse = new AddGenreResponse(-3);
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                addGenreResponse = new AddGenreResponse(-2);
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
                addGenreResponse = new AddGenreResponse(-1);
            }
            return Response.status(Status.BAD_REQUEST).entity(addGenreResponse).build();
        }
    }
    @Path("{movieid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieIdGenres(@Context HttpHeaders headers,
                                     @PathParam("movieid") String movieid){
        ServiceLogger.LOGGER.info("Received request to get all genres from specified movieid.");

        String sessionID = headers.getHeaderString("sessionID");
        String email = headers.getHeaderString("email");
        String transactionID = headers.getHeaderString("transactionID");

        MovieGenreResponse movieGenreResponse = ValidateGetMovieGenre.verify(email, sessionID, transactionID, movieid);
        return Response.status(Status.OK).entity(movieGenreResponse).build();
    }



}
