package edu.uci.ics.tingcl2.service.movies.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.tingcl2.service.movies.core.*;
import edu.uci.ics.tingcl2.service.movies.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.movies.models.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;

@Path("star")
public class StarPage {
    @Path("search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchStars(@Context HttpHeaders headers,
                                @QueryParam("name") String name,
                                @QueryParam("birthYear") Integer birthYear,
                                @QueryParam("movieTitle") String movieTitle,
                                @DefaultValue("10") @QueryParam("limit") int limit,
                                @DefaultValue("0") @QueryParam("offset") int offset,
                                @DefaultValue("name") @QueryParam("orderby") String orderby,
                                @DefaultValue("asc") @QueryParam("direction") String direction){

        ServiceLogger.LOGGER.info("Received request to search for movie stars.");

        // HTTP header key:value pairs "email" and "sessionID"
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        if(offset != 0){
            if(!(offset % limit == 0)){
                offset = 0;
            }
        }
        if(!(limit == 10 || limit == 25|| limit == 50 || limit == 100)){
            limit = 10;
        }

        /*
         *
         * insert checks for order-by and direction in the future
         *
         */

        //Build final string to map to response model
        StringBuilder jsonString = new StringBuilder();

        if(name == null && movieTitle != null){
            jsonString.append("{\"name\": " + name + ",\n")
                      .append("\"birthYear\": " + birthYear + ",\n")
                      .append("\"movieTitle\": " + "\"" + movieTitle + "\",\n")
                      .append("\"limit\": " + limit + ",\n")
                      .append("\"offset\": " + offset + ",\n")
                      .append("\"orderby\": " + "\"" + orderby + "\",\n")
                      .append("\"direction\": " + "\"" + direction + "\"\n}");
        }
        else if(movieTitle == null && name != null){
            jsonString.append("{\"name\": " + "\"" + name + "\",\n")
                      .append("\"birthYear\": " + birthYear + ",\n")
                      .append("\"movieTitle\": " + movieTitle + ",\n")
                      .append("\"limit\": " + limit + ",\n")
                      .append("\"offset\": " + offset + ",\n")
                      .append("\"orderby\": " + "\"" + orderby + "\",\n")
                      .append("\"direction\": " + "\"" + direction + "\"\n}");
        }
        else if(movieTitle == null && name == null){
            jsonString.append("{\"name\": " + name + ",\n")
                      .append("\"birthYear\": " + birthYear + ",\n")
                      .append("\"movieTitle\": " + movieTitle + ",\n")
                      .append("\"limit\": " + limit + ",\n")
                      .append("\"offset\": " + offset + ",\n")
                      .append("\"orderby\": " + "\"" + orderby + "\",\n")
                      .append("\"direction\": " + "\"" + direction + "\"\n}");
        }
        else{
            jsonString.append("{\"name\": " + "\"" + name + "\",\n")
                      .append("\"birthYear\": " + birthYear + ",\n")
                      .append("\"movieTitle\": " + "\"" + movieTitle + "\",\n")
                      .append("\"limit\": " + limit + ",\n")
                      .append("\"offset\": " + offset + ",\n")
                      .append("\"orderby\": " + "\"" + orderby + "\",\n")
                      .append("\"direction\": " + "\"" + direction + "\"\n}");
        }

        ServiceLogger.LOGGER.info("Json string to be mapped to the response model: " + jsonString.toString());
        SearchStarRequest searchStarRequest;
        SearchStarResponse searchStarResponse = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            searchStarRequest = mapper.readValue(jsonString.toString(), SearchStarRequest.class);
            ServiceLogger.LOGGER.info("mapping successful");
            if(searchStarRequest.getBirthYear() != null) {
                if (searchStarRequest.getBirthYear() < 0 || searchStarRequest.getBirthYear() > 2019) {
                    searchStarRequest.setBirthYear(null);
                }
            }
            ServiceLogger.LOGGER.info("Validating");
            ServiceLogger.LOGGER.info(searchStarRequest.toString());
            searchStarResponse = ValidateStarSearch.verify(email, sessionID, transactionID, searchStarRequest);
        }
        catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
            }
        }
        return Response.status(Response.Status.OK).entity(searchStarResponse).build();
    }
    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchStarId(@Context HttpHeaders headers,
                                 @PathParam("id") String id){
        // Query string search parameters
        ServiceLogger.LOGGER.info("Received request to search for star of " + id);
        // HTTP header key:value pairs "email" and "sessionID"
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        GetStarIdResponse getStarIdResponse = ValidateSearchStarId.verify(email, sessionID, transactionID, id);
        return Response.status(Status.OK).entity(getStarIdResponse).build();
    }
    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStar(@Context HttpHeaders headers, String jsonText){
        // Query string search parameters
        ServiceLogger.LOGGER.info("Received request to add star into database.");
        // HTTP header key:value pairs "email" and "sessionID"
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        AddStarRequest addStarRequest;
        AddStarResponse addStarResponse;
        ObjectMapper mapper = new ObjectMapper();
        try{
            addStarRequest = mapper.readValue(jsonText, AddStarRequest.class);
            addStarResponse = ValidateAddStar.verify(email, sessionID, transactionID, addStarRequest);
            return Response.status(Status.OK).entity(addStarResponse).build();
        }
        catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                addStarResponse = new AddStarResponse(-3);
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                addStarResponse = new AddStarResponse(-2);
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
                addStarResponse = new AddStarResponse(-1);
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(addStarResponse).build();
            }
            return Response.status(Status.BAD_REQUEST).entity(addStarResponse).build();
        }
    }
    @Path("starsin")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response starsIn(@Context HttpHeaders headers, String jsonText){
        // Query string search parameters
        ServiceLogger.LOGGER.info("Received request to add star into database.");
        // HTTP header key:value pairs "email" and "sessionID"
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        StarsInRequest starsInRequest;
        StarsInResponse starsInResponse;
        ObjectMapper mapper = new ObjectMapper();
        try{
            starsInRequest = mapper.readValue(jsonText, StarsInRequest.class);
            starsInResponse = ValidateStarsIn.verify(email, sessionID, transactionID, starsInRequest);
            return Response.status(Status.OK).entity(starsInResponse).build();
        }
        catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("Unable to map JSON to POJO.");
                starsInResponse = new StarsInResponse(-3);
            } else if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("Unable to parse JSON.");
                starsInResponse = new StarsInResponse(-2);
            } else {
                ServiceLogger.LOGGER.warning("IOException.");
                starsInResponse = new StarsInResponse(-1);
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(starsInResponse).build();
            }
            return Response.status(Status.BAD_REQUEST).entity(starsInResponse).build();
        }
    }
}
