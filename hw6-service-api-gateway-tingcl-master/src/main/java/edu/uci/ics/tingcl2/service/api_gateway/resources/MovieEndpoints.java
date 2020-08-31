package edu.uci.ics.tingcl2.service.api_gateway.resources;

import edu.uci.ics.tingcl2.service.api_gateway.GatewayService;
import edu.uci.ics.tingcl2.service.api_gateway.exceptions.ModelValidationException;
import edu.uci.ics.tingcl2.service.api_gateway.logger.ServiceLogger;
import edu.uci.ics.tingcl2.service.api_gateway.models.movies.*;
import edu.uci.ics.tingcl2.service.api_gateway.threadpool.ClientRequest;
import edu.uci.ics.tingcl2.service.api_gateway.utilities.ModelValidator;
import edu.uci.ics.tingcl2.service.api_gateway.utilities.TransactionIDGenerator;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

@Path("movies")
public class MovieEndpoints {
    @Path("search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchMovieRequest(@Context HttpHeaders headers,
                                       @Context UriInfo uriInfo,
                                       @QueryParam("title") String title,
                                       @QueryParam("genre") String genre,
                                       @QueryParam("year") int year,
                                       @QueryParam("director") String director,
                                       @QueryParam("hidden") boolean hidden,
                                       @QueryParam("limit") int limit,
                                       @QueryParam("offset") int offset,
                                       @QueryParam("orderby") String orderby,
                                       @QueryParam("direction") String direction) {

        ServiceLogger.LOGGER.info("Received request to search movie.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        if(transactionID == null) {
            transactionID = TransactionIDGenerator.generateTransactionID();
        }
        ClientRequest cr = new ClientRequest();
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPMovieSearch());
        cr.setQueryparams(uriInfo.getRequestUri().getQuery());
        cr.setHttpMethodType("GET");

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID()).build();
    }

    @Path("get/{movieid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieRequest(@Context HttpHeaders headers,
                                    @PathParam("movieid") String movieid) {

        ServiceLogger.LOGGER.info("Received request to search for movie of " + movieid);

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        if(transactionID == null) {
            transactionID = TransactionIDGenerator.generateTransactionID();
        }

        ClientRequest cr = new ClientRequest();
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPMovieGet());
        cr.setHttpMethodType("GET");
        cr.setPathparams("/get/" + movieid);

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID()).build();
    }

    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMovieRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to add movies.");

        String sessionID = headers.getHeaderString("sessionID");
        String email = headers.getHeaderString("email");
        String transactionID = headers.getHeaderString("transactionID");
        if(transactionID == null) {
            transactionID = TransactionIDGenerator.generateTransactionID();
        }

        AddMovieRequestModel requestModel;
        try {
            requestModel = (AddMovieRequestModel) ModelValidator.verifyModel(jsonText, AddMovieRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, AddMovieResponseModel.class);
        }

        ClientRequest cr = new ClientRequest();
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPMovieAdd());
        cr.setRequest(requestModel);
        cr.setHttpMethodType("POST");

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID()).build();
    }

    @Path("delete/{movieid}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMovieRequest(@Context HttpHeaders headers,
                                       @PathParam("movieid") String movieid) {
        ServiceLogger.LOGGER.info("Received request to delete movie of " + movieid);

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        if(transactionID == null) {
            transactionID = TransactionIDGenerator.generateTransactionID();
        }

        ClientRequest cr = new ClientRequest();
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPMovieDelete());
        cr.setHttpMethodType("DELETE");
        cr.setPathparams("/delete/" + movieid);

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID()).build();
    }

    @Path("genre")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGenresRequest(@Context HttpHeaders headers) {
        ServiceLogger.LOGGER.info("Received request to get all genres");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        if(transactionID == null) {
            transactionID = TransactionIDGenerator.generateTransactionID();
        }

        ClientRequest cr = new ClientRequest();
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPGenreGet());
        cr.setHttpMethodType("GET");

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID()).build();
    }

    @Path("genre/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addGenreRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to add genres.");

        String sessionID = headers.getHeaderString("sessionID");
        String email = headers.getHeaderString("email");
        String transactionID = headers.getHeaderString("transactionID");
        if(transactionID == null) {
            transactionID = TransactionIDGenerator.generateTransactionID();
        }

        AddGenreRequestModel requestModel;
        try {
            requestModel = (AddGenreRequestModel) ModelValidator.verifyModel(jsonText, AddGenreRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, AddGenreResponseModel.class);
        }

        ClientRequest cr = new ClientRequest();
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPGenreAdd());
        cr.setRequest(requestModel);
        cr.setHttpMethodType("POST");

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID()).build();
    }

    @Path("genre/{movieid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGenresForMovieRequest(@Context HttpHeaders headers,
                                             @PathParam("movieid") String movieid) {
        ServiceLogger.LOGGER.info("Received request for genre in " + movieid);

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        if(transactionID == null) {
            transactionID = TransactionIDGenerator.generateTransactionID();
        }

        ClientRequest cr = new ClientRequest();
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPGenreMovie());
        cr.setHttpMethodType("GET");
        cr.setPathparams("/genre/" + movieid);

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID()).build();
    }

    @Path("star/search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response starSearchRequest(@Context HttpHeaders headers,
                                      @Context UriInfo uriInfo,
                                      @QueryParam("name") String name,
                                      @QueryParam("birthYear") Integer birthYear,
                                      @QueryParam("movieTitle") String movieTitle,
                                      @QueryParam("limit") int limit,
                                      @QueryParam("offset") int offset,
                                      @QueryParam("orderby") String orderby,
                                      @QueryParam("direction") String direction) {
        ServiceLogger.LOGGER.info("Received request to search for movie stars.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        if(transactionID == null) {
            transactionID = TransactionIDGenerator.generateTransactionID();
        }

        ClientRequest cr = new ClientRequest();
        cr.setEmail(email);
        cr.setTransactionID(transactionID);
        cr.setSessionID(sessionID);
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPStarSearch());
        cr.setHttpMethodType("GET");
        cr.setQueryparams(uriInfo.getRequestUri().getQuery());

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID()).build();
    }

    @Path("star/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStarRequest(@Context HttpHeaders headers,
                                   @PathParam("id") String id) {
        ServiceLogger.LOGGER.info("Received request for star of " + id);

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        if(transactionID == null) {
            transactionID = TransactionIDGenerator.generateTransactionID();
        }

        ClientRequest cr = new ClientRequest();
        cr.setEmail(email);
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPStarGet());
        cr.setHttpMethodType("GET");
        cr.setPathparams("/star/" + id);

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID()).build();
    }

    @Path("star/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStarRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to add star.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        if(transactionID == null) {
            transactionID = TransactionIDGenerator.generateTransactionID();
        }

        InsertStarRequestModel requestModel;
        try {
            requestModel = (InsertStarRequestModel) ModelValidator.verifyModel(jsonText, InsertStarRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, InsertStarResponseModel.class);
        }
        ClientRequest cr = new ClientRequest();
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setHttpMethodType("POST");
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPStarAdd());
        cr.setRequest(requestModel);

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID()).build();
    }

    @Path("star/starsin")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStarToMovieRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to add star in movie.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        if(transactionID == null) {
            transactionID = TransactionIDGenerator.generateTransactionID();
        }

        InsertStarInRequestModel requestModel;
        try {
            requestModel = (InsertStarInRequestModel) ModelValidator.verifyModel(jsonText, InsertStarInRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, InsertStarInResponseModel.class);
        }
        ClientRequest cr = new ClientRequest();
        cr.setSessionID(sessionID);
        cr.setTransactionID(transactionID);
        cr.setEmail(email);
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPStarAdd());
        cr.setHttpMethodType("POST");
        cr.setRequest(requestModel);

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID()).build();

    }
    @Path("rating")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRatingRequest(@Context HttpHeaders headers, String jsonText) {
        ServiceLogger.LOGGER.info("Received request to add rating in movie.");

        String email = headers.getHeaderString("email");
        String sessionID = headers.getHeaderString("sessionID");
        String transactionID = headers.getHeaderString("transactionID");
        if(transactionID == null) {
            transactionID = TransactionIDGenerator.generateTransactionID();
        }

        UpdatingRatingRequestModel requestModel;
        try {
            requestModel = (UpdatingRatingRequestModel) ModelValidator.verifyModel(jsonText, UpdatingRatingRequestModel.class);
        } catch (ModelValidationException e) {
            return ModelValidator.returnInvalidRequest(e, UpdateRatingResponseModel.class);
        }
        ClientRequest cr = new ClientRequest();
        cr.setTransactionID(transactionID);
        cr.setHttpMethodType("POST");
        cr.setSessionID(sessionID);
        cr.setEmail(email);
        cr.setURI(GatewayService.getMovieConfigs().getMoviesUri());
        cr.setEndpoint(GatewayService.getMovieConfigs().getEPRating());
        cr.setRequest(requestModel);

        GatewayService.getThreadPool().getQueue().enqueue(cr);
        return Response.status(Status.NO_CONTENT)
                .header("request delay", GatewayService.getGatewayConfigs().getRequestDelay())
                .header("email", cr.getEmail())
                .header("sessionID", cr.getSessionID())
                .header("transactionID", cr.getTransactionID()).build();
    }
}
