package edu.uci.ics.tingcl2.service.movies.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.tingcl2.service.movies.core.ValidateMoviesSearch;
import edu.uci.ics.tingcl2.service.movies.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.movies.models.SearchMovieRequest;
import edu.uci.ics.tingcl2.service.movies.models.SearchMovieResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;

@Path("search")
public class SearchPage {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchMovie(@Context HttpHeaders headers,
                                @QueryParam("title") String title,
                                @QueryParam("genre") String genre,
                                @QueryParam("year") int year,
                                @QueryParam("director") String director,
                                @DefaultValue("false") @QueryParam("hidden") boolean hidden,
                                @DefaultValue("10") @QueryParam("limit") int limit,
                                @DefaultValue("0") @QueryParam("offset") int offset,
                                @DefaultValue("rating") @QueryParam("orderby") String orderby,
                                @DefaultValue("desc") @QueryParam("direction") String direction){

        ServiceLogger.LOGGER.info("Received request to search for general movie information.");

        // HTTP header key:value pairs "email" and "sessionID"
        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");

        // Query parameter
        ServiceLogger.LOGGER.info("User information...");
        ServiceLogger.LOGGER.info("email: " +  email);
        ServiceLogger.LOGGER.info("sessionID: " + sessionID);
        ServiceLogger.LOGGER.info("transactionID: " + transactionID);

        ServiceLogger.LOGGER.info("String query information...");
        ServiceLogger.LOGGER.info("title: " + title);
        ServiceLogger.LOGGER.info("genre: " + genre);
        ServiceLogger.LOGGER.info("year: " + year);
        ServiceLogger.LOGGER.info("director: " + director);
        ServiceLogger.LOGGER.info("hidden: " + hidden);
        ServiceLogger.LOGGER.info("limit: " + limit);
        ServiceLogger.LOGGER.info("offset: " + offset);
        ServiceLogger.LOGGER.info("orderby: " + orderby);
        ServiceLogger.LOGGER.info("direction: " + direction);

        // Validates input query parameters
        if(!(limit == 10 || limit == 25|| limit == 50 || limit == 100)){
            limit = 10;
        }
        if(offset != 0){
            if(!(offset % limit == 0)){
                offset = 0;
            }
        }
        //Build final string to map to response model
        StringBuilder jsonString = new StringBuilder();
        jsonString.append("{\"title\": " + "\"" + title + "\",\n")
                .append("\"genre\": " + "\"" + genre + "\",\n")
                .append("\"year\": " + year + ",\n")
                .append("\"director\": " + "\"" + director + "\",\n")
                .append("\"hidden\": " + "\"" + hidden + "\",\n")
                .append("\"offset\": " + offset + ",\n")
                .append("\"limit\": " + "\"" + limit + "\",\n")
                .append("\"orderby\": " + "\"" + orderby + "\",\n")
                .append("\"direction\": " + "\"" + direction + "\"\n}");

        ServiceLogger.LOGGER.info("Json string to be mapped to the response model: " + jsonString.toString());
        SearchMovieRequest searchMovieRequest;
        SearchMovieResponse searchMovieResponse = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            searchMovieRequest = mapper.readValue(jsonString.toString(), SearchMovieRequest.class);
            searchMovieResponse = ValidateMoviesSearch.verify(email, sessionID, transactionID, searchMovieRequest);
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
        return Response.status(Status.OK).entity(searchMovieResponse).build();
    }
}
